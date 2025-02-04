package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.AutoLog;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public interface VisionIO {

  @AutoLog
  public static class VisionIOInputs {
    public boolean connected = false;
    public TargetObservation latestTargetObservation = 
      new TargetObservation(new Rotation2d(), new Rotation2d());
  }

  public static class AprilTagIOInputs extends VisionIOInputs{
    public int[] tagIds = new int[0];
    public PoseObservation[] poseObservations = new PoseObservation[0];
  }

  public static class PhotonIOInputs extends VisionIOInputs{
    public PhotonPipelineResult result;
    public PhotonTrackedTarget bestTarget;
  }

  /** Represents the angle to a simple target, not used for pose estimation. */
  public static record TargetObservation(Rotation2d tx, Rotation2d ty) {}

  /** Represents a robot pose sample used for pose estimation. */
  public static record PoseObservation(
      double timestamp,
      Pose3d pose,
      double ambiguity,
      int tagCount,
      double averageTagDistance,
      PoseObservationType type) {}

  public static enum PoseObservationType {
    MEGATAG_1,
    MEGATAG_2,
    PHOTONVISION
  }

  public default <T extends VisionIOInputs> void updateInputs(T inputs) {}
}