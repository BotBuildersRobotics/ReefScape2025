package frc.robot.commands.leds;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.led.LightsSubsystem;
import frc.robot.subsystems.led.LightsSubsystem.LightState;




public class SetLedCommand extends Command
{
  private final LightsSubsystem lightsSubsystem;
  private LightState lightState;

  public SetLedCommand(LightsSubsystem subsystem, LightState lightState) {
    lightsSubsystem = subsystem;
    this.lightState = lightState;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    lightsSubsystem.setStrobeState(lightState);
  }
}