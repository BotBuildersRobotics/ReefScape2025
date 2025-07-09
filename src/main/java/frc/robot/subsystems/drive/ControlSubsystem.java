// PS5 Controller Binds
// A = cross
// B = circle
// X = square
// Y = triangle
// L BUMPER = L1
// R BUMPER = R1


package frc.robot.subsystems.drive;

import static edu.wpi.first.units.Units.MetersPerSecond;

import java.util.function.Supplier;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller; //Test for ps5 controller
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.drive.AutoAlignPID2;
import frc.robot.generated.TunerConstants;
import frc.robot.lib.FieldLayout.Level;
import frc.robot.subsystems.SuperSystem;
import frc.robot.subsystems.clawSubsystem.ClawSubsystem;

public class ControlSubsystem {

    public static final ControlSubsystem mInstance = new ControlSubsystem();

	private CommandXboxController driver = ControlBoardConstants.mDriverController;
	private CommandPS5Controller operator = ControlBoardConstants.mOperatorController;

	private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
	private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();


	private final Trigger overrideTrigger = driver.start();
	private OverrideBehavior overrideBehavior = OverrideBehavior.NONE;

    public void configureBindings() {
		DriveSubsystem.mInstance.setDefaultCommand(DriveSubsystem.mInstance.followSwerveRequestCommand(
				DriveConstants.teleopRequest, DriveConstants.teleopRequestUpdater));
		
        //back button to re-seed heading       
        driver.back()
				.onTrue(Commands.runOnce(
								() -> DriveSubsystem.mInstance.getGeneratedDrive().seedFieldCentric(), DriveSubsystem.mInstance)
						.ignoringDisable(true));

		driverControls();
	
	}

    public void driverControls() {
		SuperSystem s = SuperSystem.mInstance;

		/*driver.b().onTrue(
		
		);*/

		driver.rightTrigger().onTrue(
			s.Intake()
		).onFalse(
			s.idleIntakes()
		);

		driver.leftTrigger().onTrue(
			s.exhaustCoralIntake()
		).onFalse(
			s.idleIntakes()
		);

		driver.a().onTrue(
			s.ElevatorUp()
		);

		bindAutoAlign(true, driver.rightBumper());
		
		bindAutoAlign(false, driver.leftBumper());
		
		driver.x().onTrue(
			s.HomeElevator()
		);

		driver.povUp().onTrue(
			//s.L4EF()
			//command for scoring at the front
			s.FRONTL4EF()
		);

		driver.povLeft().onTrue(
			s.L3EF()
		);

		driver.povRight().onTrue(
			s.L2EF() //score from the front
		);

		driver.povDown().onTrue(
			s.L1EF()
		);

		driver.start().onTrue(
			s.StowIntake()
		);

		operator.circle().onTrue(
			s.SuperPinch()
		).onFalse(
			s.ClawOff()
		);

		operator.triangle().onTrue(
			s.EFL3SuperPinch()
		);

		operator.cross().onTrue(
			s.EFL2SuperPinch()
		);

		operator.L1().onTrue(
			s.NetScore()
		).onFalse(
			s.ClawShoot()
		);

		operator.R1().onTrue(
			s.ClawEject()	
		).onFalse(
			ClawSubsystem.mInstance.setpointCommand(ClawSubsystem.IDLE)
		);

		SwerveRequest.RobotCentric alignDrive = new SwerveRequest.RobotCentric();
		
		double SlowSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond) * 0.08;

		operator.povLeft()
		.whileTrue(
			DriveSubsystem.mInstance.getDrivetrain().applyRequest(() -> 
			alignDrive.withVelocityX(( 0) ) 
			.withVelocityY((SlowSpeed) ) 
			

		));

		SwerveRequest.RobotCentric slowMoveRight = new SwerveRequest.RobotCentric();
		
		operator.povRight()
		.whileTrue(
			DriveSubsystem.mInstance.getDrivetrain().applyRequest(() -> 
			alignDrive.withVelocityX(( 0) ) 
			.withVelocityY((-SlowSpeed) ) 
			

		));

		operator.povUp()
		.whileTrue(
			DriveSubsystem.mInstance.getDrivetrain().applyRequest(() -> 
			alignDrive.withVelocityY(( 0) ) 
			.withVelocityX((SlowSpeed) ) 
			
		));

		operator.povDown()
		.whileTrue(
			DriveSubsystem.mInstance.getDrivetrain().applyRequest(() -> 
			alignDrive.withVelocityY(( 0) ) 
			.withVelocityX((-SlowSpeed) ) 
			

		));



		overrideTrigger.onFalse(Commands.deferredProxy(() -> overrideBehavior.action.get()));

    }

    public void setRumble(boolean on) {
		ControlBoardConstants.mDriverController.getHID().setRumble(RumbleType.kBothRumble, on ? 1.0 : 0.0);
	}


	public void bindCoralAutoScore(Level level, Trigger button) {

		button.onTrue(SuperSystem.mInstance
						.goToScoringPose(level)
						.asProxy()
						.until(overrideTrigger)
						.unless(overrideTrigger)
						.onlyWhile(button)
						.withName("Auto Align " + level.toString())
		);
		
	}

	public void bindAutoAlign(boolean rightSide, Trigger button){
		button.onTrue(SuperSystem.mInstance
						.autoAlign(rightSide)
						.asProxy()
						.until(overrideTrigger)
						.unless(overrideTrigger)
						.onlyWhile(button)
						.withName("Auto Align PID")
		).onFalse(
			Commands.runOnce(() ->			
					ControlSubsystem.mInstance.setRumble(false))
		);
	}

	public static enum OverrideBehavior {
		/*CORAL_SCORE_L1(() -> SuperSystem.mInstance.softCoralScore()),
		CORAL_SCORE_L2(() -> SuperSystem.mInstance.coralScore(Level.L2)),
		CORAL_SCORE_L3(() -> SuperSystem.mInstance.coralScore(Level.L3)),
		CORAL_SCORE_L4(() -> SuperSystem.mInstance.coralScore(Level.L4)),*/
		
		NONE(() -> Commands.none());

		public final Supplier<Command> action;

		private OverrideBehavior(Supplier<Command> overrideAction) {
			action = overrideAction;
		}
	}

}
