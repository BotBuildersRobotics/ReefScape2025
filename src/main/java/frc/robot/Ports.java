package frc.robot;

import frc.robot.lib.CanDeviceId;

public class Ports {

    //each CAN device will have a name and a unique ID.
    //Right now each one has a random ID

    //Climb Subsystem Motors
    public static final CanDeviceId CLIMB_ONE = new CanDeviceId(9, "canivore");
    public static final CanDeviceId CLIMB_TWO = new CanDeviceId(10, "canivore");

    //Elevator Subsystem Motors
    public static final CanDeviceId ELEVATOR = new CanDeviceId(10002, "canivore");

    //End Effector Subsystem Motors
    public static final CanDeviceId END_EFFECTOR_ROLLER = new CanDeviceId(100, "canivore");

    //Intake Subsystem Motors
    public static final CanDeviceId INTAKE_TOP_ROLLER = new CanDeviceId(18, "canivore");
    
    //Pivot Subsystem Motors
    public static final CanDeviceId PIVOT_LEFT = new CanDeviceId(139042435,"canivore");
    public static final CanDeviceId PIVOT_RIGHT = new CanDeviceId(8972365, "canivore");

    //Transfer Subsytem Motors
    public static final CanDeviceId TRANSFER_LEFT = new CanDeviceId(1243567, "canivore");
    public static final CanDeviceId TRANSFER_RIGHT = new CanDeviceId(142356, "canivore");

    public static final CanDeviceId LEDS = new CanDeviceId(21, "canivore");

	public static final int PIGEON = 13;

	public static final int INTAKE_BEAMBREAK = 1;
	public static final int INTAKE_BEAMBREAKTWO = 2;

    // Wheel krakens
    public static final CanDeviceId TOPLEFT_LEFT = new CanDeviceId(1, "canivore");
}
