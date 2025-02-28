package frc.robot.commands.pivot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem.PivotSystemState;

public class StowPivotCommand  extends Command
{
  private final PivotSubsystem pivotSubSystem;

  public StowPivotCommand(PivotSubsystem subsystem) {
        pivotSubSystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    pivotSubSystem.setWantedState(PivotSystemState.STOWED);
  }
/*
    @Override
  public boolean isFinished(){
    return pivotSubSystem.isAtLocation(PivotSystemState.STOWED);
  }*/
}