package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.util.Units;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class PivotIOPhoenix6 implements PivotIO{
   //left and right is based off the forward direction
    private TalonFX pivotMotor;

    
    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public PivotIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        pivotMotor = TalonFXFactory.createDefaultTalon(Ports.PIVOT);
       
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
        TalonUtil.applyAndCheckConfiguration(pivotMotor, Constants.PivotConstants.PivotFXConfig());
       
       // pivotMotor.optimizeBusUtilization();
       
    }

    @Override
    public void updateInputs(PivotIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
        inputs.pivotConnected = BaseStatusSignal.refreshAll(
                        pivotMotor.getStatorCurrent(),
                        pivotMotor.getDeviceTemp(),
                        pivotMotor.getPosition(),
                        pivotMotor.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.pivotTemperature = pivotMotor.getDeviceTemp().getValueAsDouble();
        inputs.pivotRPS = pivotMotor.getRotorVelocity().getValueAsDouble();
        inputs.pivotCurrent = pivotMotor.getStatorCurrent().getValueAsDouble();
        inputs.pivotMotorPos = pivotMotor.getPosition().getValueAsDouble();

        //360 degrees of pivot movement would be roughly 50 motor rotations
        //90 degrees of pivot movement is around 12.5 rotations or 0.13 rotations per degree

       
        
        double desiredRotations = inputs.pivotPosition * 0.13;
        pivotMotor.setControl(new MotionMagicVoltage(desiredRotations));
       
        
    }

   

   
}
