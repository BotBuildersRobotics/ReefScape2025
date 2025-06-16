package frc.robot.subsystems.pivot;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PivotConstants;
import frc.robot.Robot;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;



public class PivotSubsystem extends SubsystemBase {

    //notice the static, this is shared 
    public static PivotSubsystem mInstance;
    public EndEffectorSubsystem endEffectorSubsystem;
    private IntakeSubsystem intakeSubsystem;

    public static PivotSystemState currentState = PivotSystemState.STOWED;

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
        STOWED(PivotConstants.kFullStowPosition),
        STOW_CLEAR(PivotConstants.kStowClearPosition),
        DEPLOY(PivotConstants.kDeployPosition),
        EXHAUST(PivotConstants.kExhaustPosition),
        INDEXERHOLD(PivotConstants.kIndexerHold);
       

        public Angle angle;

        PivotSystemState(Angle angle) {
            this.angle = angle;
            
        }
    }

    private PivotIO io;
    //the class below gets auto created by the use of the @autolog attribute in the IntakeIO.java file.
    private PivotIOInputsAutoLogged inputs = new PivotIOInputsAutoLogged();
   

    public PivotSubsystem(PivotIO io) {
        //this could either be a simulation object, a REV motor object (yuck) or the Phoenix6 motor object (yum)
        this.io = io;
        this.endEffectorSubsystem = EndEffectorSubsystem.getInstance();
        this.intakeSubsystem = IntakeSubsystem.getInstance();
        
       

    }

    public void setWantedState(PivotSystemState wantedState){
      

        PivotSubsystem.currentState = wantedState;
        inputs.pivotPosition = wantedState.angle;
    }


    public PivotSystemState getCurrentState(){
        return PivotSubsystem.currentState;
    }

     public Supplier<PivotSystemState> getCurrentStateSupplier(){
        return () -> PivotSubsystem.currentState;
    }

    public Angle getCurrentPosition(){
        return currentState.angle;
    }

    public boolean isAtLocation(PivotSystemState targetLocationState) {
        return true;
        //return io.getPivotAngle() <= targetLocationState.angle + 1 && io.getPivotAngle() >= targetLocationState.angle - 1;
    }

    @Override
    public void periodic() {

       
        //this actually writes to the log file.
        io.updateInputs(inputs);
       // SmartDashboard.putString("Pivot State",currentState.toString());
        Logger.processInputs("Pivot", inputs);

        //check to see that angle of the arm is clear of the intake.
        if(inputs.pivotPosition != null){
            inputs.pivotPosition = currentState.angle;
        }


    }
}