/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.*;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    Victor frontLeft = new Victor(1);
    Victor frontRight = new Victor(2);
    Victor backLeft = new Victor(3);
    Victor backRight = new Victor(4);
    double ch1, ch3, ch4;
    public void autonomous() {
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while(isOperatorControl() && isEnabled()){
            ch1 = joy1.getX();
            ch3 = joy2.getX();
            ch4 = joy2.getY();
            frontLeft.set(ch3 + ch1 + ch4);
            backLeft.set(ch3 + ch1 - ch4);
            frontRight.set(ch3 - ch1 - ch4);
            backRight.set(ch3 - ch1 + ch4);
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}
