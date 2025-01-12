package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.AutoLog;

public class CameraIO {
    public CameraIOOutputs returnOutputs(CameraIOOutputs outputs) {return outputs;} //TODO: Make this do... something...

    public CameraIO() {}

    @AutoLog
    class CameraIOOutputs {
        public boolean targetExists = false;
        public double tx = 0.0;
        public double ty = 0.0;
        public double ta = 0.0;
        public double tl = 0.0;
    }

}
