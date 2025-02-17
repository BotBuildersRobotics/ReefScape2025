package frc.robot.subsystems.intake;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class IntakeIOPhoenix6 implements IntakeIO{
    private TalonFX intakeRollersFx;
    private TalonFX transferRollersFx;

    private DigitalInput intakeBeamBreakOne;
    private DigitalInput intakeBeamBreakTwo;

    private double intakeVoltage = 0;
    private double transferVoltage = 0;
    
    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public IntakeIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        intakeRollersFx = TalonFXFactory.createDefaultTalon(Ports.INTAKE);
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
        TalonUtil.applyAndCheckConfiguration(intakeRollersFx, Constants.IntakeConstants.IntakeFXConfig());

        transferRollersFx = TalonFXFactory.createDefaultTalon(Ports.TRANSFER);

        TalonUtil.applyAndCheckConfiguration(transferRollersFx, Constants.IntakeTransferConstants.IntakeFXConfig());

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
       // inputs.intakeMotorDutyCycle = intakeDutyCycle;

        inputs.transferConnected = BaseStatusSignal.refreshAll(
                        transferRollersFx.getStatorCurrent(),
                        transferRollersFx.getDeviceTemp(),
                        transferRollersFx.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.transferTemperature = transferRollersFx.getDeviceTemp().getValueAsDouble();
        inputs.transferMotorRPS = transferRollersFx.getRotorVelocity().getValueAsDouble();
        inputs.transferCurrent = transferRollersFx.getStatorCurrent().getValueAsDouble();

        //also log the duty cycle we are asking for.
       // inputs.transferMotorDutyCycle = transferDutyCycle;
        //inputs.intakeMotorDutyCycle = intakeDutyCycle;
    }

    @Override
    public void setIntakeDutyCycle(double voltage) {
        //store this for future logging.
        this.intakeVoltage = voltage;
        //simple way to set the motor value.
        SmartDashboard.putNumber("Intake Percent", voltage);
        intakeRollersFx.setControl(new VoltageOut(voltage));
    }

    @Override
    public void setTransferDutyCycle(double voltage) {
        //store this for future logging.
        this.transferVoltage = voltage;
        //simple way to set the motor value.
        transferRollersFx.setControl(new VoltageOut(voltage));
    }
    
    @Override
    public boolean getBeamBreakOneState() {
        return !intakeBeamBreakOne.get();
    }

}
