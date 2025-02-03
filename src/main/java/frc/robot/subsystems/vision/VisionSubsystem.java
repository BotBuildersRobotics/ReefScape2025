package frc.robot.subsystems.vision;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;

import frc.robot.subsystems.vision.VisionIO.PoseObservationType;
import java.util.LinkedList;
import java.util.List;
import org.littletonrobotics.junction.Logger;

public class VisionSubsystem extends SubsystemBase {
	private final VisionConsumer consumer;
	private final VisionIO[] io;
	private final VisionIOInputsAutoLogged[] inputs;
	private final Alert[] disconnectedAlerts;

	public VisionSubsystem(VisionConsumer consumer, VisionIO... io) {
		this.consumer = consumer;
		this.io = io;

		// Initialize inputs
		this.inputs = new VisionIOInputsAutoLogged[io.length];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = new VisionIOInputsAutoLogged();
		}

		// Initialize disconnected alerts
		this.disconnectedAlerts = new Alert[io.length];
		for (int i = 0; i < inputs.length; i++) {
			disconnectedAlerts[i] = new Alert(
					"Vision camera " + Integer.toString(i) + " is disconnected.", AlertType.kWarning);
		}
	}

	public static VisionSubsystem mInstance = null;

	public static VisionSubsystem getInstance() {
		if (mInstance == null) {

			if (Robot.isReal()) {

				mInstance = new VisionSubsystem(
						CommandSwerveDrivetrain.getInstance(),
						new VisionIOLimelight("limelight", CommandSwerveDrivetrain.getInstance().getRotation()));
			} else {

			}
		}
		return mInstance;
	}

	/**
	 * Returns the X angle to the best target, which can be used for simple servoing
	 * with vision.
	 *
	 * @param cameraIndex The index of the camera to use.
	 */
	public Rotation2d getTargetX(int cameraIndex) {
		return inputs[cameraIndex].latestTargetObservation.tx();
	}

	@Override
	public void periodic() {
		for (int i = 0; i < io.length; i++) {

			io[i].updateInputs(inputs[i]);
			Logger.processInputs("Vision/Camera" + Integer.toString(i), inputs[i]);
		}

		// Initialize logging values
		List<Pose3d> allTagPoses = new LinkedList<>();
		List<Pose3d> allRobotPoses = new LinkedList<>();
		List<Pose3d> allRobotPosesAccepted = new LinkedList<>();
		List<Pose3d> allRobotPosesRejected = new LinkedList<>();

		// Loop over cameras
		for (int cameraIndex = 0; cameraIndex < io.length; cameraIndex++) {
			// Update disconnected alert
			disconnectedAlerts[cameraIndex].set(!inputs[cameraIndex].connected);

			// Initialize logging values
			List<Pose3d> tagPoses = new LinkedList<>();
			List<Pose3d> robotPoses = new LinkedList<>();
			List<Pose3d> robotPosesAccepted = new LinkedList<>();
			List<Pose3d> robotPosesRejected = new LinkedList<>();

			// Add tag poses
			for (int tagId : inputs[cameraIndex].tagIds) {
				var tagPose = Constants.VisionConstants.APRILTAG_LAYOUT.getTagPose(tagId);
				if (tagPose.isPresent()) {
					tagPoses.add(tagPose.get());
				}
			}

			// Loop over pose observations
			for (var observation : inputs[cameraIndex].poseObservations) {
				// Check whether to reject pose
				boolean rejectPose = observation.tagCount() == 0 // Must have at least one tag
						|| Math.abs(observation.pose().getZ()) > Constants.VisionConstants.maxZError // Must have
																										// realistic Z
																										// coordinate

						// TODO: could add check for distance?
						// Must be within the field boundaries
						|| observation.pose().getX() < 0.0
						|| observation.pose().getX() > Constants.VisionConstants.APRILTAG_LAYOUT.getFieldLength()
						|| observation.pose().getY() < 0.0
						|| observation.pose().getY() > Constants.VisionConstants.APRILTAG_LAYOUT.getFieldWidth();

				Logger.recordOutput("Vision/tagcount", observation.tagCount());
				Logger.recordOutput("Vision/getz", observation.pose().getZ());
				Logger.recordOutput("Vision/PoseX", observation.pose().getX());
				Logger.recordOutput("Vision/PoseY", observation.pose().getY());
				Logger.recordOutput("Vision/Rejected", rejectPose);

				// Add pose to log
				robotPoses.add(observation.pose());
				if (rejectPose) {
					robotPosesRejected.add(observation.pose());
				} else {
					robotPosesAccepted.add(observation.pose());
				}

				// Skip if rejected
				if (rejectPose) {

					continue;
				}

				// Calculate standard deviations
				double stdDevFactor = Math.pow(observation.averageTagDistance(), 2.0) / observation.tagCount();
				double linearStdDev = Constants.VisionConstants.linearStdDevBaseline * stdDevFactor;
				double angularStdDev = Constants.VisionConstants.angularStdDevBaseline * stdDevFactor;
				if (observation.type() == PoseObservationType.MEGATAG_2) {
					linearStdDev *= Constants.VisionConstants.linearStdDevMegatag2Factor;
					angularStdDev *= Constants.VisionConstants.angularStdDevMegatag2Factor;
				}
				if (cameraIndex < Constants.VisionConstants.cameraStdDevFactors.length) {
					linearStdDev *= Constants.VisionConstants.cameraStdDevFactors[cameraIndex];
					angularStdDev *= Constants.VisionConstants.cameraStdDevFactors[cameraIndex];
				}

				// Send vision observation

				consumer.accept(
						observation.pose().toPose2d(),
						observation.timestamp(),
						VecBuilder.fill(linearStdDev, linearStdDev, angularStdDev));
			}

			// Log camera datadata
			Logger.recordOutput(
					"Vision/Camera" + Integer.toString(cameraIndex) + "/TagPoses",
					tagPoses.toArray(new Pose3d[tagPoses.size()]));
			Logger.recordOutput(
					"Vision/Camera" + Integer.toString(cameraIndex) + "/RobotPoses",
					robotPoses.toArray(new Pose3d[robotPoses.size()]));
			Logger.recordOutput(
					"Vision/Camera" + Integer.toString(cameraIndex) + "/RobotPosesAccepted",
					robotPosesAccepted.toArray(new Pose3d[robotPosesAccepted.size()]));
			Logger.recordOutput(
					"Vision/Camera" + Integer.toString(cameraIndex) + "/RobotPosesRejected",
					robotPosesRejected.toArray(new Pose3d[robotPosesRejected.size()]));
			allTagPoses.addAll(tagPoses);
			allRobotPoses.addAll(robotPoses);
			allRobotPosesAccepted.addAll(robotPosesAccepted);
			allRobotPosesRejected.addAll(robotPosesRejected);
		}

		// Log summary data
		Logger.recordOutput(
				"Vision/Summary/TagPoses", allTagPoses.toArray(new Pose3d[allTagPoses.size()]));
		Logger.recordOutput(
				"Vision/Summary/RobotPoses", allRobotPoses.toArray(new Pose3d[allRobotPoses.size()]));
		Logger.recordOutput(
				"Vision/Summary/RobotPosesAccepted",
				allRobotPosesAccepted.toArray(new Pose3d[allRobotPosesAccepted.size()]));
		Logger.recordOutput(
				"Vision/Summary/RobotPosesRejected",
				allRobotPosesRejected.toArray(new Pose3d[allRobotPosesRejected.size()]));
	}

	@FunctionalInterface
	public static interface VisionConsumer {
		public void accept(
				Pose2d visionRobotPoseMeters,
				double timestampSeconds,
				Matrix<N3, N1> visionMeasurementStdDevs);
	}
}