package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;
import frc.robot.subsystems.led.LightsSubsystem;


//runs the intake until the beam break is detected on the transfer

public class IntakeOnTillBeamBreakCommand extends Command
{
  private final IntakeSubsystem intakeSubSystem;
  private final EndEffectorSubsystem effectorSubsystem;
  private final LightsSubsystem lightsSubsystem;


  public IntakeOnTillBeamBreakCommand(IntakeSubsystem subsystem, EndEffectorSubsystem effector, LightsSubsystem lights) {
      intakeSubSystem = subsystem;
      effectorSubsystem = effector;
      lightsSubsystem = lights;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem, effectorSubsystem);
  }

  @Override
  public void initialize() {
    intakeSubSystem.setWantedState(IntakeSystemState.INTAKE);
    effectorSubsystem.setWantedState(EndEffectorState.INTAKE);
     lightsSubsystem.clear();
  }

  @Override
  public boolean isFinished() {
    if(intakeSubSystem.isBeamBreakOneTripped()){
      intakeSubSystem.setWantedState(IntakeSystemState.STARS);
     
      lightsSubsystem.coralStagedLed();
      
      return true;
    
    }
    return false;
  }
}