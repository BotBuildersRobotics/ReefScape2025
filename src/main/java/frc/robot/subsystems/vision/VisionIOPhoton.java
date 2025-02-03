package frc.robot.subsystems.vision;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import org.photonvision.PhotonCamera;

import edu.wpi.first.math.geometry.Rotation2d;

public class VisionIOPhoton implements VisionIO{
    private final String name;

    /**
     * Creates a new VisionIOPhoton.
     *
     * @param name The configured name of the Photon camera.
     */
    public VisionIOPhoton(String name) {
        this.name = name;
    }

    @Override
    public void updateInputs(PhotonIOInputs inputs) {
        PhotonCamera camera = new PhotonCamera(this.name);
        List<PoseObservation> poseObservations = new LinkedList<>();

        inputs.connected = true;
        
    }
}
