package frc.robot.subsystems.pivot;
import org.littletonrobotics.junction.AutoLog;

public interface PivotIO {
    default void updateInputs(PivotIOInputs inputs) {}

    @AutoLog
    class PivotIOInputs {
        //this is an output, it will be set to either true or false
        public boolean pivotLeftConnected = true;
        //we want to log the temp of the motor (output)
        public double pivotLeftTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double pivotLeftRPS = 0.0;
        
       
        public double pivotLeftCurrent = 0.0;

        public double pivotLeftPosition = 0.0;
        
        public double pivotLeftMotorPos = 0.0;

       //repeat for right motor
        public boolean pivotRightConnected = true;
        public double pivotRightTemperature = 0.0;
        public double pivotRightRPS = 0.0;
        public double pivotRightCurrent = 0.0;
        public double pivotRightPosition = 0.0;
        public double pivotRightMotorPos = 0.0;


        public double pivotPosition = 0.0;
    }

    

}
