package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;

public class EndEffectorRollerIntake extends Command
{
  private final EndEffectorSubsystem effectorSubSystem;

  public EndEffectorRollerIntake(EndEffectorSubsystem subsystem) {
      effectorSubSystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    effectorSubSystem.setWantedState(EndEffectorState.INTAKE);
    effectorSubSystem.SetEndEffectorRollers();
  }

  @Override
  public boolean isFinished() {
    return effectorSubSystem.isCoralInIntake();
  }
}