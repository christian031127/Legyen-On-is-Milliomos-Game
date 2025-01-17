package Game;

import Game.Question.Question;
import Game.Question.QuestionsData;
import Game.GameSave.GameState;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;

/**
 * A játék logikáját kezelő osztály.
 * Tartalmazza a játékállapotot, a kérdéseket, és a segítségek állapotát.
 * Lehetővé teszi a játék mentését, betöltését, valamint a játék körökhöz kapcsolódó funkciókat.
 */
public class Game {

    /**
     * A játék nyereményeit tartalmazó tömb.
     * Minden körhöz egy nyeremény tartozik, amelyet a játékos elérhet.
     */
    private final String[] prizes = {"1.000 Ft", "5.000 Ft", "10.000 Ft", "20.000 Ft", "50.000 Ft", "100.000 Ft",
            "250.000 Ft", "500.000 Ft", "1.000.000 Ft", "2.000.000 Ft", "5.000.000 Ft", "10.000.000 Ft"};

    /**
     * Az aktuális kör száma (1-től 12-ig).
     * A játék minden kérdése egy körhöz tartozik.
     */
    private int round;

    /**
     * A "50:50" segítség használatának állapotát jelzi.
     * true: a segítség már használva van, false: a segítség még elérhető.
     */
    private boolean fiftyfifty;

    /**
     * Az "új kérdés" segítség használatának állapotát jelzi.
     * true: a segítség már használva van, false: a segítség még elérhető.
     */
    private boolean newquestion;

    /**
     * A "közönségszavazás" segítség használatának állapotát jelzi.
     * true: a segítség már használva van, false: a segítség még elérhető.
     */
    private boolean crowdvote;

    /**
     * Az aktuális kör hátralévő idejét tartalmazza másodpercben.
     * Az időzítő minden kérdésnél újraindul.
     */
    private int timeLeft;

    /**
     * A játék véget ért állapotát jelzi.
     * true: a játék véget ért, false: a játék folyamatban van.
     */
    private boolean game_end;

    /**
     * A játék kérdésadatainak forrása.
     * A JSON fájlból betöltött kérdések tárolására és elérésére szolgál.
     */
    private final QuestionsData qlist;

    /**
     * Az aktuális kérdés objektuma.
     * Ez tartalmazza a kérdés szövegét, válaszlehetőségeit és a helyes választ.
     */
    private Question q;

    /**
     * A játékállapotot mentő és visszatöltő fájl neve.
     * Ez az állandó a mentett játékfájl eléréséhez szükséges.
     */
    public static final String SAVE_FILE = "gamestate.ser";

    /**
     * Naplózó az osztály eseményeinek és hibáinak rögzítésére.
     */
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    /**
     * Konstruktor, amely inicializálja a kérdéseket, és betölti a játékállapotot, ha elérhető.
     *
     * @throws FileNotFoundException, ha a kérdések JSON fájlja nem található.
     */
    public Game() throws FileNotFoundException {
        QuestionsData tmp;
        tmp = new QuestionsData("questions.json"); // Kérdések betöltése a JSON fájlból
        qlist = tmp; // Kérdésadatok hozzárendelése
        loadGameState(); // Játékállapot betöltése, ha van mentett állapot
    }

    /**
     * Játékállapot mentése egy fájlba.
     */
    public void saveGameState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            // Játékállapot objektum létrehozása és mentése
            GameState gameState = new GameState(round, q, fiftyfifty, newquestion, crowdvote, timeLeft);
            oos.writeObject(gameState);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save game state", e); // Hiba naplózása mentéskor
        }
    }

    /**
     * Játékállapot betöltése egy fájlból.
     * Ha nincs mentett állapot vagy a betöltés sikertelen, új játék indul.
     */
    public void loadGameState() {
        File saveFile = new File(SAVE_FILE);
        if (!saveFile.exists()) {
            beginGame(); // Nincs mentett állapot, új játék indítása
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            // Mentett állapot betöltése
            GameState gameState = (GameState) ois.readObject();
            this.round = gameState.getRound();
            this.q = gameState.getCurrentQuestion();
            this.fiftyfifty = gameState.isFiftyfiftyUsed();
            this.newquestion = gameState.isNewQuestionUsed();
            this.crowdvote = gameState.isCrowdVoteUsed();
            this.timeLeft = gameState.getTimeLeft();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to load game state", e); // Hiba naplózása mentéskor
            beginGame(); // Hibás mentés esetén új játék indítása
        }
    }

    /**
     * A játékállapot alaphelyzetbe állítása.
     * Ha létezik mentett állapot, azt törli.
     */
    public void resetGameState() {
        File saveFile = new File(SAVE_FILE);
        if (saveFile.exists()) {
            if (!saveFile.delete()) {
                LOGGER.warning("Failed to delete save file: " + SAVE_FILE); // Hiba törléskor
            }
        }

        // Új játék alapállapotának beállítása
        round = 1;
        fiftyfifty = false;
        newquestion = false;
        crowdvote = false;
        timeLeft = 30;
        q = qlist.getQuestion(round);
    }

    /**
     * Visszaadja a megadott körhöz tartozó nyereményt.
     *
     * @param currentround a jelenlegi kör száma.
     * @return a körhöz tartozó nyeremény szöveges formában.
     */
    public String getPrize(int currentround) {
        return prizes[currentround - 1];
    }

    /**
     * Getterek az osztály mezőinek lekérdezéséhez.
     */
    public int getRound() {
        return round;
    }

    public boolean isFiftyfifty() {
        return fiftyfifty;
    }

    public boolean isNewquestion() {
        return newquestion;
    }

    public boolean isCrowdvote() {
        return crowdvote;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public Question getQuestion() {
        return q;
    }

    public boolean isGameOver() {
        return round >= 12 || q == null; // Ha az utolsó körben van, vagy nincs kérdés, a játék véget ért
    }

    public boolean isGameEnd() {
        return game_end;
    }

    public QuestionsData getQuestionsData() {
        return qlist;
    }

    public char getCorrectAnswer() {
        return q.getAnswer();
    }

    /**
     * Setters az osztály mezőinek módosításához.
     */
    public void setFiftyfifty(boolean fiftyfifty) {
        this.fiftyfifty = fiftyfifty;
    }

    public void setNewquestion(boolean newquestion) {
        this.newquestion = newquestion;
    }

    public void setCrowdvote(boolean crowdvote) {
        this.crowdvote = crowdvote;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setQuestion(Question q) {
        this.q = q;
    }

    public void setGame_end(boolean game_end) {
        this.game_end = game_end;
    }

    /**
     * Új játék indítása az alapállapot beállításával.
     */
    public void beginGame() {
        round = 1; // Első kör
        fiftyfifty = false; // Segítségek alaphelyzetben
        newquestion = false;
        crowdvote = false;
        game_end = false; // A játék nincs vége

        q = qlist.getQuestion(round); // Első kérdés lekérése
    }

    /**
     * Új kör indítása.
     * Frissíti a körszámot, visszaállítja az időzítőt, és lekéri a következő kérdést.
     */
    public void newRound() {
        round++; // Körszám növelése
        timeLeft = 30; // Időzítő visszaállítása
        q = qlist.getQuestion(round); // Következő kérdés lekérése
    }
}
