package frc.robot.subsystems.endEffector;
import org.littletonrobotics.junction.AutoLog;

public interface EndEffectorIO {
    default void updateInputs(EndEffectorIOInputs inputs) {}

    @AutoLog
    class EndEffectorIOInputs {
        //this is an output, it will be set to either true or false
        public boolean motorRollerConnected = true;
        //we want to log the temp of the motor (output)
        public double motorRollerTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double motorRollerRPS = 0.0;

        public boolean motorArmConnected = false;

        public double armPivotPosition = 0.0;
        
        public double desiredPivotPosition = 0;

        public double desiredClawPosition = 0;

        public double desiredArmPosition = 0;
      
    }

    //we can create methods that our concrete implementations can perform.
    default void setMotorRollerDutyCycle(double percent) {}

    default void openClaw(){}
    
    default void closeClaw(){}

    default void pivotEffector(double angle){}
    
    default void setArmPosition(double angle){}
    

}
