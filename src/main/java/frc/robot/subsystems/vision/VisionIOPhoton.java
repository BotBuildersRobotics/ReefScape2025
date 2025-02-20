package frc.robot.subsystems.vision;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.math.geometry.Rotation2d;

public class VisionIOPhoton implements VisionIO {
	private final String name;
	private final PhotonCamera camera;

	/**
	 * Creates a new VisionIOPhoton.
	 *
	 * @param name The configured name of the Photon camera.
	 */
	public VisionIOPhoton(String name) {
		this.name = name;
		this.camera = new PhotonCamera(this.name);
	}

	@Override
	public <T extends VisionIOInputs> void updateInputs(T mInputs) {
		PhotonIOInputs inputs;
		if (mInputs instanceof PhotonIOInputs) {
			inputs = (PhotonIOInputs) mInputs;
		} else {
			throw new IllegalArgumentException(
					"Limelight updateInputs must take PhotonIOInputs, not " + mInputs.getClass().toString());
		}

		inputs.connected = true;

		PhotonPipelineResult result = camera.getLatestResult();
		inputs.result = result;
		inputs.bestTarget = result.getBestTarget();
		inputs.targetObservations = new PhotonTargetObservation[result.targets.size()];
		double totalDistance = 0;
		for (int i = 0; i < inputs.targetObservations.length; i++) {
			inputs.targetObservations[i] = new PhotonTargetObservation(
				result.targets.get(i),
				PhotonUtils.calculateDistanceToTargetMeters(inputs.robotState.cameraHeight(), 0, inputs.robotState.cameraPitch(), Math.toRadians(inputs.bestTarget.pitch))); //https://docs.photonvision.org/en/latest/docs/programming/photonlib/using-target-data.html#calculating-distance-to-target thanks for all your help photonvision docs
			totalDistance += inputs.targetObservations[i].distanceToTarget();
		}
		inputs.meanDistance = totalDistance / inputs.targetObservations.length;
		inputs.latestTargetObservation = new TargetObservation(new Rotation2d(inputs.bestTarget.yaw),
				new Rotation2d(inputs.bestTarget.pitch));
	}
}