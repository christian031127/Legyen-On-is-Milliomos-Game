package Test;

import Game.Game;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A Game osztály funkcióinak tesztelésére szolgáló egységtesztek.
 * A tesztosztály biztosítja, hogy a játék logikája megfelelően működjön,
 * beleértve az állapotkezelést, a körök váltását, a mentést és betöltést,
 * valamint a nyeremények helyes visszaadását.
 */
public class TestGame {

    Game game;

    /**
     * Minden teszt előtt inicializálja a játékot alaphelyzetbe állítva.
     *
     * @throws FileNotFoundException, ha a kérdések JSON fájlja nem található.
     */
    @BeforeEach
    public void setUp() throws FileNotFoundException {
        game = new Game();
        game.resetGameState();
    }

    /**
     * Teszteli a játék inicializálás utáni alapállapotát.
     * Ellenőrzi:
     * - A körök száma 1 legyen.
     * - Minden segítség elérhető legyen.
     * - Az időzítő 30 másodperc legyen.
     * - Egy kérdés legyen inicializálva.
     */
    @Test
    public void testInitialGameState() {
        assertEquals(1, game.getRound());
        assertFalse(game.isFiftyfifty());
        assertFalse(game.isNewquestion());
        assertFalse(game.isCrowdvote());
        assertEquals(30, game.getTimeLeft());
        assertNotNull(game.getQuestion());
    }

    /**
     * Teszteli az új játék indítását.
     * Ellenőrzi, hogy minden segítség elérhető legyen,
     * és az első kör helyesen legyen inicializálva.
     */
    @Test
    public void testBeginGame() {
        game.beginGame();
        assertFalse(game.isFiftyfifty());
        assertFalse(game.isNewquestion());
        assertFalse(game.isCrowdvote());
        assertEquals(1, game.getRound());
        assertEquals(1, game.getQuestion().getDifficulty());
    }

    /**
     * Teszteli a következő kör indítását.
     * Ellenőrzi:
     * - A körök száma növekedjen.
     * - Az időzítő visszaálljon.
     * - Egy új kérdés legyen inicializálva a megfelelő nehézségi szinten.
     */
    @Test
    public void testNextRound() {
        game.beginGame();
        game.newRound();
        assertEquals(2, game.getRound());
        assertEquals(2, game.getQuestion().getDifficulty());
        assertEquals(30, game.getTimeLeft());
        assertNotNull(game.getQuestion());
    }

    /**
     * Teszteli a játék véget érő állapotát.
     * 12 kör után a játék véget ér, és nem maradnak kérdések.
     */
    @Test
    public void testGameOverCondition() {
        for (int i = 1; i <= 12; i++) {
            game.newRound();
        }

        assertTrue(game.isGameOver());
        assertNull(game.getQuestion());
    }

    /**
     * Teszteli a játékállapot mentését és betöltését.
     * Ellenőrzi, hogy a játékállapot (körök, segítségek, időzítő, kérdés) helyesen
     * töltődik vissza mentés után.
     */
    @Test
    public void testSaveAndLoadGameState() throws FileNotFoundException {
        game.newRound();

        game.setFiftyfifty(true);
        game.setNewquestion(true);
        game.setCrowdvote(false);
        game.setTimeLeft(20);

        game.saveGameState();

        Game loadedGame = new Game();
        loadedGame.loadGameState();

        assertEquals(2, loadedGame.getRound());
        assertTrue(loadedGame.isFiftyfifty());
        assertTrue(loadedGame.isNewquestion());
        assertFalse(loadedGame.isCrowdvote());
        assertEquals(20, loadedGame.getTimeLeft());
        assertNotNull(loadedGame.getQuestion());
    }

    /**
     * Teszteli a nyeremények helyes visszaadását.
     * Ellenőrzi az első és az utolsó kör nyereményét.
     */
    @Test
    public void testGetPrize() {
        String prize = game.getPrize(1);
        assertEquals("1.000 Ft", prize);

        prize = game.getPrize(12);
        assertEquals("10.000.000 Ft", prize);
    }
}
