package frc.robot.subsystems.endEffector;

import static edu.wpi.first.units.Units.Degrees;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.TalonFXS;

import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class EndEffectorIOPhoenix6 implements EndEffectorIO{
    private TalonFXS endEffectorRoller;
    private TalonFXS endEffectorPivot;
    private TalonFX endEffectorArm;


    private double dutyCycleRoller = 0;

    private CANrange effectorBeamBreak;

    private MotionMagicVoltage armMotionMagic = new MotionMagicVoltage(0);

    private MotionMagicVoltage pivotMotionMagic = new MotionMagicVoltage(0);

    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public EndEffectorIOPhoenix6() {

        effectorBeamBreak = new CANrange(Ports.END_EFFECTOR_BEAM_BREAK.getDeviceNumber());

        //use our helpers to write config over the CAN Bus
        endEffectorRoller = TalonFXFactory.createDefaultTalonFXS(Ports.END_EFFECTOR_ROLLER);
        endEffectorPivot = TalonFXFactory.createDefaultTalonFXS(Ports.END_EFFECTOR_PIVOT);

        endEffectorArm = TalonFXFactory.createDefaultTalon(Ports.END_EFFECTOR_ARM);

        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
        TalonUtil.applyAndCheckConfiguration(endEffectorRoller, Constants.EndEffectorConstants.EndEffectorFXRollerConfig());
        TalonUtil.applyAndCheckConfiguration(endEffectorPivot, Constants.EndEffectorConstants.EndEffectorFXPivotConfig());
        TalonUtil.applyAndCheckConfiguration(endEffectorArm, Constants.EndEffectorConstants.EndEffectorArmPivot());

        
    }

    @Override
    public void updateInputs(EndEffectorIOInputs inputs){
       
        inputs.endEffectorBeamBreakTripped = isCoralDetected();

        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
        inputs.motorRollerConnected = BaseStatusSignal.refreshAll(
                        endEffectorRoller.getDeviceTemp(),
                        endEffectorRoller.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.motorRollerTemperature = endEffectorRoller.getDeviceTemp().getValueAsDouble();
        inputs.motorRollerRPS = endEffectorRoller.getRotorVelocity().getValueAsDouble();

    
        inputs.motorPivotConnected = BaseStatusSignal.refreshAll(
                        endEffectorPivot.getSupplyCurrent(),
                        endEffectorPivot.getDeviceTemp(),
                        endEffectorPivot.getVelocity())
                        .isOK();
        inputs.motorPivotTemperature = endEffectorPivot.getDeviceTemp().getValueAsDouble();
        inputs.motorPivotPosition = endEffectorPivot.getPosition().getValueAsDouble();
        inputs.motorPivotCurrent = endEffectorPivot.getSupplyCurrent().getValueAsDouble();


        inputs.motorArmConnected = BaseStatusSignal.refreshAll(
                        endEffectorArm.getSupplyCurrent(),
                        endEffectorArm.getDeviceTemp(),
                        endEffectorArm.getPosition())
                        .isOK();

        inputs.armPivotPosition = endEffectorArm.getPosition().getValueAsDouble();

        setEndEffectorPivotPosition(inputs.desiredPivotPos);
        setArmPosition(inputs.desiredArmPos);

        inputs.isBeamBreakConnected = BaseStatusSignal.refreshAll(
                        effectorBeamBreak.getDistance()
        ).isOK();
       

        inputs.isCoralDetected = effectorBeamBreak.getDistance(true).getValueAsDouble() <= Constants.EndEffectorConstants.CORAL_DETECT_MIN_DISTANCE_MM;
    }

    @Override
    public void setMotorRollerDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycleRoller = percent;
        //simple way to set the motor value.
        endEffectorRoller.setControl(new DutyCycleOut(dutyCycleRoller));
    }

    public void setEndEffectorPivotPosition(double angle){
        //todo: work out the angle
        // 12:56 -> 23:29 = 
        // 4.6 * 1.26 = 5.8  rotations of motor to the full rotation of the pivot.
        
        //we set this up in the motor config.
        //endEffectorPivot.setControl(new PositionDutyCycle(Angle.ofBaseUnits(angle, Units.Degrees)));
        SmartDashboard.putNumber("Effector Pivot", Angle.ofBaseUnits(angle, Degrees).baseUnitMagnitude());
        //endEffectorPivot.setControl(pivotMotionMagic.withPosition(Angle.ofBaseUnits(angle, Degrees)).withSlot(0));
        endEffectorPivot.setControl(pivotMotionMagic.withPosition(0.016 * angle).withSlot(0));
    }

    public void setArmPosition(double angle){
        // 45:1
        //0.125 rotations per degree
        SmartDashboard.putNumber("Effector Arm", Angle.ofBaseUnits(angle, Degrees).baseUnitMagnitude());
        //PositionDutyCycle pos = new PositionDutyCycle(angle);
        //pos.withFeedForward(0.08);
        //endEffectorArm.setControl(m_positionTorque.withPosition(0.125 * angle));
       // endEffectorArm.setControl(new MotionMagicVoltage(0.125 * angle));
        endEffectorArm.setControl(armMotionMagic.withPosition(Angle.ofBaseUnits(angle, Degrees)).withSlot(0));
    }

    @Override
    public boolean isCoralDetected()
    {
        //TODO: We could use the Units and Measure classes in WPI to make this nicer.
        return effectorBeamBreak.getDistance(true).getValueAsDouble() <= Constants.EndEffectorConstants.CORAL_DETECT_MIN_DISTANCE_MM;
    }
}
