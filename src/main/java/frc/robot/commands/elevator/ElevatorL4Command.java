package frc.robot.commands.elevator;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem.ElevatorPosition;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;




public class ElevatorL4Command extends Command
{
  private final ElevatorSubsystem elevatorSubsystem;
  private final EndEffectorSubsystem effectorSubsystem;

  public ElevatorL4Command(ElevatorSubsystem subsystem,  EndEffectorSubsystem effector) {
    elevatorSubsystem = subsystem;
    effectorSubsystem = effector;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem, effector);
  }

  @Override
  public void initialize() {
    effectorSubsystem.setWantedState(EndEffectorState.L4_DEPOSIT);
    elevatorSubsystem.setWantedState(ElevatorPosition.L4);
  }

  @Override
  public boolean isFinished() {
    return elevatorSubsystem.checkElevatorPosition(ElevatorPosition.L4);
  }
}