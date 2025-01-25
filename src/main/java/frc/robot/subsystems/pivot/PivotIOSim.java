package frc.robot.subsystems.pivot;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import frc.robot.Robot;

public class PivotIOSim implements PivotIO{
    private final SingleJointedArmSim pivotSim;

    public PivotIOSim() {
        //25:1 * (2.18) = 54.5
        //90
        double PIVOT_LOWEST_ROTATION_RAD = 0;
        double PIVOT_HIGHER_LIMIT_RAD = Units.degreesToRadians(90);

        this.pivotSim = new SingleJointedArmSim(
                DCMotor.getKrakenX60Foc(1),
                54.5,
                SingleJointedArmSim.estimateMOI(0.5, 15),
                0.4,
                PIVOT_LOWEST_ROTATION_RAD,
                PIVOT_HIGHER_LIMIT_RAD,
                true,
                PIVOT_LOWEST_ROTATION_RAD);
    }

    @Override
    public void updateInputs(PivotIOInputs pivotInputs) {
        pivotSim.update(Robot.defaultPeriodSecs);
      
    }

}
