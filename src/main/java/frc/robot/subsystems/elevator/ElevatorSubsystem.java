package frc.robot.subsystems.elevator;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;



public class ElevatorSubsystem extends SubsystemBase {

    //notice the static, this is shared 
    public static ElevatorSubsystem mInstance;

    //I like having a static instance to the subsystem - we only have one subsystem, we don't need more instances.
    //this is a singleton pattern. yay
	public static ElevatorSubsystem getInstance() {
		if (mInstance == null) {
			mInstance = new ElevatorSubsystem(new ElevatorIOPhoenix6());
            if(!Robot.isReal()) {
                //TODO: Create sim instance
            }
		}
		return mInstance;
	}

    private ElevatorIO io;
    //the class below gets auto created by the use of the @autolog attribute in the IntakeIO.java file.
    private ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();
   

    public ElevatorSubsystem(ElevatorIO io) {
        //this could either be a simulation object, a REV motor object (yuck) or the Phoenix6 motor object (yum)
        this.io = io;

    }

    @Override
    public void periodic() {
   
        //this actually writes to the log file.
        io.updateInputs(inputs);

        //48 tooth pulley = 75mm diameter
        //1 rotation = 75mm lift
        //75 * 13 = 975mm of lift

        Logger.processInputs("Elevator", inputs);
    }

    public void setElevatorPosition(double position){

        inputs.elevatorPosition = position;
    }

    public void setElevatorPosition(ElevatorPosition position) {
        switch (position) {
            case STOWED:
                inputs.elevatorPosition = 0;
                break;
            case L1:
                inputs.elevatorPosition = 100;
                break;
            case L2:
                inputs.elevatorPosition = 200;
                break;
            case L3:
                inputs.elevatorPosition = 300;
                break;
            case L4:
                inputs.elevatorPosition = 400;
                break;
            default:
                break;
        }
    }

    public enum ElevatorPosition {
        STOWED(-20, 20),
        L1(80, 120),
        L2(180, 220),
        L3(280, 320),
        L4(380, 420);

        public double lowerBound;
        public double upperBound;
        ElevatorPosition(double lower, double upper) {
            lowerBound = lower;
            upperBound = upper;
        }
        
    }
}