package frc.robot.commands.drive;


import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class ControllerRumbleCommand extends Command {
    private CommandXboxController controller;
    private BooleanSupplier condition;
    private final Timer timer = new Timer();
    private static final double DURATION = 0.2;
    
    public ControllerRumbleCommand(CommandXboxController controller, BooleanSupplier condition) {
        this.controller = controller;
        this.condition = condition;
       
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        //SmartDashboard.putBoolean("Rumble", condition.getAsBoolean());
        if (condition.getAsBoolean()) {
            controller.getHID().setRumble(GenericHID.RumbleType.kBothRumble, 0.8);
        }
    }

    @Override
    public void end(boolean interrupted) {
        controller.getHID().setRumble(GenericHID.RumbleType.kBothRumble, 0.0);
         timer.stop();
    }


    @Override
    public boolean runsWhenDisabled() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(DURATION);
    }
}