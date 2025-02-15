package frc.robot.subsystems.intake;
import org.littletonrobotics.junction.AutoLog;


public interface IntakeIO {
    default void updateInputs(IntakeIOInputs inputs) {}

    @AutoLog
    public class IntakeIOInputs {
       
        //this is an output, it will be set to either true or false
        public boolean intakeConnected = true;
        //we want to log the temp of the motor (output)
        public double intakeTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double intakeMotorRPS = 0.0;
        
        //we pass (input) in a duty cycle, lets log that as well.
        public double intakeMotorVoltage = 0.0;

        public double intakeCurrent = 0;

        //this is an output, it will be set to either true or false
        public boolean transferConnected = true;
        //we want to log the temp of the motor (output)
        public double transferTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double transferMotorRPS = 0.0;
        
        //we pass (input) in a duty cycle, lets log that as well.
        public double transferVoltage = 0.0;

        public double transferCurrent = 0;

        public boolean intakeBeamBreakOneTripped = false;
        public boolean intakeBeamBreakTwoTripped = false;
        
      
    }

    //we can create methods that our concrete implementations can perform.
    default void setIntakeDutyCycle(double percent) {}

    default void setTransferDutyCycle(double percent) {}


}
