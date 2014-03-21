/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.amhs.robotdrive;

/**
 *
 * @author Developer
 */
import edu.wpi.first.wpilibj.*;
public class TEST {
    public static void main (String [] args)
    {
        Timer t = new Timer();
        t.start();
        while(t.get() < 20)
        {
            System.out.print(t.get());
        }
        t.stop();
    }
}
