package frc.robot.subsystems.clawSubsystem;

import java.util.Set;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.units.Units;
import frc.robot.lib.io.ServoMotorSubsystem;
import frc.robot.lib.io.MotorIO.Setpoint;
import frc.robot.lib.io.MotorIOTalonFX;
import frc.robot.lib.io.MotorSubsystem;



public class ClawSubsystem extends MotorSubsystem<MotorIOTalonFX> {
	public static final Setpoint IDLE = Setpoint.withNeutralSetpoint();
	public static final Setpoint OUTTAKE = Setpoint.withVoltageSetpoint(ClawConstants.kOutTakeVoltage);
	public static final Setpoint SCORE = Setpoint.withVoltageSetpoint(ClawConstants.kScoreVoltage);
	public static final Setpoint SHOOT = Setpoint.withVoltageSetpoint(ClawConstants.kShootVoltage);
	public static final Setpoint SUPER_PINCH = Setpoint.withVoltageSetpoint(ClawConstants.kSuperPinch);
	public static final ClawSubsystem mInstance = new ClawSubsystem();

	public ClawSubsystem() {
		super(ClawConstants.getMotorIO(), "Claw Rollers");
	}

	
}