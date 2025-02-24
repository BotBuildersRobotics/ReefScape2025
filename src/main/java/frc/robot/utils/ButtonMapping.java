package frc.robot.utils;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

//TODO: I think this can be made to include single function buttons for consistency in the mappings, but I dont have the time as of writing this
//Also I'm aware this is overengineered, the goal was for maximum functionality

public class ButtonMapping {
    CommandXboxController controller;

    public ButtonMapping(CommandXboxController controller) {
        this.controller = controller;
    }

    public class MultiFunctionButton{
        final Command[] functions;
        final Trigger button;

        public MultiFunctionButton(Trigger button, Command... functions) {
            this.button = button;
            this.functions = functions;
        }

        public Command current_function(int state) {
            return functions[state];
        }
    }

    public class FunctionRotateButton {
        final Trigger button;
        final MultiFunctionButton[] associatedButtons;
        public int state = 0;
        final int lastState;

        public FunctionRotateButton(Trigger button, MultiFunctionButton... associatedButtons) {
            this.button = button;
            this.associatedButtons = associatedButtons;
            this.lastState = associatedButtons.length;
            this.button.onTrue(new InstantCommand(() -> this.swap()));
        }

        public void swap() {
            state++;
            if(state > lastState) {
                state = 0;
            }
            for(MultiFunctionButton multiButton : associatedButtons) {
                multiButton.button.onTrue(multiButton.current_function(state));
            }
        }
    }

    public class SelfSwappingButton {
        final Trigger button;
        final Command[] functions;
        public int state = 0;
        final int lastState;

        public SelfSwappingButton(Trigger button, Command... functions) {
            this.button = button;
            this.functions = functions;
            this.lastState = functions.length;
            this.button.onFalse(new InstantCommand(() -> swap()));
        }

        public void swap() {
            state++;
            if(state > lastState) {
                state = 0;
            }
            button.onTrue(functions[state]);
        }
    }

    public void createMapping(Trigger swappingButton, MultiFunctionButton... functionalButtons) {
        new FunctionRotateButton(swappingButton, functionalButtons);
    }

    public MultiFunctionButton createButton(Trigger button, Command... functions) {
        return new MultiFunctionButton(button, functions);
    }

    public void createSelfSwap(Trigger button, Command... functions) {
        new SelfSwappingButton(button, functions);
    }
}