package frc.robot.subsystems.pivot;
import org.littletonrobotics.junction.AutoLog;

public interface PivotIO {
    default void updateInputs(PivotIOInputs inputs) {}

    @AutoLog
    class PivotIOInputs {
        //this is an output, it will be set to either true or false
        public boolean pivotConnected = true;
        //we want to log the temp of the motor (output)
        public double pivotTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double pivotRPS = 0.0;
        
       
        public double pivotCurrent = 0.0;

        public double pivotPosition = 0.0;
        
        public double pivotMotorPos = 0.0;
    }

    

}
