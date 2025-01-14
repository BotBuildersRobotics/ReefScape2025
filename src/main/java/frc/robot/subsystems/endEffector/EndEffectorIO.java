package frc.robot.subsystems.endEffector;
import org.littletonrobotics.junction.AutoLog;

public interface EndEffectorIO {
    default void updateInputs(EndEffectorIOInputs inputs) {}

    @AutoLog
    class EndEffectorIOInputs {
        //this is an output, it will be set to either true or false
        public boolean motorConnected = true;
        //we want to log the temp of the motor (output)
        public double motorTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double motorRPS = 0.0;
        
        //we pass (input) in a duty cycle, lets log that as well.
        public double motorDutyCycle = 0.0;
        
      
    }

    //we can create methods that our concrete implementations can perform.
    default void setMotorDutyCycle(double percent) {}

}
