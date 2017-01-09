package com.dalonedrow.rpg.base.constants;

import com.dalonedrow.engine.systems.base.Diceroller;

/**
 * @author drau
 */
public enum Dice {
    /** 1D10. */
    ONE_D10(1, Die.D10),
    /** 1D2. */
    ONE_D2(1, Die.D2),
    /** 1D3. */
    ONE_D3(1, Die.D3),
    /** 1D4. */
    ONE_D4(1, Die.D4),
    /** 1D6. */
    ONE_D6(1, Die.D6),
    /** 1D8. */
    ONE_D8(1, Die.D8),
    /** 3D6. */
    THREE_D6(3, Die.D6),
    /** 2D4. */
    TWO_D4(2, Die.D4),
    /** 2D6. */
    TWO_D6(2, Die.D6);
    /** the die rolled. */
    private Die die;
    /** the number of times the die is rolled. */
    private int num;
    /**
     * Creates a new instance of {@link Dice}.
     * @param n the number of times the die is rolled
     * @param d the die rolled
     */
    private Dice(final int n, final Die d) {
        this.num = n;
        this.die = d;
    }
    /**
     * Gets the index of the Dice, so it can be referenced later when rolled.
     * @return <code>int</code>
     */
    public int index() {
        Dice[] list = Dice.values();
        int i = list.length - 1;
        for (; i >= 0; i--) {
            if (list[i].equals(this)) {
                break;
            }
        }
        return i;
    }
    /**
     * Rolls the dice.
     * @return <code>int</code>
     */
    public int roll() {
        int val = 0;
        if (num > 1) {
            val = Diceroller.getInstance().rollXdY(num, die.getFaces());
        } else {
            val = Diceroller.getInstance().rolldX(die.getFaces());
        }
        return val;
    }
}
