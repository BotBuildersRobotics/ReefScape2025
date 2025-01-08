package frc.robot;

import frc.robot.lib.CanDeviceId;

public class Ports {

    //each CAN device will have a name and a unique ID.
    public static final CanDeviceId INTAKE_TOP_ROLLER = new CanDeviceId(1, "canivore");

    public static final CanDeviceId LEDS = new CanDeviceId(21, "canivore");

	public static final int PIGEON = 13;

	public static final int INTAKE_BEAMBREAK = 1;
	public static final int INTAKE_BEAMBREAKTWO = 2;
}
