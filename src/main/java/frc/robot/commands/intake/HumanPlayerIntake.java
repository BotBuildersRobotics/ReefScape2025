package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem.PivotSystemState;

public class HumanPlayerIntake extends Command {
    private final IntakeSubsystem intakeSubsystem;
    private final PivotSubsystem pivotSubsystem;
    public HumanPlayerIntake(IntakeSubsystem subsystem, PivotSubsystem pivot) {
        intakeSubsystem = subsystem;
        pivotSubsystem = pivot;
        addRequirements(subsystem, pivot);
    }

    
  @Override
  public void initialize() {
    pivotSubsystem.setWantedState(PivotSystemState.HUMAN_PLAYER);
    intakeSubsystem.setWantedState(IntakeSystemState.HUMAN_PLAYER);
  }

  @Override
  public boolean isFinished() {

    if(intakeSubsystem.isBeamBreakTwoTripped()){
        intakeSubsystem.setWantedState(IntakeSystemState.IDLE);
    }

    return intakeSubsystem.isBeamBreakTwoTripped();
  }
}

