package frc.robot.commands.elevator;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;




public class ElevatorL1Command extends Command
{
  private final ElevatorSubsystem elevatorSubsystem;

  public ElevatorL1Command(ElevatorSubsystem subsystem) {
    elevatorSubsystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    elevatorSubsystem.setElevatorPosition(13.5);
  }
}