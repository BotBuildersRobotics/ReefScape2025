package frc.robot.subsystems.elevator;


import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.units.BaseUnits;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Distance;
import frc.robot.lib.io.ServoMotorSubsystem.ServoHomingConfig;
import frc.robot.lib.io.MotorIOTalonFX;
import frc.robot.lib.io.MotorIOTalonFX.MotorIOTalonFXConfig;
import frc.robot.lib.Util;
import frc.robot.Ports;
import com.ctre.phoenix6.signals.InvertedValue;
import static edu.wpi.first.units.Units.*;

public class ElevatorConstants {

	public static final double kGearing = (2.18 / 1.0);

	public static final Util.DistanceAngleConverter converter = new Util.DistanceAngleConverter(
			Units.Inches.of(2.0).plus(Units.Inches.of(0.125)).div(2.0));

	public static final Pose3d stage1Offset = new Pose3d(-0.152, -0.165, -0.910, new Rotation3d());
	public static final Pose3d stage2Offset = new Pose3d(
			-0.0,
			0.0,
			0.0,
			new Rotation3d(BaseUnits.AngleUnit.zero(), BaseUnits.AngleUnit.zero(), Units.Degrees.of(90.0)));

	public static final Distance kMaxHeight = converter.toDistance(Units.Degrees.of(12*260));

	public static final Distance kL1ScoringHeight = Units.Centimeters.of(50);
	
	public static final Distance kL1ScoringLowL1Height = Units.Centimeters.of(40);
	
	public static final Distance kL2ScoringHeight = Units.Centimeters.of(6.3);
	public static final Distance kL3ScoringHeight = kL2ScoringHeight.plus(Units.Centimeters.of(16.0));
	public static final Distance kL4ScoringHeight = Units.Centimeters.of(73.25);
	
	public static final Distance kLIntakeHeight = Units.Centimeters.of(10);
	
	
	public static final Distance kStowPosition = Units.Centimeters.of(0.0);

	public static final Distance kEpsilonThreshold = Units.Centimeters.of(1.0);

	//public static final Distance kElevatorHighThreshold = kCoralHoldPosition.plus(kEpsilonThreshold);

	public static final TalonFXConfiguration getFXConfig() {
		TalonFXConfiguration FXConfig = new TalonFXConfiguration();
		FXConfig.Slot0.kP = 9.6;
		
		FXConfig.Slot0.kV = 0.3;
      
		FXConfig.Slot0.kG = 0.55; //volts to overcome gravity
      
		FXConfig.Slot0.kS = 0.34;// volts to get over the static friction
    
		FXConfig.Slot0.kA = 0.02; //volts for accel 

		FXConfig.Slot0.GravityType = GravityTypeValue.Elevator_Static;

		MotionMagicConfigs mm = FXConfig.MotionMagic;
        mm.withMotionMagicCruiseVelocity(RotationsPerSecond.of(60)) 
		.withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(80))
		.withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(220));

		FXConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
		FXConfig.CurrentLimits.SupplyCurrentLimit = 80.0;
		FXConfig.CurrentLimits.SupplyCurrentLowerLimit = 80.0;
		FXConfig.CurrentLimits.SupplyCurrentLowerTime = 0.1;

		FXConfig.CurrentLimits.StatorCurrentLimitEnable = true;
		FXConfig.CurrentLimits.StatorCurrentLimit = 120.0;

		FXConfig.Voltage.PeakForwardVoltage = 12.0;
		FXConfig.Voltage.PeakReverseVoltage = -12.0;

		FXConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
		FXConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
				converter.toAngle(kL4ScoringHeight).in(Units.Rotations);

		FXConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
		FXConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
				converter.toAngle(kStowPosition).minus(Units.Degrees.of(10.0)).in(Units.Rotations);

		FXConfig.Feedback.SensorToMechanismRatio = kGearing;

		FXConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

		FXConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

		return FXConfig;
	}

	public static MotorIOTalonFXConfig getIOConfig() {
		MotorIOTalonFXConfig IOConfig = new MotorIOTalonFXConfig();
		IOConfig.mainConfig = getFXConfig();
		IOConfig.mainID = Ports.ELEVATOR_LEFT.getDeviceNumber();
		IOConfig.mainBus = Ports.ELEVATOR_LEFT.getBus();
		IOConfig.unit = converter.getDistanceUnitAsAngleUnit(Units.Centimeters);
		IOConfig.time = Units.Second;
		IOConfig.followerConfig = getFXConfig()
				.withSoftwareLimitSwitch(new SoftwareLimitSwitchConfigs()
						.withForwardSoftLimitEnable(false)
						.withReverseSoftLimitEnable(false));
		IOConfig.followerOpposeMain = new boolean[] {true};
		IOConfig.followerBuses = new String[] {Ports.ELEVATOR_RIGHT.getBus()};
		IOConfig.followerIDs = new int[] {Ports.ELEVATOR_RIGHT.getDeviceNumber()};
		return IOConfig;
	}

	

	public static MotorIOTalonFX getMotorIO() {
		
			return new MotorIOTalonFX(getIOConfig());
		 
	}

	public static ServoHomingConfig getServoConfig() {
		ServoHomingConfig servoConfig = new ServoHomingConfig();
		servoConfig.kHomePosition = converter.toAngle(kStowPosition);
		servoConfig.kHomingTimeout = Units.Seconds.of(0.5);
		servoConfig.kHomingVoltage = Units.Volts.of(-0.5);
		servoConfig.kSetHomedVelocity = converter.toAngle(Units.Inches.of(0.1)).per(Units.Second);

		return servoConfig;
	}
}