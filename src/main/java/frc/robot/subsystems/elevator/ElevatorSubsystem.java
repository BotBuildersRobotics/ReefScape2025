package frc.robot.subsystems.elevator;


import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.lib.io.ServoMotorSubsystem;
import frc.robot.lib.io.MotorIO.Setpoint;
import frc.robot.lib.io.MotorIOTalonFX;

public class ElevatorSubsystem extends ServoMotorSubsystem<MotorIOTalonFX> {
	private final StructPublisher<Pose3d> stage1Publisher = NetworkTableInstance.getDefault()
			.getStructTopic("Mechanisms/Elevator Stage 1", Pose3d.struct)
			.publish();

	private final StructPublisher<Pose3d> stage2Publisher = NetworkTableInstance.getDefault()
			.getStructTopic("Mechanisms/Elevator Stage 2", Pose3d.struct)
			.publish();

	public static final Setpoint JOG_UP = Setpoint.withVoltageSetpoint(Voltage.ofBaseUnits(0.5, Units.Volts));
	public static final Setpoint JOG_DOWN = Setpoint.withVoltageSetpoint(Voltage.ofBaseUnits(-0.5, Units.Volts));
	
    public static final Setpoint HOLD_UP = Setpoint.withVoltageSetpoint(Voltage.ofBaseUnits(0, Units.Volts));

	public static final Setpoint L4_SCORE =
			Setpoint.withMotionMagicSetpoint(ElevatorConstants.converter.toAngle(ElevatorConstants.kL4ScoringHeight));
	public static final Setpoint L3_SCORE =
			Setpoint.withMotionMagicSetpoint(ElevatorConstants.converter.toAngle(ElevatorConstants.kL3ScoringHeight));
	public static final Setpoint L2_SCORE =
			Setpoint.withMotionMagicSetpoint(ElevatorConstants.converter.toAngle(ElevatorConstants.kL2ScoringHeight));
	public static final Setpoint L1_SCORE =
			Setpoint.withMotionMagicSetpoint(ElevatorConstants.converter.toAngle(ElevatorConstants.kL1ScoringHeight));
	public static final Setpoint STOW =
			Setpoint.withMotionMagicSetpoint(ElevatorConstants.converter.toAngle(ElevatorConstants.kStowPosition));
	
	public static final Setpoint INTAKE =
			Setpoint.withMotionMagicSetpoint(ElevatorConstants.converter.toAngle(ElevatorConstants.kLIntakeHeight));
	
			
	public static final ElevatorSubsystem mInstance = new ElevatorSubsystem();

	private ElevatorSubsystem() {
		super(
				ElevatorConstants.getMotorIO(),
				"Elevator",
				ElevatorConstants.converter.toAngle(ElevatorConstants.kEpsilonThreshold),
				ElevatorConstants.getServoConfig());
		setCurrentPosition(ElevatorConstants.converter.toAngle(ElevatorConstants.kStowPosition));
		applySetpoint(STOW);
	}

	@Override
	public void outputTelemetry() {
		super.outputTelemetry();
		stage2Publisher.set(ElevatorConstants.stage2Offset.plus(new Transform3d(
				new Translation3d(
						0.0,
						0.0,
						ElevatorConstants.converter.toDistance(getPosition()).in(Units.Meters)),
				new Rotation3d(0.0, 0.0, 0.0))));
		stage1Publisher.set(ElevatorConstants.stage1Offset.plus(new Transform3d(
				new Translation3d(
						0.0,
						0.0,
						ElevatorConstants.converter
								.toDistance(getPosition())
								.div(2.0)
								.in(Units.Meters)),
				new Rotation3d(0.0, 0.0, 0.0))));
	}
}