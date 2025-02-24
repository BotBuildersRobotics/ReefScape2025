package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem.PivotSystemState;

public class AlgaeIntake extends Command {
    private final IntakeSubsystem intakeSubsystem;
    private final PivotSubsystem pivotSubsystem;
    public AlgaeIntake(IntakeSubsystem subsystem, PivotSubsystem pivot) {
        intakeSubsystem = subsystem;
        pivotSubsystem = pivot;
        addRequirements(subsystem, pivot);
    }

    
  @Override
  public void initialize() {
    pivotSubsystem.setWantedState(PivotSystemState.ALGAE);
    intakeSubsystem.setWantedState(IntakeSystemState.ALGAE);
  }

}

