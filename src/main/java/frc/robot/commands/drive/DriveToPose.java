package frc.robot.commands.drive;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.utils.MaplePIDController;
import frc.robot.utils.MapleProfiledPIDController;

import java.util.function.Supplier;

import com.ctre.phoenix6.swerve.SwerveRequest;

public class DriveToPose extends Command {
    private final Supplier<Pose2d> desiredPoseSupplier;
    private final CommandSwerveDrivetrain driveSubsystem;
    private final HolonomicDriveController positionController;

    private final double speedConstrainMPS;
    private final Pose2d tolerance;

    public DriveToPose(CommandSwerveDrivetrain driveSubsystem, Supplier<Pose2d> desiredPoseSupplier) {
        this(driveSubsystem, desiredPoseSupplier, new Pose2d(0.1, 0.1, Rotation2d.fromDegrees(10)), 3);
    }

    public DriveToPose(
        CommandSwerveDrivetrain driveSubsystem,
            Supplier<Pose2d> desiredPoseSupplier,
            Pose2d tolerance,
            double speedConstrainMPS) {
        this.desiredPoseSupplier = desiredPoseSupplier;
        this.driveSubsystem = driveSubsystem;
        this.positionController = createPositionController(driveSubsystem);
        this.tolerance = tolerance;
        this.speedConstrainMPS = speedConstrainMPS;

        super.addRequirements(driveSubsystem);
    }

    @Override
    public void initialize() {
        getFeedBackSpeeds();
    }

    @Override
    public void execute() {
         ChassisSpeeds feedBackSpeeds =  getFeedBackSpeeds();
        final double feedBackSpeedMagnitude =
                Math.hypot(feedBackSpeeds.vxMetersPerSecond, feedBackSpeeds.vyMetersPerSecond);
        if (feedBackSpeedMagnitude < speedConstrainMPS)
            feedBackSpeeds = feedBackSpeeds.times(speedConstrainMPS / feedBackSpeedMagnitude);
       
       
        final ChassisSpeeds csp = feedBackSpeeds;

        driveSubsystem.applyRequest(() -> new SwerveRequest.RobotCentric().withVelocityX(csp.vxMetersPerSecond).withVelocityY(csp.vyMetersPerSecond)) ;   
       // driveSubsystem.runRobotCentricChassisSpeeds(feedBackSpeeds);
    }

    @Override
    public void end(boolean interrupted) {
        driveSubsystem.applyRequest(() -> new SwerveRequest.SwerveDriveBrake());
    }

    /** @return the feed-back speed, robot-relative */
     private ChassisSpeeds getFeedBackSpeeds() {
        return positionController.calculate(
                driveSubsystem.getState().Pose,
                desiredPoseSupplier.get(),
                0,
                desiredPoseSupplier.get().getRotation());
    } 

    @Override
    public boolean isFinished() {
        final Pose2d desiredPose = desiredPoseSupplier.get(), currentPose = driveSubsystem.getState().Pose;
        final ChassisSpeeds speeds = driveSubsystem.getState().Speeds;// .getMeasuredChassisSpeedsFieldRelative();
        return Math.abs(desiredPose.getX() - currentPose.getX()) < tolerance.getX()
                && Math.abs(desiredPose.getY() - currentPose.getY()) < tolerance.getY()
                && Math.abs(desiredPose.getRotation().getDegrees()
                                - currentPose.getRotation().getDegrees())
                        < tolerance.getRotation().getDegrees()
                && Math.hypot(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond) < 0.8
                && Math.abs(speeds.omegaRadiansPerSecond) < Math.toRadians(30);
    }

    public static HolonomicDriveController createPositionController(CommandSwerveDrivetrain driveSubsystem) {
       
      
       final TrapezoidProfile.Constraints chassisRotationalConstraints = new TrapezoidProfile.Constraints(
               15,// driveSubsystem.getChassisMaxAngularVelocity(),
                15); //driveSubsystem.getChassisMaxAngularAccelerationRadPerSecSq());

                MaplePIDController.MaplePIDConfig CHASSIS_TRANSLATION_CLOSE_LOOP = new MaplePIDController.MaplePIDConfig(2, 1.2, 0, 0.03, 0, false, 0);
                MaplePIDController.MaplePIDConfig CHASSIS_ROTATION_CLOSE_LOOP =
                new MaplePIDController.MaplePIDConfig(
                        Math.toRadians(540), Math.toRadians(45), 0.01, Math.toRadians(1), 0.05, true, 0);

                return new HolonomicDriveController(
                    new MaplePIDController(CHASSIS_TRANSLATION_CLOSE_LOOP),
                    new MaplePIDController(CHASSIS_TRANSLATION_CLOSE_LOOP),
                    new MapleProfiledPIDController(CHASSIS_ROTATION_CLOSE_LOOP, chassisRotationalConstraints));
    }
}