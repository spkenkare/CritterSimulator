package assignment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by smithanagar on 9/16/16.
 */
public class TestInterpreter {

    @Test
    public void testLoadSpeciesName() {
        Interpreter ip = new Interpreter();
        String name = "Flytrap";
        List<String> code = Arrays.asList("Flytrap", "ifenemy 0 4", "left", "go 1", "infect", "go" +
                " 1");
        CritterSpecies cAct = ip.loadSpecies
                ("/Users/Saarila/workspace/prog3/species/FlyTrap.cri");
        assertEquals("load species",  name, cAct.getName());
    }

    @Test
    public void testLoadSpeciesCode() {
        Interpreter ip = new Interpreter();
        String name = "Flytrap";
        List<String> code = Arrays.asList("Flytrap", "ifenemy 0 4", "left", "go 1", "infect", "go" +
                " 1");
        CritterSpecies cAct = ip.loadSpecies
                ("/Users/Saarila/workspace/prog3/species/FlyTrap.cri");
        assertEquals("load species",  code.toString(), cAct.getCode().toString());
    }


    @Test
    public void testInvalidFile() {
        Interpreter ip = new Interpreter();
        ip.loadSpecies("/Users/smithanagar/IdeaProjects/prog3.1/speceies/FlyTrap.cri");
    }

    @Test
    public void testEmptyFile() {
        Interpreter ip = new Interpreter();
        ip.loadSpecies("/Users/smithanagar/IdeaProjects/prog3.1/species/Test.cri");
    }

    @Test
    public void testbadCommands() {

        Interpreter ip = new Interpreter();
        ip.loadSpecies("/Users/smithanagar/IdeaProjects/prog3.1/species/Test.cri");
        /*
        invalid commands
        invalid line numbers
        invalid registers (r11, p1)
        parameters (too many, too few, wrong type)
         */
    }

    @Test
    public void testExtraSpaces() {
        Interpreter ip = new Interpreter();
        ip.loadSpecies("/Users/smithanagar/IdeaProjects/prog3.1/species/Test.cri");
        //we account for and handle this
    }

    @Test
    public void testNoName() {

        Interpreter ip = new Interpreter();
        ip.loadSpecies("/Users/smithanagar/IdeaProjects/prog3.1/species/Test.cri");
        //considered a name if charAt is uppercase
    }

    @Test
    public void testNoCode() {
        Interpreter ip = new Interpreter();
        ip.loadSpecies("/Users/smithanagar/IdeaProjects/prog3.1/species/Test.cri");

    }

    //cant test
    @Test
    public void testExecuteCritter() {

    }

    //cant test
    @Test
    public void testJump() {
//        Interpreter ip = new Interpreter();
//        String name = "Flytrap";
//        List<String> code = Arrays.asList("Flytrap", "ifenemy 0 4", "left", "go 1", "infect", "go" +
//                " 1");
//        CritterSpecies cAct = ip.loadSpecies
//                ("/Users/smithanagar/IdeaProjects/prog3.1/species/FlyTrap.cri");
//        String line = "-4";
//        TestCritter c = new TestCritter();
//
//        assertEquals("should return -1 for invalid jump", -1, ip.jump(line, c));
    }
}
