package frc.robot.commands.elevator;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SuperSystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem.ElevatorPosition;




public class ElevatorPositionToggleCommand extends Command
{
  private final SuperSystem superSystem;

  public ElevatorPositionToggleCommand(SuperSystem subsystem) {
    superSystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    superSystem.toggleScoringHeight();
  }
}