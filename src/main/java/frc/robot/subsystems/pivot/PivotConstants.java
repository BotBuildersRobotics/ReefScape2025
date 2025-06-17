package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
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

public class PivotConstants {
	public static final double kGearing = 40.0;

	public static final Angle kDeployPosition = Units.Degrees.of( 10.0);
	public static final Angle kStowClearPosition = Units.Degrees.of(55.0);
	public static final Angle kFullStowPosition = Units.Degrees.of(85.918);
	public static final Angle kIndexerHold = Units.Degrees.of(20.0);

	public static final Angle kExhaustPosition = kDeployPosition;
	public static final Distance kArmLength = Units.Inches.of(14.0);

	public static final Angle kEpsilonThreshold = Units.Degrees.of(6.0);

	public static final Pose3d kPoweredBarOffsetPose = new Pose3d(
			Units.Meters.of(0.1406525),
			Units.Meters.of(0.0),
			Units.Meters.of(0.23622),
			new Rotation3d(BaseUnits.AngleUnit.zero(), Units.Degrees.of(-90.0), BaseUnits.AngleUnit.zero()));
	public static final Pose3d kUnpoweredBarOffsetPose = new Pose3d(
			Units.Meters.of(0.2791203968),
			Units.Meters.of(0.0),
			Units.Meters.of(0.1820658792),
			new Rotation3d(BaseUnits.AngleUnit.zero(), Units.Degrees.of(-90.0), BaseUnits.AngleUnit.zero()));
	public static final Pose3d kMainIntakeOffsetPose = new Pose3d(
			Units.Meters.of(0.0),
			Units.Meters.of(0.0),
			Units.Meters.of(0.0),
			new Rotation3d(BaseUnits.AngleUnit.zero(), Units.Degrees.of(0.0), BaseUnits.AngleUnit.zero()));

	public static final Distance unpoweredBarDistance = Units.Inches.of(12.6744953704);
	public static final Distance poweredBarDistance = Units.Inches.of(13.2062952857);

	public static TalonFXConfiguration getFXConfig() {
		TalonFXConfiguration config = new TalonFXConfiguration();
		config.Slot0.kP = 180.0;
		config.Slot0.kD = 0.0;
		config.Slot0.kS = 0.0;
		config.Slot0.kG = 0.0;

		config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
		config.Slot0.StaticFeedforwardSign = StaticFeedforwardSignValue.UseVelocitySign;

		config.MotionMagic.MotionMagicCruiseVelocity = 7.0;
		config.MotionMagic.MotionMagicAcceleration = 15.0;

		config.Voltage.PeakForwardVoltage = 12.0;
		config.Voltage.PeakReverseVoltage = -12.0;

		config.CurrentLimits.SupplyCurrentLimitEnable = true;
		config.CurrentLimits.SupplyCurrentLimit = 40.0;

		config.Feedback.SensorToMechanismRatio = kGearing;

		config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

		config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
		config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = kFullStowPosition.in(Units.Rotations);

		config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
		config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = kDeployPosition.in(Units.Rotations);
		return config;
	}

	public static MotorIOTalonFXConfig getIOConfig() {
		MotorIOTalonFXConfig config = new MotorIOTalonFXConfig();
		config.mainConfig = getFXConfig();
		config.mainID = Ports.INTAKE_PIVOT.getDeviceNumber();
		config.mainBus = Ports.INTAKE_PIVOT.getBus();
		config.time = Units.Seconds;
		config.unit = Units.Degrees;
		return config;
	}

	public static MotorIOTalonFX getMotorIO() {
		
		return new MotorIOTalonFX(getIOConfig());
		 
	}

	

	public static ServoHomingConfig getServoHomingConfig() {
		ServoHomingConfig config = new ServoHomingConfig();
		config.kHomePosition = kDeployPosition;
		config.kHomingTimeout = Units.Seconds.of(0.2);
		config.kHomingVoltage = Units.Volts.of(-1.0);
		config.kSetHomedVelocity = Units.DegreesPerSecond.of(1.0);

		return config;
	}
}