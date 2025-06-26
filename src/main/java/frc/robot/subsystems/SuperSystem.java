package frc.robot.subsystems;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveDriveState;
import edu.wpi.first.units.BaseUnits;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants;
import frc.robot.commands.drive.AutoAlignPID2;
import frc.robot.commands.drive.FollowTagPIDToPose;
import frc.robot.commands.drive.MoveForwardSlowPP;
import frc.robot.subsystems.endEffector.EndEffectorConstants;
import frc.robot.lib.FieldLayout;
import frc.robot.lib.FieldLayout.Branch;
import frc.robot.lib.FieldLayout.Branch.Face;
import frc.robot.lib.FieldLayout.Level;
import frc.robot.lib.io.BeamBreakIO;
import frc.robot.subsystems.SuperSystemConstants.BeamBreakConstants;
import frc.robot.subsystems.clawSubsystem.ClawConstants;
import frc.robot.subsystems.clawSubsystem.ClawSubsystem;
import frc.robot.subsystems.drive.DriveSubsystem;
import frc.robot.subsystems.elevator.ElevatorConstants;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.pivot.PivotConstants;
import frc.robot.subsystems.pivot.PivotSubsystem;


public class SuperSystem extends SubsystemBase {
    
    public State state = State.TUCK;

   
   
    public static BeamBreakIO intakeRollersCurrentSpike = BeamBreakConstants.getIntakeRollersCurrentSpike();
	
    public static BeamBreakIO indexerBeamBrake = BeamBreakConstants.getIndexerBeamBreak();
	

    public static SuperSystem mInstance;

    private Branch targetingBranch = Branch.A;
    private Face targetingFace = targetingBranch.getKey().face();

    private boolean isPathFollowing = false;

	private boolean hasAlgae = false;

    public boolean readyToRaiseElevator = false;

    private boolean targetingL3ReefIntake = true;


    public static SuperSystem getInstance() {

        //Rethink this for how advantage kit does 
		if (mInstance == null) {
			mInstance = new SuperSystem();
		}
		return mInstance;
	}

    @Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		intakeRollersCurrentSpike.initSendable(builder);
        indexerBeamBrake.initSendable(builder);

       
		builder.addStringProperty("Targeting Branch", () -> targetingBranch.toString(), null);
		builder.addStringProperty("State", () -> state.toString(), null);

        builder.addDoubleProperty("Battery Voltage", () -> RobotController.getBatteryVoltage(), null);

    }

    @Override
	public void periodic() {
		if (!isPathFollowing) {
			updateTargetedBranch();
			updateTargetedFace();
			updateTargetedReefIntake();
		}
	}

    public void updateTargetedBranch() {
		SwerveDriveState currentState = DriveSubsystem.mInstance.getState();
		Transform2d speedsPose = new Transform2d(
						currentState.Speeds.vxMetersPerSecond,
						currentState.Speeds.vyMetersPerSecond,
						Rotation2d.fromRadians(currentState.Speeds.omegaRadiansPerSecond))
				.times(SuperSystemConstants.lookaheadBranchSelectionTime.in(Units.Seconds));
		Pose2d lookeaheadPose = currentState.Pose.transformBy(speedsPose);
		targetingBranch = FieldLayout.Branch.getClosestBranch(lookeaheadPose, RobotConstants.isRedAlliance);
	}

    public void updateTargetedFace() {
		targetingFace = targetingBranch.getKey().face();
	}

	public void updateTargetedReefIntake() {
		targetingL3ReefIntake = switch (targetingFace) {
			case NEAR_CENTER, FAR_LEFT, FAR_RIGHT -> true;
			case FAR_CENTER, NEAR_LEFT, NEAR_RIGHT -> false;};
	}

    /*
	 * Subsystem zero(ing) method
	 */
	public Command zero() {
		return Commands.runOnce(() -> {
					PivotSubsystem.mInstance.setCurrentPosition(PivotConstants.kFullStowPosition);
					//ElevatorSubsystem.mInstance.setCurrentPosition(
                    //    ElevatorConstants.converter.toAngle(ElevatorSubsystem.STOW));
				})
				.withName("Zero");
	}


    public Command DeployIntakePivot(){
      
       return Commands.runOnce(() -> PivotSubsystem.mInstance.applySetpoint(PivotSubsystem.DEPLOY));
        
    }

    public Command idleIntakes() {
		return Commands.parallel(
						IntakeSubsystem.mInstance.setpointCommand(IntakeSubsystem.IDLE),
						IndexerSubsystem.mInstance.setpointCommand(IndexerSubsystem.IDLE))
				.withName("Idle Intakes");
	}

	public Command autoAlign(boolean rightSide)
	{
		return Commands.sequence(Commands.defer(
				() -> {
					return new AutoAlignPID2(DriveSubsystem.mInstance, rightSide);
				},
				Set.of(DriveSubsystem.mInstance)));
		
	}

	public Command goToScoringPose(Level level) {
		return Commands.sequence(Commands.defer(
				() -> {
					
					Pose2d finalL1Pose = FieldLayout.handleAllianceFlip(
						FieldLayout.getCoralScoringPose(targetingBranch)
								.transformBy(new Transform2d(
									SuperSystemConstants.kL1CoralOffsetFactor.unaryMinus(),
										targetingBranch.getKey().isLeft()
												? SuperSystemConstants.kL1CoralHorizontalOffsetFactor
														.unaryMinus()
												: SuperSystemConstants.kL1CoralHorizontalOffsetFactor,
										new Rotation2d())),
						RobotConstants.isRedAlliance);

						Pose2d scoringPose = FieldLayout.handleAllianceFlip(
								FieldLayout.getCoralScoringPose(targetingBranch), RobotConstants.isRedAlliance);
						
						return new FollowTagPIDToPose(finalL1Pose, level);
						//return new FollowTagPIDToPose(scoringPose, level);
					
				},
				Set.of(DriveSubsystem.mInstance)));
	}


    public Command ParkIntakePivot()
    {

       return Commands.runOnce(() -> PivotSubsystem.mInstance.applySetpoint(PivotSubsystem.STOW_CLEAR));
    }

    public Command L1Elevator(){
        return  ElevatorSubsystem.mInstance.setpointCommand(ElevatorSubsystem.L1_SCORE);
    }

    public Command HomeElevator(){
        return Commands.sequence(
			setState(State.GROUND_CORAL),
            ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.IDLE),
			ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.SAFE_ARM_STOW),
            EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.STOW),
            ElevatorSubsystem.mInstance.setpointCommand(ElevatorSubsystem.STOW)
        );
    }

    public Command HomeEF(){
        return  
        Commands.sequence(
			ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.SAFE_ARM_STOW),
            EndEffectorSubsystem.mInstance.setpointCommand(EndEffectorSubsystem.STOW),
            ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.IDLE)
        );
        
    }

    public Command L1EF(){
        return  
		Commands.either(
			Commands.sequence(
				setState(State.L1_CORAL),
				ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.L1_SCORE),
				EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.L1_SCORE),
				ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.L1_SCORE_LOW)
				
			),
				Commands.either(
					Commands.waitSeconds(0.1), //do nothing, we have ground coral
				Commands.sequence(
					ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.OUTTAKE),
					Commands.waitSeconds(1),
					ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.IDLE),
					ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.SAFE_ARM_STOW),
					EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.STOW),
					ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.STOW),
					setState(State.GROUND_CORAL)
				),
				() -> state == State.GROUND_CORAL),
			() -> state == State.HOLD_CORAL

        );
    }

    public Command EFL2SuperPinch(){
        //toggle the super pinch position and stow
		return Commands.either(
			
			Commands.sequence(
				setState(State.GROUND_CORAL),
				ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.SAFE_ARM_STOW),
				EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.STOW),
				ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.IDLE),
				ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.STOW)
			),
			Commands.sequence(
				setState(State.SUPER_PINCH),
				ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.L2_ALGAE),
				EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.L2_ALGAE)
				
			),
			() -> state == State.SUPER_PINCH);
    }

	public Command StowIntake(){
		return PivotSubsystem.mInstance.setpointCommand(PivotSubsystem.STOW_CLEAR);
	}

	public Command EFL3SuperPinch(){

		 //toggle the super pinch position and stow
		 return Commands.either(
			
		 Commands.sequence(
			 setState(State.GROUND_CORAL),
			 ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.SAFE_ARM_STOW),
			 EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.STOW),
			 ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.IDLE),
			 ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.STOW)
		 ),
		 Commands.sequence(
			 setState(State.SUPER_PINCH),
			 ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.L3_ALGAE),
			 EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.L3_ALGAE)
			 
		 ),
		 () -> state == State.SUPER_PINCH);
	}

	public Command L4EF(){

		//if we are holding coral, then we can run the command and it should go to the pre-score
		//if we run the command again, we should go from pre-score to score.

		return 
		Commands.either(
			//if we are holding coral
			Commands.sequence(
				ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.L4_SCORE),
				EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.L4_PRESCORE),
				setState(State.L4_CORAL_PRESCORE)
			),
			//if we are not holding coral
			Commands.either(
				Commands.sequence(
					EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.L4_SCORE),
					ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.SCORE),
					Commands.waitSeconds(0.5), // TODO: Test
					//now to bring the system down
					//move the robot forward a small amount
					//new MoveForwardSlowPP(DriveSubsystem.mInstance),
					ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.L4_RETRACT),
					//EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.STOW),
					ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.IDLE),
					//ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.STOW),
					setState(State.GROUND_CORAL) // set ready for ground coral
					
				),
					//do nothing, we aren't in the correct state - no coral held - no scoring state
					Commands.waitSeconds(0.1)
							
				,
				() -> state == State.L4_CORAL_PRESCORE)
			,
		() -> state == State.HOLD_CORAL);

		
	}


	

	public Command L3EF(){

		//if we are holding coral, then we can run the command and it should go to the pre-score
		//if we run the command again, we should go from pre-score to score.

		return 
		Commands.either(
			//if we are holding coral
			Commands.sequence(
				ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.SAFE_ARM_STOW),
				EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.L3_PRESCORE),
				ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.L3_SCORE),
				setState(State.L3_CORAL_PRESCORE)
			),
			//if we are not holding coral
			Commands.either(
				Commands.sequence(
					EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.L3_SCORE),
					ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.SCORE),
					Commands.waitSeconds(0.5),
					//new MoveForwardSlowPP(DriveSubsystem.mInstance),
					//now to bring the system down
					//ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.SAFE_ARM_STOW),
					//EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.STOW),
					ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.IDLE),
					//ElevatorSubsystem.mInstance.setpointCommandWithWait(ElevatorSubsystem.STOW),
					setState(State.GROUND_CORAL) // set ready for ground coral
					
				),
					//do nothing, we aren't in the correct state - no coral held - no scoring state
					Commands.waitSeconds(0.1)
							
				,
				() -> state == State.L3_CORAL_PRESCORE)
			,
		() -> state == State.HOLD_CORAL);

		
	}

	public Command L4Score(){

		return Commands.either(Commands.sequence(
			EndEffectorSubsystem.mInstance.setpointCommandWithWait(EndEffectorSubsystem.L4_SCORE),
            ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.SCORE)

		), L4EF(), () -> state == State.L4_CORAL_PRESCORE);
	}

    public Command ClawOn(){
        return ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.OUTTAKE);
    }

    public Command ClawOff(){
        return ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.IDLE);
    }

    public Command SuperPinch(){
        return ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.SUPER_PINCH);
    }

    public void setPathFollowing(boolean following){
		isPathFollowing = following;
    }

	public Branch getTargetingBranch() {
		return targetingBranch;
	}

	public Face getTargetingFace() {
		return targetingFace;
	}


    public Command Intake(){
       
        return Commands.sequence(
                     
                          PivotSubsystem.mInstance.setpointCommand(PivotSubsystem.DEPLOY),
						Commands.parallel(
                                
                                ElevatorSubsystem.mInstance.setpointCommand(ElevatorSubsystem.INTAKE),
								IntakeSubsystem.mInstance.setpointCommand(IntakeSubsystem.INTAKE),
								IndexerSubsystem.mInstance.setpointCommand(IndexerSubsystem.INTAKE))
								
                        )
						.withDeadline(indexerBeamBrake.stateWaitWithDebounceIfReal(true, 1.5))
                        .andThen(
                            
                                Commands.sequence(
                                    Commands.waitSeconds(0.1),
                                    ElevatorSubsystem.mInstance.setpointCommand(ElevatorSubsystem.STOW),
                                    PivotSubsystem.mInstance.setpointCommand(PivotSubsystem.STOW_CLEAR),
                                    IntakeSubsystem.mInstance.setpointCommand(IntakeSubsystem.IDLE),
                                    IndexerSubsystem.mInstance.setpointCommand(IndexerSubsystem.IDLE),
									setState(State.HOLD_CORAL)
                                    
                                )
                            );
                        
						/* .finallyDo(() -> {
                            
                            IntakeSubsystem.mInstance.applySetpoint(IntakeSubsystem.IDLE);
                            IndexerSubsystem.mInstance.applySetpoint(IndexerSubsystem.IDLE);
                            ElevatorSubsystem.mInstance.setpointCommand(ElevatorSubsystem.STOW);
                           
                        })*/
                       // .withName("Coral Intake On");
       
       
    }

    public Command waitForEndEffectorL1Move(){
        return Commands.waitUntil(
            () -> EndEffectorSubsystem.mInstance.getPosition().lte(EndEffectorConstants.kL1Score)
        );
    }

    public Command waitForEndEffectorStowMove(){
        return Commands.waitUntil(
            () -> EndEffectorSubsystem.mInstance.getPosition().lte(EndEffectorConstants.kStowPosition)
        );
    }

    public Command waitForElevatorL1(){
        return Commands.waitUntil(
            () -> ElevatorSubsystem.mInstance.nearPosition(ElevatorConstants.converter.toAngle(ElevatorConstants.kL1ScoringHeight))
        );
    }

    public Command exhaustCoralIntake() {
		return Commands.sequence(
						Commands.parallel(
								
								IndexerSubsystem.mInstance.setpointCommand(IndexerSubsystem.EXHAUST),
								IntakeSubsystem.mInstance.setpointCommand(IntakeSubsystem.EXHAUST)),
						Commands.waitTime(Units.Seconds.of(1.0)),
						
						IndexerSubsystem.mInstance.setpointCommand(IndexerSubsystem.IDLE),
						IntakeSubsystem.mInstance.setpointCommand(IntakeSubsystem.IDLE))
				.handleInterrupt(() -> {
					IntakeSubsystem.mInstance.applySetpoint(IntakeSubsystem.IDLE);
					IndexerSubsystem.mInstance.applySetpoint(IndexerSubsystem.IDLE);
					
				});
	}

    

    public Command setState(State state) {
		return Commands.runOnce(() -> this.state = state);
	}

    public static enum State {
		TUCK,
		SPIT,
		GROUND_CORAL,
		STATION,
		HOLD_CORAL,
		HOLD_ALGAE,
		L1_CORAL,
		L2_CORAL,
		L3_CORAL,
		L3_CORAL_PRESCORE,
		L4_CORAL_PRESCORE,
		L4_CORAL,
		L2_ALGAE,
		L3_ALGAE,
		PROCESSOR,
		SUPER_PINCH,
		NET,
		GULP,
		GROUND_CORAL_WITH_ALGAE;
	}


}
