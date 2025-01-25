package frc.robot.commands.drive;

import java.io.Console;
import java.io.IOException;
import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.ApplyRobotSpeeds;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.VisionConstants;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;


public class AlignToTagPhotonvision extends Command {
  private CommandSwerveDrivetrain drivetrain;
 // private PhotonVision photonvision;
  PhotonCamera cam;
  AprilTagFieldLayout aprilTagFieldLayout;

  double x_error;
  double y_error;
  double theta_error;
  double angleToTag;
  Transform3d distanceToTag;
  boolean XFinished;
  boolean YFinished;
  boolean ThetaFinished;

  /** Creates a new Command that aligns the robot angle to an apriltag using the Limelight. 
   * <br></br> This command <b>DOES DRIVE</b>
  */
  public AlignToTagPhotonvision(CommandSwerveDrivetrain drivetrain) {
    
    this.drivetrain = drivetrain;
    cam = new PhotonCamera("FrontCam");
    cam.setDriverMode(false);

    try{
      aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2025Reefscape.m_resourceFile); }
    catch(IOException IOE){
      IOE.printStackTrace();
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    List<PhotonPipelineResult> resultList = cam.getAllUnreadResults();

    PhotonPipelineResult result = null;
    if (resultList.size() != 0) {
        result = resultList.get(resultList.size() - 1);
        PhotonTrackedTarget bestTarget = result.getBestTarget();
    
    
            if (bestTarget != null) {
                angleToTag = bestTarget.bestCameraToTarget.getRotation().getAngle();
                distanceToTag = bestTarget.bestCameraToTarget; // getTranslationToAprilTag may be incorrect

                x_error = -(0.75 - getBestTargetX(bestTarget)); // forward back
                y_error = -( getBestTargetY(bestTarget)); // l - r error
                // System.out.println("x: " + Units.metersToInches(m_photonvision.getBestTargetX()));
                theta_error = -(angleToTag - (Math.PI));
                // if theta not aligned, don't drive yet
                if(Math.abs(theta_error) > 0.1){
                    x_error = 0.0;
                    y_error = 0.0;
                }
    
        }
    }

    //drivetrain.applyRequest(null)
    SwerveRequest.ApplyRobotSpeeds robotMovement = new ApplyRobotSpeeds();
    robotMovement
            .withSpeeds(new ChassisSpeeds( 10.4 * x_error,  10.4 * y_error, 10.5 * theta_error))
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
           
            
      // drivetrain.applyRequest(() -> robotMovement);
    
    drivetrain.setControl(robotMovement);

    if(Math.abs(x_error) <= 0){
      XFinished = true;
    }
    if(Math.abs(y_error) <= 0){
      YFinished = true;
    }
    if(Math.abs(theta_error) <= 0.1){
      ThetaFinished = true;
    }
  
  }

  public double getBestTargetX(PhotonTrackedTarget bestTarget){
    return bestTarget.bestCameraToTarget.getX();// - VisionConstants.cameraOffsets.get("lastCamName").getX();
  }
  public double getBestTargetY(PhotonTrackedTarget bestTarget){
    return bestTarget.bestCameraToTarget.getY();// - VisionConstants.cameraOffsets.get(lastCamName).getY();
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return XFinished && YFinished && ThetaFinished;
  }
}