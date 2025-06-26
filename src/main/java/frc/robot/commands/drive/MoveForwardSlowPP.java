package frc.robot.commands.drive;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.drive.DriveSubsystem;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.util.Units;


public class MoveForwardSlowPP extends Command {
    private final DriveSubsystem swerveDrive;
    private Command pathCommand;
    
    public MoveForwardSlowPP(DriveSubsystem swerveDrive) {
        this.swerveDrive = swerveDrive;
    }
    
    @Override
    public void initialize() {
        Pose2d currentPose = swerveDrive.getState().Pose;
        
        // Calculate target pose 50cm forward
        Pose2d targetPose = new Pose2d(
            currentPose.getX() + 0.5 * currentPose.getRotation().getCos(),
            currentPose.getY() + 0.5 * currentPose.getRotation().getSin(),
            currentPose.getRotation()
        );
        
        // Very slow constraints
        PathConstraints constraints = new PathConstraints(
            0.5, // Max velocity (m/s) - extra slow
            0.4, // Max acceleration (m/s²)
            Units.degreesToRadians(90),  // Max angular velocity
            Units.degreesToRadians(180) // Max angular acceleration
        );
        
        // Create the path command
        pathCommand = AutoBuilder.pathfindToPose(targetPose, constraints);
        pathCommand.initialize();
    }
    
    @Override
    public void execute() {
        pathCommand.execute();
    }
    
    @Override
    public boolean isFinished() {
        return pathCommand.isFinished();
    }
    
    @Override
    public void end(boolean interrupted) {
        pathCommand.end(interrupted);
        swerveDrive.setSwerveRequest(new SwerveRequest.SwerveDriveBrake());
    }
}