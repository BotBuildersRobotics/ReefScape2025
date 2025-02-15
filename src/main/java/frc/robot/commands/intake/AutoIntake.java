package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;

public class AutoIntake extends Command {
    private final IntakeSubsystem intakeSubsystem;
    public AutoIntake(IntakeSubsystem subsystem) {
        intakeSubsystem = subsystem;
        addRequirements(subsystem);
    }
}
