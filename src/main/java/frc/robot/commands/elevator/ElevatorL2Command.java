package frc.robot.commands.elevator;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem.ElevatorPosition;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;




public class ElevatorL2Command extends Command
{
  private final ElevatorSubsystem elevatorSubsystem;
  private final EndEffectorSubsystem effectorSubsystem;

  public ElevatorL2Command(ElevatorSubsystem subsystem, EndEffectorSubsystem effector) {
    elevatorSubsystem = subsystem;
    effectorSubsystem = effector;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem, effector);
  }

  @Override
  public void initialize() {
    effectorSubsystem.setWantedState(EndEffectorState.L2_DEPOSIT);
    elevatorSubsystem.setWantedState(ElevatorPosition.L2);
  }
}