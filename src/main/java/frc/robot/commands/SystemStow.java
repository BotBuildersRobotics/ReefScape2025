package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.transfer.TransferSubsystem;

public class SystemStow extends Command{
    private final PivotSubsystem pivotSubsystem;
    private final ElevatorSubsystem elevatorSubsystem;
    private final TransferSubsystem transferSubsystem;

    public SystemStow(PivotSubsystem pivot, ElevatorSubsystem elevator, TransferSubsystem transfer) {
        pivotSubsystem = pivot;
        elevatorSubsystem = elevator;
        transferSubsystem = transfer;
    }
}
