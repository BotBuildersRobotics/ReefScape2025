package frc.robot.subsystems;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.lib.io.BeamBreakIO;
import frc.robot.lib.io.BeamBreakIOCANRange;
import frc.robot.lib.io.BeamBreakIOSim;
import frc.robot.subsystems.drive.ControlBoardConstants;
import frc.robot.subsystems.intake.IntakeSubsystem;

import frc.robot.Ports;
import frc.robot.Robot;
import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.configs.CANrangeConfiguration;


public class SuperSystemConstants {
    public static class BeamBreakConstants 
    {
        public static BeamBreakIO getIntakeRollersCurrentSpike() {
                
            BooleanSupplier bool = () -> {
                try {
                    return IntakeSubsystem.mInstance.getStatorCurrent().gte(Units.Amps.of(55.0));
                } catch (Exception e) {
                    return false;
                }
            };
            return new BeamBreakIOSim(
                    bool,
                    SuperSystemConstants.kIntakelRollersCurrentSpikeDebounce,
                    "Intake Stars Stator Current Break");
                
            
        }

        public static BeamBreakIO getIntakeRollersVelocityDip() {
        
                return new BeamBreakIOSim(
                        () -> IntakeSubsystem.mInstance.getVelocity().abs(Units.RPM) < 500.0,
                        SuperSystemConstants.kIntakeRollersVelocityDebounce,
                        "Intake Stars Rollers Velocity Dip");
        
        }

        public static BeamBreakIO getIndexerBeamBreak() {
			
            CANrangeConfiguration config = new CANrangeConfiguration();

            return new BeamBreakIOCANRange(
                Ports.INTAKE_1_CANRANGE.getDeviceNumber(), 
                Ports.INTAKE_1_CANRANGE.getBus(),
                config,
                SuperSystemConstants.kIntakeCoralBeamBreakDebounce,
                "Intake Beam Break", 100);
			
		}
    }

    public static final Time kIntakelRollersCurrentSpikeDebounce = Units.Seconds.of(0.4);
    public static final Time kIntakeRollersVelocityDebounce = Units.Seconds.of(0.04);
    public static final Time kIntakeCoralBeamBreakDebounce = Units.Seconds.of(0.04);


    public static final Time lookaheadBranchSelectionTime = Units.Milliseconds.of(100.0);
}
