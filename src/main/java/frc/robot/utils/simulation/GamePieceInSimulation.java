package frc.robot.utils.simulation;



import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rotation;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

/**
 * simulates the behavior of gamepiece on field. game pieces HAVE collision spaces. they can also be "grabbed" by an
 * Intake Simulation the game piece will also be displayed on advantage scope (once registered in
 * CompetitionFieldSimulation)
 */
public abstract class GamePieceInSimulation extends Body implements GamePieceOnFieldDisplay {
    /** for convenience, we assume all game pieces have the following properties */
    public static final double DEFAULT_MASS = 0.2,
            LINEAR_DAMPING = 3.5,
            ANGULAR_DAMPING = 5,
            EDGE_COEFFICIENT_OF_FRICTION = 0.8,
            EDGE_COEFFICIENT_OF_RESTITUTION = 0.3;

    public GamePieceInSimulation(Translation2d initialPosition, Convex shape) {
        this(initialPosition, shape, DEFAULT_MASS);
    }

    public GamePieceInSimulation(Translation2d initialPosition, Convex shape, double mass) {
        super();
        BodyFixture bodyFixture = super.addFixture(shape);
        bodyFixture.setFriction(EDGE_COEFFICIENT_OF_FRICTION);
        bodyFixture.setRestitution(EDGE_COEFFICIENT_OF_RESTITUTION);
        bodyFixture.setDensity(mass / shape.getArea());
        super.setMass(MassType.NORMAL);

        super.translate(toDyn4jVector2(initialPosition));

        super.setLinearDamping(LINEAR_DAMPING);
        super.setAngularDamping(ANGULAR_DAMPING);
        super.setBullet(true);
    }

    public  Pose2d toWpilibPose2d(Transform dyn4jTransform) {
        return new Pose2d(
                toWpilibTranslation2d(dyn4jTransform.getTranslation()),
                toWpilibRotation2d(dyn4jTransform.getRotation()));
    }

    public Translation2d toWpilibTranslation2d(Vector2 dyn4jVector2) {
        return new Translation2d(dyn4jVector2.x, dyn4jVector2.y);
    }

    public Vector2 toDyn4jVector2(Translation2d wpilibTranslation2d) {
        return new Vector2(wpilibTranslation2d.getX(), wpilibTranslation2d.getY());
    }

    public Rotation toDyn4jRotation(Rotation2d wpilibRotation2d) {
        return new Rotation(wpilibRotation2d.getRadians());
    }

    public Rotation2d toWpilibRotation2d(Rotation dyn4jRotation) {
        return new Rotation2d(dyn4jRotation.toRadians());
    }

    
    public Pose2d getObjectOnFieldPose2d() {
        return toWpilibPose2d(super.getTransform());
    }
}