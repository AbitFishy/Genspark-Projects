import java.io.ByteArrayInputStream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GuessTest{
    Guess g;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void processGuess() {
        g = new Guess(10);
        for (int i = 0; i < 50; i++) {
            assert (g.processGuess(i + 21) == 3);
        }
        for (int i = 1; i < 10; i++){
            assert(g.processGuess(i) == -1);
        }
        assert(g.processGuess(10) == 0);
        for  (int i =11; i <= 20; i++){
            assert(g.processGuess(i) == 1);
        }
    }

    @org.junit.jupiter.api.Test
    void intro() {
        String userInput = "Drake\nScorpio\n3830103\n832q3965\n\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        g = new Guess(10);

        assertDoesNotThrow(()-> g.intro());
        assert(Objects.equals(g.playerName, "Drake"));
        assertDoesNotThrow(()-> g.intro());
        assert(Objects.equals(g.playerName, "Scorpio"));
        assertDoesNotThrow(()-> g.intro());
        assert(Objects.equals(g.playerName, "3830103"));
        assertDoesNotThrow(()-> g.intro());
        assert(Objects.equals(g.playerName, "832q3965"));
        assertDoesNotThrow(()-> g.intro());
    }

    @org.junit.jupiter.api.Test
    void loop() {
        String userInput = "5\n7\n10";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        g = new Guess(10);
        assertDoesNotThrow(()-> g.loop());
        assert(g.correctlyGuessed);
    }

    @org.junit.jupiter.api.Test
    void loopwrong() {
        String userInput = "5\n7\n1\n8\n2\n73\n0\n\n\n\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        g = new Guess(10);
        assertDoesNotThrow(()-> g.loop());
        assertFalse(g.correctlyGuessed);
    }
}