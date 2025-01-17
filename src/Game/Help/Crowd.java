package Game.Help;

import Game.Game;
import Game.Question.Question;
import Display.MainGameDisplay;

import java.util.Random;

/**
 * A Crowd osztály a "közönségszavazás" segítséget valósítja meg a játékban.
 * Ez az osztály származik az absztrakt Help osztályból.
 */
public class Crowd extends Help {

    /**
     * Konstruktor, amely inicializálja a játékot és a GUI-t a közönségszavazáshoz.
     *
     * @param game    a játék logikáját reprezentáló objektum
     * @param display a játék grafikus felületét reprezentáló objektum
     */
    public Crowd(Game game, MainGameDisplay display) {
        super(game, display);
    }

    /**
     * A közönségszavazás segítség megvalósítása.
     * Lekéri az aktuális kérdést és helyes választ, majd szimulálja a szavazás eredményét,
     * és megjeleníti azokat a GUI-n.
     */
    @Override
    public void help() {
        // Az aktuális kérdés lekérése
        Question question = game.getQuestion();

        // A helyes válasz betűjele
        char correctAnswer = question.getAnswer();

        // Szimuláljuk a közönség szavazatait
        int[] votes = generateVotes(correctAnswer);

        // Megjelenítjük az eredményeket a grafikus felületen
        display.showCrowdResults(votes);

        // Kikapcsoljuk a közönségszavazás gombot, hogy ne lehessen újra használni
        display.enableCrowdVoteButton(false);
    }

    /**
     * Közönségszavazatok generálása.
     * Véletlenszerűen osztja el a szavazatokat a válaszlehetőségek között,
     * a helyes válasz nagyobb valószínűséget kap.
     *
     * @param correctAnswer a helyes válasz betűjele
     * @return egy 4 elemű tömb, amely tartalmazza az egyes válaszokra érkezett szavazatok százalékát
     */
    private int[] generateVotes(char correctAnswer) {
        int totalVotes = 100; // Az összes szavazat (100%).

        // Véletlenszerűen meghatározzuk a helyes válasz százalékát (40% és 70% között).
        Random random = new Random();
        int correctVotePercentage = 40 + random.nextInt(31); // 40-70% közötti érték

        // A maradék szavazatok száma
        int remainingVotes = totalVotes - correctVotePercentage;

        // Létrehozunk egy tömböt a négy válaszhoz
        int[] votes = new int[4];

        // A helyes válasz indexéhez beállítjuk a generált százalékot
        votes[correctAnswer - 'a'] = correctVotePercentage;

        // Véletlenszerűen szétosztjuk a maradék szavazatokat a többi válasz között
        for (int i = 0; i < 4; i++) {
            if (i != correctAnswer - 'a') { // Csak a hibás válaszokhoz
                int randomVote = remainingVotes > 0 ? random.nextInt(remainingVotes + 1) : 0;
                votes[i] = randomVote;
                remainingVotes -= randomVote;
            }
        }

        // A maradék szavazatot hozzáadjuk az utolsó hibás válaszhoz
        for (int i = 0; i < 4; i++) {
            if (i != correctAnswer - 'a' && votes[i] == 0) {
                votes[i] = remainingVotes;
                break;
            }
        }

        return votes; // Visszaadjuk a generált szavazatokat
    }
}
