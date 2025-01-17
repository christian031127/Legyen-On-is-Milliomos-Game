package Game.Help;

import Display.MainGameDisplay;
import Game.Question.Question;
import Game.Game;

/**
 * A NewQuestion osztály az "új kérdés" segítséget valósítja meg a játékban.
 * Ez az osztály az ősosztályból, a Help-ből származik.
 */
public class NewQuestion extends Help {

    /**
     * Konstruktor, amely inicializálja a játékot és a GUI-t az "új kérdés" segítséghez.
     *
     * @param game    a játék logikáját reprezentáló objektum
     * @param display a játék grafikus felületét reprezentáló objektum
     */
    public NewQuestion(Game game, MainGameDisplay display) {
        super(game, display); // Az ősosztály konstruktora meghívódik
    }

    /**
     * Az "új kérdés" segítség megvalósítása.
     * Ez a metódus új kérdést kér le az aktuális nehézségi szintről,
     * és frissíti a játék állapotát és a GUI-t.
     */
    @Override
    public void help() {
        // Lekérjük az aktuális körhöz tartozó nehézségi szintet
        int currentDifficulty = game.getRound();

        // Új kérdés lekérése az aktuális nehézségi szintről
        Question newQuestion = game.getQuestionsData().getQuestion(currentDifficulty);

        // Ha sikerült új kérdést találni
        if (newQuestion != null) {
            // Az új kérdést beállítjuk a játék logikájában
            game.setQuestion(newQuestion);

            // Frissítjük a kérdés megjelenítését a grafikus felületen
            display.updateQuestionDisplay(newQuestion);

            // Kikapcsoljuk az "új kérdés" gombot, hogy ne lehessen újra használni
            display.enableNewQuestionButton(false);
        }
    }
}
