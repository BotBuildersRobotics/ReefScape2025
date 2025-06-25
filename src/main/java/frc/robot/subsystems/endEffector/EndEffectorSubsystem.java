package frc.robot.subsystems.endEffector;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.units.Units;
import frc.robot.lib.io.ServoMotorSubsystem;
import frc.robot.lib.io.MotorIO.Setpoint;
import frc.robot.Ports;
import frc.robot.lib.io.MotorIOTalonFX;
import com.ctre.phoenix6.hardware.CANcoder;

import static edu.wpi.first.units.Units.Degrees;

import com.ctre.phoenix6.configs.CANcoderConfiguration;



public class EndEffectorSubsystem extends ServoMotorSubsystem<MotorIOTalonFX> {

    private CANcoder encoder = new CANcoder(Ports.PIVOT_ARM_ENCODER.getDeviceNumber(), Ports.PIVOT_ARM_ENCODER.getBus());
	private StructPublisher<Pose3d> publisher = NetworkTableInstance.getDefault()
			.getStructTopic("Mechanisms/End Effector Pivot", Pose3d.struct)
			.publish();

	public static final Setpoint STOW = Setpoint.withMotionMagicSetpoint(EndEffectorConstants.kStowPosition);
	public static final Setpoint PROCESSOR = Setpoint.withMotionMagicSetpoint(EndEffectorConstants.kProcessorPosition);
	public static final Setpoint L1_SCORE = Setpoint.withMotionMagicSetpoint(EndEffectorConstants.kL1Score);
	
	public static final Setpoint L4_PRESCORE = Setpoint.withMotionMagicSetpoint(EndEffectorConstants.kL4PreScore);
	public static final Setpoint L4_SCORE = Setpoint.withMotionMagicSetpoint(EndEffectorConstants.kL4Score);
	

	public static final Setpoint L3_PRESCORE = Setpoint.withMotionMagicSetpoint(EndEffectorConstants.kL3PreScore);
	public static final Setpoint L3_SCORE = Setpoint.withMotionMagicSetpoint(EndEffectorConstants.kL3Score);
	
	public static final Setpoint L2_ALGAE = Setpoint.withMotionMagicSetpoint(EndEffectorConstants.kL2Algae);

	public static final EndEffectorSubsystem mInstance = new EndEffectorSubsystem();

	public EndEffectorSubsystem() {
       
		super(
            EndEffectorConstants.getMotorIO(),
				"End Effector ARM Pivot",
				Units.Degrees.of(3.0));
        encoder.getConfigurator().apply(EndEffectorConstants.getEncoderConfig());
		applySetpoint(STOW);
	}

	@Override
	public void outputTelemetry() {
		super.outputTelemetry();
		publisher.set(EndEffectorConstants.kOffsetPose.plus(new Transform3d(
				new Translation3d(), new Rotation3d(0.0, getPosition().in(Units.Radians), 0.0))));
	}

    @Override
    public void periodic(){
        super.periodic();
       
    }
}