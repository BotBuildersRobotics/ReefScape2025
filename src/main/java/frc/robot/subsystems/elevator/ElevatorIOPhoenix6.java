package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class ElevatorIOPhoenix6 implements ElevatorIO{
    private TalonFX elevatorLeft;
    private TalonFX elevatorRight;

    //this is a TalonFX implementation of our elevator
    //we could in theory write one for REV motors but we love krakens so hello TalonFX.
    public ElevatorIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
        elevatorLeft = TalonFXFactory.createDefaultTalon(Ports.ELEVATOR_LEFT);
        elevatorRight = TalonFXFactory.createDefaultTalon(Ports.ELEVATOR_RIGHT);
        
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs. Very useful for the future. Tedious to setup now.
        TalonUtil.applyAndCheckConfiguration(elevatorLeft, Constants.ElevatorConstants.ElevatorFXConfig());
        TalonUtil.applyAndCheckConfiguration(elevatorRight, Constants.ElevatorConstants.ElevatorFXConfig());
       
        //setup leader / follower configuration.

        elevatorLeft.setControl(new Follower(Ports.ELEVATOR_RIGHT.getDeviceNumber(), true));

    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
        inputs.elevatorLeftConnected = BaseStatusSignal.refreshAll(
                            elevatorLeft.getSupplyCurrent(),
                            elevatorLeft.getPosition(),
                            elevatorLeft.getDeviceTemp(),
                            elevatorLeft.getVelocity())
                        .isOK();

        inputs.elevatorRightConnected = BaseStatusSignal.refreshAll(
                            elevatorRight.getSupplyCurrent(),
                            elevatorRight.getPosition(),
                            elevatorRight.getDeviceTemp(),
                            elevatorRight.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.elevatorLeftTemperature = elevatorLeft.getDeviceTemp().getValueAsDouble();
        inputs.elevatorLeftRPS = elevatorLeft.getRotorVelocity().getValueAsDouble();
        inputs.elevatorLeftPosition = elevatorLeft.getPosition().getValueAsDouble();
        inputs.elevatorLeftCurrent = elevatorLeft.getSupplyCurrent().getValueAsDouble();

        inputs.elevatorRightTemperature = elevatorRight.getDeviceTemp().getValueAsDouble();
        inputs.elevatorRightRPS = elevatorRight.getRotorVelocity().getValueAsDouble();
        inputs.elevatorRightPosition = elevatorRight.getPosition().getValueAsDouble();
        inputs.elevatorRightCurrent = elevatorRight.getSupplyCurrent().getValueAsDouble();

       
    }

    @Override
    public void setElevatorPosition(double position) {
       
    }
}
