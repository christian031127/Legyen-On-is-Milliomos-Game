package Game.Score;

import java.io.Serializable;

/**
 * Ez az osztály egy eredményt (Highscore) reprezentál,
 * amely tartalmazza a játékos nevét és a megnyert díjat.
 */
public class Highscore implements Comparable<Highscore>, Serializable {

    private final String name; // A játékos neve.
    private final String prize; // A díj összege szövegként (pl. "1.000 Ft").

    /**
     * Az osztály konstruktora, amely inicializálja az eredményt.
     *
     * @param name  a játékos neve
     * @param prize a megnyert díj szöveg formátumban
     */
    public Highscore(String name, String prize) {
        this.name = name;
        this.prize = prize;
    }

    /**
     * Visszaadja a játékos nevét.
     *
     * @return a játékos neve
     */
    public String getName() {
        return name;
    }

    /**
     * A díjat szám formátumra (int) konvertálja.
     * Az " Ft" és a "." karaktereket eltávolítja a szövegből, mielőtt számként értelmezné.
     *
     * @return a díj összege egész számként
     */
    public int getPrizeInt() {
        return Integer.parseInt(prize.replace(" Ft", "").replace(".", ""));
    }

    /**
     * Felülírja az alapértelmezett toString metódust,
     * hogy a játékos nevét és a díjat egy formázott szövegként jelenítse meg.
     *
     * @return egy formázott szöveg, amely tartalmazza a játékos nevét és díját
     */
    @Override
    public String toString() {
        return name + " " + prize;
    }

    /**
     * Összehasonlítja ezt az eredményt egy másikkal a díj összege alapján.
     * A rendezés csökkenő sorrendben történik.
     *
     * @param o egy másik Highscore objektum, amelyet összehasonlítunk
     * @return negatív érték, ha ez az eredmény nagyobb, 0, ha egyenlő,
     *         pozitív érték, ha kisebb
     */
    @Override
    public int compareTo(Highscore o) {
        return o.getPrizeInt() - this.getPrizeInt();
    }
}
