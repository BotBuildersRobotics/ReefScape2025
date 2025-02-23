package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.elevator.ElevatorHomeCommand;
import frc.robot.commands.elevator.ElevatorL1Command;
import frc.robot.commands.elevator.ElevatorL2Command;
import frc.robot.commands.elevator.ElevatorL3Command;
import frc.robot.commands.elevator.ElevatorL4Command;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.drive.ReefTargeting.ReefBranchLevel;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;
import frc.robot.subsystems.led.LightsSubsystem;
import frc.robot.subsystems.led.LightsSubsystem.LightState;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem.PivotSystemState;
import frc.robot.subsystems.vision.TagVisionSubsystem;

public class SuperSystem extends SubsystemBase {
    
    private ElevatorSubsystem elevator = ElevatorSubsystem.getInstance();
    private IntakeSubsystem intake = IntakeSubsystem.getInstance();
    private EndEffectorSubsystem effector = EndEffectorSubsystem.getInstance();
    private PivotSubsystem pivot = PivotSubsystem.getInstance();
    private LightsSubsystem leds = LightsSubsystem.getInstance();
    private TagVisionSubsystem vision = TagVisionSubsystem.getInstance();
    private EndEffectorSubsystem effector = EndEffectorSubsystem.getInstance();
    

    private CommandSwerveDrivetrain swerveDriveTrain = CommandSwerveDrivetrain.getInstance();

    public static SuperSystem mInstance;

    ReefBranchLevel desiredReefLevel = ReefBranchLevel.L4;

    LightState desiredLightState = LightState.OFF;

    private boolean finishedAutoAlignment = false;

    public static SuperSystem getInstance() {

        //Rethink this for how advantage kit does 
		if (mInstance == null) {
			mInstance = new SuperSystem();
		}
		return mInstance;
	}

    @Override
    public void periodic() {

        SmartDashboard.putString("Desired Location", desiredReefLevel.toString());
    }

    //toggle the scoring position around around
    public ReefBranchLevel toggleScoringHeight(){

        if(desiredReefLevel == ReefBranchLevel.L4){
            desiredReefLevel = ReefBranchLevel.L1;
        }
        else if(desiredReefLevel == ReefBranchLevel.L1){
            desiredReefLevel = ReefBranchLevel.L2;
        }
        else if(desiredReefLevel == ReefBranchLevel.L2){
            desiredReefLevel = ReefBranchLevel.L3;
        }
        else if(desiredReefLevel == ReefBranchLevel.L3){
            desiredReefLevel = ReefBranchLevel.L4;
        }
    
        return desiredReefLevel;
    }

    
    public ReefBranchLevel getDesiredScoringLevel(){
        return desiredReefLevel;
    }

    public void targetReefAlgae(){
        desiredReefLevel = ReefBranchLevel.ALGAE;
    }

    public Command RunTargetElevator(){
        if(desiredReefLevel == ReefBranchLevel.L4){
            return new ElevatorL4Command(elevator);
        }
        else if(desiredReefLevel == ReefBranchLevel.L3){
            return new ElevatorL3Command(elevator);
        }
        else if(desiredReefLevel == ReefBranchLevel.L2){
            return new ElevatorL2Command(elevator);
        }
        else if(desiredReefLevel == ReefBranchLevel.L1){
            return new ElevatorL1Command(elevator);
        }
        else{
            return new ElevatorHomeCommand(elevator);
        }
       
    }

    public Command ToggleReefHeight(){
        return Commands.runOnce(() -> this.toggleScoringHeight());
    }

    public Command HumanPlayerIntake() {
        if(intake.isBeamBreakOneTripped()) {
            intake.setWantedState(IntakeSystemState.HUMAN_PLAYER);
        }
        
        
        //TODO: Need to make sure no coral is in the transfer
        //then pivot to a height that is correct for the coral station
        //then we intake just the front rollers (no stars)
        //once the sensor triggers in the intake, 
        //then we need to pivot down, so the coral can go through the transfer
        //we then start the transfer stars
        //kick the end effector in reverse for a brief second to open the arms up
        //then when we trigger the transfer beam break, we start the intake of the end effector
        //we then stop once the end effector detects the coral.

        if(effector.isCoralInIntake()) {
            pivot.setWantedState(PivotSystemState.HUMAN_PLAYER);
            intake.setWantedState(IntakeSystemState.HUMAN_PLAYER);
            if(intake.isBeamBreakOneTripped()) {
                pivot.setWantedState(PivotSystemState.INTAKE);
                intake.setWantedState(IntakeSystemState.INTAKE);
                if(!effector.isCoralInIntake()) {
                    effector.setWantedState(EndEffectorState.REVERSE);
                    if(intake.isBeamBreakTwoTripped()) {
                        effector.setWantedState(EndEffectorState.INTAKE);
                    }
                } else {
                    effector.setWantedState(EndEffectorState.IDLE);
                }
            }
        }

        return Commands.print("TODO: Complete me");
    }


    public LightState GetLightState() {
        if (intake.getCurrentState() == IntakeSystemState.INTAKE || intake.getCurrentState() == IntakeSystemState.HUMAN_PLAYER) {
            desiredLightState = LightState.ORANGE;
        } else if (intake.getCurrentState() == IntakeSystemState.IDLE) {
            if (intake.isBeamBreakOneTripped()) {
                if (finishedAutoAlignment) {
                    desiredLightState = LightState.GREEN;
                } else {
                    desiredLightState = LightState.BLUE;
                }
            } else {
                desiredLightState = LightState.OFF;
            }
        }
        return desiredLightState;
    }




    public LightState GetLightState() {
        if (intake.getCurrentState() == IntakeSystemState.INTAKE || intake.getCurrentState() == IntakeSystemState.HUMAN_PLAYER) {
            desiredLightState = LightState.ORANGE;
        } else if (intake.getCurrentState() == IntakeSystemState.IDLE) {
            if (intake.isBeamBreakOneTripped()) {
                if (finishedAutoAlignment) {
                    desiredLightState = LightState.GREEN;
                } else {
                    desiredLightState = LightState.BLUE;
                }
            } else {
                desiredLightState = LightState.OFF;
            }
        }
        return desiredLightState;
    }



    public Command IntakeIntoEndEffector(){

        return Commands.run( () ->{

            if(intake.isBeamBreakOneTripped()){
                intake.setWantedState(IntakeSystemState.STARS);
                effector.EndEffectorRollersOn(80);
            }else{
                intake.setWantedState(IntakeSystemState.INTAKE);
            }
        });
    }

    

}
