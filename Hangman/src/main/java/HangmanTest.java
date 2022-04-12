import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HangmanTest {
    Hangman hm;

    @org.junit.jupiter.api.Test
    void play() {
        String input = "Larry\nB\na\nc\nn\nc\nr\ne\nm\np\ni\nyes\nt\nz\nx\nv\nq\nw\nu\nd\ns\nh\nmachupichu\n";
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
        assertFalse(result);
        assertTrue( hm.win);
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
        assertFalse(result);
        assertFalse( hm.win);
    }
    @org.junit.jupiter.api.Test
    void loop3()  {
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
        assertTrue( result);
        assertFalse(hm.win);

    }

    @org.junit.jupiter.api.Test
    void askForName(){
        String input = "Bobby\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        assertDoesNotThrow(() ->hm.askForName());
        assertEquals("Bobby", hm.playerName);
    }

    @org.junit.jupiter.api.Test
    void askForName2(){
        String input = "\r\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        assertDoesNotThrow(() ->hm.askForName());
        assertEquals("", hm.playerName);
    }

    @org.junit.jupiter.api.Test
    void askForName3(){
        String input = "asupifbaefgawepf;idjfafaweferagardfgaharsdhj`\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        assertDoesNotThrow(() ->hm.askForName());
        assertEquals("asupifbaefgawepf;idj", hm.playerName);
    }

    @org.junit.jupiter.api.Test
    void checkAndRecordHighScore(){
        hm = new Hangman();
        var res = hm.checkAndRecordHighScore(3);
        assertEquals( "You Have The First Score Of 3!", res);
        var lines = assertDoesNotThrow(()-> Files.readAllLines(Hangman.highScoreFile));
       var line = assertDoesNotThrow( () -> lines.get(0));
        assertEquals("" + Hangman.highScoreDelim + "3", line);
    }

    @org.junit.jupiter.api.Test
    void checkAndRecordHighScore2() {
        String scores = "Bob" + Hangman.highScoreDelim + "5\n"
                + "Dave" + Hangman.highScoreDelim + "5\n"
                + "Wort" + Hangman.highScoreDelim + "6\n"
                + "Gort" + Hangman.highScoreDelim + "6\n"
                + "Bort" + Hangman.highScoreDelim + "6\n"
                + "Me" + Hangman.highScoreDelim + "3\n";
        assertDoesNotThrow(() -> Files.writeString(Hangman.highScoreFile, scores));

        hm = new Hangman();
        hm.playerName = "Me";
        var res = hm.checkAndRecordHighScore(3);
        assertEquals("You Tied Your Previous Record Of 3!", res);

        res = hm.checkAndRecordHighScore(2);
        assertEquals("You Beat Your Old Record Of 3!", res);
    }
    @org.junit.jupiter.api.Test
    void checkAndRecordHighScore3(){
        String scores = "Bob"+ Hangman.highScoreDelim +"5\n"
                + "Dave"+ Hangman.highScoreDelim +"5\n"
                + "Wort"+ Hangman.highScoreDelim +"6\n"
                + "Gort"+ Hangman.highScoreDelim +"6\n"
                + "Bort"+ Hangman.highScoreDelim +"6\n";
        assertDoesNotThrow(() -> Files.writeString(Hangman.highScoreFile, scores));

        hm = new Hangman();
        hm.playerName = "Bob";
        var res = hm.checkAndRecordHighScore(5);
        assertEquals("You Tied Your Previous Record Of 5! Along With Dave!", res);

        hm.playerName = "Rick";
        res = hm.checkAndRecordHighScore(5);
        assertEquals("You Tied The High Score Of 5! Set By Dave and Bob!", res);

        var readScoreFile = assertDoesNotThrow(() -> Files.readAllLines(Hangman.highScoreFile));
        assertTrue(readScoreFile.contains("Rick"+ Hangman.highScoreDelim + "5"));
    }

    @org.junit.jupiter.api.Test
    void checkAndRecordHighScore4(){
        String scores =
                  "Wort"+ Hangman.highScoreDelim +"6\n"
                + "Gort"+ Hangman.highScoreDelim +"6\n"
                + "Bort"+ Hangman.highScoreDelim +"6\n";
        assertDoesNotThrow(() -> Files.writeString(Hangman.highScoreFile, scores));

        hm = new Hangman();
        hm.playerName = "Bort";
        var res = hm.checkAndRecordHighScore(6);
        assertEquals("You Tied Your Previous Record Of 6! Along With Gort and Wort!", res);

        hm.playerName = "Mort";
        res = hm.checkAndRecordHighScore(6);
        assertEquals("You Tied The High Score Of 6! Set By Wort, Gort and Bort!", res);

        var readScoreFile = assertDoesNotThrow(() -> Files.readAllLines(Hangman.highScoreFile));
        assertTrue(readScoreFile.contains("Mort"+ Hangman.highScoreDelim + "6"));
    }

    @org.junit.jupiter.api.Test
    void saveGame(){

        String input = "B\na\nc\nn\nc\nr\ne\nm\np\nd\nq\nz\nx\ny\nh\ng\nu\nk\ns\ni\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        assertDoesNotThrow(  () -> Files.deleteIfExists(Hangman.savefile));
        hm.wordlist.clear();
        hm.wordlist.add("BANANACREAMPI");
        hm.secretWord =  hm.generateGuess(0);
        hm.saveGame();

        String reader ;
        reader = assertDoesNotThrow( () -> Files.readString(Hangman.savefile) );
        var split = reader.split("\n%\n");
        System.out.println("\n/************************************/\nSave File Testing: ");
        System.out.println(reader);

        var missingLetters = hm.missedLetters;
        var guessedLetters = hm.guessedLetters;
        var secretWord = hm.secretWord;

        assertTrue( hm.deserialize(reader));
        assertEquals(secretWord,hm.secretWord);
        assertEquals(secretWord, split[3]);
        assertEquals("Missed Letters: " + missingLetters.toString(), split[1]);
        var set = split[2].chars().mapToObj(c->(char)c).collect(Collectors.toSet());
        assertTrue(guessedLetters.containsAll(set));
        assertTrue(set.containsAll(guessedLetters));

        assertEquals(missingLetters.toString(), hm.missedLetters.toString());
        assertEquals(guessedLetters, hm.guessedLetters);

        assertEquals(hm.drawGallows(0), " " + split[0]+"\n");

        int count = input.length()/2;
        boolean result = true;
        while (count-- >0){
            result = hm.loop();
        }
        assertFalse(result);
        assertTrue( hm.win);
    }

    @org.junit.jupiter.api.Test
    void saveGame2() {
        String file = "+---+\n" +
                " |   |\n" +
                " O   |\n" +
                "/T\\  |\n" +
                " ^   |\n" +
                "/ \\  |\n" +
                "     |\n" +
                "=======\n" +
                "%\n" +
                "Missed Letters: DQZXYHGU\n" +
                "%\n" +
                "B,A,C,N,R,E,M,P\n" +
                "%\n" +
                "BANANACREAMPI";


        String input = "B\na\nc\nn\nc\nr\ne\nm\np\nd\nq\nz\nx\ny\nh\ng\nu\nk\ns\ni\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        assertDoesNotThrow(  () -> Files.deleteIfExists(Hangman.savefile));
        assertDoesNotThrow( () -> Files.writeString(Hangman.savefile, file));
        hm.wordlist.clear();
        hm.wordlist.add("BANANACREAMPI");
        hm.secretWord =  hm.generateGuess(0);
        int count = input.length()/2;
        boolean result = true;
        while (count-- >0){
            result = hm.loop();
        }
        assertFalse(result);
        assertTrue( hm.win);

    }
    @org.junit.jupiter.api.Test
    void saveGame3() {

        String input = "B\na\nc\nn\nx\nq\nr\ne\nm\np\nd\nq\nz\nx\ny\nh\ng\nu\nk\ns\ni\nh\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        hm = new Hangman();
        assertDoesNotThrow(  () -> Files.deleteIfExists(Hangman.savefile));
        hm.wordlist.clear();
        hm.wordlist.add("BANANACREAMPI");
        hm.secretWord =  hm.generateGuess(0);

        int count = input.length()/2;
        final int earlyStop = count -6;
        while (count-- > earlyStop){
            hm.loop();
        }

        String inputCont = "Larry\nr\ne\nm\np\ni\nn\n";
        ByteArrayInputStream inn = new ByteArrayInputStream(inputCont.getBytes());
        System.setIn(inn);
        hm = new Hangman();
        hm.play();

        assertTrue( hm.win);

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
        try {
            Files.deleteIfExists(Hangman.savefile);
            Files.deleteIfExists(Hangman.highScoreFile);
        } catch (IOException e) {
            System.out.println("setUp failure.");
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try {
            Files.deleteIfExists(Hangman.savefile);
            Files.deleteIfExists(Hangman.highScoreFile);
        } catch (IOException e) {
            System.out.println("setUp failure.");
            e.printStackTrace();
        }
    }
}