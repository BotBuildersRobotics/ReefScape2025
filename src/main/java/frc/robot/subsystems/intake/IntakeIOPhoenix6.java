package frc.robot.subsystems.intake;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;

public class IntakeIOPhoenix6 implements IntakeIO{
    private TalonFX intakeRollersFx;
    private TalonFX transferRollersFx;

    private DigitalInput intakeBeamBreakOne;
    private DigitalInput intakeBeamBreakTwo;

    private double intakeDutyCycle = 0;
    private double transferDutyCycle = 0;
    
    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public IntakeIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        intakeRollersFx = TalonFXFactory.createDefaultTalon(Ports.INTAKE);
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
        TalonUtil.applyAndCheckConfiguration(intakeRollersFx, Constants.IntakeConstants.IntakeFXConfig());

        transferRollersFx = TalonFXFactory.createDefaultTalon(Ports.TRANSFER);

        TalonUtil.applyAndCheckConfiguration(transferRollersFx, Constants.IntakeConstants.IntakeFXConfig());

        intakeBeamBreakOne = new DigitalInput(Ports.INTAKE_BEAMBREAK_ONE);
        intakeBeamBreakTwo = new DigitalInput(Ports.INTAKE_BEAMBREAK_TWO);
       
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs){
       
        inputs.intakeBeamBreakOneTripped = !intakeBeamBreakOne.get();
        inputs.intakeBeamBreakTwoTripped = !intakeBeamBreakTwo.get();

        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
        inputs.intakeConnected = BaseStatusSignal.refreshAll(
                        intakeRollersFx.getStatorCurrent(),
                        intakeRollersFx.getDeviceTemp(),
                        intakeRollersFx.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.intakeTemperature = intakeRollersFx.getDeviceTemp().getValueAsDouble();
        inputs.intakeMotorRPS = intakeRollersFx.getRotorVelocity().getValueAsDouble();
        inputs.intakeCurrent = intakeRollersFx.getStatorCurrent().getValueAsDouble();

        //also log the duty cycle we are asking for.
        inputs.intakeMotorDutyCycle = intakeDutyCycle;

        inputs.transferConnected = BaseStatusSignal.refreshAll(
                        intakeRollersFx.getStatorCurrent(),
                        intakeRollersFx.getDeviceTemp(),
                        intakeRollersFx.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.transferTemperature = intakeRollersFx.getDeviceTemp().getValueAsDouble();
        inputs.transferMotorRPS = intakeRollersFx.getRotorVelocity().getValueAsDouble();
        inputs.transferCurrent = intakeRollersFx.getStatorCurrent().getValueAsDouble();

        //also log the duty cycle we are asking for.
        inputs.transferMotorDutyCycle = transferDutyCycle;
    }

    @Override
    public void setIntakeDutyCycle(double percent) {
        //store this for future logging.
        this.intakeDutyCycle = percent;
        //simple way to set the motor value.
        intakeRollersFx.setControl(new DutyCycleOut(intakeDutyCycle));
    }

    @Override
    public void setTransferDutyCycle(double percent) {
        //store this for future logging.
        this.transferDutyCycle = percent;
        //simple way to set the motor value.
        transferRollersFx.setControl(new DutyCycleOut(transferDutyCycle));
    }
    
}
