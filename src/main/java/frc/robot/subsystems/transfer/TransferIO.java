package frc.robot.subsystems.transfer;
import org.littletonrobotics.junction.AutoLog;

public interface TransferIO {
    default void updateInputs(TransferIOInputs inputs) {}

    @AutoLog
    class TransferIOInputs {
        //this is an output, it will be set to either true or false
        public boolean transferPulleyLeftConnected = true;
        //we want to log the temp of the motor (output)
        public double transferPulleyLeftTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double transferPulleyLeftRPS = 0.0;
        
        //we pass (input) in a duty cycle, lets log that as well.
        public double transferPulleyLeftDutyCycle = 0.0;
        

        //repeat for other motor
        public boolean transferPulleyRightConnected = true;
        public double transferPulleyRightTemperature = 0.0;
        public double transferPulleyRightRPS = 0.0;
        public double transferPulleyRightDutyCycle = 0.0;
      
    }

    //we can create methods that our concrete implementations can perform.
    default void setTransferPulleyLeftDutyCycle(double percent) {}
    default void setTransferPulleyRightDutyCycle(double percent) {}

}