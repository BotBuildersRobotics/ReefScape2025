package frc.robot.commands.elevator;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem.ElevatorPosition;




public class ElevatorL3Command extends Command
{
  private final ElevatorSubsystem elevatorSubsystem;

  public ElevatorL3Command(ElevatorSubsystem subsystem) {
    elevatorSubsystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    elevatorSubsystem.setElevatorPosition(ElevatorPosition.L3);
  }
}