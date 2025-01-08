package frc.robot.subsystems;
import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
    default void updateInputs(IntakeIOInputs inputs) {}

    @AutoLog
    class IntakeIOInputs {
        //this is an output, it will be set to either true or false
        public boolean topMotorConnected = true;
        //we want to log the temp of the motor (output)
        public double topMotorTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double topMotorRPS = 0.0;
        
        //we pass (input) in a duty cycle, lets log that as well.
        public double topMotorDutyCycle = 0.0;
        
      
    }

    //we can create methods that our concrete implementations can perform.
    default void setTopMotorDutyCycle(double percent) {}

}
