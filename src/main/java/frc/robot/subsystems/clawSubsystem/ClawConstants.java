package frc.robot.subsystems.clawSubsystem;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.units.BaseUnits;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.lib.io.ServoMotorSubsystem.ServoHomingConfig;
import frc.robot.lib.io.MotorIOTalonFX;
import frc.robot.lib.io.MotorIOTalonFX.MotorIOTalonFXConfig;

import frc.robot.Ports;

public class ClawConstants {

	public static final Voltage kIdleVoltage = Units.Volts.of(0.0);
	public static final Voltage kOutTakeVoltage = Units.Volts.of( -6.0);
	public static final Voltage kScoreVoltage = Units.Volts.of( -2.0);
	public static final Voltage kSuperPinch = Units.Volts.of(12);

	public static final Voltage kSuperPinchVoltage = Units.Volts.of( 8.0);
	
	public static TalonFXConfiguration getFXConfig() {
		TalonFXConfiguration config = new TalonFXConfiguration();
		config.Slot0.kP = 115.0;
		config.Slot0.kS = 0.0;
		config.Slot0.kG = 0.2;


		config.MotionMagic.MotionMagicCruiseVelocity = 100.0;
		config.MotionMagic.MotionMagicAcceleration = 80.0;

		config.Voltage.PeakForwardVoltage = 12.0;
		config.Voltage.PeakReverseVoltage = -12.0;

		config.CurrentLimits.SupplyCurrentLimitEnable = true;
		config.CurrentLimits.SupplyCurrentLimit = 40.0;
		config.CurrentLimits.SupplyCurrentLowerLimit = 40.0;
		config.CurrentLimits.SupplyCurrentLowerTime = 0.1;


		config.MotorOutput.NeutralMode = NeutralModeValue.Brake;


		return config;
	}

	public static MotorIOTalonFXConfig getIOConfig() {
		MotorIOTalonFXConfig config = new MotorIOTalonFXConfig();
		config.mainConfig = getFXConfig();
		config.mainID = Ports.END_EFFECTOR_CLAW.getDeviceNumber();
		config.mainBus = Ports.END_EFFECTOR_CLAW.getBus();
		config.time = Units.Seconds;
		config.unit = Units.Degrees;
		return config;
	}

	public static MotorIOTalonFX getMotorIO() {
		
		return new MotorIOTalonFX(getIOConfig());
		
	}

	
}