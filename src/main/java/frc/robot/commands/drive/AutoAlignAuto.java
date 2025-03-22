package frc.robot.commands.drive;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.LimelightResults;
import frc.robot.LimelightHelpers.LimelightTarget_Fiducial;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.led.LightsSubsystem;
import frc.robot.subsystems.led.LightsSubsystem.LightState;



public class AutoAlignAuto extends Command {

  private Timer stopTimer, dontSeeTag;
  
  private final CommandSwerveDrivetrain swerveDrive;

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
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_CONSTRAINTS
       );

  private final ProfiledPIDController yTranslationController =
      new ProfiledPIDController(
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_P,
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_I,
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_D,
        Constants.TrajectoryConstants.AUTO_LINEUP_TRANSLATION_CONSTRAINTS
        );

  private boolean isRightTargetted = false;

  /**
   * Creates a new AutoAlign.
   *
   * @param swerveDrive The subsystem for the swerve drive
   */
  public AutoAlignAuto(CommandSwerveDrivetrain swerveDrive, boolean rightSide) {
    

    xTranslationController.setTolerance(0.005);
    yTranslationController.setTolerance(0.005);
    rotationController.setTolerance(0.05);
    
    this.swerveDrive = swerveDrive;
    this.isRightTargetted = rightSide;
   
    this.stopTimer = new Timer();
    this.dontSeeTag = new Timer();

    this.stopTimer.start();
    this.dontSeeTag.start();
   
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    //check to see if we have a target
    if(LimelightHelpers.getTV("limelight-back")){
        double[] positions = LimelightHelpers.getBotPose_TargetSpace("limelight-back");
        

        double ySpeed = MathUtil.clamp( MathUtil.applyDeadband(yTranslationController.calculate(positions[0], this.isRightTargetted ? Constants.ALIGN_RIGHT_OFFSET : Constants.ALIGN_LEFT_OFFSET), 0.05), -Constants.AUTO_ALIGN_MAX_SPEED, Constants.AUTO_ALIGN_MAX_SPEED); //TODO
        double xSpeed =   MathUtil.clamp(MathUtil.applyDeadband(-xTranslationController.calculate(positions[2], Constants.ALIGN_DIS_REEF), 0.05), -Constants.AUTO_ALIGN_MAX_SPEED, Constants.AUTO_ALIGN_MAX_SPEED);
        double rotation =   MathUtil.clamp(MathUtil.applyDeadband(-rotationController.calculate(positions[4], 0), 0.05), -Constants.AUTO_ALIGN_MAX_SPEED, Constants.AUTO_ALIGN_MAX_SPEED);

       

        final SwerveRequest.ApplyRobotSpeeds robotSpeed = new SwerveRequest.ApplyRobotSpeeds();
      
        robotSpeed
            .withSpeeds(new ChassisSpeeds(xSpeed, ySpeed, rotation))
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
      
        swerveDrive.setControl(robotSpeed);

        if(!rotationController.atSetpoint() || !yTranslationController.atSetpoint() || !xTranslationController.atSetpoint()){
          stopTimer.reset();
        }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    swerveDrive.setControl(new SwerveRequest.SwerveDriveBrake());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    return this.dontSeeTag.hasElapsed(1) || stopTimer.hasElapsed(0.3);

  }
}