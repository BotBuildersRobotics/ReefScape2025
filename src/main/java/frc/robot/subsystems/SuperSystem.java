package frc.robot.subsystems;

import java.util.Map;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.elevator.ElevatorL1Command;
import frc.robot.commands.elevator.ElevatorL2Command;
import frc.robot.commands.elevator.ElevatorL3Command;
import frc.robot.commands.elevator.ElevatorL4Command;
import frc.robot.commands.pivot.IntakePivotCommand;
import frc.robot.commands.pivot.StowPivotCommand;
import frc.robot.subsystems.drive.ReefTargeting.ReefBranchLevel;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem;
import frc.robot.subsystems.endEffector.EndEffectorSubsystem.EndEffectorState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.IntakeSystemState;
import frc.robot.subsystems.led.LightsSubsystem;
import frc.robot.subsystems.led.LightsSubsystem.LightState;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem.PivotSystemState;

public class SuperSystem extends SubsystemBase {
    
    private ElevatorSubsystem elevator = ElevatorSubsystem.getInstance();
    private IntakeSubsystem intake = IntakeSubsystem.getInstance();
    private EndEffectorSubsystem effector = EndEffectorSubsystem.getInstance();
    private PivotSubsystem pivot = PivotSubsystem.getInstance();
    private LightsSubsystem leds = LightsSubsystem.getInstance();
    //private TagVisionSubsystem vision = TagVisionSubsystem.getInstance();
   

    //private CommandSwerveDrivetrain swerveDriveTrain = CommandSwerveDrivetrain.getInstance();

    public static SuperSystem mInstance;

    public static ReefBranchLevel desiredReefLevel = ReefBranchLevel.L4; 

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

    }

    //toggle the scoring position around around
    public ReefBranchLevel toggleScoringHeightUp(){

        //show the green flow when the L4 is set
        if(desiredReefLevel == ReefBranchLevel.L4){
            leds.clear();
            desiredReefLevel = ReefBranchLevel.L2;
        }
        else if(desiredReefLevel == ReefBranchLevel.L2){
            desiredReefLevel = ReefBranchLevel.L3;
        }
        else if(desiredReefLevel == ReefBranchLevel.L3){
            leds.setStrobeState(LightState.COLOR_FLOW_GREEN);
            desiredReefLevel = ReefBranchLevel.L4;
        }

        SmartDashboard.putString("Desired Location", desiredReefLevel.toString());
    
        return desiredReefLevel;
    }

    public ReefBranchLevel toggleScoringHeightDown(){

        //show the green flow when the L4 is set
        if(desiredReefLevel == ReefBranchLevel.L3){
            leds.clear();
            desiredReefLevel = ReefBranchLevel.L2;
        }
        else if(desiredReefLevel == ReefBranchLevel.L4){
            desiredReefLevel = ReefBranchLevel.L3;
        }
        else if(desiredReefLevel == ReefBranchLevel.L2){
            leds.setStrobeState(LightState.COLOR_FLOW_GREEN);
            desiredReefLevel = ReefBranchLevel.L4;
        }

        SmartDashboard.putString("Desired Location", desiredReefLevel.toString());
    
        return desiredReefLevel;
    }
    
    public ReefBranchLevel getDesiredScoringLevel(){
        return desiredReefLevel;
    }

    public void targetReefAlgae(){
        desiredReefLevel = ReefBranchLevel.ALGAE;
    }

    public Command RunTargetElevator(){

        SmartDashboard.putString("Desired Location", desiredReefLevel.toString());

        return new SelectCommand<>
        (
            Map.ofEntries
            (
                Map.entry(ReefBranchLevel.L1, new ElevatorL1Command(elevator, effector)),
                Map.entry(ReefBranchLevel.L2, new ElevatorL2Command(elevator, effector)),
                Map.entry(ReefBranchLevel.L3, new ElevatorL3Command(elevator, effector)),
                Map.entry(ReefBranchLevel.L4, new ElevatorL4Command(elevator, effector))),
            this::getDesiredScoringLevel
        );
       
      
    }

    public PivotSystemState getCurrentPivotState(){
        return pivot.getCurrentState();
    }

    public Supplier<PivotSystemState> getPivotState(){
        return () -> pivot.getCurrentState();
    }

    public Command ToggleReefHeightUp() {
        return Commands.runOnce(()-> this.toggleScoringHeightUp());
    }

    public Command ToggleReefHeightDown(){
        return Commands.runOnce(() -> this.toggleScoringHeightDown());
    }

    public void ToogleIntakePivot(){
        
        effector.setWantedState(EndEffectorState.IDLE);

        if(pivot.getCurrentState() == PivotSystemState.STOWED){
            pivot.setWantedState(PivotSystemState.INTAKE);
        }else{
            if(intake.isBeamBreakTwoTripped()){
                //don't allow the pivot to go back while coral is in the intake
                pivot.setWantedState(PivotSystemState.INTAKE);
            }
            else if(intake.isBeamBreakOneTripped()){
                //don't allow the pivot to go back while coral is in the intake
                pivot.setWantedState(PivotSystemState.INTAKE);
            }else{
                //check the arm position
                if(!effector.isArmInIntakePosition()){
                    pivot.setWantedState(PivotSystemState.STOWED);
                }
            }
        }
      
    }

    public boolean isElevatorUp(){
        return elevator.isElevatorUp();
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


}
