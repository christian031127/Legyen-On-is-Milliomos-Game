package Game.Help;

import Game.Game;
import Display.MainGameDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A FiftyFifty osztály a "felező" segítséget valósítja meg a játékban.
 * Ez az osztály az ősosztályból, a Help-ből származik.
 */
public class FiftyFifty extends Help {

    /**
     * Konstruktor, amely inicializálja a játékot és a GUI-t a felező segítséghez.
     *
     * @param game    a játék logikáját reprezentáló objektum
     * @param display a játék grafikus felületét reprezentáló objektum
     */
    public FiftyFifty(Game game, MainGameDisplay display) {
        super(game, display); // Az ősosztály konstruktora meghívódik
    }

    /**
     * A felező segítség megvalósítása.
     * Ez a metódus két hibás választ véletlenszerűen letilt a négy válaszlehetőség közül.
     */
    @Override
    public void help() {
        // Lekérjük a helyes válasz betűjelét
        char cAns = game.getCorrectAnswer();

        // Ellenőrizzük, hogy a helyes válasz érvényes betűjel-e ('a'-'d' között)
        if (cAns < 'a' || cAns > 'd') {
            throw new IllegalStateException("Érvénytelen válasz karakter: " + cAns);
        }

        // Kikapcsoljuk a felező gombot, hogy ne lehessen újra használni
        display.enableFiftyFiftyButton(false);

        // Lista létrehozása az összes válaszopcióval
        List<Character> options = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd'));

        // Eltávolítjuk a listából a helyes választ
        options.remove(Character.valueOf(cAns));

        // Véletlenszerűen kiválasztunk két helytelen választ
        Random r = new Random();
        char firstIncorrect = options.remove(r.nextInt(options.size())); // Első hibás válasz
        char secondIncorrect = options.remove(r.nextInt(options.size())); // Második hibás válasz

        // A GUI-n letiltjuk a két kiválasztott hibás válasz gombját
        if (firstIncorrect == 'a' || secondIncorrect == 'a') display.enableAButton(false);
        if (firstIncorrect == 'b' || secondIncorrect == 'b') display.enableBButton(false);
        if (firstIncorrect == 'c' || secondIncorrect == 'c') display.enableCButton(false);
        if (firstIncorrect == 'd' || secondIncorrect == 'd') display.enableDButton(false);
    }
}
