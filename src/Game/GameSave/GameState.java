package Game.GameSave;

import Game.Question.Question;

import java.io.Serial;
import java.io.Serializable;

/**
 * A GameState osztály a játék aktuális állapotát reprezentálja, amely lehetővé teszi
 * a játék mentését és visszatöltését. Az osztály sorosítható.
 */
public class GameState implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // Egyedi azonosító a sorosításhoz.

    private final int round;                // A játék aktuális köre.
    private final Question currentQuestion; // A jelenleg megjelenített kérdés.
    private final boolean fiftyfiftyUsed;   // A "50:50" segítség használatának állapota.
    private final boolean newQuestionUsed;  // Az "új kérdés" segítség használatának állapota.
    private final boolean crowdVoteUsed;    // A "közönségszavazás" segítség használatának állapota.
    private final int timeLeft;             // A játék hátralévő ideje másodpercben.

    /**
     * Konstruktor, amely inicializálja a játék állapotát.
     *
     * @param round             a játék aktuális köre
     * @param currentQuestion   a jelenlegi kérdés
     * @param fiftyfiftyUsed    a "50:50" segítség használatának állapota
     * @param newQuestionUsed   az "új kérdés" segítség használatának állapota
     * @param crowdVoteUsed     a "közönségszavazás" segítség használatának állapota
     * @param timeLeft          a játék hátralévő ideje másodpercben
     */
    public GameState(int round, Question currentQuestion, boolean fiftyfiftyUsed, boolean newQuestionUsed, boolean crowdVoteUsed, int timeLeft) {
        this.round = round;
        this.currentQuestion = currentQuestion;
        this.fiftyfiftyUsed = fiftyfiftyUsed;
        this.newQuestionUsed = newQuestionUsed;
        this.crowdVoteUsed = crowdVoteUsed;
        this.timeLeft = timeLeft;
    }

    // Getterek az állapotadatokhoz

    /**
     * Visszaadja a játék aktuális körét.
     *
     * @return a játék aktuális köre
     */
    public int getRound() {
        return round;
    }

    /**
     * Visszaadja a jelenlegi kérdést.
     *
     * @return a jelenlegi kérdés objektuma
     */
    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * Visszaadja, hogy a "50:50" segítséget használták-e már.
     *
     * @return true, ha használták, különben false
     */
    public boolean isFiftyfiftyUsed() {
        return fiftyfiftyUsed;
    }

    /**
     * Visszaadja, hogy az "új kérdés" segítséget használták-e már.
     *
     * @return true, ha használták, különben false
     */
    public boolean isNewQuestionUsed() {
        return newQuestionUsed;
    }

    /**
     * Visszaadja, hogy a "közönségszavazás" segítséget használták-e már.
     *
     * @return true, ha használták, különben false
     */
    public boolean isCrowdVoteUsed() {
        return crowdVoteUsed;
    }

    /**
     * Visszaadja a játék hátralévő idejét másodpercben.
     *
     * @return a játék hátralévő ideje másodpercben
     */
    public int getTimeLeft() {
        return timeLeft;
    }
}
