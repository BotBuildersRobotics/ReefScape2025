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
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.lib.TalonFXFactory;
import frc.robot.lib.TalonUtil;

public class EndEffectorIOPhoenix6 implements EndEffectorIO{
    private TalonFX endEffectorRoller;
    private TalonFX endEffectorArm;

    private Servo pivotServo;
    private Servo clawServo;

    private double dutyCycleRoller = 0;

    private MotionMagicVoltage armMotionMagic = new MotionMagicVoltage(0);

    private MotionMagicVoltage pivotMotionMagic = new MotionMagicVoltage(0);

    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public EndEffectorIOPhoenix6() {

        //use our helpers to write config over the CAN Bus
       // endEffectorRoller = TalonFXFactory.createDefaultTalon(Ports.END_EFFECTOR_ROLLER);
        endEffectorArm = TalonFXFactory.createDefaultTalon(Ports.END_EFFECTOR_ARM);

        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
       // TalonUtil.applyAndCheckConfiguration(endEffectorRoller, Constants.EndEffectorConstants.EndEffectorFXRollerConfig());
        TalonUtil.applyAndCheckConfiguration(endEffectorArm, Constants.EndEffectorConstants.EndEffectorArmPivot());

       // pivotServo = new Servo(0);
        //clawServo = new Servo(0); //TODO: add constants and port numbers
    }

    @Override
    public void updateInputs(EndEffectorIOInputs inputs){
       
      

        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
       /*  inputs.motorRollerConnected = BaseStatusSignal.refreshAll(
                        endEffectorRoller.getDeviceTemp(),
                        endEffectorRoller.getVelocity())
                        .isOK();*/

        //the motor knows we want info from it, so the following requests should be cool
      //  inputs.motorRollerTemperature = endEffectorRoller.getDeviceTemp().getValueAsDouble();
       // inputs.motorRollerRPS = endEffectorRoller.getRotorVelocity().getValueAsDouble();



        inputs.motorArmConnected = BaseStatusSignal.refreshAll(
                        endEffectorArm.getSupplyCurrent(),
                        endEffectorArm.getDeviceTemp(),
                        endEffectorArm.getPosition())
                        .isOK();

        inputs.armPivotPosition = endEffectorArm.getPosition().getValueAsDouble();

        setArmPosition(inputs.desiredArmPosition);
        pivotEffector(inputs.desiredPivotPosition);
       
       
    }

    @Override
    public void setMotorRollerDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycleRoller = percent;
        //simple way to set the motor value.
       // endEffectorRoller.setControl(new DutyCycleOut(dutyCycleRoller));
    }


    @Override
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
    public void pivotEffector(double angle){

    }

    
}
