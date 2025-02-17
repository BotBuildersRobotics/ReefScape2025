package frc.robot.subsystems.transfer;

import com.ctre.phoenix6.hardware.TalonFX;


public class TransferIOPhoenix6 implements TransferIO{
    private TalonFX transferPulleyLeft;
    private TalonFX transferPulleyRight;

    private double dutyCycleLeft = 0;
    private double dutyCycleRight = 0;
    
    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public TransferIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
      //  transferPulleyLeft = TalonFXFactory.createDefaultTalon(Ports.TRANSFER_LEFT);
       // transferPulleyRight = TalonFXFactory.createDefaultTalon(Ports.TRANSFER_RIGHT);
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
     //   TalonUtil.applyAndCheckConfiguration(transferPulleyLeft, Constants.TransferConstants.TransferFXConfig());
      //  TalonUtil.applyAndCheckConfiguration(transferPulleyRight, Constants.TransferConstants.TransferFXConfig());
       
    }

    @Override
    public void updateInputs(TransferIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
      /*  inputs.transferPulleyLeftConnected = BaseStatusSignal.refreshAll(
                        
                        transferPulleyLeft.getDeviceTemp(),
                        transferPulleyLeft.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.transferPulleyLeftTemperature = transferPulleyLeft.getDeviceTemp().getValueAsDouble();
        inputs.transferPulleyLeftRPS = transferPulleyLeft.getRotorVelocity().getValueAsDouble();

        //also log the duty cycle we are asking for.
        inputs.transferPulleyLeftDutyCycle = dutyCycleLeft;

        //repeat for other motor
        inputs.transferPulleyRightConnected = BaseStatusSignal.refreshAll(
            transferPulleyRight.getDeviceTemp(),
            transferPulleyRight.getVelocity())
            .isOK();

        inputs.transferPulleyRightTemperature = transferPulleyRight.getDeviceTemp().getValueAsDouble();
        inputs.transferPulleyRightRPS = transferPulleyRight.getVelocity().getValueAsDouble();
        inputs.transferPulleyRightDutyCycle = dutyCycleRight;*/

    }

    @Override
    public void setTransferPulleyLeftDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycleLeft = percent;
        //simple way to set the motor value.
      //  transferPulleyLeft.setControl(new DutyCycleOut(dutyCycleLeft));
    }

    //repeat for other motor
    public void setTransferPulleyRightDutyCycle(double percent) {
        this.dutyCycleRight = percent;
       // transferPulleyRight.setControl(new DutyCycleOut(dutyCycleRight));
    }
}
