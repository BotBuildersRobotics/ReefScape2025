package frc.robot.commands.endEffector;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;


public class EndEffectorRollerOff extends Command
{
  private final EndEffectorSubsystem effectorSubSystem;

  public EndEffectorRollerOff(EndEffectorSubsystem subsystem) {
      effectorSubSystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
   // effectorSubSystem.setWantedState(EndEffectorState.IDLE);
    effectorSubSystem.SetEndEffectorRollers(0);
   
  }
}