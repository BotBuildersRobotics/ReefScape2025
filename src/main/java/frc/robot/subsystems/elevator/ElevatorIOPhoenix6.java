package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicExpoTorqueCurrentFOC;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class ElevatorIOPhoenix6 implements ElevatorIO{
    private TalonFX elevatorLeft;
    private TalonFX elevatorRight;

    private DigitalInput elevatorBeamBreak;

 

    private MotionMagicExpoTorqueCurrentFOC moveRequest = new MotionMagicExpoTorqueCurrentFOC(0).withSlot(0);

    private MotionMagicVoltage mmVoltage = new MotionMagicVoltage(0);

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

        elevatorBeamBreak = new DigitalInput(Ports.ELEVATOR_BEAMBREAK);

        
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs){
       inputs.elevatorBeamBreakTripped = !elevatorBeamBreak.get();

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

        double desiredRotations = inputs.desiredElevatorPosition;

        SmartDashboard.putNumber("DesiredRotations", desiredRotations);
        elevatorRight.setControl(mmVoltage.withPosition(desiredRotations).withSlot(0).withEnableFOC(true));
       
      
       
    }

    public void setVoltage(Voltage volts){
        elevatorRight.setControl(new VoltageOut(volts));
    }

    @Override
    public void resetElevatorZero()
    {
        elevatorRight.setPosition(0);
        elevatorLeft.setPosition(0);
    }

   
}
