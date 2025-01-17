package Test;

import Game.Question.Question;
import Game.Question.QuestionsData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A QuestionsData osztály funkcióinak tesztelésére szolgáló egységtesztek.
 * A tesztek biztosítják a kérdésadatok helyes betöltését, a nehézségi szint szerinti kérdéslekérdezést,
 * valamint a hibás fájlkezelési esetek megfelelő kezelését.
 */
public class TestQuestionData {

    static final String TEST_JSON_FILE = "test_questions.json";

    QuestionsData questionsData;

    /**
     * Tesztadatok előállítása egy JSON fájl létrehozásával.
     * A fájl három kérdést tartalmaz, különböző nehézségi szinteken.
     *
     * @throws IOException, ha a fájl írása nem sikerül.
     */
    @BeforeAll
    public static void setUpClass() throws IOException {
        // Create a test JSON file for testing
        JSONArray questions = new JSONArray();

        JSONObject question1 = new JSONObject();
        question1.put("question", "Mi Franciaország fővárosa?");
        question1.put("a", "Párizs");
        question1.put("b", "London");
        question1.put("c", "Berlin");
        question1.put("d", "Madrid");
        question1.put("answer", "a");
        question1.put("difficulty", 1);

        JSONObject question2 = new JSONObject();
        question2.put("question", "Mennyi 2+2?");
        question2.put("a", "3");
        question2.put("b", "4");
        question2.put("c", "5");
        question2.put("d", "6");
        question2.put("answer", "b");
        question2.put("difficulty", 1);

        JSONObject question3 = new JSONObject();
        question3.put("question", "Ki írta az '1984'-et?");
        question3.put("a", "George Orwell");
        question3.put("b", "Aldous Huxley");
        question3.put("c", "J.K. Rowling");
        question3.put("d", "Ernest Hemingway");
        question3.put("answer", "a");
        question3.put("difficulty", 2);

        questions.put(question1);
        questions.put(question2);
        questions.put(question3);

        try (FileWriter file = new FileWriter(TEST_JSON_FILE)) {
            file.write(questions.toString());
        }
    }

    /**
     * Teszteli a kérdések betöltését a JSON fájlból.
     * Ellenőrzi, hogy:
     * - Az objektum létrejött.
     * - A megfelelő számú kérdés lett betöltve.
     */
    @Test
    public void testLoadQuestions() throws Exception {
        questionsData = new QuestionsData(TEST_JSON_FILE);

        assertNotNull(questionsData, "QuestionsData instance should not be null");
        assertEquals(3, questionsData.questionsArray.size(), "There should be 3 questions loaded");
    }

    /**
     * Teszteli a kérdések lekérdezését nehézségi szint alapján.
     * Ellenőrzi:
     * - A helyes nehézségi szinthez tartozó kérdés visszaadását.
     * - A hiányzó nehézségi szint helyes kezelését.
     */
    @Test
    public void testGetQuestionByDifficulty() throws Exception {
        questionsData = new QuestionsData(TEST_JSON_FILE);

        Question question = questionsData.getQuestion(1);
        assertNotNull(question, "A question with difficulty 1 should be returned");
        assertEquals(1, question.getDifficulty(), "Returned question should have difficulty 1");

        Question question2 = questionsData.getQuestion(2);
        assertNotNull(question2, "A question with difficulty 2 should be returned");
        assertEquals(2, question2.getDifficulty(), "Returned question should have difficulty 2");

        Question question3 = questionsData.getQuestion(3);
        assertNull(question3, "No question should be returned for difficulty 3");
    }

    /**
     * Teszteli a betöltött kérdések listájának helyességét.
     * Ellenőrzi:
     * - A lista elemszámát.
     * - Az első kérdés szövegét és helyes válaszát.
     */
    @Test
    public void testCreateQuestionList() throws Exception {
        questionsData = new QuestionsData(TEST_JSON_FILE);

        assertEquals(3, questionsData.questionsArray.size(), "3 questions should be in the question list");

        Question question = questionsData.questionsArray.getFirst();
        assertEquals("Mi Franciaország fővárosa?", question.getQuestion(), "The question text should match");
        assertEquals('a', question.getAnswer(), "The correct answer should be 'A'");
    }

    /**
     * Teszteli egy hibás fájl betöltését.
     * Ellenőrzi, hogy hibás fájl esetén megfelelő kivétel keletkezik.
     */
    @Test
    public void testInvalidFile() {
        assertThrows(FileNotFoundException.class, () -> new QuestionsData("invalid_file.json"),
                "Loading an invalid file should throw FileNotFoundException");
    }
}

