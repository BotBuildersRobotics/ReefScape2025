package frc.robot.commands.transfer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.transfer.TransferSubsystem;

public class L1Command extends Command{
    private final TransferSubsystem transferSubsystem;
    private final ElevatorSubsystem elevatorSubsystem;

    public L1Command(TransferSubsystem transfer, ElevatorSubsystem elevator) {
        transferSubsystem = transfer;
        elevatorSubsystem = elevator;
    }
}
