// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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

    // copying more constans
    public static TalonFXConfiguration PivotFXConfig() {
      TalonFXConfiguration config = new TalonFXConfiguration();

      config.CurrentLimits.SupplyCurrentLimit = 20.0;
      config.CurrentLimits.SupplyCurrentLimitEnable = true;

      config.CurrentLimits.StatorCurrentLimitEnable = true;
      config.CurrentLimits.StatorCurrentLimit = 80;

      config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
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
}
