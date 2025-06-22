package frc.robot.commands.drive;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.lib.FieldLayout.Level;
import frc.robot.lib.drive.PIDToPoseCommand;
import frc.robot.Robot;
import frc.robot.subsystems.SuperSystem;
import frc.robot.subsystems.SuperSystemConstants;
import frc.robot.subsystems.vision.Limelight;

/**
 * Adds another layer to the FollowSyncedPIDToPose command, alerting the driver when a tag is not seen while scoring so that they know that they're on their own.
 * With where our camera is placed, no matter where you're scoring on the reef you should be able to see at least one tag, so something must be up if you can't.
 */
public class FollowTagPIDToPose extends PIDToPoseCommand {
	public FollowTagPIDToPose(Pose2d finalPose, Level level) {
		super(finalPose, level);
	}

	public FollowTagPIDToPose(Pose2d rawEndPose, Level level, boolean diffParam) {
		super(rawEndPose, level, diffParam);
	}


	public boolean hasRecentEstimate() {
		return Units.Seconds.of(Timer.getFPGATimestamp())
				.minus(Limelight.mInstance.getLastUpdateTime())
				.lte(SuperSystemConstants.kRecentUpdateTime);
	}

	public boolean latestEstimateNearTarget() {
		Pose2d currentVisPose = Limelight.mInstance.getLatestUpdate();

		return currentVisPose.getTranslation().getDistance(finalPose.getTranslation()) < epsilonDist.in(Units.Meters)
				&& MathUtil.angleModulus(Math.abs(currentVisPose
								.getRotation()
								.minus(finalPose.getRotation())
								.getRadians()))
						< epsilonAngle.in(Units.Radians);
	}

	
}