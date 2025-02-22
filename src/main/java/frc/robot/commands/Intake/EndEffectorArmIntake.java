package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;

public class EndEffectorArmIntake extends Command
{
  private final EndEffectorSubsystem effectorSubSystem;

  public EndEffectorArmIntake(EndEffectorSubsystem subsystem) {
      effectorSubSystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    effectorSubSystem.SetEndEffectorPivotPos(0);
    effectorSubSystem.SetEndEffectorArmPos(0);
  }
}