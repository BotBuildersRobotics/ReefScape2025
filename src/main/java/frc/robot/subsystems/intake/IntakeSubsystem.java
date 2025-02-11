package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.subsystems.intake.IntakeIO.IntakeIOInputs;




@SuppressWarnings("unused") // Shut it
public class IntakeSubsystem extends SubsystemBase {

    //notice the static, this is shared 
    public static IntakeSubsystem mInstance;

    //I like having a static instance to the subsystem - we only have one subsystem, we don't need more instances.
    //this is a singleton pattern
	public static IntakeSubsystem getInstance() {
		if (mInstance == null) {
			
            if(Robot.isReal()) {
                mInstance = new IntakeSubsystem(new IntakeIOPhoenix6());
            }else{
                mInstance = new IntakeSubsystem(new IntakeIOSim());
            }
		}
		return mInstance;
	}

    private IntakeIO io;
    //the class below gets auto created by the use of the @autolog attribute in the IntakeIO.java file.
    private IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();
   

    public IntakeSubsystem(IntakeIO io) {
        //this could either be a simulation object, a REV motor object (yuck) or the Phoenix6 motor object (yum)
        this.io = io;

    }

    public enum IntakeSystemState 
    {
        IDLE(0.0, 0.0),
		INTAKE(8.0, 8.0),
		REVERSE(-6.0, -6.0),
        HUMAN_PLAYER(8, 8);

        public double intake_voltage;
        public double transfer_voltage;
       

		IntakeSystemState(double intake_voltage, double transfer_voltage) {
			this.intake_voltage = intake_voltage;
            this.transfer_voltage = transfer_voltage;
            
		}
    }

    private IntakeSystemState currentState = IntakeSystemState.IDLE;

    public IntakeSystemState getCurrentState()
    {
        
        return currentState;
    }

    public void setWantedState(IntakeSystemState state) {

        currentState = state;

    }
    
    @Override
    public void periodic() {
   
        //this actually writes to the log file.
        io.updateInputs(inputs);

        SmartDashboard.putString("Intake State",currentState.toString());
        Logger.processInputs("intake", inputs);

        // Stop moving when disabled
        if (DriverStation.isDisabled()) {
            currentState = IntakeSystemState.IDLE;
        }

        io.setIntakeDutyCycle(currentState.intake_voltage);
        io.setTransferDutyCycle(currentState.transfer_voltage);
       

    }
}