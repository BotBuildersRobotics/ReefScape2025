package frc.robot.subsystems.transfer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.subsystems.transfer.TransferIO.TransferIOInputs;




public class TransferSubsystem extends SubsystemBase {

    //notice the static, this is shared 
    public static TransferSubsystem mInstance;

    //I like having a static instance to the subsystem - we only have one subsystem, we don't need more instances.
    //this is a singleton pattern
	public static TransferSubsystem getInstance() {
		if (mInstance == null) {
			mInstance = new TransferSubsystem(new TransferIOPhoenix6());
            if(!Robot.isReal()) {
                //TODO: Create sim instance
            }
		}
		return mInstance;
	}

    private TransferIO io;
    //the class below gets auto created by the use of the @autolog attribute in the IntakeIO.java file.
    private TransferIOInputs inputs = new TransferIOInputs();
   

    public TransferSubsystem(TransferIO io) {
        //this could either be a simulation object, a REV motor object (yuck) or the Phoenix6 motor object (yum)
        this.io = io;

    }

    @Override
    public void periodic() {
   
        //this actually writes to the log file.
        io.updateInputs(inputs);

    }
}