package frc.robot.subsystems.endEffector;
import org.littletonrobotics.junction.AutoLog;

public interface EndEffectorIO {
    default void updateInputs(EndEffectorIOInputs inputs) {}

    @AutoLog
    class EndEffectorIOInputs {
       
        public boolean motorArmConnected = false;

        public double armPivotPosition = 0.0;
        
        public double desiredArmPosition = 0;

        public boolean motorClawConnected = false;

        public double clawPosition = 0.0;
        
        public double desiredClawPosition = 0;

        public double clawSupplyCurrent = 0;

        public double clawVelocity = 0;

        public double clawTemp = 0;
      
    }

    default void openClaw(){}
    
    default void closeClaw(){}
    
    default void setArmPosition(double angle){}

    default double getArmAngle(){return 0;}
    

}
