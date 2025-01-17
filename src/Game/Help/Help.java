package Game.Help;

import Game.Game;
import Display.MainGameDisplay;

/**
 * Ez az absztrakt osztály a segítségek alapját képezi a játékban.
 * Minden konkrét segítség ebből az osztályból származik.
 */
public abstract class Help {

    // Referencia a játék logikájára.
    protected Game game;

    // Referencia a játék grafikus felhasználói felületére (GUI).
    protected MainGameDisplay display;

    /**
     * Konstruktor, amely inicializálja a játék és a GUI referenciait.
     *
     * @param game    a játék logikáját reprezentáló objektum
     * @param display a játék grafikus felületét reprezentáló objektum
     */
    public Help(Game game, MainGameDisplay display) {
        this.game = game;
        this.display = display;
    }

    /**
     * Absztrakt metódus, amelyet minden konkrét segítségnek implementálnia kell.
     * Ez határozza meg, hogy az adott segítség hogyan működik.
     */
    public abstract void help();
}
