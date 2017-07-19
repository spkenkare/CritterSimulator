/*

CS314H - Critter Interpreter

Provided here is skeleton code for your interpreter.
Remove this comment when you have finished implementation.

*/
package assignment;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class Interpreter implements CritterInterpreter {

    private boolean jumped;

    //Check if the line numbers and registers are valid numbers to jump to, and check that the
    // register is r# where # is some number between 1 and 10. If they are not, return false.
    public boolean checkJump(String codeLine) {
        //For relative jumps, check the second position in the string (after + or -).
        if (((codeLine.charAt(0) == '+') || (codeLine.charAt(0) == '-')) && (codeLine.length() >
                1)) {
            try {
                Integer.parseInt(codeLine.substring(1));
            }
            catch (Exception e) {
                System.err.println("Invalid line number.");
                return false;
            }
        }
        else if ((codeLine.charAt(0) == 'r') && (codeLine.length() > 1)) {
            try {
                //Cannot check to make sure the register has been set and is not 0. This is a
                // runtime issue and is handled by checking if getCodeLine() is out of bounds at
                // the beginning of executeCritter().
                if((Integer.parseInt(codeLine.substring(1)) > 10) || (Integer.parseInt(codeLine
                        .substring(1)) < 1)) {
                    System.err.println("Invalid register number");
                    return false;
                }
            }
            catch (Exception e) {
                System.err.println("Invalid register number.");
                return false;
            }
        }
        //This will be the case for an absolute line number jump.
        else {
            try {
                Integer.parseInt(codeLine);
            } catch (Exception e) {
                System.err.println("Invalid line number.");
                return false;
            }
        }
        //If we made it to this point, the line numbers and/or register numbers are valid.
        return true;
    }

    private void jump(String numLine, Critter c) {
        if ((numLine.charAt(0) == '+') || (numLine.charAt(0) == '-')) {
            c.setNextCodeLine(c.getNextCodeLine() + Integer.parseInt(numLine));
        }
        else if (numLine.charAt(0) == 'r') {
            c.setNextCodeLine(c.getReg(Integer.parseInt(numLine.substring(1))));
        }
        else {
            c.setNextCodeLine(Integer.parseInt(numLine));
        }
        //Set jumped to true so that we know not to increment getNextCodeLine for this iteration.
        jumped = true;
    }

    public void executeCritter(Critter c) {
        List<String[]> code = c.getCode();
        //Go through the behavior code until the critter calls an action method.
        boolean actionDone = false;
        while (!actionDone) {
            //If the code line we are to act on is out of bounds, do nothing for the rest of the
            // turns by just returning without having acted.
            if ((c.getNextCodeLine() >= code.size()) || (c.getNextCodeLine() < 1)) {
                return;
            }
            jumped = false;
            String[] codeLine = code.get(c.getNextCodeLine());

            //Check all the possible commands and execute them.
            if (codeLine[0].equals("go")) {
                jump(codeLine[1], c);
            }
            else if (codeLine[0].equals("ifrandom")) {
                Random rand = new Random();
                //nextInt(2) gives us either 0 or 1, effectively a 50/50 random chance.
                int seed = rand.nextInt(2);
                if (seed == 0) {
                    jump(codeLine[1], c);
                }
            }
            else if (codeLine[0].equals("ifempty")) {
                if (c.getCellContent(Integer.parseInt(codeLine[1])) == Critter.EMPTY) {
                    jump(codeLine[2], c);
                }
            }
            else if (codeLine[0].equals("ifally")) {
                if (c.getCellContent(Integer.parseInt(codeLine[1])) == Critter.ALLY) {
                    jump(codeLine[2], c);
                }
            }
            else if (codeLine[0].equals("ifwall")) {
                if (c.getCellContent(Integer.parseInt(codeLine[1])) == Critter.WALL) {
                    jump(codeLine[2], c);
                }
            }
            else if (codeLine[0].equals("ifenemy")) {
                if (c.getCellContent(Integer.parseInt(codeLine[1])) == Critter.ENEMY) {
                    jump(codeLine[2], c);
                }
            }
            else if (codeLine[0].equals("ifangle")) {
                //If the difference in headings of the two critters match the second argument, jump.
                if (c.getOffAngle(Integer.parseInt(codeLine[1])) == Integer.parseInt(codeLine[2])) {
                    jump(codeLine[3], c);
                }
            }
            else if(codeLine[0].equals("write")) {
                c.setReg(Integer.parseInt(codeLine[1].substring(1)), Integer.parseInt(codeLine[2]));
            }
            else if(codeLine[0].equals("add")) {
                int sum = c.getReg(Integer.parseInt(codeLine[1].substring(1))) + c.getReg(Integer
                        .parseInt(codeLine[2].substring(1)));
                c.setReg(Integer.parseInt(codeLine[1].substring(1)), sum);
            }
            else if(codeLine[0].equals("sub")) {
                int diff = c.getReg(Integer.parseInt(codeLine[1].substring(1))) - c.getReg(Integer
                        .parseInt(codeLine[2].substring(1)));
                c.setReg(Integer.parseInt(codeLine[1].substring(1)), diff);
            }
            else if(codeLine[0].equals("inc")) {
                c.setReg(Integer.parseInt(codeLine[1].substring(1)), (c.getReg(Integer.parseInt
                        (codeLine[1].substring(1))) + 1));
            }
            else if(codeLine[0].equals("dec")) {
                c.setReg(Integer.parseInt(codeLine[1].substring(1)), (c.getReg(Integer.parseInt
                        (codeLine[1].substring(1))) - 1));
            }
            else if (codeLine[0].equals("iflt")) {
                int r1Val = c.getReg(Integer.parseInt(codeLine[1].substring(1)));
                int r2Val = c.getReg(Integer.parseInt(codeLine[2].substring(1)));
                if (r1Val < r2Val) {
                    jump(codeLine[3], c);
                }
            }
            else if (codeLine[0].equals("ifeq")) {
                int r1Val = c.getReg(Integer.parseInt(codeLine[1].substring(1)));
                int r2Val = c.getReg(Integer.parseInt(codeLine[2].substring(1)));
                if (r1Val == r2Val) {
                    jump(codeLine[3], c);
                }
            }
            else if (codeLine[0].equals("ifgt")) {
                int r1Val = c.getReg(Integer.parseInt(codeLine[1].substring(1)));
                int r2Val = c.getReg(Integer.parseInt(codeLine[2].substring(1)));
                if (r1Val > r2Val) {
                    jump(codeLine[3], c);
                }
            }
            else if ((codeLine[0].equals("infect")) && (codeLine.length == 2)) {
                c.infect(Integer.parseInt(codeLine[1]));
            }
            else {
                //Required to have this try-catch block, but because we've checked for this
                // already, the method should always exist and the code within the catch block
                // should never be executed.
                try {
                    Method method = Critter.class.getDeclaredMethod(codeLine[0]);
                    method.invoke(c);
                    actionDone = true;
                }
                catch (Exception e) {
                    System.err.println("The code command " + codeLine[0] + " does not exist.");
                }
            }
            //If we haven't jumped to a line number yet, increment the line number we will act on
            // for the next iteration.
            if (!jumped) {
                c.setNextCodeLine(c.getNextCodeLine() + 1);
            }
        }
    }

    public CritterSpecies loadSpecies(String filename) {
        String name = new String();
        //Each position in the list points to a String array consisting of the commands and
        // arguments without spaces.
        List<String[]> list = new ArrayList<String[]>();
        FileReader fr;
        try {
            fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            //Return null if file can't be read or no name or code.
            if (((name = br.readLine()) == null) || (name.equals(""))) {
                if (name == null) {
                    System.err.println("File cannot be empty.");
                    return null;
                }
                System.err.println("First line of file must be name of critter.");
                return null;
            }

            //We define a name as a string where the first letter is capitalized to distinguish it.
            if (!((name.charAt(0) >= 65) && (name.charAt(0) <= 90))) {
                System.err.println("First line of file must be name of critter and it must start " +
                        "with a capital letter.");
                return null;
            }

            //Make the first entry in code (list.get(0)) a filler Index 0 so that the first line of
            // code starts at index 1 as is indicated by the API.
            list.add(new String[]{"Index 0"});
            String readLine;
            //Check validity of commands (we account for extra spaces). If anything is invalid,
            // return null.
            while (((readLine = br.readLine()) != null) && !(readLine.equals(""))) {
                //Split each command by spaces and add commands and their arguments to a String
                // array which we then strategically check.
                String[] temp = readLine.split(" ");;
                ArrayList<String> tempToCode = new ArrayList<String>();
                for (int idx = 0; idx < temp.length; idx++) {
                    if (!(temp[idx].equals(""))) {
                        tempToCode.add(temp[idx]);
                    }
                }
                String[] code = new String[tempToCode.size()];
                tempToCode.toArray(code);
                //Check each possible command and make sure there are the right number and type
                // of parameters. If any part of the command is invalid, return null.
                if (code[0].equals("go")) {
                    if (code.length == 2) {
                        if (!(checkJump(code[1]))) {
                            return null;
                        }
                    }
                    else {
                        System.err.println("1 parameter needed for go.");
                        return null;
                    }

                }
                else if (code[0].equals("ifrandom")) {
                    if (code.length == 2) {
                        if (!(checkJump(code[1]))) {
                            return null;
                        }
                    }
                    else {
                        System.err.println("1 parameter needed for ifrandom.");
                        return null;
                    }
                }
                else if (code[0].equals("ifempty")) {
                    if (code.length == 3) {
                        try {
                            Integer.parseInt(code[1]);
                            if (!(checkJump(code[2]))) {
                                return null;
                            }
                        } catch (Exception e) {
                            System.err.println("Invalid parameter for ifempty.");
                            return null;
                        }
                    }
                    else {
                        System.err.println("2 parameters needed for ifempty.");
                        return null;
                    }
                }
                else if (code[0].equals("ifally")) {
                    if (code.length == 3) {
                        try {
                            Integer.parseInt(code[1]);
                            if (!(checkJump(code[2]))) {
                                return null;
                            }
                        }
                        catch (Exception e) {
                            System.err.println("Invalid parameter for ifally.");
                            return null;
                        }
                    }
                    else {
                        System.err.println("2 parameters needed for ifally.");
                        return null;
                    }
                }
                else if (code[0].equals("ifwall")) {
                    if (code.length == 3) {
                        try {
                            Integer.parseInt(code[1]);
                            if (!(checkJump(code[2]))) {
                                return null;
                            }
                        } catch (Exception e) {
                            System.err.println("Invalid parameter for ifwall.");
                            return null;
                        }
                    }
                    else {
                        System.err.println("2 parameters needed for ifwall.");
                        return null;
                    }
                }
                else if (code[0].equals("ifenemy")) {
                    if (code.length == 3) {
                        try {
                            Integer.parseInt(code[1]);
                            if (!(checkJump(code[2]))) {
                                return null;
                            }
                        } catch (Exception e) {
                            System.err.println("Invalid parameter for ifenemy.");
                            return null;
                        }
                    }
                    else {
                        System.err.println("2 parameters needed for ifenemy.");
                        return null;
                    }
                }
                else if (code[0].equals("ifangle")) {
                    if (code.length == 4) {
                        try {
                            Integer.parseInt(code[1]);
                            Integer.parseInt(code[2]);
                            if (!(checkJump(code[3]))) {
                                return null;
                            }
                        } catch (Exception e) {
                            System.err.println("Invalid parameter for ifangle.");
                            return null;
                        }
                    }
                    else {
                        System.err.println("3 parameters needed for ifangle.");
                        return null;
                    }
                }
                else if(code[0].equals("write")) {
                    if (code.length == 3) {
                        try {
                            Integer.parseInt(code[2]);
                            if (!(checkJump(code[1]))) {
                                return null;
                            }
                        }
                        catch (Exception e) {
                            System.err.println("Value to write must be a number.");
                            return null;
                        }
                    }
                    else {
                        System.err.println("2 parameters needed for write.");
                        return null;
                    }
                }
                else if(code[0].equals("add")) {
                    if (code.length == 3) {
                        if (!(checkJump(code[1])) || !(checkJump(code[2]))) {
                            return null;
                        }
                    }
                    else {
                        System.err.println("2 parameters needed for add.");
                        return null;
                    }
                }
                else if(code[0].equals("sub")) {
                    if (code.length == 3) {
                        if (!(checkJump(code[1])) || !(checkJump(code[2]))) {
                            return null;
                        }
                    }
                    else {
                        System.err.println("2 parameters needed for sub.");
                        return null;
                    }
                }
                else if(code[0].equals("inc")) {
                    if (code.length == 2) {
                        if (!(checkJump(code[1]))) {
                            return null;
                        }
                    }
                    else {
                        System.err.println("1 parameter needed for inc.");
                        return null;
                    }
                }
                else if(code[0].equals("dec")) {
                    if (code.length == 2) {
                        if (!(checkJump(code[1]))) {
                            return null;
                        }
                    }
                    else {
                        System.err.println("1 parameter needed for inc.");
                        return null;
                    }
                }
                else if(code[0].equals("iflt")) {
                    if (code.length == 4) {
                        if (!(checkJump(code[1])) || !(checkJump(code[2])) || !(checkJump
                                (code[3]))) {
                            return null;
                        }
                    }
                    else {
                        System.err.println("3 parameters needed for iflt.");
                        return null;
                    }
                }
                else if(code[0].equals("ifeq")) {
                    if (code.length == 4) {
                        if (!(checkJump(code[1])) || !(checkJump(code[2])) || !(checkJump
                                (code[3]))) {
                            return null;
                        }
                    }
                    else {
                        System.err.println("3 parameters needed for ifeq.");
                        return null;
                    }
                }
                else if(code[0].equals("ifgt")) {
                    if (code.length == 4) {
                        if (!(checkJump(code[1])) || !(checkJump(code[2])) || !(checkJump
                                (code[3]))) {
                            return null;
                        }
                    }
                    else {
                        System.err.println("3 parameters needed for ifgt.");
                        return null;
                    }
                }
                else if ((code[0].equals("infect")) && (code.length == 2)) {
                    if (!(checkJump(code[1]))) {
                        return null;
                    }
                }
                else if (!((code[0].equals("hop")) || (code[0].equals("infect")) || (code[0].equals
                        ("left")) || (code[0].equals("right")) || (code[0].equals("eat"))) ||
                        (code.length > 1)) {
                    System.err.println("The code command " + code[0] + " does not exist.");
                    return null;
                }
                //If we got to this point, the command must be valid, so add it to the list of
                // behavior code.
                list.add(code);
            }
            //If there was no behavior code, the size of the list will only be 1 and consist of
            // the filler "Index 0".
            if (list.size() == 1) {
                System.err.println("The critter must have code to execute directly after the " +
                        "name of the critter and there must be an empty line after the end of " +
                        "the code.");
                return null;
            }
            fr.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("The species input file could not be found.");
            return null;
        }
        catch (IOException e) {
            System.err.println("The species input file could not be read.");
            return null;
        }
        //If we got to this point, we have successfully read the name and list of code, so return
        // a CritterSpecies.
        CritterSpecies critterSpecies = new CritterSpecies(name, list);
        return critterSpecies;
    }
}
