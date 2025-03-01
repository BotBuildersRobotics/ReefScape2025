package frc.robot.subsystems.vision.apriltags;

import static frc.robot.LogPaths.*;
import static frc.robot.VisionConstants.*;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.apriltags.MapleMultiTagPoseEstimator.RobotPoseEstimationResult;
import frc.robot.utils.Alert;
import java.util.List;
import java.util.Optional;
import org.littletonrobotics.junction.Logger;

public class AprilTagVision extends SubsystemBase {
    private final AprilTagVisionIO io;
    private final AprilTagVisionIO.VisionInputs inputs;

    private final MapleMultiTagPoseEstimator multiTagPoseEstimator;
    private final CommandSwerveDrivetrain driveSubsystem;
    private final Alert[] camerasDisconnectedAlerts;

    public AprilTagVision(
            AprilTagVisionIO io,
            List<PhotonCameraProperties> camerasProperties,
            CommandSwerveDrivetrain driveSubsystem) {
        super("Vision");
        this.io = io;
        this.inputs = new AprilTagVisionIO.VisionInputs(camerasProperties.size());
        this.camerasDisconnectedAlerts = new Alert[camerasProperties.size()];
        for (int i = 0; i < camerasProperties.size(); i++) {
            this.camerasDisconnectedAlerts[i] = new Alert(
                    "Photon Camera " + i + " '" + camerasProperties.get(i).name + "' disconnected",
                    Alert.AlertType.WARNING);
            this.camerasDisconnectedAlerts[i].setActivated(false);
        }

        this.multiTagPoseEstimator = new MapleMultiTagPoseEstimator(
                fieldLayout, new CameraHeightAndPitchRollAngleFilter(), camerasProperties);
        this.driveSubsystem = driveSubsystem;
    }

    private Optional<RobotPoseEstimationResult> result = Optional.empty();

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs(APRIL_TAGS_VISION_PATH + "Inputs", inputs);

        for (int i = 0; i < inputs.camerasInputs.length; i++)
            this.camerasDisconnectedAlerts[i].setActivated(!inputs.camerasInputs[i].cameraConnected);

         result = multiTagPoseEstimator.estimateRobotPose(inputs.camerasInputs, driveSubsystem.getState().Pose);
       
          result.ifPresent(robotPoseEstimationResult ->
                driveSubsystem.addVisionMeasurement(robotPoseEstimationResult.pointEstimation, getResultsTimeStamp()));

        Logger.recordOutput(
                APRIL_TAGS_VISION_PATH + "Results/Estimated Pose", displayVisionPointEstimateResult(result));
        
      
        double[] m_poseArray = new double[3];
        //send this
        Pose2d estimatedP = displayVisionPointEstimateResult(result);
        m_poseArray[0] = estimatedP.getX();
        m_poseArray[1] = estimatedP.getY();
        m_poseArray[2] = estimatedP.getRotation().getDegrees();
       
       
        Logger.recordOutput(APRIL_TAGS_VISION_PATH + "Results/Presented", result.isPresent());
    }

    private Pose2d displayVisionPointEstimateResult(Optional<RobotPoseEstimationResult> result) {
        if (result.isEmpty()) return new Pose2d(-114514, -114514, new Rotation2d());

        if (Double.isInfinite(result.get().rotationalStandardDeviationRadians))
            return new Pose2d(result.get().pointEstimation.getTranslation(), driveSubsystem.getState().Pose.getRotation());
        return result.get().pointEstimation;
    }

    private double getResultsTimeStamp() {
        return inputs.inputsFetchedRealTimeStampSeconds - getResultsAverageLatencySeconds(inputs.camerasInputs);
    }

    private static double getResultsAverageLatencySeconds(AprilTagVisionIO.CameraInputs[] camerasInputs) {
        if (camerasInputs.length == 0) return 0;
        double totalLatencySeconds = 0;
        for (AprilTagVisionIO.CameraInputs cameraInputs : camerasInputs)
            totalLatencySeconds += cameraInputs.resultsDelaySeconds;

        return totalLatencySeconds / camerasInputs.length;
    }
}