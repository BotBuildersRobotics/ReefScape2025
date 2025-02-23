package frc.robot.commands.elevator;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem.ElevatorPosition;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;




public class ElevatorL1Command extends Command
{
  private final ElevatorSubsystem elevatorSubsystem;
  private final EndEffectorSubsystem effectorSubsystem;

  public ElevatorL1Command(ElevatorSubsystem subsystem, EndEffectorSubsystem endEffector) {
    elevatorSubsystem = subsystem;
    effectorSubsystem = endEffector;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem, endEffector);
      
  }

  @Override
  public void initialize() {
    effectorSubsystem.setWantedState(EndEffectorState.L1_DEPOSIT);
    elevatorSubsystem.setWantedState(ElevatorPosition.L1);
  }
}