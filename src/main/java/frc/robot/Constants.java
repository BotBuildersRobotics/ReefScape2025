// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;

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

      config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
      return config;

    }
  }

  public static final class EndEffectorConstants {

    // copying more constans
    public static TalonFXConfiguration EndEffectorFXConfig() {
      TalonFXConfiguration config = new TalonFXConfiguration();

      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
      return config;
    }
  }

  public static final class IntakeConstants {

    public static TalonFXConfiguration IntakeFXConfig() {
      TalonFXConfiguration config = new TalonFXConfiguration();

      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
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
}
