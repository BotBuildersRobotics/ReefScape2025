package frc.robot.subsystems.intake;

import frc.robot.lib.io.MotorSubsystem;
import frc.robot.lib.io.MotorIO.Setpoint;
import frc.robot.lib.io.MotorIOTalonFX;


public class IntakeSubsystem extends MotorSubsystem<MotorIOTalonFX> {
	public static final Setpoint IDLE = Setpoint.withNeutralSetpoint();
	public static final Setpoint INTAKE = Setpoint.withVoltageSetpoint(IntakeConstants.kIntakeVoltage);
	public static final Setpoint EXHAUST = Setpoint.withVoltageSetpoint(IntakeConstants.kExhaustVoltage);
	public static final Setpoint PELICAN = Setpoint.withVoltageSetpoint(IntakeConstants.kPelicanVoltage);
	public static final Setpoint START = Setpoint.withVoltageSetpoint(IntakeConstants.kStartVoltage);

	public static final IntakeSubsystem mInstance = new IntakeSubsystem();

	public IntakeSubsystem() {
		super(IntakeConstants.getMotorIO(), "Coral Rollers");
	}

	public boolean beamBreakTripped(){
		return false;
	}
}