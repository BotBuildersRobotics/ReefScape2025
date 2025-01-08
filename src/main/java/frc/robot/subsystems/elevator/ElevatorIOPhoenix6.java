package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class ElevatorIOPhoenix6 implements ElevatorIO{
    private TalonFX elevatorMotor;

    private double dutyCycle = 0;
    
    //this is a TalonFX implementation of our elevator
    //we could in theory write one for REV motors but we love krakens so hello TalonFX.
    public ElevatorIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        elevatorMotor = TalonFXFactory.createDefaultTalon(Ports.ELEVATOR_MOTOR);
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs. Very useful for the future. Tedious to setup now.
        TalonUtil.applyAndCheckConfiguration(elevatorMotor, Constants.ElevatorConstants.ElevatorFXConfig());
       
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
        inputs.topMotorConnected = BaseStatusSignal.refreshAll(
                        
                        elevatorMotor.getDeviceTemp(),
                        elevatorMotor.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.topMotorTemperature = elevatorMotor.getDeviceTemp().getValueAsDouble();
        inputs.topMotorRPS = elevatorMotor.getRotorVelocity().getValueAsDouble();

        //also log the duty cycle we are asking for.
        inputs.topMotorDutyCycle = dutyCycle;
    }

    @Override
    public void setTopMotorDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycle = percent;
        //simple way to set the motor value.
        elevatorMotor.setControl(new DutyCycleOut(dutyCycle));
    }
}
