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
       
        Logger.processInputs("EndEffector", inputs);

    }

    public void EndEffectorRollersOn(double dutycycle){
        io.setMotorRollerDutyCycle(dutycycle);
    }

    public void SetEndEffectorPivotPos(double angle){
        inputs.desiredPivotPos = angle;
        io.setEndEffectorPivotPosition(angle);
    }

    public void SetEndEffectorArmPos(double angle){
        inputs.desiredArmPos = angle;
        io.setArmPosition(angle);
    }
}