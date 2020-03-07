package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import static huglife.HugLifeUtils.randomEntry;

public class Clorus extends Creature {

    public Clorus(double e) {
        super("clorus");
        energy = e;
    }

    public Clorus() {
        this(1);
    }

    @Override
    public void move() {
        this.energy = this.energy - 0.03;
        if (energy < 0) {
            energy = 0;
        }
    }

    @Override
    public void attack(Creature c) {
        this.energy = this.energy + c.energy();
    }

    @Override
    public Creature replicate() {
        this.energy = this.energy * 0.5;
        return new Clorus(this.energy);
    }

    @Override
    public void stay() {
        this.energy = this.energy - 0.01;
        if (energy < 0) {
            energy = 0;
        }
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> anyPlips = new ArrayDeque<>();
        for (Direction d : neighbors.keySet()) {
            if (neighbors.get(d).name().equals("empty")) {
                emptyNeighbors.add(d);
            } else if (neighbors.get(d).name().equals("plip")) {
                anyPlips.add(d);
            }
        }
        if (emptyNeighbors.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        } else if (!anyPlips.isEmpty()) {
            return new Action(Action.ActionType.ATTACK, randomEntry(anyPlips));
        } else if (energy >= 1.0) {
            return new Action(Action.ActionType.REPLICATE, randomEntry(emptyNeighbors));
        } else {
            return new Action(Action.ActionType.MOVE, randomEntry(emptyNeighbors));
        }
    }

    @Override
    public Color color() {
        return color(34,0,231);
    }
}
