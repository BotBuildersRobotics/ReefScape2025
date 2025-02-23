package frc.robot.subsystems.elevator;
import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Voltage;

//Interface to log the status of the elevator motors

public interface ElevatorIO {
    default void updateInputs(ElevatorIOInputs inputs) {}

    @AutoLog
    class ElevatorIOInputs {
        //this is an output, it will be set to either true or false
        public boolean elevatorLeftConnected = true;
        //we want to log the temp of the motor (output)
        public double elevatorLeftTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double elevatorLeftRPS = 0.0;

        public double elevatorLeftCurrent = 0.0;
        
        public double elevatorLeftPosition = 0.0;

        public boolean elevatorRightConnected = true;
        //we want to log the temp of the motor (output)
        public double elevatorRightTemperature = 0.0;
        // we want to log the revolutions per second of the motor (output)
        public double elevatorRightRPS = 0.0;

        public double elevatorRightCurrent = 0.0;
        
        public double elevatorRightPosition = 0.0;

        public boolean elevatorBeamBreakTripped = false;

        public double desiredElevatorPosition = 0.0;
      
    }

    //TBD: can we measure in terms of mm ? The slides can't go to exact measurements
    //as they are driven by a sprocket, but we could do approx.
    default void setElevatorPosition(double position) {}
    default void setVoltage(Voltage volts){}

    default void resetElevatorZero(){}

}
