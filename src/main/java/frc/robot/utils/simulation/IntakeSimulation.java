package frc.robot.utils.simulation;

import java.util.ArrayDeque;
import java.util.Queue;

import org.dyn4j.collision.CollisionBody;
import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.contact.Contact;
import org.dyn4j.dynamics.contact.SolvedContact;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Segment;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.world.listener.ContactListener;

import edu.wpi.first.math.geometry.Translation2d;

public abstract class IntakeSimulation extends BodyFixture  {
    
    private final int capacity;
    protected int gamePieceCount;

    private final Queue<GamePieceInSimulation> gamePiecesToRemove;

    public IntakeSimulation(Translation2d startPointOnRobot, Translation2d endPointOnRobot, int capacity) {
        this(new Segment(
            new Vector2(startPointOnRobot.getX(), startPointOnRobot.getY()),
            new Vector2(endPointOnRobot.getX(), endPointOnRobot.getY()))
            ,capacity);
    }

    /**
     * Creates an intake simulation the intake is fixed shape on the robot any game piece that touches the line will be
     * grabbed
     *
     * @param shape the shape of the intake
     * @param capacity the amount of game-pieces that can be hold in the intake\
     */
    public IntakeSimulation(Convex shape, int capacity) {
        super(shape);
        if (capacity > 100) throw new IllegalArgumentException("capacity too large, max is 100");
        this.capacity = capacity;

        this.gamePiecesToRemove = new ArrayDeque<>(capacity);
    }

    protected abstract boolean isIntakeRunning();

    @SuppressWarnings("rawtypes")
    public final class GamePieceContactListener implements ContactListener<Body> {
        @Override
        public void begin(ContactCollisionData collision, Contact contact) {
            if (!isIntakeRunning()) return;
            if (gamePieceCount == capacity) return;

            final CollisionBody<?> collisionBody1 = collision.getBody1(), collisionBody2 = collision.getBody2();
            final Fixture fixture1 = collision.getFixture1(), fixture2 = collision.getFixture2();

            if (collisionBody1 instanceof GamePieceInSimulation && fixture2 == IntakeSimulation.this)
                flagGamePieceForRemoval((GamePieceInSimulation) collisionBody1);
            else if (collisionBody2 instanceof GamePieceInSimulation && fixture1 == IntakeSimulation.this)
                flagGamePieceForRemoval((GamePieceInSimulation) collisionBody2);
        }

        private void flagGamePieceForRemoval(GamePieceInSimulation gamePiece) {
            gamePiecesToRemove.add(gamePiece);
            gamePieceCount++;
        }

        /* functions not used */
        @Override
        public void persist(ContactCollisionData collision, Contact oldContact, Contact newContact) {}

        @Override
        public void end(ContactCollisionData collision, Contact contact) {}

        @Override
        public void destroyed(ContactCollisionData collision, Contact contact) {}

        @Override
        public void collision(ContactCollisionData collision) {}

        @Override
        public void preSolve(ContactCollisionData collision, Contact contact) {}

        @Override
        public void postSolve(ContactCollisionData collision, SolvedContact contact) {}
    }

    public GamePieceContactListener getGamePieceContactListener() {
        return new GamePieceContactListener();
    }

    public Queue<GamePieceInSimulation> getGamePiecesToRemove() {
        return gamePiecesToRemove;
    }

    public void clearGamePiecesToRemoveQueue() {
        this.gamePieceCount += gamePiecesToRemove.size();
        gamePiecesToRemove.clear();
    }

     public Vector2 toDyn4jVector2(Translation2d wpilibTranslation2d) {
        return new Vector2(wpilibTranslation2d.getX(), wpilibTranslation2d.getY());
    }
}
