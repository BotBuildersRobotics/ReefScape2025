package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.AutoLog;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class CameraIO {
    public CameraIOOutputs returnOutputs(CameraIOOutputs outputs) {return outputs;} //TODO: Make this do... something...

    public CameraIO() {}

    @AutoLog
    class CameraIOOutputs {
        //Limelight
        public boolean limelightExists = false;
        public double tx = 0.0;
        public double ty = 0.0;
        public double ta = 0.0;
        public double tl = 0.0;

        //Photon
        public boolean photonExists = false;
        public PhotonPipelineResult photonResult = null;
        public PhotonTrackedTarget photonTarget = null;
    }

}
