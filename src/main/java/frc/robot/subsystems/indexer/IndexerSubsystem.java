package frc.robot.subsystems.indexer;

import frc.robot.lib.io.MotorSubsystem;
import frc.robot.lib.io.MotorIO.Setpoint;
import frc.robot.lib.io.MotorIOTalonFX;

public class IndexerSubsystem extends MotorSubsystem<MotorIOTalonFX> {
	public static final Setpoint IDLE = Setpoint.withNeutralSetpoint();
	public static final Setpoint INTAKE = Setpoint.withVoltageSetpoint(IndexerConstants.kIntakeVoltage);
	public static final Setpoint EXHAUST = Setpoint.withVoltageSetpoint(IndexerConstants.kExhaustVoltage);

	public static final IndexerSubsystem mInstance = new IndexerSubsystem();

	private IndexerSubsystem() {
		super(IndexerConstants.getMotorIO(), "Coral Indexer");
	}
}