package frc.robot.commands.drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

public class ChassisFaceToRotation extends Command {
    private final CommandSwerveDrivetrain driveSubsystem;
    private final Supplier<Rotation2d> targetRotationSupplier;
    private final PIDController chassisRotationController;
    private final Rotation2d tolerance;

    public ChassisFaceToRotation(
        CommandSwerveDrivetrain driveSubsystem, Supplier<Rotation2d> targetRotationSupplier, Rotation2d tolerance) {
        this.driveSubsystem = driveSubsystem;
        this.targetRotationSupplier = targetRotationSupplier;
        this.tolerance = tolerance;

        this.chassisRotationController = new PIDController(5, 0, 0);// new MaplePIDController(DriveControlLoops.CHASSIS_ROTATION_CLOSE_LOOP);
    }

    @Override
    public void initialize() {
        chassisRotationController.calculate(
                driveSubsystem.getState().Pose.getRotation().getRadians(),
                targetRotationSupplier.get().getRadians());
    }

    @Override
    public void execute() {
        final double rotationFeedBack = chassisRotationController.calculate(
            driveSubsystem.getState().Pose.getRotation().getRadians(),
                targetRotationSupplier.get().getRadians());
       
       final SwerveRequest.ApplyRobotSpeeds robotSpeed = new SwerveRequest.ApplyRobotSpeeds();
      
       robotSpeed
            .withSpeeds(new ChassisSpeeds(0, 0, rotationFeedBack))
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
           
            driveSubsystem.setControl(robotSpeed);

        Logger.recordOutput("FaceToRotationCommand/ErrorDegrees", getError().getDegrees());
    }

    @Override
    public void end(boolean interrupted) {
        driveSubsystem.setControl( new SwerveRequest.SwerveDriveBrake());
    }

    @Override
    public boolean isFinished() {
        return Math.abs(getError().getRadians()) < tolerance.getRadians();
    }

    private Rotation2d getError() {
        return driveSubsystem.getState().Pose.getRotation().minus(targetRotationSupplier.get());
    }

    public static ChassisFaceToRotation faceToTarget(
            CommandSwerveDrivetrain driveSubsystem, Supplier<Translation2d> targetPositionSupplier) {
        return new ChassisFaceToRotation(
                driveSubsystem,
                () -> targetPositionSupplier
                        .get()
                        .minus(driveSubsystem.getState().Pose.getTranslation())
                        .getAngle(),
                Rotation2d.fromDegrees(2));
    }
}