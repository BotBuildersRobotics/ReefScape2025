package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;

//designed to open the rollers
public class EndEffectorRollerReverse extends Command
{
  private final EndEffectorSubsystem effectorSubSystem;

  public EndEffectorRollerReverse(EndEffectorSubsystem subsystem) {
      effectorSubSystem = subsystem;
      
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    effectorSubSystem.EndEffectorRollersOn(-5);
  }

  @Override
  public void end(boolean interupted)
  {
    effectorSubSystem.EndEffectorRollersOn(0);
  }
}