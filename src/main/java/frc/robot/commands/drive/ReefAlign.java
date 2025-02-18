package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.TagVisionSubsystem;

public class ReefAlign extends Command {
    private final CommandSwerveDrivetrain swerveDrive;
    private final TagVisionSubsystem visionSubsystem;
    public ReefAlign(CommandSwerveDrivetrain drivetrain, TagVisionSubsystem subsystem) {
        swerveDrive = drivetrain;
        visionSubsystem = subsystem;
    }
}
