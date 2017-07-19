package assignment;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by smithanagar on 9/16/16.
 */
public class TestHarness {

    //run(commands)
    public static void main(String[] args) {
        TestCritter[][] world = new TestCritter[3][3];
        Interpreter ip = new Interpreter();
        for (int i = 0; i < world.length; i ++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = null;
            }
        }

        ArrayList<TestCritter> critters = new ArrayList<TestCritter>();
        critters.add(new TestCritter(ip.loadSpecies
                ("/Users/Saarila/workspace/prog3/species/Hop2.cri"), world, false, 1, 2));
        critters.add(new TestCritter(ip.loadSpecies
                ("/Users/Saarila/workspace/prog3/species/Hop3.cri"), world, false, 1, 2));
        critters.add(new TestCritter(ip.loadSpecies
                ("/Users/Saarila/workspace/prog3/species/Hop4.cri"), world, false, 1, 2));
        printWorld(world);
        
        //testing for add
        critters.get(0).setReg(1, 5);
        critters.get(0).setReg(2, -12);
        System.out.println("Reg 1: " + critters.get(0).getReg(1));
        System.out.println("Reg 2: " + critters.get(0).getReg(2));
        
        //testing for subtract
        critters.get(0).setReg(3, 6);
        critters.get(0).setReg(4, -1);
        System.out.println("Reg 3: " + critters.get(0).getReg(3));
        System.out.println("Reg 4: " + critters.get(0).getReg(4));
        
        //testing for inc
        critters.get(0).setReg(5, -3);
        System.out.println("Reg 5: " + critters.get(0).getReg(5));
        
        //testing for dec
        critters.get(0).setReg(6, -3);
        System.out.println("Reg 6: " + critters.get(0).getReg(6));
        
        critters.get(1).setReg(7, 1);
        critters.get(1).setReg(8, -6);
        
        critters.get(2).setReg(9, 1);
        critters.get(2).setReg(10, 1);

        
        while(true) {
            Scanner s = new Scanner(System.in);
            if (s.hasNextLine()) {
                for (TestCritter c : critters) {
                    if (c.isWall()) {
                        continue;
                    }
                    ip.executeCritter(c);
                    System.out.println("Reg 1 after adding: " + critters.get(0).getReg(1));
                    System.out.println("Reg 3 after subtracting: " + critters.get(0).getReg(3));
                    System.out.println("Reg 5 after incrementing: " + critters.get(0).getReg(5));
                    System.out.println("Reg 6 after decrementing: " + critters.get(0).getReg(6));
                    
                    System.out.println(c.getReg(7));
                    System.out.println(c.getReg(8));
                    System.out.println(c.getNextCodeLine()); //-1 if the ifgt was true, should be 15
                    
                    System.out.println(c.getReg(9));
                    System.out.println(c.getReg(10));
                    System.out.println(c.getNextCodeLine()); //-1 if the ifeq was true, should be 99
                    printWorld(world);
                    
                }
            }
        }
        
        
        
       

    }

    public static void printWorld(TestCritter[][] world) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (world[i][j] == null) {
                    System.out.print("null ");
                }
                else if (world[i][j].isWall()) {
                    System.out.print("wall ");
                }
                else {
                    System.out.print("crit ");
                }
            }
            System.out.print("\n");
        }
    }
}
