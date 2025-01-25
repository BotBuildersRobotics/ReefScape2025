package frc.robot.commands.drive;

import frc.robot.Constants.PathFollowingConstants;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import java.util.function.Supplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;


public class PathFindToPose extends CommandOnFly {
    public PathFindToPose(
            CommandSwerveDrivetrain driveSubsystem,
            Supplier<Pose2d> targetPose,
            double speedMultiplier,
            double goalEndVelocity) {
           
              
          super(() -> AutoBuilder.pathfindToPose(
                        targetPose.get(),  new PathConstraints(
                PathFollowingConstants.SpeedConstrainMPS, 4.0,
                Units.degreesToRadians(540), Units.degreesToRadians(720)), goalEndVelocity)
                
                .beforeStarting(Commands.print("Starting path")));
    }
}