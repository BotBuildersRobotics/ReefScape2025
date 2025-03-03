// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import java.util.List;
import java.util.Map;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.units.TimeUnit;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.elevator.ElevatorHomeCommand;
import frc.robot.commands.elevator.ElevatorL1Command;
import frc.robot.commands.endEffector.EndEffectorArmIntake;
import frc.robot.commands.endEffector.EndEffectorArmL4;
import frc.robot.commands.endEffector.EndEffectorClawClose;
import frc.robot.commands.endEffector.EndEffectorClawOpen;
import frc.robot.commands.endEffector.EndEffectorIdle;
import frc.robot.commands.endEffector.EndEffectorPivotIntake;
import frc.robot.commands.endEffector.EndEffectorPivotL4;
import frc.robot.commands.endEffector.IntakeClampCommand;
import frc.robot.commands.endEffector.EndEffectorIntake;
import frc.robot.commands.intake.IntakeIdleCommand;
import frc.robot.commands.intake.IntakeOnCommand;
import frc.robot.commands.pivot.IntakePivotCommand;
import frc.robot.commands.pivot.StowPivotCommand;
import frc.robot.commands.drive.AutoAlignPID;
import frc.robot.commands.drive.AutoAlignPID2;
import frc.robot.commands.drive.AutoAlignment;
import frc.robot.commands.drive.AutoLineUpReef;
import frc.robot.commands.drive.ControllerRumbleCommand;
import frc.robot.commands.drive.PathFindToPose;
import frc.robot.commands.drive.TagAutoAlign;
import frc.robot.commands.elevator.ElevatorHomeCommand;
import frc.robot.commands.intake.AlgaeIntake;
import frc.robot.commands.intake.AutoL1Delivery;
import frc.robot.commands.intake.HumanPlayerIntake;
import frc.robot.commands.intake.IntakeIdleCommand;
import frc.robot.commands.intake.IntakeOnCommand;
import frc.robot.commands.intake.IntakeOnTillBeamBreakCommand;
import frc.robot.commands.intake.IntakeReverseCommand;
import frc.robot.commands.pivot.IntakePivotCommand;
import frc.robot.commands.pivot.StowPivotCommand;
import frc.robot.commands.leds.SetLedCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.SuperSystem;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.drive.ReefTargeting;
import frc.robot.subsystems.drive.ReefTargeting.ReefBranch;
import frc.robot.subsystems.drive.ReefTargeting.ReefBranchLevel;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;
import frc.robot.subsystems.led.LightsSubsystem;
import frc.robot.subsystems.led.LightsSubsystem.LightState;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem.PivotSystemState;
import frc.robot.subsystems.vision.TagVisionSubsystem;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.subsystems.vision.apriltags.AprilTagVision;
import frc.robot.subsystems.vision.apriltags.AprilTagVisionIO;
import frc.robot.subsystems.vision.apriltags.AprilTagVisionIOReal;
import frc.robot.subsystems.vision.apriltags.ApriltagVisionIOSim;
import frc.robot.subsystems.vision.apriltags.PhotonCameraProperties;
import frc.robot.utils.ButtonMapping;
import frc.robot.utils.JoystickInterruptible;
import frc.robot.utils.ButtonMapping.MultiFunctionButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */

 @SuppressWarnings("unused")
public class RobotContainer {

	// get an instance of our subsystem, either sim or pheonix.
	private IntakeSubsystem intakeSubsystem = IntakeSubsystem.getInstance();

	private PivotSubsystem pivotSubsystem = PivotSubsystem.getInstance();

	private ElevatorSubsystem elevatorSubsystem = ElevatorSubsystem.getInstance();

	private LightsSubsystem leds = LightsSubsystem.getInstance();

	private EndEffectorSubsystem endEffectorSubsystem = EndEffectorSubsystem.getInstance();

	private LightsSubsystem lightsSubsystem = LightsSubsystem.getInstance();

	private SuperSystem superSystem = SuperSystem.getInstance();


	
	private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond) * 0.5; // kSpeedAt12Volts desired top speed
	private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond) * 0.5; // 3/4 of a rotation per second
																						// max angular velocity

	private double SlowSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond) * 0.08; // kSpeedAt12Volts desired top speed
	private double SlowAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond) * 0.3; // 3/4 of a rotation per second
																						
																						/* Setting up bindings for necessary control of the swerve drive platform */
	private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
			.withDeadband(MaxSpeed )
			.withRotationalDeadband(MaxAngularRate) // Add a 10% deadband
			.withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive
	
	
	private final SwerveRequest.RobotCentric alignDrive = new SwerveRequest.RobotCentric()
			.withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive
			

	private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
	private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
	private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric()
			.withDriveRequestType(DriveRequestType.OpenLoopVoltage);

	
	private final CommandXboxController driverControl = new CommandXboxController(0);
	private final CommandXboxController operatorControl = new CommandXboxController(1);

	public final CommandSwerveDrivetrain drivetrain =  TunerConstants.createDrivetrain();

	/*private TagVisionSubsystem aprilTagVisionSubsystem = new TagVisionSubsystem(drivetrain, new VisionIOLimelight[]{
			new VisionIOLimelight("limelight-back", drivetrain::getRotation),
			//new VisionIOLimelight("limelight-front", drivetrain::getRotation)

		});*/
	



	private final Telemetry logger = new Telemetry(MaxSpeed);

	/* Path follower */
	private final SendableChooser<Command> autoChooser;

	public RobotContainer() {
		
		NamedCommands.registerCommand("L1AutoDelivery", 
				
				Commands.runOnce( () -> {
					pivotSubsystem.setWantedState(PivotSystemState.HUMAN_PLAYER);
					intakeSubsystem.setWantedState(IntakeSystemState.REVERSE);
				}
				)
			);

		
		NamedCommands.registerCommand("LightShow", 
			Commands.runOnce( () -> leds.coralStagedLed())
		);

		autoChooser = AutoBuilder.buildAutoChooser("Tests");
		if(SmartDashboard.containsKey("Auto Mode")) {
			SmartDashboard.getEntry("Auto Mode").close();
		}
		SmartDashboard.putData("Auto Mode", autoChooser);

		configureBindings();
		

		//drivetrain.resetPose(new Pose2d(3, 3, new Rotation2d()));
	}

	/**
	 * Use this method to define your trigger->command mappings. Triggers can be
	 * created via the
	 * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
	 * an arbitrary
	 * predicate, or via the named factories in {@link
	 * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
	 * {@link
	 * CommandXboxController
	 * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
	 * PS4} controllers or
	 * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
	 * joysticks}.
	 */
	private void configureBindings() {

		// setup our control scheme here.
		// Note that X is defined as forward according to WPILib convention,
		// and Y is defined as to the left according to WPILib convention.
		
		//resets the field position
		driverControl.start().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

		
		

		drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX((-driverControl.getLeftY() * MaxSpeed) ) // Drive forward with negative Y (forward)
                    .withVelocityY((-driverControl.getLeftX() * MaxSpeed) ) // Drive left with negative X (left)
                    .withRotationalRate((-driverControl.getRightX() * MaxAngularRate)  ) // Drive counterclockwise with negative X (left)
            )
        );

		driverControl.povLeft()
		.whileTrue(
			drivetrain.applyRequest(() -> 
				alignDrive.withVelocityX(( 0) ) 
				.withVelocityY((SlowSpeed) ) 
				

			));

		driverControl.povRight()
		.whileTrue(
				drivetrain.applyRequest(() -> 
				alignDrive.withVelocityX(( 0) ) 
				.withVelocityY((-SlowSpeed) ) 
				

			));
		

		driverControl.povUp()
		.whileTrue(
			drivetrain.applyRequest(() -> 
			alignDrive.withVelocityX(( SlowSpeed) ) 
			.withVelocityY((0) ) 
			

			));

		driverControl.povDown()
		.whileTrue(
			drivetrain.applyRequest(() -> 
			alignDrive.withVelocityX(( -SlowSpeed) ) 
			.withVelocityY((0) ) 
			

			));


		driverControl.leftBumper().whileTrue(new AutoAlignPID2(drivetrain, false))
		.onFalse(drivetrain.applyRequest(() -> brake));

		driverControl.rightBumper().whileTrue(new AutoAlignPID2(drivetrain, true))
		.onFalse(drivetrain.applyRequest(() -> brake));

		
		//toggle the intake, but do it safely
		driverControl.x().onTrue(new InstantCommand( ()->
		{ 
			superSystem.ToogleIntakePivot();
			
			
		}));

		Trigger intakeClampTrigger = new Trigger(() ->{

			if(intakeSubsystem.isBeamBreakTwoTripped() && endEffectorSubsystem.getCurrentState() == EndEffectorState.CLAMP){
					return true;
			}

			return false;
		});

		//automatic clamp the coral
		intakeClampTrigger.onTrue(
			Commands.runOnce(() -> {
				endEffectorSubsystem.closeClaw();
				leds.setStrobeState(LightState.GREEN);
			}).andThen(
				Commands.waitSeconds(0.5).andThen(
					Commands.runOnce(
						() -> {
							endEffectorSubsystem.setWantedState(EndEffectorState.IDLE);
							intakeSubsystem.setWantedState(IntakeSystemState.IDLE);
						}

					).andThen(
						Commands.waitSeconds(0.3).andThen(
							//auto close up the pivot once the system is ready
							//this is software protected and shouldn't close
							//if coral is mis picked.
							Commands.runOnce( () ->superSystem.ToogleIntakePivot())
						)
					)
				)
			)
		);
		

		operatorControl.povUp()
		.onTrue(new AlgaeIntake(intakeSubsystem, pivotSubsystem))
		.onFalse(new IntakeIdleCommand(intakeSubsystem));

		operatorControl.a()
		.onTrue(
			Commands.runOnce(()-> {
				//move the pivot to human player range
				//but hide the end effector
				endEffectorSubsystem.setWantedState(EndEffectorState.IDLE);
				pivotSubsystem.setWantedState(PivotSystemState.HUMAN_PLAYER);

			})

		)
		.onFalse(new IntakeIdleCommand(intakeSubsystem));

		//toggle reef heights
		operatorControl.rightBumper().onTrue(superSystem.ToggleReefHeightUp());
		operatorControl.leftBumper().onTrue(superSystem.ToggleReefHeightDown());
		
		//runs the elevator to the set position
		operatorControl.rightTrigger().onTrue(
			superSystem.RunTargetElevator()
		);

		//driver deliver coral
		driverControl.a().onTrue(
			new InstantCommand(() -> {
				endEffectorSubsystem.openClaw();
				leds.setStrobeState(LightState.FIRE);
			}
			).andThen(Commands.waitSeconds(0.8))
			.andThen(new EndEffectorIdle(endEffectorSubsystem))
			.andThen(new ElevatorHomeCommand(elevatorSubsystem)).andThen(
				() ->
				leds.clear()
			)
		);

		//operator can bring elevator home
		operatorControl.leftTrigger().onTrue(
			new EndEffectorIdle(endEffectorSubsystem).andThen(
			new ElevatorHomeCommand(elevatorSubsystem)).andThen(
			() -> leds.clear()
		));

		
		
		//outtake
		driverControl.leftTrigger()
		.onTrue(Commands.runOnce(() -> intakeSubsystem.setWantedState(IntakeSystemState.REVERSE)))
		.onFalse(Commands.runOnce(() -> intakeSubsystem.setWantedState(IntakeSystemState.IDLE)));
		
		//intake
		driverControl.rightTrigger().whileTrue(
			new IntakeOnTillBeamBreakCommand(intakeSubsystem, endEffectorSubsystem, lightsSubsystem, pivotSubsystem)
					.alongWith(
						new ControllerRumbleCommand(driverControl, () -> intakeSubsystem.isBeamBreakOneTripped())
					)	
		
			).onFalse(
					Commands.runOnce(() -> intakeSubsystem.setWantedState(IntakeSystemState.IDLE))
					
			);
		
		//Test way to show how to set reef target and get the pose

		/*driverControl.rightBumper().onTrue(
			Commands.runOnce(() ->
				{
					endEffectorSubsystem.openClaw();
					leds.setStrobeState(LightState.ORANGE);
				}
			)
		);
		driverControl.leftBumper().onTrue(
			Commands.runOnce(() ->
			{
				endEffectorSubsystem.closeClaw();
				leds.setStrobeState(LightState.FIRE);
			}
			)
		);*/

		drivetrain.registerTelemetry(logger::telemeterize);

		
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 * 
	 */
	public Command getAutonomousCommand() {
		
		return autoChooser.getSelected();
		// return Commands.print("Auto command selected");
	}
}
