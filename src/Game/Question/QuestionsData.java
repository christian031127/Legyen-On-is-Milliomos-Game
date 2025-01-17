package Game.Question;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Ez az osztály a kérdéseket kezeli, JSON fájlból olvassa be azokat,
 * és különböző funkciókat biztosít a kérdések lekéréséhez.
 */
public class QuestionsData {

    public ArrayList<Question> questionsArray = new ArrayList<>(); // A kérdéseket tároló lista.

    /**
     * Konstruktor, amely a megadott JSON fájlból olvassa be a kérdéseket.
     *
     * @param json a kérdéseket tartalmazó JSON fájl elérési útja
     * @throws FileNotFoundException ha a fájl nem található
     */
    public QuestionsData(String json) throws FileNotFoundException {
        try {
            // A JSON fájl tartalmának beolvasása szövegként.
            String content = new String(Files.readAllBytes(Paths.get(json)));

            // A fájl tartalmát JSON tömbbé alakítja.
            JSONArray questionlist = new JSONArray(content);

            // A kérdések feldolgozása és hozzáadása a listához.
            questionlist.forEach(temp -> createQuestionList((JSONObject) temp));
        } catch (IOException e) {
            throw new FileNotFoundException("File not found: " + json); // Hibakezelés, ha a fájl nem található.
        }
    }

    /**
     * Egyetlen kérdés objektum létrehozása és hozzáadása a listához.
     *
     * @param questions egy JSON objektum, amely tartalmazza a kérdés adatait
     */
    public void createQuestionList(JSONObject questions) {
        String question = questions.getString("question"); // A kérdés szövege.

        // A válaszlehetőségek beolvasása (üres, ha nem létezik az adott kulcs).
        String A = questions.optString("a", "");
        String B = questions.optString("b", "");
        String C = questions.optString("c", "");
        String D = questions.optString("d", "");

        String answer = questions.getString("answer"); // A helyes válasz betűjele.
        int difficulty = questions.getInt("difficulty"); // A kérdés nehézségi szintje.

        // Új Question objektum létrehozása és hozzáadása a listához.
        questionsArray.add(new Question(difficulty, question, A, B, C, D, answer.charAt(0)));
    }

    /**
     * Egy véletlenszerű kérdés lekérése a megadott nehézségi szint alapján.
     *
     * @param currentdifficulty a kívánt nehézségi szint
     * @return egy véletlenszerű kérdés az adott nehézségi szintről, vagy null, ha nincs ilyen kérdés
     */
    public Question getQuestion(int currentdifficulty) {
        List<Question> currentQuestions = new ArrayList<>(); // Lista az aktuális nehézségi szinthez tartozó kérdések tárolására.

        // Az aktuális nehézségi szintű kérdések kiszűrése.
        questionsArray.forEach(q -> {
            if (q.getDifficulty() == currentdifficulty) {
                currentQuestions.add(q);
            }
        });

        // Ha nincs kérdés az adott nehézségi szinten, null értéket ad vissza.
        if (currentQuestions.isEmpty()) {
            return null;
        }

        // Véletlenszerű kérdés kiválasztása az adott nehézségi szint listájából.
        return currentQuestions.get(new Random().nextInt(currentQuestions.size()));
    }
}
