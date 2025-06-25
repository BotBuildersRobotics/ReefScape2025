package frc.robot.subsystems.drive;

import java.util.function.Supplier;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.drive.AutoAlignPID2;
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

        driver.a().onTrue(
			//s.HomeEF()
			s.ClawOn()
		).onFalse(
			s.ClawOff()
		);

		driver.b().onTrue(
			s.L4Score()
		);

		
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

		bindCoralAutoScore(Level.L1, driver.povRight());

		// Top Right Paddle
		bindCoralAutoScore(Level.L2, driver.povUp());

		// Bottom Left Paddle
		bindCoralAutoScore(Level.L3, driver.povLeft());

		// Bottom Right Paddle
		bindCoralAutoScore(Level.L4, driver.povDown());

		operator.a().onTrue(
			s.SuperPinch()
		).onFalse(
			s.ClawOff()
		);

		operator.b().onTrue(
			s.EFSuperPinch()
		);


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
