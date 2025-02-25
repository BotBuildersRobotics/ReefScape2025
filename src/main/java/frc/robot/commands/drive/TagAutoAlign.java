package frc.robot.commands.drive;


import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.LimelightResults;
import frc.robot.LimelightHelpers.LimelightTarget_Fiducial;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;


import com.ctre.phoenix6.swerve.SwerveRequest;
    
public class TagAutoAlign extends Command {
    private final CommandSwerveDrivetrain drivetrain;
   

    // PID constants for alignment
    private static final double kP_Yaw = 0.02;  // Proportional constant for yaw correction
    private static final double kP_Distance = 0.1; // Proportional constant for distance correction
    private static final double YAW_THRESHOLD = 1.0; // Degrees threshold for alignment
    private static final double DISTANCE_THRESHOLD = 0.1; // Meters threshold for alignment

    public TagAutoAlign(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
       
        addRequirements(drivetrain);
    }
    
    @Override
    public void execute() {
        // Get the latest result from the camera
        LimelightResults results =  LimelightHelpers.getLatestResults("limelight-back");

        if (results.targets_Fiducials.length > 0) {
            LimelightTarget_Fiducial tag = results.targets_Fiducials[0];
            Pose3d tagPoseRobot = tag.getTargetPose_RobotSpace();
        
            double yaw = tagPoseRobot.getX(); // Horizontal offset to the AprilTag
            double distance = tagPoseRobot.getZ(); // Distance to the tag (forward)

            // Calculate adjustments for yaw and forward movement
            double yawAdjustment = kP_Yaw * yaw;
            double distanceAdjustment = kP_Distance * distance;

            // Apply swerve drive request
            drivetrain.applyRequest(() -> 
                new SwerveRequest.FieldCentric()
                    .withVelocityX(distanceAdjustment)
                    .withVelocityY(0.0) // No lateral movement for alignment
                    .withRotationalRate(yawAdjustment)
            );
        } else {
            // Stop the robot if no targets are found
            drivetrain.applyRequest(() -> 
                new SwerveRequest.FieldCentric()
                    .withVelocityX(0.0)
                    .withVelocityY(0.0)
                    .withRotationalRate(0.0)
            );
        }
    }
    
    @Override
    public boolean isFinished() {
       LimelightResults results =  LimelightHelpers.getLatestResults("limelight-back");


        if (results.targets_Fiducials.length > 0) {
             LimelightTarget_Fiducial tag = results.targets_Fiducials[0];
            Pose3d tagPoseRobot = tag.getTargetPose_RobotSpace();
            double yaw = tagPoseRobot.getX();
            double distance = tagPoseRobot.getZ();

            // Finish command when robot is aligned
            return Math.abs(yaw) < YAW_THRESHOLD && Math.abs(distance) < DISTANCE_THRESHOLD;
        }
        return false;
    }
    
    @Override
    public void end(boolean interrupted) {
        // Stop the robot
        drivetrain.applyRequest(() -> 
            new SwerveRequest.FieldCentric()
                .withVelocityX(0.0)
                .withVelocityY(0.0)
                .withRotationalRate(0.0)
        );
    }
}
