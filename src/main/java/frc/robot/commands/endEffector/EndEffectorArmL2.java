package frc.robot.commands.endEffector;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;

public class EndEffectorArmL2 extends Command
{
  private final EndEffectorSubsystem effectorSubSystem;

  public EndEffectorArmL2(EndEffectorSubsystem subsystem) {
      effectorSubSystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    
    effectorSubSystem.setWantedState(EndEffectorState.L2_DEPOSIT);
  }
}