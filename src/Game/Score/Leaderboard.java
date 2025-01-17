package Game.Score;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringJoiner;

/**
 * Ez az osztály egy ranglistát (Leaderboard) kezel, amely a legjobb eredményeket tárolja.
 */
public class Leaderboard implements Serializable {

    ArrayList<Highscore> leaderboard; // A ranglista, amely Highscore objektumokat tárol.

    /**
     * Az osztály konstruktora, amely inicializálja a ranglistát.
     */
    public Leaderboard() {
        leaderboard = new ArrayList<>();
    }

    /**
     * A ranglistát adja vissza.
     *
     * @return az aktuális ranglista
     */
    public ArrayList<Highscore> getLeaderboard() {
        return leaderboard;
    }

    /**
     * A ranglista szöveges formátumban történő megjelenítése.
     * Legfeljebb 10 bejegyzést listáz ki, soronként új helyezést jelölve.
     *
     * @return a ranglista formázott szöveges változata
     */
    public String listLeaderboard() {
        StringJoiner joiner = new StringJoiner("\n"); // Új StringJoiner, ami sortörésekkel fűzi össze a részeket
        int limit = Math.min(leaderboard.size(), 10); // Maximum 10 bejegyzés jeleníthető meg
        for (int i = 0; i < limit; i++) {
            joiner.add((i + 1) + ". " + leaderboard.get(i).toString());
        }
        return joiner.toString();
    }

    /**
     * Új eredmény hozzáadása a ranglistához.
     * Ha a ranglista eléri a 10-es méretet, csak a jobb eredmény kerülhet be.
     *
     * @param player a játékos neve
     * @param prize az elnyert díj (pl. "1.000 Ft")
     */
    public void addScore(String player, String prize) {
        Highscore temp = new Highscore(player, prize);
        if(leaderboard.size() == 10 && leaderboard.get(9).getPrizeInt() < temp.getPrizeInt()) {
            leaderboard.remove(9); // Ha az új eredmény jobb, a leggyengébb (10.) eredményt törli
        }
        leaderboard.add(temp);
        Collections.sort(leaderboard);
    }

    /**
     * A ranglista teljes törlése.
     */
    public void clear() {
        leaderboard.clear();
    }
}
