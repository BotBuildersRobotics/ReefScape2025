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
import com.revrobotics.servohub.config.ServoHubConfig;
import com.revrobotics.servohub.ServoChannel;
import com.revrobotics.servohub.ServoChannel.ChannelId;
import com.revrobotics.servohub.ServoHub;
import com.revrobotics.servohub.config.ServoChannelConfig.BehaviorWhenDisabled;

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

   
    private double dutyCycleRoller = 0;

    ServoHubConfig config = new ServoHubConfig();

    ServoHub servoHub = new ServoHub(Ports.SERVO_HUB.getDeviceNumber());

    // Obtain a servo channel controller
    ServoChannel pivotServo = servoHub.getServoChannel(ChannelId.kChannelId0);
    ServoChannel pivotServo2 = servoHub.getServoChannel(ChannelId.kChannelId1);

    //claw servo
    ServoChannel clawServo = servoHub.getServoChannel(ChannelId.kChannelId2);

   
    private double currentArmAngle = 0;

    private MotionMagicVoltage armMotionMagic = new MotionMagicVoltage(0);

    private MotionMagicVoltage pivotMotionMagic = new MotionMagicVoltage(0);

    //this is a TalonFX implementation of our intake
    //we could in theory write one for REV motors, the core subsystem would remain the same, just how we talk to the motors is different.
    public EndEffectorIOPhoenix6() {

        //pivot is on channel 0 - don't supply power when disabled
         config.channel0.pulseRange(500, 1500, 2500)
        .disableBehavior(BehaviorWhenDisabled.kDoNotSupplyPower);

        config.channel1.pulseRange(500, 1500, 2500)
        .disableBehavior(BehaviorWhenDisabled.kDoNotSupplyPower);

        //claw is on channel 2 - supply power always
        config.channel2.pulseRange(500, 1500, 2500)
        .disableBehavior(BehaviorWhenDisabled.kSupplyPower);

        //use our helpers to write config over the CAN Bus
       // endEffectorRoller = TalonFXFactory.createDefaultTalon(Ports.END_EFFECTOR_ROLLER);
        endEffectorArm = TalonFXFactory.createDefaultTalon(Ports.END_EFFECTOR_ARM);

        //we store all of the current limits in the constants file
        //only need to look in one place to change all motor configs.
       // TalonUtil.applyAndCheckConfiguration(endEffectorRoller, Constants.EndEffectorConstants.EndEffectorFXRollerConfig());
        TalonUtil.applyAndCheckConfiguration(endEffectorArm, Constants.EndEffectorConstants.EndEffectorArmPivot());

    }

    @Override
    public void updateInputs(EndEffectorIOInputs inputs){
       
      

        //check that the motor is connected and tell it that we are interested in knowing the following bits of information
        //device temp and speed.
         inputs.motorRollerConnected = BaseStatusSignal.refreshAll(
                        endEffectorRoller.getDeviceTemp(),
                        endEffectorRoller.getVelocity())
                        .isOK();

        //the motor knows we want info from it, so the following requests should be cool
        inputs.motorRollerTemperature = endEffectorRoller.getDeviceTemp().getValueAsDouble();
        inputs.motorRollerRPS = endEffectorRoller.getRotorVelocity().getValueAsDouble();



        inputs.motorArmConnected = BaseStatusSignal.refreshAll(
                        endEffectorArm.getSupplyCurrent(),
                        endEffectorArm.getDeviceTemp(),
                        endEffectorArm.getPosition())
                        .isOK();

        inputs.armPivotPosition = endEffectorArm.getPosition().getValueAsDouble();

        //TODO: check this is correct in terms of rotations vs angle
        currentArmAngle = inputs.armPivotPosition;

        setArmPosition(inputs.desiredArmPosition);
        pivotEffector(inputs.desiredPivotPosition);
        
    }

    @Override
    public void setMotorRollerDutyCycle(double percent) {
        //store this for future logging.
        this.dutyCycleRoller = percent;
        //simple way to set the motor value.
        endEffectorRoller.setControl(new DutyCycleOut(dutyCycleRoller));
    }


    @Override
    public void setArmPosition(double angle){
        // 45:1
        //0.125 rotations per degree
        SmartDashboard.putNumber("Effector Arm", Angle.ofBaseUnits(angle, Degrees).baseUnitMagnitude());
       
        

        endEffectorArm.setControl(armMotionMagic.withPosition(Angle.ofBaseUnits(angle, Degrees)).withSlot(0));
    }

    @Override
    public void pivotEffector(int angle){
        pivotServo.setEnabled(true);
        pivotServo2.setEnabled(true);
        pivotServo.setPulseWidth(angle * 250); //TODO: calc angle vs pulse width
        pivotServo2.setPulseWidth(angle * 250);
    }

    @Override
    public void depowerPivotServos(){
        pivotServo.setEnabled(false);
        pivotServo2.setEnabled(false);
    }

    @Override
    public void closeClaw(){
        clawServo.setPulseWidth(2500);
    }

    @Override
    public void openClaw(){
        clawServo.setPulseWidth(0);
    }

    @Override
    public double getArmAngle(){

        return currentArmAngle;

    }
    
}
