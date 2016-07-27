package ru.org.sarg.dungeon.game;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RockPaperTest {
    @Test
    public void compare() throws Exception {
        String[] wins = new String[]{"rs", "sp", "pr"};

        for (int i = 0; i < wins.length; i++) {
            String win = wins[i];

            FightActivity.RockPaper r1 = FightActivity.RockPaper.fromChar(win.substring(0, 1));
            FightActivity.RockPaper r2 = FightActivity.RockPaper.fromChar(win.substring(1, 2));

            assertTrue(win, r1.compare(r2) > 0);
            assertTrue(win, r2.compare(r1) < 0);
            assertTrue(win, r1.compare(r1) == 0);
        }
    }

}