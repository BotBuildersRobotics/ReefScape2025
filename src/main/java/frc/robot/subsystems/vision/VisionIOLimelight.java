// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.subsystems.vision;


import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.Utils;

/** IO implementation for real Limelight hardware. */
public class VisionIOLimelight implements VisionIO {
  private final Supplier<Rotation2d> rotationSupplier;
  private final String limelightName;

  /**
   * Creates a new VisionIOLimelight.
   *
   * @param name The configured name of the Limelight.
   * @param rotationSupplier Supplier for the current estimated rotation, used for MegaTag 2.
   */
  public VisionIOLimelight(String name, Supplier<Rotation2d> rotationSupplier) {
      this.limelightName = name;
      this.rotationSupplier = rotationSupplier;
  }

  @Override
  public void updateInputs(VisionIOInputs inputs) {
  
    // Read new pose observations from NetworkTables
    Set<Integer> tagIds = new HashSet<>();
    List<PoseObservation> poseObservations = new LinkedList<>();

    inputs.connected = true;
    
    PoseEstimate estimate = LimelightHelpers.getBotPoseEstimate_wpiBlue(this.limelightName);

    Logger.recordOutput("LL Name",this.limelightName);

    if(estimate != null){
      
      Logger.recordOutput("PE",estimate.pose);
      poseObservations.add(
        new PoseObservation(  Utils.fpgaToCurrentTime(estimate.timestampSeconds),
                              new Pose3d(estimate.pose),
                              0.0, 
                              estimate.tagCount, 
                              estimate.avgTagDist, 
                              PoseObservationType.MEGATAG_1
                            )
      );

      for(int i =0; i < estimate.rawFiducials.length; i++){
        tagIds.add(estimate.rawFiducials[i].id);
      }
     
    }
    LimelightHelpers.SetRobotOrientation(this.limelightName, this.rotationSupplier.get().getDegrees(), 0, 0, 0, 0, 0);
    PoseEstimate megaTag2Estimate = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(this.limelightName);
   
    if(megaTag2Estimate != null){

      Logger.recordOutput("PE2",megaTag2Estimate.pose);
      poseObservations.add(
        new PoseObservation(  Utils.fpgaToCurrentTime(megaTag2Estimate.timestampSeconds),
                              new Pose3d(megaTag2Estimate.pose),
                              0.0, 
                              megaTag2Estimate.tagCount, 
                              megaTag2Estimate.avgTagDist, 
                              PoseObservationType.MEGATAG_2
                            )
      );

      for(int i =0; i < megaTag2Estimate.rawFiducials.length; i++){
        tagIds.add(megaTag2Estimate.rawFiducials[i].id);
      }
    }


    // Save pose observations to inputs object
    inputs.poseObservations = new PoseObservation[poseObservations.size()];
    for (int i = 0; i < poseObservations.size(); i++) {
      inputs.poseObservations[i] = poseObservations.get(i);
    }

    // Save tag IDs to inputs objects
    inputs.tagIds = new int[tagIds.size()];
    int i = 0;
    for (int id : tagIds) {
      inputs.tagIds[i++] = id;
    }
  }

 
}