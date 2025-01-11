package frc.robot.subsystems.vision;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.util.PixelFormat;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.vision.VisionPipeline;

public class CameraSubsytem extends SubsystemBase{
    public static CameraSubsytem mInstance;

    public static CameraSubsytem getInstance() {
        if(mInstance == null) {
            mInstance = new CameraSubsytem(640, 480, 30);
        }
        return mInstance;
    }

    private UsbCamera camera;
    private MjpegServer inputServer;
    private CvSink cvSink;
    private CvSource source;
    private MjpegServer outputServer;
    final String camName = "USB Camera 0";
    final String camPath = "0";
    final String inputServerName = "serve_USB Camera 0";
    final int inputServerPort = 1181;
    final String cvName = "opencv_USB Camera 0"; 
    final String sourceName = "Blur";
    final String outputServerName = "serve_Blur";
    final int outputServerPort = 1182;

    public CameraSubsytem(int width, int height, int fps) {
        camera = new UsbCamera(camName, camPath); //TODO: Figure out what camPath is a string the docs didnt show this
        inputServer = new MjpegServer(inputServerName, inputServerPort);
        inputServer.setSource(camera);
        cvSink = new CvSink(inputServerName);
        cvSink.setSource(camera);
        source = new CvSource(cvName, PixelFormat.kMJPEG, width, height, fps);
        outputServer = new MjpegServer(outputServerName, outputServerPort);
        outputServer.setSource(source);
    }

    VisionPipeline vision = new VisionPipeline() {

        @Override
        public void process(Mat image) {
            double satTolerance = 50;
            double valTolerance = 50;

            // Mask the image
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
            Scalar lower = new Scalar(0, satTolerance, valTolerance);
            Scalar upper = new Scalar(255, 100, 100);
            Mat mask = new Mat();
            Core.inRange(image, lower, upper, mask);
        }
        
    };

    @Override
    public void periodic() {
        
    }
}