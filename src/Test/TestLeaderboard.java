package Test;

import Game.Score.Leaderboard;
import Game.Score.Highscore;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A Leaderboard osztály funkcióinak tesztelésére szolgáló egységtesztek.
 * A tesztek biztosítják a ranglista helyes működését, beleértve:
 * - Pontok hozzáadását és sorrendbe állítását.
 * - A ranglista formázott listázását.
 * - A ranglista maximális méretének kezelését.
 * - A ranglista törlését.
 */
public class TestLeaderboard {

    Leaderboard leaderboard;

    /**
     * Minden teszt előtt inicializálja az üres ranglistát.
     */
    @BeforeEach
    public void setUp() {
        leaderboard = new Leaderboard();
    }

    /**
     * Teszteli a pontok hozzáadását a ranglistához.
     * Ellenőrzi:
     * - A pontok helyes sorrendjét.
     * - A ranglistán szereplő elemek számát.
     */
    @Test
    public void testAddScore() {
        leaderboard.addScore("Player1", "5.000 Ft");
        leaderboard.addScore("Player2", "10.000 Ft");
        leaderboard.addScore("Player3", "1.000 Ft");

        ArrayList<Highscore> scores = leaderboard.getLeaderboard();
        assertEquals(3, scores.size(), "Leaderboard should contain 3 entries.");
        assertEquals("Player2", scores.get(0).getName(), "Player2 should have the highest score.");
        assertEquals("Player3", scores.get(2).getName(), "Player3 should have the lowest score.");
    }

    /**
     * Teszteli a ranglista szöveges megjelenítését.
     * Ellenőrzi:
     * - A generált szöveg formátumát.
     * - A sorrendet és a nyeremények helyes megjelenítését.
     */
    @Test
    public void testListLeaderboard() {
        leaderboard.addScore("Player1", "5.000 Ft");
        leaderboard.addScore("Player2", "10.000 Ft");
        leaderboard.addScore("Player3", "1.000 Ft");

        String result = leaderboard.listLeaderboard();
        String expected =
                """
                        1. Player2 10.000 Ft
                        2. Player1 5.000 Ft
                        3. Player3 1.000 Ft""";
        assertEquals(expected, result, "The leaderboard string should match the expected format.");
    }

    /**
     * Teszteli a ranglista maximális méretének kezelését.
     * Ellenőrzi:
     * - Hogy a ranglista csak a legjobb 10 pontot tartalmazza.
     * - A pontszámok sorrendjét.
     */
    @Test
    public void testLeaderboardLimit() {
        for (int i = 1; i <= 15; i++) {
            leaderboard.addScore("Player" + i, (i * 1_000) + " Ft");
        }

        ArrayList<Highscore> scores = leaderboard.getLeaderboard();
        assertEquals(10, scores.size(), "Leaderboard should only contain the top 10 scores.");
        assertEquals("Player15", scores.get(0).getName(), "Player15 should have the highest score.");
        assertEquals("Player6", scores.get(9).getName(), "Player6 should have the lowest score in the top 10.");
    }

    /**
     * Teszteli a ranglista törlését.
     * Ellenőrzi:
     * - Hogy a ranglista üres lesz a clear() metódus hívása után.
     */
    @Test
    public void testClearLeaderboard() {
        leaderboard.addScore("Player1", "5.000 Ft");
        leaderboard.addScore("Player2", "10.000 Ft");

        leaderboard.clear();
        ArrayList<Highscore> scores = leaderboard.getLeaderboard();
        assertTrue(scores.isEmpty(), "Leaderboard should be empty after clearing.");
    }

    /**
     * Teszteli a pontszámok hozzáadását, ha két játékosnak azonos pontszáma van.
     * Ellenőrzi:
     * - Az elemek helyes sorrendjét.
     * - A döntetlenek helyes kezelését.
     */
    @Test
    public void testAddScoreWithTie() {
        leaderboard.addScore("Player1", "10.000 Ft");
        leaderboard.addScore("Player2", "10.000 Ft");

        ArrayList<Highscore> scores = leaderboard.getLeaderboard();
        assertEquals(2, scores.size(), "Leaderboard should contain 2 entries.");
        assertEquals("Player1", scores.getFirst().getName(), "Player1 should be listed first if scores are tied.");
    }
}

