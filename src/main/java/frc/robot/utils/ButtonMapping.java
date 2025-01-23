package frc.robot.utils;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ButtonMapping {
    CommandXboxController controller;

    public ButtonMapping(CommandXboxController controller) {
        this.controller = controller;
    }

    public class MultiFunctionButton {
        final Command function1;
        final Command function2;
        final Trigger button;
        public MultiFunctionButton(Trigger button, Command function1, Command function2) {
            this.button = button;
            this.function1 = function1;
            this.function2 = function2;
        }

        public Command current_function(boolean inverted) {
            if(inverted) {
                return function2;
            }
            else {
                return function1;
            }
        }
    }

    public class FunctionSwitchingButton {
        final Trigger button;
        final MultiFunctionButton[] associatedButtons;
        public boolean isAlternate = false;

        public FunctionSwitchingButton(Trigger button, MultiFunctionButton[] associatedButtons) {
            this.button = button;
            this.associatedButtons = associatedButtons;
            button.onTrue(new InstantCommand(() -> this.invert()));
        }

        public void invert() {
            isAlternate = !isAlternate;
            for(MultiFunctionButton button : associatedButtons) {
                button.button.onTrue(button.current_function(isAlternate));
            }
        }
    }

    MultiFunctionButton aButton = new MultiFunctionButton(controller.a(), null, null);
    MultiFunctionButton bButton = new MultiFunctionButton(controller.b(), null, null);
    MultiFunctionButton xButton = new MultiFunctionButton(controller.x(), null, null);
    MultiFunctionButton yButton = new MultiFunctionButton(controller.y(), null, null);
    FunctionSwitchingButton inverter = new FunctionSwitchingButton(controller.leftTrigger(), new MultiFunctionButton[] {aButton, bButton, xButton, yButton}); //TODO: Change to a dpad button
}
