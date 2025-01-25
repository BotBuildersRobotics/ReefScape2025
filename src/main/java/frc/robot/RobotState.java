package frc.robot;


import org.littletonrobotics.junction.AutoLogOutput;

import frc.robot.utils.TargettedReef;

public class RobotState {
    
  private static RobotState instance;

  public static RobotState getInstance() {
    if (instance == null) instance = new RobotState();
    return instance;
  }

 
  @AutoLogOutput(key = "RobotState/TargetReef")
  public TargettedReef TargetReef = Constants.Reef1;
 
  @AutoLogOutput(key = "RobotState/TargetLocation")
  public DropOffLocation TargetLocation = DropOffLocation.MIDDLE;

  
  @AutoLogOutput(key = "RobotState/TargetHeight")
  public DropOffHeight TargetHeight = DropOffHeight.L1;

  public enum DropOffLocation
  {
    LEFT,
    RIGHT,
    MIDDLE
  }

  public enum DropOffHeight
  {
    L1,
    L2,
    L3,
    L4
  }

  
}
