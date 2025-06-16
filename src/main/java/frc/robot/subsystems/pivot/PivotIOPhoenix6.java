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
    private TalonFX intakePivotMotor;

    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public PivotIOPhoenix6() {

        intakePivotMotor = TalonFXFactory.createDefaultTalon(Ports.INTAKE_PIVOT);
        
      TalonUtil.applyAndCheckConfiguration(intakePivotMotor, Constants.PivotConstants.PivotFXConfig());
       
       
    }

    @Override
    public void updateInputs(PivotIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
     
        //repeat for right motor
        inputs.pivotRightConnected = BaseStatusSignal.refreshAll(
            intakePivotMotor.getStatorCurrent(),
                        intakePivotMotor.getDeviceTemp(),
                        intakePivotMotor.getPosition(),
                        intakePivotMotor.getVelocity())
                        .isOK();

        inputs.pivotRightTemperature = intakePivotMotor.getDeviceTemp().getValueAsDouble();
        inputs.pivotRightRPS = intakePivotMotor.getRotorVelocity().getValueAsDouble();
        inputs.pivotRightCurrent = intakePivotMotor.getStatorCurrent().getValueAsDouble();
        inputs.pivotRightMotorPos = intakePivotMotor.getPosition().getValueAsDouble();

        if(inputs.pivotPosition != null){
            intakePivotMotor.setControl(new MotionMagicVoltage(inputs.pivotPosition));
        }
        
    }   

    @Override
    public double getPivotAngle() {
        //TODO: calculate new angle.
        return intakePivotMotor.getPosition().getValueAsDouble() * 0.71;
    }

   
}
