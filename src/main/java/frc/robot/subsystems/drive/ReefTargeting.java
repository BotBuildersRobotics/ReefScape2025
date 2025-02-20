package frc.robot.subsystems.drive;


import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import frc.robot.utils.AllianceFlipUtil;
import frc.robot.utils.FieldConstants.Reef;
import frc.robot.utils.FieldConstants.ReefHeight;

public class ReefTargeting {
     private ReefBranch          targetBranch;
    private ReefBranchLevel     targetBranchLevel;
    private Transform2d         robotBranchScoringOffset = new Transform2d(Inches.of(12).in(Meters),
                                                                         Inches.of(0).in(Meters),
                                                                         Rotation2d.fromDegrees(0));

  public double getTargetBranchHeightMeters()
  {
    switch (targetBranchLevel)
    {
      case L2 ->
      {
        return ReefHeight.L2.height;
      }
      case L3 ->
      {
        return ReefHeight.L3.height;
      }
      case L4 ->
      {
        return ReefHeight.L4.height;
      }
    }
    return 0;
  }

  public double getTargetBranchAlgaeArmAngle()
  {

    return 0;
  }

  public double getTargetBranchCoralArmAngle()
  {
    switch (targetBranchLevel)
    {
      case L2 ->
      {
        return ReefHeight.L2.pitch;
      }
      case L3 ->
      {
        return ReefHeight.L3.pitch;
      }
      case L4 ->
      {
        return ReefHeight.L4.pitch;
      }
    }
    return 0;
  }

  public void setTarget(ReefBranch targetBranch, ReefBranchLevel targetBranchLevel)
  {
    this.targetBranch = targetBranch;
    this.targetBranchLevel = targetBranchLevel;
  }

  public void left()
  {
    if (targetBranch == ReefBranch.H)
    {
      targetBranch = ReefBranch.I;
    }
  }

  public Pose2d getTargetPose()
  {
    Pose2d scoringPose = Pose2d.kZero;
    if(targetBranch != null)
      scoringPose = Reef.branchPositions.get(targetBranch.ordinal()).get(ReefHeight.L2).toPose2d()
                                                      .plus(robotBranchScoringOffset);
    return AllianceFlipUtil.apply(scoringPose);
  }

  public enum ReefBranch
  {
    A,
    B,
    K,
    L,
    I,
    J,
    G,
    H,
    E,
    F,
    C,
    D
  }


  public enum ReefBranchLevel
  {
    L1,
    L2,
    L3,
    L4,
    ALGAE
  }
}
