/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.amhs.robotdrive;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DriverStationLCD;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robotdrive extends SimpleRobot {
    //Joysitcks 1 and 2
    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    //Victors for drive
    Victor frontLeft = new Victor(1);
    Victor frontRight = new Victor(2);
    Victor backLeft = new Victor(3);
    Victor backRight = new Victor(4);
    //Other Victors
    Victor shooter = new Victor(5);
    Victor loader = new Victor(6);
    //Limit Switch
    DigitalInput button1 = new DigitalInput(1);
    //Doubles for joystick axis
    double ch1, ch3, ch4;
    //doubles for motor speed modifiers
    double frontLeftModifier, frontRightModifier, backLeftModifier, backRightModifier;
    boolean old = false;
    boolean old4 = false;
    String status;
    //message printed when robot is initialized
    public void robotInit() {
        System.out.println("Robot Initialized");
    }
    public void autonomous() {
        
    }
    
    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while(isOperatorControl() && isEnabled()){
           //limit switch basic code
            if(button1.get() != old){
                System.out.println(button1.get());
                old = button1.get();
            }
            //driving codex
            ch1 = joy1.getX();
            ch3 = joy2.getY();
            ch4 = joy2.getX();
            //Setting motor speeds
            frontLeft.set(frontLeftModifier * (-(ch3 + ch1 + ch4)));//port 1
            frontRight.set(frontRightModifier * ((ch3 - ch1 - ch4)));//port 2
            backLeft.set(backLeftModifier * (-(ch3 + ch1 - ch4)));//port 3
            backRight.set(backRightModifier * ((ch3 - ch1 + ch4)));//port 4
            
            //Changing the modifiers
            /*
            if(joy2.getRawButton()){
                changeValue(frontLeftModifier);
            }
            if(joy2.getRawButton()){
                changeValue(frontRightModifier);
            }
            if(joy2.getRawButton()){
                changeValue(backLeftModifier);
            }
            if(joy2.getRawButton()){
                changeValue(backRightModifier);
            }
            */
            //PRIMITIVE LOADING CODE
         /**if(joy1.getRawButton(4) && old4 == false)
            {
                loader.set(1.0);
                old4 = true;
            }
            else
            {
                if(joy1.getRawButton(4) == false)
                {
                    loader.set(0.0);
                }
            }
             */
            //PRIMITIVE SHOOTING CODE
            //ALL VALUES ARE UNTESTED AND ARE ROUGH GUESTIMATES
         /**if(joy1.getRawButton(2) && joy1.getTrigger() && button1.get())
            {
                shooter.set(1);
                Timer.delay(3);
                shooter.set(0);
                Timer.delay(.2);
                shooter.set(-.3);
                Timer.delay(5);
                shooter.set(0);
            }
            */
    }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
   
    }
    public double specialSquare(double setMe)
    {
        if(setMe > .05)
            {
                return setMe * setMe;
            }
            else
            {
                if(setMe < -.05)
                {
                    return setMe * setMe * -1;
                }
                else
                {
                    return 0;
                }
            }
    }
    /*
    public void changeValue(double value){
        if(joy2.getRawButton()){
            value-=.05;
        }
        if(joy2.getRawButton()){
            value+=.o5;
        }
    }
    */
}

