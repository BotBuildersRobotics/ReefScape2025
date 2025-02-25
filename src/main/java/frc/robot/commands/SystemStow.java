package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;


public class SystemStow extends Command{
    private final PivotSubsystem pivotSubsystem;
    private final ElevatorSubsystem elevatorSubsystem;
    

    public SystemStow(PivotSubsystem pivot, ElevatorSubsystem elevator) {
        pivotSubsystem = pivot;
        elevatorSubsystem = elevator;
       
    }
}
