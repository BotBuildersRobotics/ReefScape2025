package frc.robot.utils;

import org.littletonrobotics.junction.AutoLogOutput;

import edu.wpi.first.math.geometry.Pose2d;

public class TargettedReef
  {
        public TargettedReef(String name, Pose2d location){
            this.Location = location;
            this.Name = name;
        }
     
        public Pose2d Location;
        
        public String Name;
  }