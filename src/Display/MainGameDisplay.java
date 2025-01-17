package Display;

import Game.Game;
import Game.Help.Crowd;
import Game.Help.FiftyFifty;
import Game.Help.NewQuestion;
import Game.Question.Question;
import Game.Score.Leaderboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A játék grafikus felületét (GUI) megvalósító osztály.
 * A JFrame kiterjesztésével biztosítja a játék megjelenítését és kezelését.
 */
public class MainGameDisplay extends JFrame implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(MainGameDisplay.class.getName()); // Naplózó eszköz.

    private Game game;                      // A játék logikáját kezelő objektum.
    private Leaderboard scoretable;         // A ranglistát kezelő objektum.

    // A segítségek objektumai.
    private FiftyFifty fiftyFifty;
    private NewQuestion newQuestion;
    private Crowd crowdvote;

    private Timer questionTimer;            // Az időzítő a kérdésekhez.
    private int timeLeft;                   // A hátralévő idő másodpercben.
    private JLabel timerLabel;              // A hátralévő idő megjelenítése.

    // A válaszlehetőségek gombjai.
    private JButton optionA, optionB, optionC, optionD;

    // A segítségnyújtó gombok.
    private JButton helpA, helpB, helpC;

    // Menü és menüelemek.
    private JMenu helpMenu;
    private JMenuBar menubar;

    // Görgethető szöveg- és díjlista.
    private JScrollPane scrollText;
    private JScrollPane scrollPrizes;

    // A díjakat megjelenítő panel.
    private JPanel prizeText;

    // A kérdés szövegét megjelenítő szövegterület.
    private JTextArea questionText;

    // Menüelemek.
    private JMenuItem newGame;
    private JMenuItem stopGame;
    private JMenuItem leaderboard;
    private JMenuItem deleteData;
    private JMenuItem quitGame;
    private JMenuItem description;
    private JMenuItem infoHelp;

    private JLabel[] prizes; // A díjakat megjelenítő címkék.

    /**
     * Gomb engedélyezése vagy letiltása az 'A' válaszhoz.
     *
     * @param b true, ha engedélyezett, false, ha letiltott
     */
    public void enableAButton(boolean b) {
        optionA.setEnabled(b);
    }

    /**
     * Gomb engedélyezése vagy letiltása a 'B' válaszhoz.
     *
     * @param b true, ha engedélyezett, false, ha letiltott
     */
    public void enableBButton(boolean b) {
        optionB.setEnabled(b);
    }

    /**
     * Gomb engedélyezése vagy letiltása a 'C' válaszhoz.
     *
     * @param b true, ha engedélyezett, false, ha letiltott
     */
    public void enableCButton(boolean b) {
        optionC.setEnabled(b);
    }

    /**
     * Gomb engedélyezése vagy letiltása a 'D' válaszhoz.
     *
     * @param b true, ha engedélyezett, false, ha letiltott
     */
    public void enableDButton(boolean b) {
        optionD.setEnabled(b);
    }

    /**
     * A "50:50" segítség gombjának engedélyezése vagy letiltása.
     *
     * @param b true, ha engedélyezett, false, ha letiltott
     */
    public void enableFiftyFiftyButton(boolean b) {
        helpA.setEnabled(b);
    }

    /**
     * Az "új kérdés" segítség gombjának engedélyezése vagy letiltása.
     *
     * @param b true, ha engedélyezett, false, ha letiltott
     */
    public void enableNewQuestionButton(boolean b) {
        helpB.setEnabled(b);
    }

    /**
     * A "közönségszavazás" segítség gombjának engedélyezése vagy letiltása.
     *
     * @param b true, ha engedélyezett, false, ha letiltott
     */
    public void enableCrowdVoteButton(boolean b) {
        helpC.setEnabled(b);
    }

    /**
     * A játék grafikus felületének konstruktora.
     * Inicializálja a felhasználói felületet, és ellenőrzi, hogy van-e mentett játékállapot.
     * Ha létezik mentett állapot, azt betölti, különben új játékot állít be.
     */
    public MainGameDisplay() {
        initComponents(); // A grafikus felület inicializálása.

        // Ellenőrizzük, hogy létezik-e mentett állapotot tartalmazó fájl.
        File saveFile = new File(Game.SAVE_FILE);
        if (saveFile.exists() && !game.isGameOver()) {
            refreshUIAfterLoad(); // Betöltjük a mentett állapotot, ha elérhető.
        } else {
            setupNewGameUI(); // Ha nincs mentett állapot, új játék felületét állítjuk be.
        }
    }

    /**
     * Inicializálja a játék grafikus felületének összetevőit.
     */
    private void initComponents() {

        try {
            game = new Game(); // Játék inicializálása
        } catch (FileNotFoundException e1) {
            LOGGER.log(Level.SEVERE, "Failed to load json file", e1);
        }


        // Segítségek inicializálása
        fiftyFifty = new FiftyFifty(game, this);
        newQuestion = new NewQuestion(game, this);
        crowdvote = new Crowd(game, this);

        // Ablak címe
        setTitle("Legyen Ön is Milliomos");

        // Az alkalmazás bezárása az ablak bezárásakor
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Válaszlehetőségek gombjainak beállítása
        optionA = new JButton();
        optionB = new JButton();
        optionC = new JButton();
        optionD = new JButton();

        optionA.setText("A");
        optionA.addActionListener(this);

        optionB.setText("B");
        optionB.addActionListener(this);

        optionC.setText("C");
        optionC.addActionListener(this);

        optionD.setText("D");
        optionD.addActionListener(this);

        // Segítségek gombjainak beállítása
        helpA = new JButton();
        helpB = new JButton();
        helpC = new JButton();

        helpA.setText("50/50");
        helpA.addActionListener(this);

        helpB.setText("???");
        helpB.addActionListener(this);

        helpC.setText("웃웃웃");
        helpC.addActionListener(this);

        // Menüpontok beállítása
        JMenu gameMenu = new JMenu();
        gameMenu.setText("Játék");

        newGame = new JMenuItem();
        newGame.setText("Új játék");
        newGame.addActionListener(this);

        stopGame = new JMenuItem();
        stopGame.setText("Játék befejezése");
        stopGame.addActionListener(this);

        leaderboard = new JMenuItem();
        leaderboard.setText("Dicsőséglista");
        leaderboard.addActionListener(this);

        deleteData = new JMenuItem();
        deleteData.setText("Visszaállítás");
        deleteData.addActionListener(this);

        quitGame = new JMenuItem();
        quitGame.setText("Kilépés");
        quitGame.addActionListener(this);

        gameMenu.add(newGame);
        gameMenu.add(stopGame);
        gameMenu.add(leaderboard);
        gameMenu.add(deleteData);
        gameMenu.add(quitGame);

        // Súgó menüpont
        helpMenu = new JMenu();
        helpMenu.setText("Súgó");

        description = new JMenuItem();
        description.setText("Leírás");
        description.addActionListener(this);

        infoHelp = new JMenuItem();
        infoHelp.setText("Névjegy");
        infoHelp.addActionListener(this);

        helpMenu.add(description);
        helpMenu.add(infoHelp);

        // Menü hozzáadása
        menubar = new JMenuBar();

        menubar.add(gameMenu);
        menubar.add(helpMenu);

        setJMenuBar(menubar);

        // Kérdésmező beállítása
        questionText = new JTextArea();

        questionText.setEditable(false);
        questionText.setColumns(20); // Oszlopok száma
        questionText.setRows(3); // Sorok száma
        questionText.setFont(questionText.getFont().deriveFont(22f)); // Betűméret
        questionText.setOpaque(false); // Átlátszó háttér
        questionText.setLineWrap(true); // Szövegtörés
        questionText.setWrapStyleWord(true); // Szavak törése

        // Kérdésmező görgetőpanelje
        scrollText = new JScrollPane();

        scrollText.setViewportView(questionText);
        scrollText.setOpaque(false);
        scrollText.setBorder(null);

        // Díjmező beállítása
        prizeText = new JPanel();

        prizeText.setOpaque(false);
        prizeText.add(initPrizes());

        // Díjmező görgetőpanelje
        scrollPrizes = new JScrollPane();

        scrollPrizes.setViewportView(prizeText);
        scrollPrizes.setOpaque(false);
        scrollPrizes.setBorder(null);

        // Időzítő beállítása
        timerLabel = new JLabel("Idő: " + timeLeft + "s");
        timerLabel.setFont(timerLabel.getFont().deriveFont(30f));
        timerLabel.setOpaque(false);

        // Segítségek és válaszlehetőségek gombjainak letiltása
        enableHelpButtons(false);
        enableAnsButtons(false);

        // Játék befejezése gomb tiltása
        stopGame.setEnabled(false);

        // Dicsőséglista betöltése
        try {
            loadLeaderboard();
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load leaderboard", e);
        }

        // Ablak bezárásakor mentési megerősítés
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmAndSaveBeforeExit();
            }
        });

        // Elrendezés beállítása
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        // Vízszintes elrendezés
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(15, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(optionA, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(optionB, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(optionC, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(optionD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(scrollText)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(helpA, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(helpB, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(helpC, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(timerLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollPrizes))
                                .addContainerGap(15, Short.MAX_VALUE)
                        )
        );

        // Függőleges elrendezés
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(15, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(scrollText, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(helpA, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(helpB, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(helpC, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
                                .addComponent(timerLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(optionA, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(optionB, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(optionC, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(optionD, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(scrollPrizes, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(15, Short.MAX_VALUE)
                        )
        );
        pack(); // Az ablak méretének igazítása a tartalomhoz
    }

    /**
     * A nyeremények megjelenítéséhez létrehozza a szükséges panelt.
     *
     * @return egy JPanel objektum, amely tartalmazza a körökhöz tartozó nyereményeket.
     */
    private JPanel initPrizes() {
        JPanel prizesP = new JPanel(new GridLayout(12, 1, 10, 0));
        prizes = new JLabel[12];
        for (int i = 12; i >= 1; i--) { // Visszafelé iterálunk, hogy a legnagyobb nyeremény legyen felül
            prizes[i - 1] = new JLabel(i + ".kör:  " + game.getPrize(i));
            prizes[i - 1].setHorizontalAlignment(SwingConstants.CENTER); // A szöveget középre igazítjuk
            prizesP.add(prizes[i - 1]);
        }
        return prizesP;
    }

    /**
     * A válaszlehetőségek gombjainak engedélyezése vagy letiltása.
     *
     * @param b true, ha engedélyezett, false, ha letiltott.
     */
    private void enableAnsButtons(boolean b) {
        enableAButton(b);
        enableBButton(b);
        enableCButton(b);
        enableDButton(b);
    }

    /**
     * A segítségek gombjainak engedélyezése vagy letiltása.
     * A gombok csak akkor engedélyezettek, ha a segítség még nem használt.
     *
     * @param b true, ha engedélyezett, false, ha letiltott.
     */
    private void enableHelpButtons(boolean b) {
        helpA.setEnabled(b && !game.isFiftyfifty());
        helpB.setEnabled(b && !game.isNewquestion());
        helpC.setEnabled(b && !game.isCrowdvote());
    }

    /**
     * Frissíti a kérdés és a válaszlehetőségek megjelenítését a képernyőn.
     *
     * @param question az új kérdés, amelyet meg kell jeleníteni.
     */
    public void updateQuestionDisplay(Question question) {
        questionText.setText(question.getQuestion());  // Kérdés szövegének frissítése
        optionA.setText("A: " + question.getA());  // "A" opció frissítése
        optionB.setText("B: " + question.getB());  // "B" opció frissítése
        optionC.setText("C: " + question.getC());  // "C" opció frissítése
        optionD.setText("D: " + question.getD());  // "D" opció frissítése
    }

    /**
     * Megjeleníti a közönség szavazatainak eredményeit egy felugró ablakban.
     *
     * @param votes egy 4 elemű tömb, amely tartalmazza az egyes válaszlehetőségekre érkezett százalékos szavazatokat.
     */
    public void showCrowdResults(int[] votes) {
        // Formázott szöveg létrehozása a szavazatok alapján
        String resultMessage = String.format(
                "Közönség szavazatai:\nA: %d%%\nB: %d%%\nC: %d%%\nD: %d%%",
                votes[0], votes[1], votes[2], votes[3]
        );
        // Felugró ablak megjelenítése az eredményekkel
        JOptionPane.showMessageDialog(this, resultMessage, "Közönség Szavazás", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Kezeli a felhasználói interakciókat (gombnyomásokat, menüpont választásokat).
     *
     * @param e az esemény, amely a felhasználói interakciót leírja.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(newGame)) {
            newGame(); // Új játék indítása
        } else if (e.getSource().equals(optionA)) {
            // Ellenőrizzük, hogy az 'A' válasz helyes-e
            if (game.getCorrectAnswer() == 'a') {
                goodAns(); // Helyes válasz
            } else {
                gameOver(); // Helytelen válasz
            }
        } else if (e.getSource().equals(optionB)) {
            if (game.getCorrectAnswer() == 'b') {
                goodAns();
            } else {
                gameOver();
            }
        } else if (e.getSource().equals(optionC)) {
            if (game.getCorrectAnswer() == 'c') {
                goodAns();
            } else {
                gameOver();
            }
        } else if (e.getSource().equals(optionD)) {
            if (game.getCorrectAnswer() == 'd') {
                goodAns();
            } else {
                gameOver();
            }
        } else if (e.getSource().equals(helpA)) {
            fiftyFifty.help(); // "50:50" segítség használata
            game.setFiftyfifty(true); // Állapot frissítése
            enableHelpButtons(false); // Gombok frissítése
        } else if (e.getSource().equals(helpB)) {
            newQuestion.help(); // Új kérdés segítség használata
            game.setNewquestion(true);
            enableHelpButtons(false);
        } else if (e.getSource().equals(helpC)) {
            crowdvote.help(); // Közönségszavazás használata
            game.setCrowdvote(true);
            enableHelpButtons(false);
        } else if (e.getSource().equals(stopGame)) {
            gameOver(); // Játék befejezése
        } else if (e.getSource().equals(leaderboard)) {
            openLeaderboard(); // Dicsőséglista megnyitása
        } else if (e.getSource().equals(deleteData)) {
            deleteLeaderboard(); // Dicsőséglista törlése
        } else if (e.getSource().equals(quitGame)) {
            confirmAndSaveBeforeExit(); // Kilépés előtti mentés
            System.exit(0); // Alkalmazás bezárása
        } else if (e.getSource().equals(description)) {
            showDescription(); // Játékleírás megjelenítése
        } else if (e.getSource().equals(infoHelp)) {
            info(); // Névjegy megjelenítése
        }
    }

    /**
     * Új játék indítása.
     * - A játék állapotának alaphelyzetbe állítása és új játék kezdése.
     * - A válasz- és segítséggombok engedélyezése.
     */
    private void newGame() {
        game.resetGameState(); // Játék állapotának alaphelyzetbe állítása
        game.beginGame(); // Új játék indítása

        updateQstn(); // Kérdés megjelenítésének frissítése

        enableAnsButtons(true); // Válaszgombok engedélyezése
        enableHelpButtons(true); // Segítséggombok engedélyezése

        newGame.setEnabled(false); // Az "Új játék" gomb letiltása
        stopGame.setEnabled(true); // A "Játék befejezése" gomb engedélyezése
    }

    /**
     * Frissíti az aktuális kérdést, a válaszlehetőségeket és a nyereménytábla megjelenítését.
     */
    public void updateQstn() {
        Question cQuestion = game.getQuestion(); // Lekérjük az aktuális kérdést

        questionText.setText(cQuestion.getQuestion()); // A kérdés szövegének beállítása

        // Ellenőrizzük, hogy van-e futó időzítő, és csak akkor állítjuk be az időt, ha szükséges
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop(); // Megállítjuk a futó időzítőt
        }

        // Az időzítő újraindítása; 30 másodperc, ha új játék, mentett idő, ha visszatöltött játék
        int timerStart = game.isGameEnd() ? 30 : game.getTimeLeft();
        startTimer(timerStart);

        // Válaszok beállítása HTML formátumban, automatikus sortöréssel
        optionA.setText("<html>A: " + cQuestion.getA() + "</html>");
        optionB.setText("<html>B: " + cQuestion.getB() + "</html>");
        optionC.setText("<html>C: " + cQuestion.getC() + "</html>");
        optionD.setText("<html>D: " + cQuestion.getD() + "</html>");

        // Nyereménytábla színeinek frissítése
        for (int i = 0; i < 12; i++) {
            if (i < game.getRound() - 1) {
                prizes[i].setForeground(Color.GREEN); // Előző körök: zöld
            } else if (i == game.getRound() - 1) {
                prizes[i].setForeground(Color.BLACK); // Aktuális kör: fekete
            } else {
                prizes[i].setForeground(Color.GRAY); // Jövőbeli körök: szürke
            }
        }
    }

    /**
     * Jó válasz esetén új kör indítása.
     * Ha ez volt az utolsó kör, a játékos nyer.
     */
    private void goodAns() {
        if (game.getRound() == 12) { // Ha a játékos az utolsó kérdést válaszolta meg
            win(); // Nyertes állapot megjelenítése
            return;
        }

        game.newRound(); // Új kör indítása
        updateQstn(); // Kérdés frissítése

        enableAnsButtons(true); // Válaszgombok engedélyezése
        enableHelpButtons(true); // Segítséggombok engedélyezése
    }

    /**
     * A játékos nyerésekor végrehajtandó műveletek.
     * - Az időzítő leállítása.
     * - A válaszgombok és segítséggombok letiltása.
     * - A játékos nevének bekérése és a nyeremény rögzítése.
     */
    private void win() {
        if (questionTimer != null) {
            questionTimer.stop(); // Időzítő leállítása
        }

        enableAnsButtons(false); // Válaszgombok letiltása
        enableHelpButtons(false); // Segítséggombok letiltása

        questionText.setText(""); // Kérdés mező ürítése

        prizes[11].setForeground(Color.GREEN); // Az utolsó nyeremény zöld színnel kiemelése

        // Játékos nevének bekérése
        String name = JOptionPane.showInputDialog(this,
                "Gratulálok Ön nyert!\nKérlek add meg a neved alább.",
                "Játék vége", JOptionPane.PLAIN_MESSAGE);

        if (name == null || name.isEmpty()) name = "Anonymous"; // Ha nincs név, "Anonymous" az alapértelmezett
        scoretable.addScore(name, game.getPrize(12)); // Nyeremény rögzítése a ranglistán

        game.setGame_end(true); // Játék vége állapot
        game.resetGameState(); // Játék állapotának alaphelyzetbe állítása

        newGame.setEnabled(true); // "Új játék" gomb engedélyezése
        stopGame.setEnabled(false); // "Játék befejezése" gomb letiltása

        setupNewGameUI(); // Alaphelyzetű felhasználói felület beállítása
    }

    /**
     * A játékos vesztésekor végrehajtandó műveletek.
     * - Az időzítő leállítása.
     * - A válaszgombok és segítséggombok letiltása.
     * - A helyes válasz megjelenítése és a vesztes kör rögzítése.
     */
    private void gameOver() {
        if (questionTimer != null) {
            questionTimer.stop(); // Időzítő leállítása
        }

        char cAns = game.getCorrectAnswer(); // Helyes válasz lekérése
        int cRound = game.getRound(); // Jelenlegi kör lekérése

        enableAnsButtons(false); // Válaszgombok letiltása
        enableHelpButtons(false); // Segítséggombok letiltása

        questionText.setText(""); // Kérdés mező ürítése

        if (cRound == 1) { // Ha az első körben esett ki a játékos
            JOptionPane.showMessageDialog(this, "Ön veszített, a játéknak vége!\nHelyes válasz: " + Character.toUpperCase(cAns),
                    "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
        } else { // Ha nem az első körben esett ki
            String name = JOptionPane.showInputDialog(this,
                    "Ön veszített, a játéknak vége!\nHelyes válasz: " + Character.toUpperCase(cAns) +
                            "\nKérlek add meg a neved alább.", "GAME OVER", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.isEmpty()) name = "Anonymous"; // Alapértelmezett név, ha nincs megadva
            scoretable.addScore(name, game.getPrize(cRound - 1)); // Az utolsó helyes kör nyereményének rögzítése
        }

        prizes[cRound - 1].setForeground(Color.RED); // A vesztes kör piros színnel kiemelése

        game.setGame_end(true); // Játék vége állapot
        game.resetGameState(); // Játék állapotának alaphelyzetbe állítása

        newGame.setEnabled(true); // "Új játék" gomb engedélyezése
        stopGame.setEnabled(false); // "Játék befejezése" gomb letiltása

        setupNewGameUI(); // Alaphelyzetű felhasználói felület beállítása
    }

    /**
     * Megnyitja a dicsőséglistát, és egy felugró ablakban megjeleníti azt.
     */
    private void openLeaderboard() {
        JOptionPane.showMessageDialog(this, scoretable.listLeaderboard(), "Dicsőséglista", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Betölti a dicsőséglistát egy fájlból.
     *
     * @throws IOException,            ha a fájl olvasása nem sikerül.
     * @throws ClassNotFoundException, ha a fájl formátuma nem felel meg az elvártnak.
     */
    private void loadLeaderboard() throws IOException, ClassNotFoundException {
        scoretable = new Leaderboard(); // Inicializáljuk az üres dicsőséglistát
        File f = new File("leaderboard");
        FileInputStream fs = new FileInputStream(f);
        ObjectInputStream os = new ObjectInputStream(fs);
        scoretable = (Leaderboard) os.readObject(); // Betöltjük az objektumot a fájlból
        os.close(); // Zárjuk az adatfolyamokat
    }

    /**
     * Mentésre kerül a dicsőséglista egy fájlba.
     *
     * @throws IOException, ha a fájl írása nem sikerül.
     */
    private void saveLeaderboard() throws IOException {
        FileOutputStream fs = new FileOutputStream("leaderboard");
        ObjectOutputStream oos = new ObjectOutputStream(fs);
        oos.writeObject(scoretable); // Az aktuális dicsőséglista mentése
        oos.close(); // Az adatfolyam lezárása
    }

    /**
     * Törli a dicsőséglistát, és egy üres listát ment a fájlba.
     * A törlés előtt megerősítést kér a felhasználótól.
     */
    private void deleteLeaderboard() {
        // Törlés megerősítése
        int confirm = JOptionPane.showConfirmDialog(this, "Biztosan törölni szeretné a dicsőséglistát?", "Megerősítés", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            scoretable.clear(); // Dicsőséglista törlése
            try {
                saveLeaderboard(); // Az üres lista mentése fájlba
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, "A dicsőséglista sikeresen törölve."); // Visszajelzés a felhasználónak
        }
    }

    /**
     * Kilépés előtt megerősíti, hogy a játékos szeretné-e menteni a játékállapotot.
     * A dicsőséglistát mindig menti kilépés előtt.
     */
    private void confirmAndSaveBeforeExit() {
        // Csak akkor kér mentést, ha a játék még nem ért véget
        if (!game.isGameEnd()) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Szeretné menteni az aktuális játékállapotot kilépés előtt?",
                    "Játékállapot mentése",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                game.saveGameState(); // Játékállapot mentése
            }
        }

        try {
            saveLeaderboard(); // Dicsőséglista mentése
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save leaderboard", e); // Hiba naplózása, ha nem sikerül
        }
    }

    /**
     * A játék leírását jeleníti meg egy felugró ablakban.
     */
    private void showDescription() {
        JOptionPane.showMessageDialog(this, """
                Ez a játék a klasszikus Legyen Ön is Milliomos műsorra épül. A játék célja, hogy a játékos 12
                kérdésre helyes választ adva elérje a végső 10 millió forintos nyereményt. Minden körben négy
                válaszlehetőség közül kell kiválasztani a helyes választ.
                
                A játék három segítséget biztosít a játékos számára:
                   Felező (50/50): Két helytelen válaszlehetőség eltávolításra kerül.
                   Új Kérdés: A jelenlegi kérdés egy másikra cserélődik.
                   Közönség Szavazata: A közönség szavazatai alapján kapott javaslat segít a helyes válasz kiválasztásában.
                
                A játék mentése és betöltése is támogatott, így bármikor folytathatod ott, ahol abbahagytad,
                beleértve az aktuális kérdést, a megmaradt időt és a segítségek állapotát.
                
                Próbáld meg helyesen megválaszolni az összes kérdést, és érj el a főnyereményig!""", "Leírás", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Megjeleníti a játék készítőjének információit egy felugró ablakban.
     */
    private void info() {
        JOptionPane.showMessageDialog(this, "A játékot készítette:\nSpertli Krisztián\nElérhetőség:\nspertlik13@gmail.com", "Névjegy", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Az új játék felhasználói felületét alaphelyzetbe állítja.
     */
    public void setupNewGameUI() {
        questionText.setText("Indíts egy új játékot!"); // Üdvözlő üzenet a kérdésmezőben
        optionA.setText("A"); // Alaphelyzetű válaszok
        optionB.setText("B");
        optionC.setText("C");
        optionD.setText("D");

        timerLabel.setText("Nyereménylista"); // Időzítő mező frissítése

        game.setGame_end(true); // Játék vége állapot beállítása

        enableAnsButtons(false); // Válaszgombok letiltása
        enableHelpButtons(false); // Segítséggombok letiltása
        newGame.setEnabled(true); // "Új játék" gomb engedélyezése
        stopGame.setEnabled(false); // "Játék befejezése" gomb letiltása
    }

    /**
     * A felhasználói felület frissítése egy mentett játék betöltése után.
     */
    public void refreshUIAfterLoad() {
        if (game.getQuestion() != null) {
            updateQstn(); // Kérdés frissítése, ha van aktuális kérdés
        }

        // Segítségek állapotának frissítése
        enableFiftyFiftyButton(!game.isFiftyfifty());
        enableNewQuestionButton(!game.isNewquestion());
        enableCrowdVoteButton(!game.isCrowdvote());

        enableAnsButtons(true); // Válaszgombok engedélyezése

        newGame.setEnabled(false); // Új játék gomb letiltása
        stopGame.setEnabled(true); // Játék megállítása engedélyezett

        if (questionTimer == null || !questionTimer.isRunning()) {
            startTimer(game.getTimeLeft()); // Időzítő indítása a mentett idővel
        }
    }

    /**
     * Elindítja az időzítőt a megadott kezdeti idővel.
     *
     * @param initialTime az időzítő kezdőértéke másodpercben.
     */
    private void startTimer(int initialTime) {
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop(); // Ha létezik futó időzítő, állítsuk le
        }

        timeLeft = initialTime; // Az időzítő kezdeti értékének beállítása
        timerLabel.setText("Idő: " + timeLeft + "s");

        questionTimer = new Timer(1000, _ -> { // Időzítő létrehozása 1 másodperces léptékkel
            timeLeft--;
            game.setTimeLeft(timeLeft); // Az idő csökkentése a játék állapotában
            timerLabel.setText("Idő: " + timeLeft + "s");

            if (timeLeft <= 0) {
                questionTimer.stop(); // Időzítő leállítása, ha lejárt az idő
                gameOver(); // A játék vége
            }
        });

        questionTimer.start(); // Időzítő indítása
    }
}
