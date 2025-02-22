package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;

public class EndEffectorPivotL4 extends Command
{
  private final EndEffectorSubsystem effectorSubSystem;

  public EndEffectorPivotL4(EndEffectorSubsystem subsystem) {
      effectorSubSystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    effectorSubSystem.SetEndEffectorPivotPos(70);
  }
}