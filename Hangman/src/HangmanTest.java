import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class HangmanTest {
    Hangman hm;

    @org.junit.jupiter.api.Test
    void play() {
        String input = "B\na\nc\nn\nc\nr\ne\nm\np\ni\nyes\nt\nz\nx\nv\nq\nw\nu\nd\ns\nh\nmachupichu\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        hm.wordlist.clear();
        hm.wordlist.add("BANANACREAMPI");
        hm.secretWord =   hm.generateGuess(0);

        assertTrue( hm.play());
        assertFalse(hm.play());
    }

    @org.junit.jupiter.api.Test
    void loop() {
        String input = "B\na\nc\nn\nc\nr\ne\nm\np\ni\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        hm.wordlist.clear();
        hm.wordlist.add("BANANACREAMPI");
        hm.secretWord =   hm.generateGuess(0);
        int count = input.length()/2;
        boolean result = true;
        while (count-- >0){
            result = hm.loop();
        }
        assert !result;
        assert hm.win;
    }
    @org.junit.jupiter.api.Test
    void loop2() {
        String input = "B\na\nc\nn\nc\nr\ne\nm\np\nd\nq\nz\nx\ny\nh\ng\nu\nk\ns\ni\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        hm.wordlist.clear();
        hm.wordlist.add("BANANACREAMPI");
        hm.secretWord =   hm.generateGuess(0);
        int count = input.length()/2;
        boolean result = true;
        while (count-- >1){
            result = hm.loop();
        }
        assert !result;
        assert !hm.win;
    }
    @org.junit.jupiter.api.Test
    void loop3() {
        String input = "B\na\nc\nn\n`\n`\n`\n`\nc\nr\ne\nm\np\nd\nq\nz\nx\ny\nh\ng\nu\nk\ns\ni\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        hm.wordlist.clear();
        hm.wordlist.add("BANANACREAMPI");
        hm.secretWord =  hm.generateGuess(0);
        int count = input.length()/3;
        boolean result = true;
        while (count-- >1){
            result = hm.loop();
        }
        assert result;
        assert !hm.win;
    }

    @org.junit.jupiter.api.Test
    void userInput() {
        String input = "tt\n5\n3\n8\nasdf\ng4sfdg\nwoirfdw\n`\n\\\nf\nfjf\n321\nf7a6\nfsa6ds\n";
        //String input = "j\ns\na\ne\nh\n4\nh\nxx\nj\ne\nw\ne\nr\nt\ny\nu\ni\no\np\na\ns\nd\nf\ng\nh\nj\nk\nl\nz\nx\nxc\nv\nb\nb\ngn\n\nf\nm\n\nn\n\nfj\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        assertDoesNotThrow(() -> hm.userInput("Test",0));
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman("C:\\Users\\catdo\\source\\repos\\Genspark-Projects\\Hangman\\25 common nouns.txt");
        assert(hm.userInput("Test", 0) == 'F');

    }

    @org.junit.jupiter.api.Test
    void checkIfLetterInWord() {
        hm = new Hangman();
        hm.secretWord = "BANANACREAMPI";
        assert(hm.checkIfLetterInWord('B'));
        assert(hm.checkIfLetterInWord('A'));
        assert(hm.checkIfLetterInWord('N'));
        assert(hm.checkIfLetterInWord('C'));
        assert(hm.checkIfLetterInWord('R'));
        assert(hm.checkIfLetterInWord('E'));
        assert(hm.checkIfLetterInWord('M'));
        assert(hm.checkIfLetterInWord('P'));
        assert(hm.checkIfLetterInWord('I'));
        assert(!hm.checkIfLetterInWord('H'));
        assert(!hm.checkIfLetterInWord('Z'));
        assert(!hm.checkIfLetterInWord('X'));
        assert(!hm.checkIfLetterInWord('L'));
        assert(!hm.checkIfLetterInWord('T'));
        assert(!hm.checkIfLetterInWord('2'));
        assert(!hm.checkIfLetterInWord('`'));
        assert(!hm.checkIfLetterInWord('\n'));
    }

    @org.junit.jupiter.api.Test
    void showUnGuessedWord() {
        hm = new Hangman();
        hm.secretWord = "BANANACREAMPI";
        assert(Objects.equals(hm.showUnGuessedWord(), "_____________"));
        hm.guessedLetters.add('A');
        assert(Objects.equals(hm.showUnGuessedWord(), "_A_A_A___A___"));
        hm.guessedLetters.add('A');
        assert(Objects.equals(hm.showUnGuessedWord(), "_A_A_A___A___"));
        hm.guessedLetters.add('B');
        assert(Objects.equals(hm.showUnGuessedWord(), "BA_A_A___A___"));
        hm.guessedLetters.add('C');
        assert(Objects.equals(hm.showUnGuessedWord(), "BA_A_AC__A___"));
        hm.guessedLetters.add('M');
        assert(Objects.equals(hm.showUnGuessedWord(), "BA_A_AC__AM__"));
        hm.guessedLetters.add('P');
        assert(Objects.equals(hm.showUnGuessedWord(), "BA_A_AC__AMP_"));
        hm.guessedLetters.add('N');
        assert(Objects.equals(hm.showUnGuessedWord(), "BANANAC__AMP_"));
        hm.guessedLetters.add('R');
        assert(Objects.equals(hm.showUnGuessedWord(), "BANANACR_AMP_"));
        hm.guessedLetters.add('E');
        assert(Objects.equals(hm.showUnGuessedWord(), "BANANACREAMP_"));
        hm.guessedLetters.add('I');
        assert(Objects.equals(hm.showUnGuessedWord(), "BANANACREAMPI"));

    }

    @org.junit.jupiter.api.Test
    void missedLetters() {
        String input = "A\nG\nU\no\nI\nN\n7\n`\n;\n\nr\na\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        hm.secretWord = "BANANACREAMPI";

        //A
        hm.loop();
        assertEquals("_A_A_A___A___", hm.showUnGuessedWord());
        assertEquals("Missed Letters: ", hm.missedLetters());
        //G
        hm.loop();
        assertEquals("_A_A_A___A___", hm.showUnGuessedWord());
        assertEquals("Missed Letters: G", hm.missedLetters());
        //U
        hm.loop();
        assertEquals("_A_A_A___A___", hm.showUnGuessedWord());
        assertEquals("Missed Letters: GU", hm.missedLetters());
        //o
        hm.loop();
        assertEquals("_A_A_A___A___", hm.showUnGuessedWord());
        assertEquals("Missed Letters: GUO", hm.missedLetters());
        //I
        hm.loop();
        assertEquals("_A_A_A___A__I", hm.showUnGuessedWord());
        assertEquals("Missed Letters: GUO", hm.missedLetters());
        //N
        hm.loop();
        assertEquals("_ANANA___A__I", hm.showUnGuessedWord());
        assertEquals("Missed Letters: GUO", hm.missedLetters());
        //7
        //`
        //;
        // \nr
        hm.loop();
        assertEquals("_ANANA_R_A__I", hm.showUnGuessedWord());
        assertEquals("Missed Letters: GUO", hm.missedLetters());
        //a
        hm.loop();
        assertEquals("_ANANA_R_A__I", hm.showUnGuessedWord());
        assertEquals("Missed Letters: GUO", hm.missedLetters());
    }

    @org.junit.jupiter.api.Test
    void drawGallows() {
        hm = new Hangman();
        assert(Objects.equals(hm.drawGallows(0), " +---+\n     |\n     |\n     |\n     |\n     |\n     |\n=======\n"));


        assert(hm.drawGallows(8).equals(" +---+\n" +
                " |   |\n" +
                " O   |\n" +
                "/T\\  |\n" +
                " ^   |\n" +
                "/ \\  |\n" +
                "     |\n" +
                "=======\n"));

        assert(hm.drawGallows(-1).equals(" +---+\n" +
                "     |\n" +
                "     |\n" +
                "     |\n" +
                "     |\n" +
                "     |\n" +
                "     |\n" +
                "=======\n"));

        assert(hm.drawGallows(483415).equals(" +---+\n" +
                " |   |\n" +
                " O   |\n" +
                "/T\\  |\n" +
                " ^   |\n" +
                "/ \\  |\n" +
                "     |\n" +
                "=======\n"));
    }

    @org.junit.jupiter.api.Test
    void generateGuess() {
        hm = new Hangman("");
        assert hm.generateGuess(0).equals("CAT");
        //hm.wordlist.add("CAT");
        hm.wordlist.add("DOGE");
        hm.wordlist.add("BUNNY");
        hm.wordlist.add("RABBIT");
        hm.wordlist.add("TERRIER");
        assert hm.generateGuess(3).equals("CAT");
        assert hm.generateGuess(4).equals("DOGE");
        assert hm.generateGuess(5).equals("BUNNY");
        assert hm.generateGuess(6).equals("RABBIT");
        assert hm.generateGuess(7).equals("TERRIER");
    }

    @org.junit.jupiter.api.Test
    void readWordFile() {
        hm = new Hangman();
        assert hm.wordlist.size() == 25;
        ArrayList<String> words = new ArrayList<>(Arrays.asList("time", "person", "year", "way", "day", "thing", "man", "world", "life", "hand", "part", "child", "eye", "woman", "place", "work", "week","case", "point", "government", "company", "number", "group", "problem", "fact"));
        for (var word: words){
            assert hm.wordlist.contains(word.toUpperCase());
        }
    }


    @BeforeEach
    void setUp() {
        //hm = new Hangman("C:\\Users\\catdo\\source\\repos\\Genspark-Projects\\Hangman\\25 common nouns.txt");
        //hm.secretWord = "CAT";
    }

    @AfterEach
    void tearDown() {
    }
}