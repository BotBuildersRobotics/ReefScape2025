package frc.robot.commands.endEffector;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;

public class EndEffectorIdle extends Command
{
  private final EndEffectorSubsystem effectorSubSystem;

  public EndEffectorIdle(EndEffectorSubsystem subsystem) {
      effectorSubSystem = subsystem;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    effectorSubSystem.setWantedState(EndEffectorState.IDLE);
   
  }

  @Override
  public boolean isFinished(){
   
    return (effectorSubSystem.getArmAngle()) >= 0.2 ;
  }
}