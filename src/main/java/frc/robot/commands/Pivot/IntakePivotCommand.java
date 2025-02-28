package frc.robot.commands.pivot;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem.PivotSystemState;

public class IntakePivotCommand  extends Command
{
  private final PivotSubsystem pivotSubSystem;

  private Supplier<PivotSystemState> pivotState;

  public IntakePivotCommand(PivotSubsystem subsystem) {
      pivotSubSystem = subsystem;

      pivotState = pivotSubSystem.getCurrentStateSupplier();
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {

    
    if(pivotState.get() == PivotSystemState.STOWED){
      pivotSubSystem.setWantedState(PivotSystemState.INTAKE);
    }else{
      pivotSubSystem.setWantedState(PivotSystemState.STOWED);
    }

  }


  

 /* @Override
  public boolean isFinished(){
    return true;//pivotSubSystem.isAtLocation(PivotSystemState.INTAKE);
  }*/

}