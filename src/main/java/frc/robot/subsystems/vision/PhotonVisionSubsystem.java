package frc.robot.subsystems.vision;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;

public class PhotonVisionSubsystem extends SubsystemBase{
    private final PhotonConsumer consumer;
    private final VisionIO[] io;
    private final PhotonIOInputsAutoLogged[] inputs;
    private final Alert[] disconnectedAlerts;

    public PhotonVisionSubsystem(PhotonConsumer consumer, VisionIO... io) {
        this.consumer = consumer;
        this.io = io;

        this.inputs = new PhotonIOInputsAutoLogged[io.length];
        for(int i = 0; i < inputs.length; i++) {
            inputs[i] = new PhotonIOInputsAutoLogged();
        }

        this.disconnectedAlerts = new Alert[io.length];
        for(int i = 0; i < disconnectedAlerts.length; i++) {
            disconnectedAlerts[i] = 			disconnectedAlerts[i] = new Alert(
					"Vision camera " + Integer.toString(i) + " is disconnected.", AlertType.kWarning);
        }
    }

    public static PhotonVisionSubsystem mInstance;

    public static PhotonVisionSubsystem getInstance() {
        if(mInstance == null) {
            mInstance = new PhotonVisionSubsystem(CommandSwerveDrivetrain.getInstance(), new VisionIOPhoton("photonvision")); //TODO: Change photon name
        }
        return mInstance;
    }

    @FunctionalInterface
	public static interface PhotonConsumer {
		public void accept(
				Pose2d visionRobotPoseMeters,
				double timestampSeconds,
				Matrix<N3, N1> visionMeasurementStdDevs);
	}
}
