package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class PivotIOPhoenix6 implements PivotIO{
   //left and right is based off the forward direction
   // private TalonFX pivotLeftMotor;
    private TalonFX pivotRightMotor;

    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public PivotIOPhoenix6() {

      pivotRightMotor = TalonFXFactory.createDefaultTalon(Ports.PIVOT_RIGHT);
        
      TalonUtil.applyAndCheckConfiguration(pivotRightMotor, Constants.PivotConstants.PivotFXConfig());
       
       
    }

    @Override
    public void updateInputs(PivotIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
     
        //repeat for right motor
        inputs.pivotRightConnected = BaseStatusSignal.refreshAll(
                        pivotRightMotor.getStatorCurrent(),
                        pivotRightMotor.getDeviceTemp(),
                        pivotRightMotor.getPosition(),
                        pivotRightMotor.getVelocity())
                        .isOK();

        inputs.pivotRightTemperature = pivotRightMotor.getDeviceTemp().getValueAsDouble();
        inputs.pivotRightRPS = pivotRightMotor.getRotorVelocity().getValueAsDouble();
        inputs.pivotRightCurrent = pivotRightMotor.getStatorCurrent().getValueAsDouble();
        inputs.pivotRightMotorPos = pivotRightMotor.getPosition().getValueAsDouble();


        //2.1 * 125 = 262
        //2.1 * 64 = 134.4

        
        double desiredRotations = inputs.pivotPosition * 0.373;
        
        pivotRightMotor.setControl(new MotionMagicVoltage(desiredRotations));
        
    }   

    @Override
    public double getPivotAngle() {
        return pivotRightMotor.getPosition().getValueAsDouble() * 0.71;
    }

   
}
