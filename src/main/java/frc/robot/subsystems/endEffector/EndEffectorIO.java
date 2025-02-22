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
        
       
        public double motorPivotCurrent = 0;
        public boolean motorPivotConnected = true;
        public double motorPivotTemperature = 0.0;
        
        public double motorPivotPosition = 0;

        public boolean endEffectorBeamBreakTripped = false;

        public double armPivotPosition = 0;

        public double desiredPivotPos = 10;

        public double desiredArmPos = -12;

        public boolean motorArmConnected = false;

        public double motorArmCurrent = 0;

        public double motorArmPosition = 0;

        public boolean isCoralDetected = false;

        public boolean isBeamBreakConnected = false;
      
    }

    //we can create methods that our concrete implementations can perform.
    default void setMotorRollerDutyCycle(double percent) {}

    default void setEndEffectorPivotPosition(double angle){}

    default void setArmPosition(double angle){}

    default boolean isCoralDetected(){return false;}

}
