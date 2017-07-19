package assignment;

import org.junit.Test;

import java.util.List;
import java.util.Random;

/**
 * Created by smithanagar on 9/16/16.
 */
public class TestCritter implements Critter {

    private static final int NONE = 0;
    private static final int HOP = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    private static final int EAT = 4;
    private static final int INFECT = 5;
    private int x;
    private int y;
    private boolean wall;
    private int heading = 0;
    private CritterSpecies species;
    private TestCritter[][] world;
    private int codeLine = 1;
    private int[] reg = new int[10];
    private int pendingAction = 0;
    private int pendingCodeLine = 1;
    private int width = 60;
    private int height = 35;
    public boolean wrapX = false;
    public boolean wrapY = false;

    public TestCritter(CritterSpecies species, TestCritter[][] world, boolean wall, int x, int y) {
        this.species = species;
        this.world = world;
        this.wall = wall;
        this.x = x;
        this.y = y;
        world[x][y] = this;
    }

    public void setHeading(int h) {
        heading = h;
    }

    public int getHeading() {
        return heading;
    }

    public CritterSpecies getSpecies() {
        return species;
    }

    public List getCode() {
        return this.species.getCode();
    }

    public int getNextCodeLine() {
        return this.codeLine;
    }

    public void setNextCodeLine(int var1) {
        this.codeLine = var1;
    }

    public int getReg(int var1) {
        return var1 >= 1 && var1 <= 10?this.reg[var1 - 1]:0;
    }

    public void setReg(int var1, int var2) {
        if(var1 >= 1 && var1 <= 10) {
            this.reg[var1 - 1] = var2;
        }
    }

    public void hop() {
        this.pendingAction = 1;
        world[x][y] = null;
        world[this.getCellX(0)][this.getCellY(0)] = this;
        x = this.getCellX(0);
        y = this.getCellY(0);
    }

    public void left() {
        this.pendingAction = 2;
        this.heading -= 45;
    }

    public void right() {
        this.pendingAction = 3;
        this.heading += 45;
    }

    public void eat() {
        this.pendingAction = 4;
        world[getCellX(0)][getCellY(0)] = null;
    }

    public void infect() {
        this.pendingAction = 5;
        this.pendingCodeLine = 1;
        world[getCellX(0)][getCellY(0)].species = this.getSpecies();
        this.setNextCodeLine(1);
    }

    public void infect(int var1) {
        this.pendingAction = 5;
        this.pendingCodeLine = var1;
        world[getCellX(0)][getCellY(0)].species = this.getSpecies();
        this.setNextCodeLine(var1);
    }

    public boolean ifRandom() {
        Random rand = new Random();
        return rand.nextBoolean();
    }

    public int getCellContent(int var1) {
        if (var1 >= 0 && var1 <= 315 && var1 % 45 == 0) {
            int var2 = this.getCellX(var1);
            int var3 = this.getCellY(var1);

            if (world[var2][var3] == null) {
                return Critter.EMPTY;
            }
            if (world[var2][var3].isWall()) {
                return Critter.WALL;
            }
            if (world[var2][var3].getSpecies().getName().equals(this.species)) {
                return Critter.ALLY;
            }
            return Critter.ENEMY;
        }
        return -1;
    }

    public boolean isWall() {
        return wall;
    }

    public int getOffAngle(int var1) {
        if(var1 >= 0 && var1 <= 315 && var1 % 45 == 0) {
            int var2 = this.getCellX(var1);
            int var3 = this.getCellY(var1);
            if (world[var2][var3] == null) {
                return -1;
            }
            return ((world[var2][var3].heading - this.heading + 360) % 360);
        }
        return -1;
    }

    private int getCellX(int var1) {
        switch((Math.abs(this.heading) + var1) % 360) {
            case 0:
            case 180:
            default:
                return this.x;
            case 45:
            case 90:
            case 135:
                return this.x + 1;
            case 225:
            case 270:
            case 315:
                return this.x - 1;
        }
    }

    private int getCellY(int var1) {
        switch((this.heading + var1) % 360) {
            case 0:
            case 45:
            case 315:
            default:
                return this.y - 1;
            case 90:
            case 270:
                return this.y;
            case 135:
            case 180:
            case 225:
                return this.y + 1;
        }
    }
}
