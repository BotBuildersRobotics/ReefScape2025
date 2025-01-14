package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class PivotIOPhoenix6 implements PivotIO{
   //left and right is based off the forward direction
    private TalonFX pivotLeft;
    private TalonFX pivotRight;

    private double dutyCycleLeft = 0;
    private double dutyCycleRight = 0;
    
    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public PivotIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        pivotLeft = TalonFXFactory.createDefaultTalon(Ports.PIVOT_LEFT);
        pivotRight = TalonFXFactory.createDefaultTalon(Ports.PIVOT_RIGHT);
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
        TalonUtil.applyAndCheckConfiguration(pivotLeft, Constants.PivotConstants.PivotFXConfig());
        TalonUtil.applyAndCheckConfiguration(pivotRight, Constants.PivotConstants.PivotFXConfig());
       
    }

    @Override
    public void updateInputs(PivotIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
        inputs.pivotLeftConnected = BaseStatusSignal.refreshAll(
                        
                        pivotLeft.getDeviceTemp(),
                        pivotLeft.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.pivotLeftTemperature = pivotLeft.getDeviceTemp().getValueAsDouble();
        inputs.pivotLeftRPS = pivotLeft.getRotorVelocity().getValueAsDouble();

        //also log the duty cycle we are asking for.
        inputs.pivotLeftDutyCycle = dutyCycleLeft;

        //repeat for other motor
        inputs.pivotRightConnected = BaseStatusSignal.refreshAll(
            pivotRight.getDeviceTemp(),
            pivotRight.getVelocity())
            .isOK();

        inputs.pivotRightTemperature = pivotRight.getDeviceTemp().getValueAsDouble();
        inputs.pivotLeftRPS = pivotRight.getVelocity().getValueAsDouble();
        inputs.pivotRightDutyCycle = dutyCycleRight;

    }

    @Override
    public void setPivotLeftDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycleLeft = percent;
        //simple way to set the motor value.
        pivotLeft.setControl(new DutyCycleOut(dutyCycleLeft));
    }

    //repeat for other motor
    public void setPivotRightDutyCycle(double percent) {
        this.dutyCycleRight = percent;
        pivotRight.setControl(new DutyCycleOut(dutyCycleRight));
    }
}
