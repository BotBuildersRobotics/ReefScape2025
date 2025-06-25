package frc.robot.subsystems.drive;

import static edu.wpi.first.units.Units.MetersPerSecond;

import java.util.function.Supplier;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.drive.AutoAlignPID2;
import frc.robot.generated.TunerConstants;
import frc.robot.lib.FieldLayout.Level;
import frc.robot.subsystems.SuperSystem;

public class ControlSubsystem {

    public static final ControlSubsystem mInstance = new ControlSubsystem();

	private CommandXboxController driver = ControlBoardConstants.mDriverController;
	private CommandXboxController operator = ControlBoardConstants.mOperatorController;

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

		
		bindAutoAlign(true, driver.rightBumper());
		
		bindAutoAlign(false, driver.leftBumper());
		
		driver.x().onTrue(
			s.HomeElevator()
		);

		driver.povUp().onTrue(
			s.L4EF()
		);

		driver.povLeft().onTrue(
			s.L3EF()
		);

		driver.povDown().onTrue(
			s.L1EF()
		);

		/*bindCoralAutoScore(Level.L1, driver.povRight());

		// Top Right Paddle
		bindCoralAutoScore(Level.L2, driver.povUp());

		// Bottom Left Paddle
		bindCoralAutoScore(Level.L3, driver.povLeft());

		// Bottom Right Paddle
		bindCoralAutoScore(Level.L4, driver.povDown());*/

		operator.b().onTrue(
			s.SuperPinch()
		).onFalse(
			s.ClawOff()
		);

		operator.y().onTrue(
			s.EFL3SuperPinch()
		);

		operator.a().onTrue(
			s.EFL2SuperPinch()
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
