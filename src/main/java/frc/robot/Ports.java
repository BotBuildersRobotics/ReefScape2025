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
    public static final CanDeviceId INTAKE = new CanDeviceId(18, "canivore");
    public static final CanDeviceId TRANSFER = new CanDeviceId(19, "canivore");
    
    //Pivot Subsystem Motors
    public static final CanDeviceId PIVOT = new CanDeviceId(139042435,"canivore");
   
    //Transfer Subsytem Motors
    public static final CanDeviceId TRANSFER_LEFT = new CanDeviceId(1243567, "canivore");
    public static final CanDeviceId TRANSFER_RIGHT = new CanDeviceId(142356, "canivore");

    public static final CanDeviceId LEDS = new CanDeviceId(21, "canivore");

	public static final int PIGEON = 13;

	public static final int INTAKE_BEAMBREAK = 1;
	public static final int INTAKE_BEAMBREAKTWO = 2;

    // Wheel krakens
    public static final CanDeviceId TOPRIGHT_ROT = new CanDeviceId(1, "canivore");
    public static final CanDeviceId TOPRIGHT_DRIVE = new CanDeviceId(3, "canivore");
    public static final CanDeviceId BOTTOMRIGHT_ROT = new CanDeviceId(4, "canivore");
    public static final CanDeviceId BOTTOMRIGHT_DRIVE = new CanDeviceId(6, "canivore");
    public static final CanDeviceId BOTTOMLEFT_ROT = new CanDeviceId(7, "canivore");
    public static final CanDeviceId BOTTOMLEFT_DRIVE = new CanDeviceId(9, "canivore");
    public static final CanDeviceId TOPLEFT_ROT = new CanDeviceId(10, "canivore");
    public static final CanDeviceId TOPLEFT_DRIVE = new CanDeviceId(12, "canivore");

    // Wheel cancoder
    public static final CanDeviceId TOPRIGHT_ENCODER = new CanDeviceId(2, "canivore");
    public static final CanDeviceId BOTTOMRIGHT_ENCODER = new CanDeviceId(5, "canivore");
    public static final CanDeviceId BOTTOMLEFT_ENCODER = new CanDeviceId(8, "canivore");
    public static final CanDeviceId TOPLEFT_ENCODER = new CanDeviceId(11, "canivore");
}
