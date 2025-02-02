package frc.robot.commands.drive;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.VisionSubsystem;

public class AutoAlignPID extends Command {

  private final CommandSwerveDrivetrain swerveDrive;
  private final VisionSubsystem visionSubsystem;

  private Pose2d Pose;

  private final ProfiledPIDController rotationController =
      new ProfiledPIDController(
          Constants.TrajectoryConstants.AUTO_LINEUP_ROTATION_P,
          Constants.TrajectoryConstants.AUTO_LINEUP_ROTATION_I,
          Constants.TrajectoryConstants.AUTO_LINEUP_ROTATION_D,
          Constants.TrajectoryConstants.AUTO_LINEUP_ROTATION_CONSTRAINTS);

  private final ProfiledPIDController xTranslationController =
      new ProfiledPIDController(
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_P,
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_I,
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_D,
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_CONSTRAINTS);

  private final ProfiledPIDController yTranslationController =
      new ProfiledPIDController(
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_P,
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_I,
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_D,
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_CONSTRAINTS);

  /**
   * Creates a new AutoAlign.
   *
   * @param visionSubsystem The subsystem for vision
   * @param swerveDrive The subsystem for the swerve drive
   * @param targetPose The target pose for the robot to align to
   */
  public AutoAlignPID(CommandSwerveDrivetrain swerveDrive, VisionSubsystem visionSubsystem, Pose2d targetPose) {
   
    this.Pose = targetPose;
    this.swerveDrive = swerveDrive;
    this.visionSubsystem = visionSubsystem;
   
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    super.execute();
    // Gets the error between the desired pos (the target) and the current pos of the robot
    Pose2d drivePose = swerveDrive.getState().Pose; //current robot estimate
    double xPoseError = Pose.getX() - drivePose.getX();
    double yPoseError = Pose.getY() - drivePose.getY();
    double thetaPoseError = Pose.getRotation().getRadians() - drivePose.getRotation().getRadians();

    // Uses the PID controllers to calculate the drive output
    double xOutput = MathUtil.applyDeadband(xTranslationController.calculate(xPoseError, 0), 0.05);
    double yOutput = MathUtil.applyDeadband(yTranslationController.calculate(yPoseError, 0), 0.05);
    double turnOutput =
        MathUtil.applyDeadband(rotationController.calculate(thetaPoseError, 0), 0.05);

    // Enables continuous input for the rotation controller
    rotationController.enableContinuousInput(0, 2 * Math.PI);

    // Gets the chassis speeds for the robot using the odometry rotation (not alliance relative)
    ChassisSpeeds chassisSpeeds =
        ChassisSpeeds.fromFieldRelativeSpeeds(
            xOutput, yOutput, turnOutput, swerveDrive.getState().Pose.getRotation()); 


    final SwerveRequest.ApplyRobotSpeeds robotSpeed = new SwerveRequest.ApplyRobotSpeeds();
      

    robotSpeed
            .withSpeeds(new ChassisSpeeds(chassisSpeeds.vxMetersPerSecond, chassisSpeeds.vyMetersPerSecond, chassisSpeeds.omegaRadiansPerSecond))
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
           
    swerveDrive.setControl(robotSpeed);
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    swerveDrive.setControl(new SwerveRequest.SwerveDriveBrake());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}