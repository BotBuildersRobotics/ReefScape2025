package frc.robot.commands.transfer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;

public class EndEffectorL4Deposit extends Command
{
  private final EndEffectorSubsystem endEffectorSubsystem;

  public EndEffectorL4Deposit(EndEffectorSubsystem subsystem) {
      endEffectorSubsystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    endEffectorSubsystem.setWantedState(EndEffectorState.L4_DEPOSIT);
    endEffectorSubsystem.SetEndEffectorRollers();
  }
}