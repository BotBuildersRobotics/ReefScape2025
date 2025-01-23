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
			mInstance = new IntakeSubsystem(new IntakeIOPhoenix6());
            if(!Robot.isReal()) {
                //TODO: Create sim instance
            }
		}
		return mInstance;
	}

    private IntakeIO io;
    //the class below gets auto created by the use of the @autolog attribute in the IntakeIO.java file.
    private IntakeIOInputs inputs = new IntakeIOInputs();
   

    public IntakeSubsystem(IntakeIO io) {
        //this could either be a simulation object, a REV motor object (yuck) or the Phoenix6 motor object (yum)
        this.io = io;

    }

    public void intakeOn(){
        this.io.setTopMotorDutyCycle(1);
    }

    public void intakeOff(){
        this.io.setTopMotorDutyCycle(0);
    }

    @Override
    public void periodic() {
   
        //this actually writes to the log file.
        io.updateInputs(inputs);

    }
}