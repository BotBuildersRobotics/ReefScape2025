package frc.robot.subsystems.endEffector;
import org.littletonrobotics.junction.AutoLog;

public interface EndEffectorIO {
    default void updateInputs(EndEffectorIOInputs inputs) {}

    @AutoLog
    class EndEffectorIOInputs {
       
        public boolean motorArmConnected = false;

        public double armPivotPosition = 0.0;
        
        public double desiredArmPosition = 0;
      
    }

    default void openClaw(){}
    
    default void closeClaw(){}
    
    default void setArmPosition(double angle){}

    default double getArmAngle(){return 0;}
    

}
