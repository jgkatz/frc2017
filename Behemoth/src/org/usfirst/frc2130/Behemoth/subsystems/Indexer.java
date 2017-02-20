// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc2130.Behemoth.subsystems;

import org.usfirst.frc2130.Behemoth.Robot;
import org.usfirst.frc2130.Behemoth.RobotMap;
import org.usfirst.frc2130.Behemoth.commands.*;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Indexer extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon indexerMaster = RobotMap.indexerIndexerMaster;
    private final CANTalon indexerSlave = RobotMap.indexerIndexerSlave;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    
    // Indexer
    
	public void indexerFeed() {
		if (Robot.flyWheel.readEncoder() > Robot.flyWheel.adjustedFlywheelSpeed) {
			indexerMaster.set(0.8);
			indexerSlave.set(-0.8);
		}
		else {
			indexerStop();
		}
	}

	public void indexerClear() {
		indexerSlave.set(-0.8);
		indexerMaster.set(0.8);
	}

	public void indexerStop() {
		indexerSlave.set(0);
		indexerMaster.set(0);
		}
}

