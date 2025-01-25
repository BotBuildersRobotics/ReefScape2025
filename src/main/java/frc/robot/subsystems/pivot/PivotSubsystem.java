package frc.robot.subsystems.pivot;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.subsystems.pivot.PivotIO.PivotIOInputs;;




public class PivotSubsystem extends SubsystemBase {

    //notice the static, this is shared 
    public static PivotSubsystem mInstance;

    private PivotSystemState currentState = PivotSystemState.STOWED;

    //I like having a static instance to the subsystem - we only have one subsystem, we don't need more instances.
    //this is a singleton pattern
	public static PivotSubsystem getInstance() {
		if (mInstance == null) {
			
            if(Robot.isReal()) {
                
                mInstance = new PivotSubsystem(new PivotIOPhoenix6());
            }else{
                mInstance = new PivotSubsystem(new PivotIOSim());
            }
		}
		return mInstance;
	}

    //position of 0 represents the arm being held horizontally forward
    //this allows us to model gravity feedforward with a cosine.

    public enum PivotSystemState{
        STOWED(90),
        INTAKE(0),
        HUMAN_PLAYER(45),
        RAISED(60);

        public int angle;

        PivotSystemState(int angle) {
            this.angle = angle;
            
        }
    }

    private PivotIO io;
    //the class below gets auto created by the use of the @autolog attribute in the IntakeIO.java file.
    private PivotIOInputsAutoLogged inputs = new PivotIOInputsAutoLogged();
   

    public PivotSubsystem(PivotIO io) {
        //this could either be a simulation object, a REV motor object (yuck) or the Phoenix6 motor object (yum)
        this.io = io;

    }

    public void setWantedState(PivotSystemState wantedState){
        this.currentState = wantedState;
    }

    public double getCurrentPosition(){
        return currentState.angle;
    }

    @Override
    public void periodic() {
   
        //this actually writes to the log file.
        io.updateInputs(inputs);
        SmartDashboard.putString("Pivot State",currentState.toString());
        Logger.processInputs("Pivot", inputs);
        
        io.setPivotAngle(inputs.pivotPosition);

    }
}