package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class PivotIOPhoenix6 implements PivotIO{
   //left and right is based off the forward direction
   // private TalonFX pivotLeftMotor;
    private TalonFX pivotRightMotor;

    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public PivotIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
      //  pivotLeftMotor = TalonFXFactory.createDefaultTalon(Ports.PIVOT_LEFT);
        pivotRightMotor = TalonFXFactory.createDefaultTalon(Ports.PIVOT_RIGHT);
        
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
     //   TalonUtil.applyAndCheckConfiguration(pivotLeftMotor, Constants.PivotConstants.PivotFXConfig());
        TalonUtil.applyAndCheckConfiguration(pivotRightMotor, Constants.PivotConstants.PivotFXConfig());
       
       // pivotMotor.optimizeBusUtilization();

       

       //Set up for pivot leader/follower
     //  pivotLeftMotor.setControl(new Follower(Ports.PIVOT_RIGHT.getDeviceNumber(), false));
       
    }

    @Override
    public void updateInputs(PivotIOInputs inputs){
       
        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
      /*   inputs.pivotLeftConnected = BaseStatusSignal.refreshAll(
                        pivotLeftMotor.getStatorCurrent(),
                        pivotLeftMotor.getDeviceTemp(),
                        pivotLeftMotor.getPosition(),
                        pivotLeftMotor.getVelocity())
                        .isOK(); */

        //the motor knows we want info from it, so the following requests should be cool
       /*  inputs.pivotLeftTemperature = pivotLeftMotor.getDeviceTemp().getValueAsDouble();
        inputs.pivotLeftRPS = pivotLeftMotor.getRotorVelocity().getValueAsDouble();
        inputs.pivotLeftCurrent = pivotLeftMotor.getStatorCurrent().getValueAsDouble();
        inputs.pivotLeftMotorPos = pivotLeftMotor.getPosition().getValueAsDouble(); */

        //repeat for right motor
        inputs.pivotRightConnected = BaseStatusSignal.refreshAll(
                        pivotRightMotor.getStatorCurrent(),
                        pivotRightMotor.getDeviceTemp(),
                        pivotRightMotor.getPosition(),
                        pivotRightMotor.getVelocity())
                        .isOK();

        inputs.pivotRightTemperature = pivotRightMotor.getDeviceTemp().getValueAsDouble();
        inputs.pivotRightRPS = pivotRightMotor.getRotorVelocity().getValueAsDouble();
        inputs.pivotRightCurrent = pivotRightMotor.getStatorCurrent().getValueAsDouble();
        inputs.pivotRightMotorPos = pivotRightMotor.getPosition().getValueAsDouble();


        //2.1 * 125 = 262

        //360 degrees of pivot movement would be roughly 50 motor rotations
        //90 degrees of pivot movement is around 12.5 rotations or 0.13 rotations per degree
        
        double desiredRotations = inputs.pivotPosition * 0.71;
        SmartDashboard.putNumber("DesiredRotations", desiredRotations);
        pivotRightMotor.setControl(new MotionMagicVoltage(desiredRotations));
        
    }   

   
}
