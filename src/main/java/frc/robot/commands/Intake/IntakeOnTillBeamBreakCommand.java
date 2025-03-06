package frc.robot.commands.intake;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;
import frc.robot.subsystems.led.LightsSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem.PivotSystemState;


//runs the intake until the beam break is detected on the transfer

public class IntakeOnTillBeamBreakCommand extends Command
{
  private final IntakeSubsystem intakeSubSystem;
  private final EndEffectorSubsystem effectorSubsystem;
  private final PivotSubsystem pivotSubsystem;
  private final LightsSubsystem lightsSubsystem;


  public IntakeOnTillBeamBreakCommand(IntakeSubsystem subsystem, EndEffectorSubsystem effector, LightsSubsystem lights, PivotSubsystem pivot) {
      intakeSubSystem = subsystem;
      effectorSubsystem = effector;
      lightsSubsystem = lights;
      pivotSubsystem = pivot;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem, effectorSubsystem);
  }

  @Override
  public void initialize() {
   // intakeSubSystem.setWantedState(IntakeSystemState.INTAKE);
    if(effectorSubsystem.getCurrentState() != EndEffectorState.INTAKE){
      if(pivotSubsystem.getCurrentState() == PivotSystemState.INTAKE){
        //only move the end effector down when intaking
        effectorSubsystem.setWantedState(EndEffectorState.INTAKE);
      }else{
        effectorSubsystem.setWantedState(EndEffectorState.IDLE);
      }
      effectorSubsystem.openClaw();
      
    }
    lightsSubsystem.clear();
  }

  @Override
  public void end(boolean interrupted){
    if(interrupted){
      intakeSubSystem.setWantedState(IntakeSystemState.IDLE);
    }
  }

  @Override 
  public void execute(){
   
   

      if(pivotSubsystem.getCurrentState() == PivotSystemState.ALGAE){
        intakeSubSystem.setWantedState(IntakeSystemState.ALGAE);
        effectorSubsystem.setWantedState(EndEffectorState.IDLE);
      }else if(pivotSubsystem.getCurrentState() == PivotSystemState.HUMAN_PLAYER){
        intakeSubSystem.setWantedState(IntakeSystemState.HUMAN_PLAYER);
        effectorSubsystem.setWantedState(EndEffectorState.IDLE);
      }else{
        //deploy the pivot and the intake
        pivotSubsystem.setWantedState(PivotSystemState.INTAKE);

        if(intakeSubSystem.isBeamBreakOneTripped()){
          intakeSubSystem.setWantedState(IntakeSystemState.STARS);
          effectorSubsystem.setWantedState(EndEffectorState.PRE_CLAMP);
          lightsSubsystem.coralStagedLed();
         
        }else{
          intakeSubSystem.setWantedState(IntakeSystemState.INTAKE);
        }
      
      }
   
  }

  @Override
  public boolean isFinished() {
    if(effectorSubsystem.getCurrentState() == EndEffectorState.PRE_CLAMP && intakeSubSystem.isBeamBreakTwoTripped())
    {
      effectorSubsystem.setWantedState(EndEffectorState.CLAMP);
     // intakeSubSystem.setWantedState(IntakeSystemState.IDLE);
    }
      
    return intakeSubSystem.isBeamBreakTwoTripped();
  }
}