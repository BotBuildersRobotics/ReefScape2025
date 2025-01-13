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
        
        //we pass (input) in a duty cycle, lets log that as well.
        public double pivotLeftDutyCycle = 0.0;
        

        //repeat for other motor
        public boolean pivotRightConnected = true;
        public double pivotRightTemperature = 0.0;
        public double pivotRightRPS = 0.0;
        public double pivotRightDutyCycle = 0.0;
      
    }

    //we can create methods that our concrete implementations can perform.
    default void setPivotLeftDutyCycle(double percent) {}
    default void setPivotRightDutyCycle(double percent) {}

}
