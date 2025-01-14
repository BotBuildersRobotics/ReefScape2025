package frc.robot.subsystems.elevator;
import org.littletonrobotics.junction.AutoLog;

//Interface to log the status of the elevator motors

public interface ElevatorIO {
    default void updateInputs(ElevatorIOInputs inputs) {}

    @AutoLog
    class ElevatorIOInputs {
        //this is an output, it will be set to either true or false
        public boolean elevatorConnected = true;
        //we want to log the temp of the motor (output)
        public double elevatorTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double elevatorRPS = 0.0;
        
        //we pass (input) in a duty cycle, lets log that as well.
        public double elevatorDutyCycle = 0.0;
        
      
    }

    //we can create methods that our concrete implementations can perform.
    default void setElevatorDutyCycle(double percent) {}

}
