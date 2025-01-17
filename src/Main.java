import Display.MainGameDisplay;

/**
 * A főprogram, amely elindítja a játékot.
 */
public class Main {

    public static void main(String[] args) {
        // Létrehozza és megjeleníti a fő játék grafikus felületét (JFrame).
        new MainGameDisplay().setVisible(true);
    }
}
