package frc.robot.subsystems.vision;

import frc.robot.LimelightHelpers;
import frc.robot.subsystems.vision.CameraIO.CameraIOOutputs;
import org.photonvision.PhotonCamera;
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
    PhotonCamera camera = new PhotonCamera("photonvision");
    public CameraSubsytem(CameraIO io) {
        this.io = io;
        this.outputs = io.new CameraIOOutputs();
    }

    @Override
    public void periodic() {
        outputs.limelightExists = LimelightHelpers.getTV("");
        outputs.tx = LimelightHelpers.getTX("");
        outputs.ty = LimelightHelpers.getTY("");
        outputs.ta = LimelightHelpers.getTA("");
        outputs.tl = LimelightHelpers.getLatency_Capture("") + LimelightHelpers.getLatency_Pipeline("");
        
        outputs.photonResult = camera.getLatestResult();
        outputs.photonExists = outputs.photonResult.hasTargets();
        outputs.photonTarget = outputs.photonResult.getBestTarget();

        io.returnOutputs(outputs);
    }
}   