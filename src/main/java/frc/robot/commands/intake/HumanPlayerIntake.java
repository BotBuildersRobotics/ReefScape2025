package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;

public class HumanPlayerIntake extends Command {
    private final IntakeSubsystem intakeSubsystem;
    public HumanPlayerIntake(IntakeSubsystem subsystem) {
        intakeSubsystem = subsystem;
        addRequirements(subsystem);
    }
}
