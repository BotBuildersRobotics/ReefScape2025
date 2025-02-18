package frc.robot.subsystems.climb;

import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class ClimbIOPhoenix6 implements ClimbIO{
    private TalonFX climbOne;
    private TalonFX climbTwo;

    private double dutyCycleOne = 0;
    private double dutyCycleTwo = 0;
    
    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public ClimbIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        climbOne = TalonFXFactory.createDefaultTalon(Ports.CLIMB_ONE);
       // climbTwo = TalonFXFactory.createDefaultTalon(Ports.CLIMB_TWO);
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
        TalonUtil.applyAndCheckConfiguration(climbOne, Constants.ClimbConstants.ClimbFXConfig());
        //TalonUtil.applyAndCheckConfiguration(climbTwo, Constants.ClimbConstants.ClimbFXConfig());
       
    }

    @Override
    public void updateInputs(ClimbIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
      /*   inputs.climbOneConnected = BaseStatusSignal.refreshAll(
                        
                        climbOne.getDeviceTemp(),
                        climbOne.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.climbOneTemperature = climbOne.getDeviceTemp().getValueAsDouble();
        inputs.climbOneRPS = climbOne.getRotorVelocity().getValueAsDouble();

        //also log the duty cycle we are asking for.
        inputs.climbOneDutyCycle = dutyCycleOne;

        //repeat for other motor
        inputs.climbTwoConnected = BaseStatusSignal.refreshAll(
            climbTwo.getDeviceTemp(),
            climbTwo.getVelocity())
            .isOK();

        inputs.climbTwoTemperature = climbTwo.getDeviceTemp().getValueAsDouble();
        inputs.climbTwoRPS = climbTwo.getVelocity().getValueAsDouble();
        inputs.climbTwoDutyCycle = dutyCycleTwo;*/

    }

    @Override
    public void setClimbOneDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycleOne = percent;
        //simple way to set the motor value.
      //  climbOne.setControl(new DutyCycleOut(dutyCycleOne));
    }

    //repeat for other motor
    public void setClimbTwoDutyCycle(double percent) {
        this.dutyCycleTwo = percent;
      //  climbTwo.setControl(new DutyCycleOut(dutyCycleTwo));
    }
}
