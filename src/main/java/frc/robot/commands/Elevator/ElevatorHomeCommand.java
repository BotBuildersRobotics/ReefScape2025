package frc.robot.commands.elevator;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem.ElevatorPosition;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;



public class ElevatorHomeCommand extends Command
{
  private final ElevatorSubsystem elevatorSubsystem;
  

  public ElevatorHomeCommand(ElevatorSubsystem subsystem) {
    elevatorSubsystem = subsystem;
   
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {

    elevatorSubsystem.setWantedState(ElevatorPosition.STOWED);

  }
}