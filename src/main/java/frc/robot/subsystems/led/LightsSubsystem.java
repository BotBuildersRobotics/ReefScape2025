package frc.robot.subsystems.led;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.ColorFlowAnimation;

import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.StrobeAnimation;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ports;

public class LightsSubsystem extends SubsystemBase {

    private CANdle led;

    public static LightsSubsystem mInstnace;

    public static LightsSubsystem getInstance() {
        if (mInstnace == null) {
            mInstnace = new LightsSubsystem();
        }
        return mInstnace;
    }

    public LightsSubsystem() {

        // set up the CANdle
        led = new CANdle(Ports.LEDS.getDeviceNumber(), Ports.LEDS.getBus());
    }

    public void setStrobeState(LightState state) {

        led.configBrightnessScalar(0.6);

        if (state == LightState.RED) {
            led.animate(new StrobeAnimation(255, 0, 0));
        }

        if (state == LightState.ORANGE) {
            led.animate(new StrobeAnimation(255, 165, 0));
        }

        if (state == LightState.YELLOW) {
            led.animate(new StrobeAnimation(246, 255, 0));
        }

        if (state == LightState.GREEN) {
            led.animate(new StrobeAnimation(0, 255, 0));
        }

        if (state == LightState.BLUE) {
            led.animate(new StrobeAnimation(0, 0, 255));
        }
        if (state == LightState.PURPLE) {
            led.animate(new StrobeAnimation(170, 0, 2555));
        }
        if (state == LightState.FIRE) {
            led.animate(new FireAnimation());
        }

        if (state == LightState.RAINBOW) {
            led.animate(new RainbowAnimation());
        }

        if (state == LightState.COLOR_FLOW_RED) {
            led.animate(new ColorFlowAnimation(255, 0, 0));
        }
        if (state == LightState.COLOR_FLOW_GREEN) {
            led.animate(new ColorFlowAnimation(0, 255, 0));
        }
        if (state == LightState.COLOR_FLOW_BLUE) {
            led.animate(new ColorFlowAnimation(0, 0, 255));
        }
        if (state == LightState.OFF) {
            led.setLEDs(0, 0, 0);
            led.configBrightnessScalar(0);
        }
    }

    public void lowTimeLed() {
        this.setStrobeState(LightState.YELLOW);
    }

    public void coralStagedLed() {
        this.setStrobeState(LightState.BLUE);
    }

    public void reefAlignLed() {
        this.setStrobeState(LightState.PURPLE);
    }

    public void algaeDepositLed() {
        this.setStrobeState(LightState.GREEN);
    }

    public void errorLed() {
        this.setStrobeState(LightState.RED);
    }

    public enum LightState {
        RED,
        ORANGE,
        YELLOW,
        GREEN,
        BLUE,
        PURPLE,
        FIRE,
        RAINBOW,
        COLOR_FLOW_RED,
        COLOR_FLOW_GREEN,
        COLOR_FLOW_BLUE,
        OFF
    }

}
