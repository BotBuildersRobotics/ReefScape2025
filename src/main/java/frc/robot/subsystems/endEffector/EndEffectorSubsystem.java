package frc.robot.subsystems.endEffector;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

        io.openClaw();

    }

    @Override
    public void periodic() {
   
        //this actually writes to the log file.
        io.updateInputs(inputs);
       
        inputs.desiredArmPosition = EndEffectorSubsystem.currentState.end_effector_arm_angle;
       
        SetEndEffectorArmPos();
      
        Logger.processInputs("EndEffector", inputs);

    }

    public enum EndEffectorState 
    {
        IDLE(15),
		INTAKE(120),
        PRE_CLAMP(140),
        CLAMP(145),
        L1_DEPOSIT(-20),
        L2_L3_DEPOSIT(-20),
        L4_DEPOSIT(-15),
		REVERSE(.0);
       
        public double end_effector_arm_angle;
      
       
        
		EndEffectorState(double armAngle) {
			this.end_effector_arm_angle = armAngle;
            
		}
    }
    
    public static EndEffectorState currentState = EndEffectorState.IDLE;

    public EndEffectorState getCurrentState()
    {
        return EndEffectorSubsystem.currentState;
    }

    public void setWantedState(EndEffectorState state) {

        SmartDashboard.putString("End Effector State", state.toString());
        EndEffectorSubsystem.currentState = state;

      

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
        return io.getArmAngle() >= 15;
    }

    public double getArmAngle()
    {
        return io.getArmAngle();
    }

   
}