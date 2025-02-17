package frc.robot.commands.Intake;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;




public class IntakeReverseCommand extends Command
{
  private final IntakeSubsystem intakeSubSystem;

  public IntakeReverseCommand(IntakeSubsystem subsystem) {
      intakeSubSystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    intakeSubSystem.setWantedState(IntakeSystemState.REVERSE);
  }
}