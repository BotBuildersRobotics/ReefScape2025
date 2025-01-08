package frc.robot.subsystems.elevator;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.elevator.ElevatorIO.ElevatorIOInputs;



public class ElevatorSubsystem extends SubsystemBase {

    //notice the static, this is shared 
    public static ElevatorSubsystem mInstance;

    //I like having a static instance to the subsystem - we only have one subsystem, we don't need more instances.
    //this is a singleton pattern. yay
	public static ElevatorSubsystem getInstance() {
		if (mInstance == null) {
            //TODO: add a check to see if we are in sim mode, if we are, then create a new simulation instance.
			mInstance = new ElevatorSubsystem(new ElevatorIOPhoenix6());
		}
		return mInstance;
	}

    private ElevatorIO io;
    //the class below gets auto created by the use of the @autolog attribute in the IntakeIO.java file.
    private ElevatorIOInputs inputs = new ElevatorIOInputs();
   

    public ElevatorSubsystem(ElevatorIO io) {
        //this could either be a simulation object, a REV motor object (yuck) or the Phoenix6 motor object (yum)
        this.io = io;

    }

    @Override
    public void periodic() {
   
        //this actually writes to the log file.
        io.updateInputs(inputs);

    }
}