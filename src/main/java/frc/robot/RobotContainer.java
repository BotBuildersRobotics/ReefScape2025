// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.elevator.ElevatorHomeCommand;
import frc.robot.commands.elevator.ElevatorL1Command;
import frc.robot.commands.intake.IntakeIdleCommand;
import frc.robot.commands.intake.IntakeOnCommand;
import frc.robot.commands.pivot.IntakePivotCommand;
import frc.robot.commands.pivot.StowPivotCommand;
import frc.robot.commands.drive.AutoAlignment;
import frc.robot.commands.drive.AutoLineUpReef;
import frc.robot.commands.drive.PathFindToPose;
import frc.robot.commands.elevator.ElevatorHomeCommand;
import frc.robot.commands.intake.EndEffectorArmIntake;
import frc.robot.commands.intake.EndEffectorArmL4;
import frc.robot.commands.intake.EndEffectorPivotIntake;
import frc.robot.commands.intake.EndEffectorPivotL4;
import frc.robot.commands.intake.EndEffectorRollerOff;
import frc.robot.commands.intake.EndEffectorRollerReverse;
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
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.led.LightsSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.vision.TagVisionSubsystem;
import frc.robot.utils.JoystickInterruptible;

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

	private TagVisionSubsystem visionSubsystem = TagVisionSubsystem.getInstance();

	private LightsSubsystem leds = LightsSubsystem.getInstance();

	private EndEffectorSubsystem endEffectorSubsystem = EndEffectorSubsystem.getInstance();

	private LightsSubsystem lightsSubsystem = LightsSubsystem.getInstance();

	private SuperSystem superSystem = SuperSystem.getInstance();


	//public final AprilTagVision aprilTagVision;

	private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
	private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second
																						// max angular velocity

	/* Setting up bindings for necessary control of the swerve drive platform */
	private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
			.withDeadband(MaxSpeed * 0.1)
			.withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
			.withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive
																		// motors
	private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
	private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
	private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric()
			.withDriveRequestType(DriveRequestType.OpenLoopVoltage);

	
	private final CommandXboxController driverControl = new CommandXboxController(0);
	private final CommandXboxController operatorControl = new CommandXboxController(1);

	public final CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();// TunerConstants.createDrivetrain();


	private final Command leftCoralAutoDrive = new JoystickInterruptible(new AutoLineUpReef(drivetrain, 0), driverControl, 0.5);
    private final Command rightCoralAutoDrive = new JoystickInterruptible(new AutoLineUpReef(drivetrain, 1), driverControl, 0.5);
   


	private final Telemetry logger = new Telemetry(MaxSpeed);

	/* Path follower */
	private final SendableChooser<Command> autoChooser;

	public RobotContainer() {
		//the below is for simulation / Photon Vision testing
		//Limelight is integrated into the CommandSwerveDrivetrain
		//final List<PhotonCameraProperties> camerasProperties =
				// PhotonCameraProperties.loadCamerasPropertiesFromConfig("5516-2024-OffSeason-Vision");
				// //
				// loads camera properties from
				// deploy/PhotonCamerasProperties/5516-2024-OffSeason-Vision.xml
			//	VisionConstants.photonVisionCameras; // load configs stored directly in VisionConstants.java

		/*if (Robot.isReal()) {

			aprilTagVision = new AprilTagVision(
					new AprilTagVisionIOReal(camerasProperties),
					camerasProperties,
					drivetrain);
		} else {

			aprilTagVision = new AprilTagVision(
					new ApriltagVisionIOSim(
							camerasProperties,
							VisionConstants.fieldLayout,
							() -> {
								return drivetrain.getState().Pose;
							}),
					camerasProperties,
					drivetrain);
		}*/

		autoChooser = AutoBuilder.buildAutoChooser("Tests");
		if(SmartDashboard.containsKey("Auto Mode")) {
			SmartDashboard.getEntry("Auto Mode").close();
		}
		SmartDashboard.putData("Auto Mode", autoChooser);

		configureBindings();
		

		drivetrain.resetPose(new Pose2d(3, 3, new Rotation2d()));
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
		/*drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-driverControl.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-driverControl.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-driverControl.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );*/

		/*driverControl.a().whileTrue(drivetrain.applyRequest(() -> brake));
		driverControl.b()
				.whileTrue(drivetrain.applyRequest(
						() -> point.withModuleDirection(new Rotation2d(-driverControl.getLeftY(),
								-driverControl.getLeftX()))));

		driverControl.pov(0)
				.whileTrue(drivetrain.applyRequest(
						() -> forwardStraight.withVelocityX(0.5).withVelocityY(0)));
		driverControl.pov(180)
				.whileTrue(drivetrain.applyRequest(
						() -> forwardStraight.withVelocityX(-0.5).withVelocityY(0)));

		driverControl.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));*/

		//simple intake controls
		/*driverControl.rightTrigger()
				.onTrue(new IntakeOnCommand(intakeSubsystem))
				.onFalse(new IntakeIdleCommand(intakeSubsystem));*/

		//simplePivotCommands.
		driverControl.leftTrigger().onTrue(new IntakeReverseCommand(intakeSubsystem)).onFalse(new IntakeIdleCommand(intakeSubsystem));

		
		driverControl.y()
		.onTrue(Commands.runOnce(() -> elevatorSubsystem.setVoltage(2)))
		.onFalse(Commands.runOnce(() -> elevatorSubsystem.setVoltage(0)));
		
		
		driverControl.a().onTrue(new IntakePivotCommand(pivotSubsystem));
		driverControl.b().onTrue(new StowPivotCommand(pivotSubsystem));


		operatorControl.rightBumper().onTrue(superSystem.ToggleReefHeight());
		operatorControl.a().onTrue(
			superSystem.RunTargetElevator()
		);

		operatorControl.b().onTrue(
			new ElevatorHomeCommand(elevatorSubsystem)
		);

		/*operatorControl.leftBumper().onTrue(
			Commands.runOnce(() -> elevatorSubsystem.ResetElevatorZero())
		);*/


		driverControl.rightBumper()
		.onTrue(new EndEffectorRollerReverse(endEffectorSubsystem)).onFalse(new EndEffectorRollerOff(endEffectorSubsystem));
		

		driverControl.rightTrigger().onTrue(
					
				new IntakeOnTillBeamBreakCommand(intakeSubsystem, endEffectorSubsystem)
			
			
			).onFalse(
				new ParallelCommandGroup(				
					
				new IntakeIdleCommand(intakeSubsystem),
				new EndEffectorRollerOff(endEffectorSubsystem)
				
				));
		//Test way to show how to set reef target and get the pose

		final ReefTargeting target = new ReefTargeting();
		target.setTarget(ReefBranch.A, ReefBranchLevel.L4 );

		final AutoAlignment exampleAutoAlignment = new AutoAlignment(
				drivetrain,
				() -> target.getTargetPose().plus(new Transform2d(0,-1, Rotation2d.k180deg)));
		//driverControl.y().onTrue(exampleAutoAlignment);


		//driverControl.leftBumper().whileTrue(leftCoralAutoDrive);
        //driverControl.rightBumper().whileTrue(rightCoralAutoDrive);

		drivetrain.registerTelemetry(logger::telemeterize);

		
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 * 
	 */
	public Command getAutonomousCommand() {
		// An example command will be run in autonomous

		/*return new SequentialCommandGroup(

				new PathFindToPose(drivetrain, () -> {
					return new Pose2d(1.82, 7.30, Rotation2d.fromDegrees(91.50136));
				}, 100, 0),
				// new AutoAlignment(drivetrain, () -> new Pose2d()),
				// drivetrain.applyRequest(() -> lateralMovement.withVelocityX( 25))
				// ,
				new WaitCommand(1));
		*/
		return autoChooser.getSelected();
		// return Commands.print("Auto command selected");
	}
}
