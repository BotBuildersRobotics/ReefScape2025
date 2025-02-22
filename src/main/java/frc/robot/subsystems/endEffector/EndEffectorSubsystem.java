package frc.robot.subsystems.endEffector;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.subsystems.endEffector.EndEffectorIO.EndEffectorIOInputs;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;




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
       
        Logger.processInputs("EndEffector", inputs);

    }

    public enum EndEffectorState 
    {
        IDLE(0.0, 0.0,.0),
		INTAKE(20,.0,.90),
        L1_DEPOSIT(0,0,0),
        L2_L3_DEPOSIT(0,0,0),
        L4_DEPOSIT(40,45,0),
		REVERSE(.0,.0,.0);
        
        public double end_effector_duty_cycle;
        public double end_effector_angle;
        public double arm_pos;

		EndEffectorState(double end_effector_duty_cycle, double end_effector_angle, double arm_pos) {
			this.end_effector_duty_cycle = end_effector_duty_cycle;
            this.end_effector_angle = end_effector_angle;
            this.arm_pos = arm_pos;
            
		}
    }
    
    private EndEffectorState currentState = EndEffectorState.IDLE;

    public EndEffectorState getCurrentState()
    {
        return currentState;
    }

    public void setWantedState(EndEffectorState state) {

        currentState = state;

    }

    public void SetEndEffectorRollers(){
        io.setMotorRollerDutyCycle(currentState.end_effector_duty_cycle);
    }

    public void SetEndEffectorPivotPos(){
        inputs.desiredArmPivotPos = currentState.end_effector_angle;
        io.setEndEffectorPivotPosition(currentState.end_effector_angle);
    }

    public void SetEndEffectorArmPos(){
        inputs.desiredArmPos = currentState.arm_pos;
        io.setArmPosition(currentState.arm_pos);
    }
}