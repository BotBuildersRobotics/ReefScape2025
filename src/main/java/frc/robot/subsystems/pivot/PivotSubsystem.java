package frc.robot.subsystems.pivot;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
        STOWED(0),
        INTAKE(119),
        HUMAN_PLAYER(25),
        ALGAE(50),
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
        this.endEffectorSubsystem = EndEffectorSubsystem.getInstance();
        this.intakeSubsystem = IntakeSubsystem.getInstance();

        //we need to check the arm position, if it's in the intake position, we want to swap the state
        //to keep the pivot in the intake position

        if(io.getPivotAngle() > 5){
            //we are currently in the intake position
            if(this.endEffectorSubsystem.getArmAngle() > 15){
                //reset the state to reflect
                PivotSubsystem.currentState = PivotSystemState.INTAKE;
            }
        }
        if(this.intakeSubsystem.isBeamBreakOneTripped()){
            //overwrite this as well
            PivotSubsystem.currentState = PivotSystemState.INTAKE;
        }

    }

    public void setWantedState(PivotSystemState wantedState){
      

        PivotSubsystem.currentState = wantedState;
    }

    public void togglePivot(){

        
        if(PivotSubsystem.currentState == PivotSystemState.STOWED){
            setWantedState(PivotSystemState.INTAKE);
        }else{
             setWantedState(PivotSystemState.STOWED);
        }

        
        //we are currently in the intake position
        if(this.endEffectorSubsystem.getArmAngle() > 5){
            //reset the state to reflect
            PivotSubsystem.currentState = PivotSystemState.INTAKE;
        }
        
        if(this.intakeSubsystem.isBeamBreakOneTripped() || this.intakeSubsystem.isBeamBreakTwoTripped()){
            //overwrite this as well
            PivotSubsystem.currentState = PivotSystemState.INTAKE;
        }
    }

    public PivotSystemState getCurrentState(){
        return PivotSubsystem.currentState;
    }

     public Supplier<PivotSystemState> getCurrentStateSupplier(){
        return () -> PivotSubsystem.currentState;
    }

    public double getCurrentPosition(){
        return currentState.angle;
    }

    public boolean isAtLocation(PivotSystemState targetLocationState) {
        return io.getPivotAngle() <= targetLocationState.angle + 1 && io.getPivotAngle() >= targetLocationState.angle - 1;
    }

    @Override
    public void periodic() {

        if(this.intakeSubsystem.getCurrentState() == IntakeSystemState.AUTO_L1 ){
            //we are happy with this, it's controlled.
            PivotSubsystem.currentState = PivotSubsystem.PivotSystemState.HUMAN_PLAYER;
            return;
        }
   
        if(this.intakeSubsystem.isBeamBreakOneTripped() && PivotSubsystem.currentState == PivotSystemState.STOWED){
            //overwrite this as well
            PivotSubsystem.currentState = PivotSystemState.INTAKE;
        }

        if(this.intakeSubsystem.isBeamBreakTwoTripped() && PivotSubsystem.currentState == PivotSystemState.STOWED){
            //overwrite this as well
            PivotSubsystem.currentState = PivotSystemState.INTAKE;
        }

        //this actually writes to the log file.
        io.updateInputs(inputs);
       // SmartDashboard.putString("Pivot State",currentState.toString());
        Logger.processInputs("Pivot", inputs);

        //check to see that angle of the arm is clear of the intake.
        inputs.pivotPosition = currentState.angle;


    }
}