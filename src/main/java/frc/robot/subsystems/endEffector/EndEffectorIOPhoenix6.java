package frc.robot.subsystems.endEffector;

import static edu.wpi.first.units.Units.Degrees;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.TalonFXS;


import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;

public class EndEffectorIOPhoenix6 implements EndEffectorIO{
   
    private TalonFX endEffectorArm;

    private TalonFXS endEffectorClaw;


    private TalonFX algaeSpinner;

   
    private double dutyCycleRoller = 0;
   
    private double currentArmAngle = 0;

    private MotionMagicVoltage armMotionMagic = new MotionMagicVoltage(0);

    private MotionMagicVoltage clawMotionMagic = new MotionMagicVoltage(0);

    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public EndEffectorIOPhoenix6() {

      
        endEffectorArm = TalonFXFactory.createDefaultTalon(Ports.END_EFFECTOR_ARM);

        endEffectorClaw =  TalonFXFactory.createDefaultTalonFXS(Ports.END_EFFECTOR_CLAW);

        algaeSpinner =  TalonFXFactory.createDefaultTalon(Ports.END_EFFECTOR_SPINNER);
       
        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
       // TalonUtil.applyAndCheckConfiguration(endEffectorRoller, Constants.EndEffectorConstants.EndEffectorFXRollerConfig());
        TalonUtil.applyAndCheckConfiguration(endEffectorArm, Constants.EndEffectorConstants.EndEffectorArmPivot());
        TalonUtil.applyAndCheckConfiguration(endEffectorClaw, Constants.EndEffectorConstants.endEffectorClaw());
        TalonUtil.applyAndCheckConfiguration(algaeSpinner, Constants.EndEffectorConstants.EndEffectorSpinner());
      
       // endEffectorClaw.setPosition(0);
    }

    @Override
    public void updateInputs(EndEffectorIOInputs inputs){
       
      

        inputs.motorArmConnected = BaseStatusSignal.refreshAll(
                        endEffectorArm.getSupplyCurrent(),
                        endEffectorArm.getDeviceTemp(),
                        endEffectorArm.getPosition())
                        .isOK();

        inputs.motorClawConnected = BaseStatusSignal.refreshAll(
            endEffectorClaw.getSupplyCurrent(),
            endEffectorClaw.getDeviceTemp(),
            endEffectorClaw.getVelocity(),
            endEffectorClaw.getPosition())
                        .isOK();

        inputs.armPivotPosition = endEffectorArm.getPosition().getValueAsDouble();
        inputs.clawPosition = endEffectorClaw.getPosition().getValueAsDouble();
        inputs.clawSupplyCurrent = endEffectorClaw.getSupplyCurrent().getValueAsDouble();
        inputs.clawVelocity = endEffectorClaw.getVelocity().getValueAsDouble();
        inputs.clawTemp = endEffectorClaw.getDeviceTemp().getValueAsDouble();

        //TODO: check this is correct in terms of rotations vs angle
        currentArmAngle = inputs.armPivotPosition;

        setArmPosition(inputs.desiredArmPosition);
       // openClaw(); 
      
        
    }


    @Override
    public void setArmPosition(double angle){
        // 45:1
        //0.125 rotations per degree
        //double desiredRotations = angle / 360;
        //endEffectorArm.setControl(armMotionMagic.withPosition(desiredRotations).withSlot(0));

        endEffectorArm.setControl(armMotionMagic.withPosition(Angle.ofBaseUnits(angle, Degrees)).withSlot(0));
    }

    
    @Override
    public void closeClaw(){
      
        endEffectorClaw.setControl(clawMotionMagic.withPosition(Constants.EndEffectorConstants.END_EFFECTOR_CLAW_END));
     
    }

    @Override
    public void openClaw(){
       
        endEffectorClaw.setControl(clawMotionMagic.withPosition(Constants.EndEffectorConstants.END_EFFECTOR_CLAW_START));
     
    }

    @Override
    public boolean isClawClosed(){
        return (endEffectorClaw.getPosition(true).getValueAsDouble() >= (Constants.EndEffectorConstants.END_EFFECTOR_CLAW_END - 5));
    }

    @Override
    public double getArmAngle(){

        return endEffectorArm.getPosition(true).getValueAsDouble();

    }

    @Override
    public void setSpinnerSpeed(double speed){
        algaeSpinner.setControl(new DutyCycleOut(speed));
    }

}
