package frc.robot.subsystems.endEffector;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.units.BaseUnits;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import frc.robot.lib.io.ServoMotorSubsystem.ServoHomingConfig;
import frc.robot.lib.io.MotorIOTalonFX;
import frc.robot.lib.io.MotorIOTalonFX.MotorIOTalonFXConfig;

import frc.robot.Ports;

public class EndEffectorConstants {

	//56 teeth / 18 tooth   * 5:1 * 5:1
	public static final double kGearing = 87.5 / 1;

	public static final Angle kStowPosition = Units.Degrees.of(90.0);
	
	public static final Angle kProcessorPosition = Units.Degrees.of(70.0);

	public static final Angle kL1Score = Units.Degrees.of(145.0);

	public static final Angle kMaxPos = Units.Degrees.of(0.0);
	
	public static final Distance kArmLength = Units.Centimeters.of(60);

	public static final Angle kEpsilonThreshold = Units.Degrees.of(8.0);

	public static final Pose3d kOffsetPose = new Pose3d(
			Units.Meters.of(-0.502971),
			Units.Meters.of(0.0),
			Units.Meters.of(0.177800),
			new Rotation3d(BaseUnits.AngleUnit.zero(), Units.Degrees.of(90.0), BaseUnits.AngleUnit.zero()));

	public static TalonFXConfiguration getFXConfig() {
		TalonFXConfiguration config = new TalonFXConfiguration();
		config.Slot0.kP = 80.0;//115.0;
		config.Slot0.kS = 0.0;
		config.Slot0.kG = 0.2;

		config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;

		config.MotionMagic.MotionMagicCruiseVelocity = 100.0;
		config.MotionMagic.MotionMagicAcceleration = 40.0;//80.0;

		config.Voltage.PeakForwardVoltage = 12.0;
		config.Voltage.PeakReverseVoltage = -12.0;

		config.CurrentLimits.SupplyCurrentLimitEnable = true;
		config.CurrentLimits.SupplyCurrentLimit = 40.0;
		config.CurrentLimits.SupplyCurrentLowerLimit = 40.0;
		config.CurrentLimits.SupplyCurrentLowerTime = 0.1;

		config.Feedback.SensorToMechanismRatio = kGearing;

		config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

		config.SoftwareLimitSwitch.ForwardSoftLimitEnable = false;
		//config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = kStowPosition.in(Units.Rotations);

		config.SoftwareLimitSwitch.ReverseSoftLimitEnable = false;
		//config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = kMaxPos.in(Units.Rotations);

		config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

		
		
		config.Feedback.FeedbackRemoteSensorID = Ports.PIVOT_ARM_ENCODER.getDeviceNumber();
		config.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
		config.Feedback.SensorToMechanismRatio = 1.0;
		config.Feedback.RotorToSensorRatio = kGearing;
		
		return config;
	}

	public static CANcoderConfiguration getEncoderConfig(){
		
		CANcoderConfiguration config = new CANcoderConfiguration();
		//config.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5;
		config.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
		
		//config.MagnetSensor.withMagnetOffset(Rotations.of(-0.668457).plus(Degrees.of(-38.672)));
		//config.MagnetSensor.withMagnetOffset(-0.085957);
		config.MagnetSensor.withMagnetOffset(Degrees.of(20));
		
		return config;
	}

	public static MotorIOTalonFXConfig getIOConfig() {
		MotorIOTalonFXConfig config = new MotorIOTalonFXConfig();
		config.mainConfig = getFXConfig();
		config.mainID = Ports.PIVOT_ARM.getDeviceNumber();
		config.mainBus = Ports.PIVOT_ARM.getBus();
		config.time = Units.Seconds;
		config.unit = Units.Degrees;
		return config;
	}

	public static MotorIOTalonFX getMotorIO() {
		
		return new MotorIOTalonFX(getIOConfig());
		
	}

	
	public static ServoHomingConfig getServoHomingConfig() {
		ServoHomingConfig config = new ServoHomingConfig();
		config.kHomePosition = kStowPosition;
		config.kHomingTimeout = Units.Seconds.of(0.2);
		config.kHomingVoltage = Units.Volts.of(1.0);
		config.kSetHomedVelocity = Units.DegreesPerSecond.of(5.0);
		
		return config;
	}
}