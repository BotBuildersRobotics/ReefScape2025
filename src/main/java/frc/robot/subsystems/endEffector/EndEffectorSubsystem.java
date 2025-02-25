package frc.robot.subsystems.endEffector;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;



public class EndEffectorSubsystem extends SubsystemBase {

    //notice the static, this is shared 
    public static EndEffectorSubsystem mInstance;

    

    //I like having a static instance to the subsystem - we only have one subsystem, we don't need more instances.
    //this is a singleton pattern
	public static EndEffectorSubsystem getInstance() {
		if (mInstance == null) {
			mInstance = new EndEffectorSubsystem(new EndEffectorIOPhoenix6());
            if(!Robot.isReal()) {
                //TODO: Create sim instance
            }
		}
		return mInstance;
	}

    private EndEffectorIO io;
    //the class below gets auto created by the use of the @autolog attribute in the IntakeIO.java file.
    private EndEffectorIOInputsAutoLogged inputs = new EndEffectorIOInputsAutoLogged();
   

    public EndEffectorSubsystem(EndEffectorIO io) {
        //this could either be a simulation object, a REV motor object (yuck) or the Phoenix6 motor object (yum)
        this.io = io;

    }

    @Override
    public void periodic() {
   
        //this actually writes to the log file.
        io.updateInputs(inputs);
        inputs.desiredPivotPosition = currentState.end_effector_pivot_angle;
        inputs.desiredArmPosition = currentState.end_effector_arm_angle;
        inputs.desiredClawPosition = currentState.claw_position;
        SetEndEffectorArmPos();
        SetEndEffectorPivotPos();
        Logger.processInputs("EndEffector", inputs);

    }

    public enum EndEffectorState 
    {
        IDLE(10, 5,0),
		INTAKE(-20,-13,0),
        L1_DEPOSIT(0,0,1),
        L2_L3_DEPOSIT(20,20,1),
        L4_DEPOSIT(115,55,1),
		REVERSE(.0,0, 0);
        
        public int end_effector_pivot_angle;
        public double end_effector_arm_angle;
        public double claw_position;
       
        
		EndEffectorState(double armAngle, int pivotAngle, double claw_position) {
			this.end_effector_arm_angle = armAngle;
            this.end_effector_pivot_angle = pivotAngle;
            this.claw_position = claw_position;
            
		}
    }
    
    private EndEffectorState currentState = EndEffectorState.IDLE;

    public EndEffectorState getCurrentState()
    {
        return currentState;
    }

    public void setWantedState(EndEffectorState state) {

        currentState = state;

        if(currentState == EndEffectorState.INTAKE){
            io.depowerPivotServos();
        }

    }

    public void SetEndEffectorRollers(double dutycycle){
        io.setMotorRollerDutyCycle(dutycycle);
    }

    public void SetEndEffectorPivotPos(){
       
        io.pivotEffector(currentState.end_effector_pivot_angle);
    }

    public void SetEndEffectorArmPos(){
       
        io.setArmPosition(currentState.end_effector_arm_angle);
    }

    public void closeClaw(){
        io.closeClaw();
    }

    public void openClaw(){
        io.openClaw();
    }

    public boolean isArmInIntakePosition(){
        //TODO: check to see if our arm is in position
        return io.getArmAngle() <= EndEffectorState.INTAKE.end_effector_arm_angle + 5;
    }

   
}