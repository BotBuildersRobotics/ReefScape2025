package frc.robot.subsystems.vision;

import frc.robot.lib.LimelightHelpers;
import frc.robot.subsystems.vision.CameraIO.CameraIOOutputs;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSubsytem extends SubsystemBase{
    public static CameraSubsytem mInstance;

    public static CameraSubsytem getInstance() {
        if(mInstance == null) {
            mInstance = new CameraSubsytem(new CameraIO());
        }
        return mInstance;
    }

    CameraIO io;
    CameraIOOutputs outputs;
    public CameraSubsytem(CameraIO io) {
        this.io = io;
        this.outputs = io.new CameraIOOutputs();
    }

    @Override
    public void periodic() {
        outputs.targetExists = LimelightHelpers.getTV("");
        outputs.tx = LimelightHelpers.getTX("");
        outputs.ty = LimelightHelpers.getTY("");
        outputs.ta = LimelightHelpers.getTA("");
        outputs.tl = LimelightHelpers.getLatency_Capture("") + LimelightHelpers.getLatency_Pipeline("");
        io.returnOutputs(outputs);
    }
}   