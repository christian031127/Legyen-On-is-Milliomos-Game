package Game.Question;

import java.io.Serial;
import java.io.Serializable;

/**
 * Ez az osztály egy kérdést reprezentál, amely tartalmazza a kérdés szövegét,
 * a válaszlehetőségeket, a helyes választ és a kérdés nehézségi szintjét.
 */
public class Question implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // Verzió azonosító a szerializációhoz.

    private final int difficulty; // A kérdés nehézségi szintje.
    private final String question; // A kérdés szövege.
    private final String a; // 'A' válaszlehetőség.
    private final String b; // 'B' válaszlehetőség.
    private final String c; // 'C' válaszlehetőség.
    private final String d; // 'D' válaszlehetőség.
    private final char answer; // A helyes válasz betűjele (pl. 'A').

    /**
     * Konstruktor, amely inicializálja a kérdést és a hozzá tartozó adatokat.
     *
     * @param difficulty a kérdés nehézségi szintje
     * @param question   a kérdés szövege
     * @param a          az 'A' válaszlehetőség
     * @param b          a 'B' válaszlehetőség
     * @param c          a 'C' válaszlehetőség
     * @param d          a 'D' válaszlehetőség
     * @param answer     a helyes válasz betűjele
     */
    public Question(int difficulty, String question, String a, String b, String c, String d, char answer) {
        this.difficulty = difficulty;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.answer = answer;
    }

    /**
     * Visszaadja a kérdés nehézségi szintjét.
     *
     * @return a kérdés nehézségi szintje
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Visszaadja a kérdés szövegét.
     *
     * @return a kérdés szövege
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Visszaadja az 'A' válaszlehetőséget.
     *
     * @return az 'A' válaszlehetőség
     */
    public String getA() {
        return a;
    }

    /**
     * Visszaadja a 'B' válaszlehetőséget.
     *
     * @return a 'B' válaszlehetőség
     */
    public String getB() {
        return b;
    }

    /**
     * Visszaadja a 'C' válaszlehetőséget.
     *
     * @return a 'C' válaszlehetőség
     */
    public String getC() {
        return c;
    }

    /**
     * Visszaadja a 'D' válaszlehetőséget.
     *
     * @return a 'D' válaszlehetőség
     */
    public String getD() {
        return d;
    }

    /**
     * Visszaadja a helyes válasz betűjelét.
     *
     * @return a helyes válasz betűjele
     */
    public char getAnswer() {
        return answer;
    }
}
