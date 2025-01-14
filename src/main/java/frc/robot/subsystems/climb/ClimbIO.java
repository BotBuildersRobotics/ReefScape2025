package frc.robot.subsystems.climb;
import org.littletonrobotics.junction.AutoLog;

public interface ClimbIO {
    default void updateInputs(ClimbIOInputs inputs) {}

    @AutoLog
    class ClimbIOInputs {
        //this is an output, it will be set to either true or false
        public boolean climbOneConnected = true;
        //we want to log the temp of the motor (output)
        public double climbOneTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double climbOneRPS = 0.0;
        
        //we pass (input) in a duty cycle, lets log that as well.
        public double climbOneDutyCycle = 0.0;
        

        //repeat for other motor
        public boolean climbTwoConnected = true;
        public double climbTwoTemperature = 0.0;
        public double climbTwoRPS = 0.0;
        public double climbTwoDutyCycle = 0.0;
      
    }

    //we can create methods that our concrete implementations can perform.
    default void setClimbOneDutyCycle(double percent) {}
    default void setClimbTwoDutyCycle(double percent) {}

}