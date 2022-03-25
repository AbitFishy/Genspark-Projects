import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayInputStream;


class DragonCaveTest {

    DragonCave dc = new DragonCave();
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        dc.actuallySleep = false;
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void play() {
        String userInput = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Assertions.assertDoesNotThrow(()->dc.Play());

    }

    @org.junit.jupiter.api.Test
    void lettersbeforechoice(){
        String userInput = "a\nb\nc\ntt\n74\n1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Assertions.assertDoesNotThrow(()->dc.Play());
    }

    @org.junit.jupiter.api.Test
    void donotchoose1or2(){
        String userInput = "Test\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Assertions.assertDoesNotThrow(()->dc.Play());
        Assertions.assertFalse(dc.Play());
    }

    @org.junit.jupiter.api.Test
    void enterEnterEnter(){
        String userInput = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Assertions.assertDoesNotThrow(()->dc.Play());
        Assertions.assertFalse(dc.Play());
    }
}