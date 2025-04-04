// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;

import static edu.wpi.first.units.Units.*;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  // Right now each motor is using common config for all intake motors
  // its ok to have a specific motor config
  // but always define values here to make it easy to find.

  public static final class ClimbConstants {

    // copying more constants. possible to add more constants for different motors
    // by adding more functions but for now just use the same ones
    public static TalonFXConfiguration ClimbFXConfig() {
      TalonFXConfiguration config = new TalonFXConfiguration();

      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
      return config;
    }

  }
  
  public static final class ElevatorConstants {

    public static TalonFXConfiguration ElevatorFXConfig() {
      TalonFXConfiguration config = new TalonFXConfiguration();

      // copy the configurations because it doesn't actually exist
      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
      config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
      config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 12.2;
      config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -0.05;

      //want the motors to stop.
      config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

      //slot configs

      config.Slot0 = new Slot0Configs();
      
      config.Slot0.kP = 9.6;
      config.Slot0.kD = 0;
      config.Slot0.GravityType = GravityTypeValue.Elevator_Static;
     
      config.Slot0.kV = 0.3;
      
      config.Slot0.kG = 0.55; //volts to overcome gravity
      
      config.Slot0.kS = 0.34;// volts to get over the static friction
    
      config.Slot0.kA = 0.02; //volts for accel 
     
        //motion magic

      MotionMagicConfigs mm = config.MotionMagic;
      mm.withMotionMagicCruiseVelocity(RotationsPerSecond.of(60)) 
      .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(80))
     
      .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(220));



      return config;

    }
  }

  public static final class EndEffectorConstants {

   
    public static double END_EFFECTOR_CLAW_START = 15;
    public static double END_EFFECTOR_CLAW_END = 46;
    

    // copying more constans
    public static TalonFXConfiguration EndEffectorSpinner() {
      TalonFXConfiguration config = new TalonFXConfiguration();

      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
     
      return config;
    }

    public static TalonFXSConfiguration endEffectorClaw() {
      TalonFXSConfiguration config = new TalonFXSConfiguration();
  
        config.CurrentLimits.SupplyCurrentLimit = 20.0;
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
  
        config.CurrentLimits.StatorCurrentLimitEnable = true;
        config.CurrentLimits.StatorCurrentLimit = 80;
  
        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        config.Commutation.MotorArrangement = MotorArrangementValue.NEO550_JST;
        config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        //config.ExternalFeedback.RotorToSensorRatio = 5.8;

            /* Configure Motion Magic */
        MotionMagicConfigs mm = config.MotionMagic;
        mm.withMotionMagicCruiseVelocity(RotationsPerSecond.of(1150)) 
          .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(800));
          

      Slot0Configs slot0 = config.Slot0;
     // slot0.kS = 0.99; //  overcome static friction
     
     // slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
      //slot0.kA = 0.02; // An acceleration of 1 rps/s requires 0.02 V output
      slot0.kP = 2.5; // 
    // slot0.kI = 0; // No output for integrated error
     // slot0.kD = 0.05; 
   

        return config;
    }

    public static TalonFXConfiguration endEffectorClaw2() {
      TalonFXConfiguration config = new TalonFXConfiguration();

      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      //config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

      config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

      config.Slot0.kP = 2;
      config.Slot0.kD = 0;
      config.Slot0.kS = 0.1;
      config.Slot0.kG = 0.1;
      config.Voltage.withPeakForwardVoltage(Volts.of(8)).withPeakReverseVoltage(Volts.of(-8));
     
      return config;
    }

   

    public static TalonFXConfiguration EndEffectorArmPivot(){
      TalonFXConfiguration config = new TalonFXConfiguration();

      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
      config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

      //config.Feedback.RotorToSensorRatio = 0;//45;
      

       /* Configure Motion Magic */
    MotionMagicConfigs mm = config.MotionMagic;
    mm.withMotionMagicCruiseVelocity(RotationsPerSecond.of(200)) 
      .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(260))
      // Take approximately 0.1 seconds to reach max accel 
      .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(450));

    Slot0Configs slot0 = config.Slot0;
    slot0.kS = 0.35; //  overcome static friction
    slot0.kA = 0.05;
    slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    slot0.kA = 0.01; // An acceleration of 1 rps/s requires 0.01 V output
    slot0.kP = 5.2; // 
   // slot0.kI = 0; // No output for integrated error
    slot0.kD = 0.15; 
    slot0.GravityType = GravityTypeValue.Arm_Cosine;

    /*CANcoder m_cc = new CANcoder(Ports.FUSED_END_EFFECTOR_ARM.getDeviceNumber(), Ports.FUSED_END_EFFECTOR_ARM.getBus());

    CANcoderConfiguration cc_cfg = new CANcoderConfiguration();
    
    cc_cfg.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
    cc_cfg.MagnetSensor.MagnetOffset = 0.25;
    m_cc.getConfigurator().apply(cc_cfg);

    config.Feedback.FeedbackRemoteSensorID = Ports.FUSED_END_EFFECTOR_ARM.getDeviceNumber();
    config.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;

   
*/

      return config;
    }

    public static double CORAL_DETECT_MIN_DISTANCE_MM = 25;
  }

  public static final class IntakeConstants {

    public static TalonFXConfiguration IntakeFXConfig() {
      TalonFXConfiguration config = new TalonFXConfiguration();

      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
      config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
      return config;
    }
  }
  public static final class IntakeTransferConstants {

      public static TalonFXConfiguration IntakeFXConfig() {
        TalonFXConfiguration config = new TalonFXConfiguration();
  
        config.CurrentLimits.SupplyCurrentLimit = 20.0;
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
  
        config.CurrentLimits.StatorCurrentLimitEnable = true;
        config.CurrentLimits.StatorCurrentLimit = 80;
  
        config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        return config;
      }
  }

  public static final class PivotConstants {

   
    public static TalonFXConfiguration PivotFXConfig() {
      TalonFXConfiguration config = new TalonFXConfiguration();

      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
      config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

     /*   config.SoftwareLimitSwitch = new SoftwareLimitSwitchConfigs();
      config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
      config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0;
      config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
      //config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 20; //TODO: Work out */


      config.Slot0 = new Slot0Configs();
      config.Slot0.kA = 0;
      config.Slot0.kP = 0.3;
      config.Slot0.kI = 0;
      config.Slot0.kD = 0;
      config.Slot0.kS = 0.3;
      config.Slot0.kV = 0.12;

      


      config.MotionMagic = new MotionMagicConfigs();
      config.MotionMagic.MotionMagicAcceleration = 100;
      config.MotionMagic.MotionMagicCruiseVelocity = 100;
      config.MotionMagic.MotionMagicJerk = 0;
      


      config.ClosedLoopRamps = new ClosedLoopRampsConfigs();
      config.ClosedLoopRamps .DutyCycleClosedLoopRampPeriod = 0.02;
      config.ClosedLoopRamps .TorqueClosedLoopRampPeriod = 0.02;
      config.ClosedLoopRamps .VoltageClosedLoopRampPeriod = 0.02;

      //config.Feedback.SensorToMechanismRatio = 51;

      config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

      return config;
    }
  }

  public static final class TransferConstants{

    public static TalonFXConfiguration TransferFXConfig() {
      TalonFXConfiguration config = new TalonFXConfiguration();
      
      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
      return config;
    }

  }

  public static final class PathFollowingConstants{

    public static double SpeedConstrainMPS = 2;

    public static double AutoAlignSpeedMultiplier = 0.75;

    public static double RotationKp = 1;

    public static double RotationKd = 0;

    public static double RotationKi = 0;

    public static double ChassisMaxAngularVelocity = 3;

    public static double ChassisMaxAngularAccelerationRadPerSecSq = 3;
  }

  public class VisionConstants {
    // AprilTag layout
    public static AprilTagFieldLayout APRILTAG_LAYOUT = AprilTagFieldLayout.loadField(AprilTagFields.k2025Reefscape);

    // Camera names, must match names configured on coprocessor
    public static String CAM_0_NAME = "limelight";
    public static String CAM_1_NAME = "camera_1";

    // Robot to camera transforms
    // (Not used by Limelight, configure in web UI instead)
    public static Transform3d robotToCamera0 =
        new Transform3d(0.2, 0.0, 0.2, new Rotation3d(0.0, -0.4, 0.0)); // facing forward
    public static Transform3d robotToCamera1 =
        new Transform3d(-0.2, 0.0, 0.2, new Rotation3d(0.0, -0.4, Math.PI)); // facing backward

    // Basic filtering thresholds
    public static double maxAmbiguity = 0;
    public static double maxZError = 0.2;//75;

    // Standard deviation baselines, for 1 meter distance and 1 tag
    // (Adjusted automatically based on distance and # of tags)
    public static double linearStdDevBaseline = 0.08; // Meters
    public static double angularStdDevBaseline = 0.06; // Radians

    // Standard deviation multipliers for each camera
    // (Adjust to trust some cameras more than others)
    public static double[] cameraStdDevFactors =
        new double[] {
          1.0, // Camera 0
          1.0 // Camera 1
        };

    // Multipliers to apply for MegaTag 2 observations
    public static double linearStdDevMegatag2Factor = 0.5; // More stable than full 3D solve
    public static double angularStdDevMegatag2Factor =
        Double.POSITIVE_INFINITY; // No rotation data available
  }

  public static class TrajectoryConstants {

    public static final double AUTO_LINEUP_ROTATION_P = 0.058;
    public static final double AUTO_LINEUP_ROTATION_I = 0;
    public static final double AUTO_LINEUP_ROTATION_D = 0;
    public static final Constraints AUTO_LINEUP_ROTATION_CONSTRAINTS =
        new Constraints(0.1 * Math.PI, 6 * Math.PI);

    public static final double AUTO_LINEUP_TRANSLATION_P = 4.0;
    public static final double AUTO_LINEUP_TRANSLATION_I = 0;
    public static final double AUTO_LINEUP_TRANSLATION_D = 0;
    public static final Constraints AUTO_LINEUP_TRANSLATION_CONSTRAINTS = new Constraints(0.3, 0.3);
  }

  public static final double AUTO_ALIGN_MAX_SPEED = 0.7; //meters per second
  public static final double ALIGN_RIGHT_OFFSET = -0.15;
  public static final double ALIGN_LEFT_OFFSET = 0.15; //meters from center
  public static final double ALIGN_DIS_REEF = -0.6; //meters - distance to reef

  public static class SwerveConstants {

        public static final double DRIVE_KP = 0.17105;
        public static final double DRIVE_KI = 0.0;
        public static final double DRIVE_KD = 0.0;
        public static final double DRIVE_KS = 0.045286;
        public static final double DRIVE_KV = 0.1141075;
        public static final double DRIVE_KA = 0.005900075;
        
        public static final double TURNING_KP = 75.0;
        public static final double TURNING_KI = 0.0;
        public static final double TURNING_KD = 0.0;
        public static final double TURNING_KS = 0.0;
        public static final double TURNING_KV = 0.0;
        public static final double TURNING_KA = 0.0;

        public static final double ROTATION_KP = 0.0;
        public static final double ROTATION_KI = 0.0;
        public static final double ROTATION_KD = 0.0;
        public static final double ROTATION_KS = 0.0;
        public static final double ROTATION_KV = 0.0;
        public static final double ROTATION_KA = 0.0;

        public static final double TRANSLATION_PP_KP = 4.0;
        public static final double TRANSLATION_PP_KI = 0.0;
        public static final double TRANSLATION_PP_KD = 0.0;

        public static final double ROTATION_PP_KP = 2.5;
        public static final double ROTATION_PP_KI = 0.0;
        public static final double ROTATION_PP_KD = 0.0;

        public static final double CLOSE_TRANSLATION_PP_KP = 3.0;
        public static final double CLOSE_TRANSLATION_PP_KI = 0.0;
        public static final double CLOSE_TRANSLATION_PP_KD = 0.0;

        public static final double CLOSE_ROTATION_PP_KP = 2.0;
        public static final double CLOSE_ROTATION_PP_KI = 0.0;
        public static final double CLOSE_ROTATION_PP_KD = 0.0;

        public static final double MAX_LINEAR_VELOCITY = 2.0;
        public static final double MAX_LINEAR_ACCELERATION = 1.5;
        public static final double MAX_ANGULAR_VELOCITY = 2 * Math.PI;
        public static final double MAX_ANGULAR_ACCELERATION = 4 * Math.PI;

    }

    public static class AutoDriveConstants {

        public static final Pose2d[] BLUE_REEF_POSES = {
            new Pose2d(2.823, 4.000, new Rotation2d(0 * Math.PI / 180.0)),
            new Pose2d(3.719, 2.614, new Rotation2d(60 * Math.PI / 180.0)),
            new Pose2d(5.430, 2.640, new Rotation2d(120 * Math.PI / 180.0)),
            new Pose2d(6.131, 4.000, new Rotation2d(180 * Math.PI / 180.0)),
            new Pose2d(5.384, 5.406, new Rotation2d(-120 * Math.PI / 180.0)),
            new Pose2d(3.689, 5.515, new Rotation2d(-60 * Math.PI / 180.0))
        };

        public static final Pose2d[] RED_REEF_POSES = {
            new Pose2d(2.823 + 8.553921, 4.000, new Rotation2d(0 * Math.PI / 180.0)),
            new Pose2d(3.719 + 8.553921, 2.614, new Rotation2d(60 * Math.PI / 180.0)),
            new Pose2d(5.430 + 8.553921, 2.640, new Rotation2d(120 * Math.PI / 180.0)),
            new Pose2d(6.000 + 8.553921, 4.000, new Rotation2d(180 * Math.PI / 180.0)),
            new Pose2d(5.384 + 8.553921, 5.406, new Rotation2d(-120 * Math.PI / 180.0)),
            new Pose2d(3.689 + 8.553921, 5.515, new Rotation2d(-60 * Math.PI / 180.0))
        };

        public static final double[][] ADDITIONS = {
            {0.342, 0}, // LEFT ADDITION
            {0.342, -0.348}  // RIGHT ADDITION
        };

    }


}
