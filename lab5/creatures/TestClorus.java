package creatures;
import huglife.*;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TestClorus {

    @Test
    public void testbasic() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.move();
        assertEquals(1.94, c.energy(), 0.01);
        c.stay();
        assertEquals(1.93, c.energy(), 0.01);
        c.stay();
        assertEquals(1.92, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);
        Creature Rep = c.replicate();
        assertEquals(new Clorus(1).energy(),c.energy(),0.01);
        assertEquals(new Clorus(1).energy(),Rep.energy(),0.01);
    }


    @Test
    public void testChoose() {

        // Attack
        Clorus c = new Clorus(2);
        Plip plipVictim = new Plip(1.2);
        HashMap<Direction, Occupant> sandbox = new HashMap<Direction, Occupant>();
        sandbox.put(Direction.TOP, new Impassible());
        sandbox.put(Direction.BOTTOM, new Impassible());
        sandbox.put(Direction.LEFT, new Empty());
        sandbox.put(Direction.RIGHT, plipVictim);

        Action actualAttack = c.chooseAction(sandbox);
        Action expectedAttack  = new Action(Action.ActionType.ATTACK,Direction.RIGHT);
        assertEquals(expectedAttack, actualAttack);

        // STAY
        sandbox.clear();
        sandbox.put(Direction.TOP, plipVictim);
        sandbox.put(Direction.BOTTOM, plipVictim);
        sandbox.put(Direction.LEFT, plipVictim);
        sandbox.put(Direction.RIGHT, plipVictim);

        Action actualStay = c.chooseAction(sandbox);
        Action expectedStay = new Action(Action.ActionType.STAY);
        assertEquals(expectedStay, actualStay);

        //REPLICATE
        sandbox.clear();
        sandbox.put(Direction.TOP, new Impassible());
        sandbox.put(Direction.BOTTOM, new Empty());
        sandbox.put(Direction.LEFT, new Impassible());
        sandbox.put(Direction.RIGHT, new Impassible());

        Action actualReplicate = c.chooseAction(sandbox);
        Action expectedReplicate = new Action(Action.ActionType.REPLICATE, Direction.BOTTOM);
        assertEquals(expectedReplicate, actualReplicate);

        // MOVE
        Clorus c2 = new Clorus(0.9);
        sandbox.clear();
        sandbox.put(Direction.TOP, new Impassible());
        sandbox.put(Direction.BOTTOM, new Empty());
        sandbox.put(Direction.LEFT, new Impassible());
        sandbox.put(Direction.RIGHT, new Impassible());

        Action actualMove = c2.chooseAction(sandbox);
        Action expectedMove = new Action(Action.ActionType.MOVE, Direction.BOTTOM);
        assertEquals(expectedMove, actualMove);


    }

}
