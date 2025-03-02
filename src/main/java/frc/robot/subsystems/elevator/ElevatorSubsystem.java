package frc.robot.subsystems.elevator;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Robot;



public class ElevatorSubsystem extends SubsystemBase {

    //notice the static, this is shared 
    public static ElevatorSubsystem mInstance;

    public ElevatorPosition currentState = ElevatorPosition.STOWED;

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

        inputs.desiredElevatorPosition = currentState.lowerBound;

        //48 tooth pulley = 75mm diameter
        //1 rotation = 75mm lift
        //75 * 13 = 975mm of lift

        Logger.processInputs("Elevator", inputs);
    }

   

    public void setManualElevatorPosition(double position)
    {

        inputs.desiredElevatorPosition = position;
    }

    public void setWantedState(ElevatorPosition position){
       this.currentState = position;
    }

    public void setVoltage(double volts){
        io.setVoltage(Units.Volts.of(volts));
    }

    public void setElevatorPosition(ElevatorPosition position) {
        
        //could make this the difference between upper and lower
        
        inputs.desiredElevatorPosition = position.lowerBound;
           
        
    }

    @AutoLogOutput
    public boolean checkElevatorPosition(ElevatorPosition target) {
        return target.isNear(inputs.elevatorLeftRPS) && target.isNear(inputs.elevatorRightPosition);
    }

    public boolean isElevatorUp(){
        if(currentState == ElevatorPosition.STOWED){
            return false;
        }
        return true;
    }

    public enum ElevatorPosition {
        //! TODO Change positions
        STOWED(-0.3, 0.2),
        L1(3, 3.1),
        L2(2.8, 3),
        L3(5, 5.2),
        L4(10.3, 10.5);

        public double lowerBound;
        public double upperBound;
        ElevatorPosition(double lower, double upper) {
            lowerBound = lower;
            upperBound = upper;
        }

        private boolean isNear(double position) { //* This function is needed because java doesnt allow you to chain comparison statments. For reference, see https://www.geeksforgeeks.org/chaining-comparison-operators-python/
            boolean over = this.lowerBound < position;
            boolean under = position < this.upperBound;
            return over && under;
        }
        
    }

    public void ResetElevatorZero(){
        io.resetElevatorZero();
    }

       // Creates a SysIdRoutine
    SysIdRoutine routine =
      new SysIdRoutine(
          new SysIdRoutine.Config(),
          new SysIdRoutine.Mechanism(this::voltageDrive, this::logMotors, this));
    
    public void logMotors(SysIdRoutineLog log){
        io.logMotors(log);
    }

    public void voltageDrive(Voltage volts){
        io.voltageDrive(volts);
    }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) 
    {
        return routine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) 
    {
        return routine.dynamic(direction);
    }

}