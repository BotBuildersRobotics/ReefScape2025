package frc.robot.subsystems.intake;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.utils.simulation.IntakeSimulation;


public class IntakeIOSim extends IntakeSimulation implements IntakeIO {
   
 
     private double intakeCurrent = 0.0;
    
    public IntakeIOSim() {
        super(
                new Translation2d(1, 0.3),
                new Translation2d(1, -0.3),
                1);
        // initial preload
        super.gamePieceCount = 1;
       
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.intakeConnected = true;
        inputs.intakeCurrent = intakeCurrent;
    }

    @Override
    protected boolean isIntakeRunning() {
        return intakeCurrent > 5;
    }
}
