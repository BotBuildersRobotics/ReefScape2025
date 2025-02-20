package frc.robot.subsystems.pivot;
import org.littletonrobotics.junction.AutoLog;

public interface PivotIO {
    default void updateInputs(PivotIOInputs inputs) {}

    @AutoLog
    class PivotIOInputs {
        

       //repeat for right motor
        public boolean pivotRightConnected = true;
        public double pivotRightTemperature = 0.0;
        public double pivotRightRPS = 0.0;
        public double pivotRightCurrent = 0.0;
        public double pivotRightPosition = 0.0;
        public double pivotRightMotorPos = 0.0;


        public double pivotPosition = 0.0;
    }

    

}
