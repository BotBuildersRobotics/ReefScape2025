package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class ElevatorIOPhoenix6 implements ElevatorIO{
    private TalonFX elevator;

    private double dutyCycle = 0;
    
    //this is a TalonFX implementation of our elevator
    //we could in theory write one for REV motors but we love krakens so hello TalonFX.
    public ElevatorIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        elevator = TalonFXFactory.createDefaultTalon(Ports.ELEVATOR);
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs. Very useful for the future. Tedious to setup now.
        TalonUtil.applyAndCheckConfiguration(elevator, Constants.ElevatorConstants.ElevatorFXConfig());
       
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
        inputs.elevatorConnected = BaseStatusSignal.refreshAll(
                        
                        elevator.getDeviceTemp(),
                        elevator.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.elevatorTemperature = elevator.getDeviceTemp().getValueAsDouble();
        inputs.elevatorRPS = elevator.getRotorVelocity().getValueAsDouble();

        //also log the duty cycle we are asking for.
        inputs.elevatorDutyCycle = dutyCycle;
    }

    @Override
    public void setElevatorDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycle = percent;
        //simple way to set the motor value.
        elevator.setControl(new DutyCycleOut(dutyCycle));
    }
}
