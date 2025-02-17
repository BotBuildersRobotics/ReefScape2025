package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.VisionSubsystem;

public class ReefAlign extends Command {
    private final CommandSwerveDrivetrain swerveDrive;
    private final VisionSubsystem visionSubsystem;
    public ReefAlign(CommandSwerveDrivetrain drivetrain, VisionSubsystem subsystem) {
        swerveDrive = drivetrain;
        visionSubsystem = subsystem;
    }
}
