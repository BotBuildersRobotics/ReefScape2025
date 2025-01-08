package frc.robot.subsystems.intake;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class IntakeIOPhoenix6 implements IntakeIO{
    private TalonFX topIntakeRoller;

    private double dutyCycle = 0;
    
    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public IntakeIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        topIntakeRoller = TalonFXFactory.createDefaultTalon(Ports.INTAKE_TOP_ROLLER);
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
        TalonUtil.applyAndCheckConfiguration(topIntakeRoller, Constants.IntakeConstants.IntakeFXConfig());
       
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
        inputs.topMotorConnected = BaseStatusSignal.refreshAll(
                        
                        topIntakeRoller.getDeviceTemp(),
                        topIntakeRoller.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.topMotorTemperature = topIntakeRoller.getDeviceTemp().getValueAsDouble();
        inputs.topMotorRPS = topIntakeRoller.getRotorVelocity().getValueAsDouble();

        //also log the duty cycle we are asking for.
        inputs.topMotorDutyCycle = dutyCycle;
    }

    @Override
    public void setTopMotorDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycle = percent;
        //simple way to set the motor value.
        topIntakeRoller.setControl(new DutyCycleOut(dutyCycle));
    }
}
