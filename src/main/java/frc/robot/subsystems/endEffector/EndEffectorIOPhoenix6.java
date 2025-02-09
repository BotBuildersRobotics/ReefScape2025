package frc.robot.subsystems.endEffector;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class EndEffectorIOPhoenix6 implements EndEffectorIO{
    private TalonFX endEffectorRoller;
    private TalonFX endEffectorPivot;

    private double dutyCycleRoller = 0;
    
    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public EndEffectorIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        endEffectorRoller = TalonFXFactory.createDefaultTalon(Ports.END_EFFECTOR_ROLLER);
        endEffectorPivot = TalonFXFactory.createDefaultTalon(Ports.END_EFFECTOR_PIVOT);
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
        TalonUtil.applyAndCheckConfiguration(endEffectorRoller, Constants.EndEffectorConstants.EndEffectorFXRollerConfig());
        TalonUtil.applyAndCheckConfiguration(endEffectorPivot, Constants.EndEffectorConstants.EndEffectorFXPivotConfig());
       
    }

    @Override
    public void updateInputs(EndEffectorIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
        inputs.motorRollerConnected = BaseStatusSignal.refreshAll(
                        
                        endEffectorRoller.getDeviceTemp(),
                        endEffectorRoller.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.motorRollerTemperature = endEffectorRoller.getDeviceTemp().getValueAsDouble();
        inputs.motorRollerRPS = endEffectorRoller.getRotorVelocity().getValueAsDouble();

        //also log the duty cycle we are asking for.
        inputs.motorRollerDutyCycle = dutyCycleRoller;



        inputs.motorPivotConnected = BaseStatusSignal.refreshAll(
                        
                        endEffectorPivot.getDeviceTemp(),
                        endEffectorPivot.getVelocity())
                        .isOK();
        inputs.motorPivotTemperature = endEffectorPivot.getDeviceTemp().getValueAsDouble();
        inputs.motorPivotRPS = endEffectorPivot.getRotorVelocity().getValueAsDouble();
    
    }

    @Override
    public void setMotorRollerDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycleRoller = percent;
        //simple way to set the motor value.
        endEffectorRoller.setControl(new DutyCycleOut(dutyCycleRoller));
    }
}
