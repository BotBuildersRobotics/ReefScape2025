package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;


//runs the intake until the beam break is detected on the transfer

public class IntakeOnTillBeamBreakCommand extends Command
{
  private final IntakeSubsystem intakeSubSystem;
  private final EndEffectorSubsystem effectorSubsystem;

  public boolean hasTrippedSensor = false;

  public IntakeOnTillBeamBreakCommand(IntakeSubsystem subsystem, EndEffectorSubsystem effector) {
      intakeSubSystem = subsystem;
      effectorSubsystem = effector;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    intakeSubSystem.setWantedState(IntakeSystemState.INTAKE);
    //hasTrippedSensor = false;
    //effectorSubsystem.SetEndEffectorArmPos(-15);
    //effectorSubsystem.SetEndEffectorPivotPos(-10);

    effectorSubsystem.setWantedState(EndEffectorState.INTAKE);
  }

  @Override
  public boolean isFinished() {
    if(intakeSubSystem.isBeamBreakOneTripped()){
      intakeSubSystem.setWantedState(IntakeSystemState.STARS);
      effectorSubsystem.SetEndEffectorRollers(-5);
      
      hasTrippedSensor = true;
    
    }else{
      
      effectorSubsystem.SetEndEffectorRollers(0);
    }

    if(hasTrippedSensor){
      if(!intakeSubSystem.isBeamBreakOneTripped()){
         effectorSubsystem.SetEndEffectorRollers(0);
          effectorSubsystem.setWantedState(EndEffectorState.IDLE);
         
         hasTrippedSensor = false;
         return true;
      }
    }
    return false;
  }
}