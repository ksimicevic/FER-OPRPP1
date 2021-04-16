package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Here are tested all examples from extra.zip as well as the exercise example text.
 */
public class SmartScriptParserTest {

    @Test
    public void testExerciseExample() throws IOException {
        String path = "src\\test\\resources\\primjer0.txt";
        String docBody = Files.readString(Paths.get(path));

        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode nodeOriginal = parser.getDocumentNode();

        SmartScriptParser parser2 = new SmartScriptParser(nodeOriginal.toString());
        DocumentNode nodeCopy = parser2.getDocumentNode();

        assertEquals(nodeOriginal, nodeCopy);
    }

    @Test
    public void testExample1() throws IOException {
        String path = "src\\test\\resources\\primjer1.txt";
        String docBody = Files.readString(Paths.get(path));

        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode document = parser.getDocumentNode();

        assertTrue(document.getChild(0) instanceof TextNode);
        assertEquals(document.numberOfChildren(), 1);
    }

    @Test
    public void testExample2() throws IOException {
        String path = "src\\test\\resources\\primjer2.txt";
        String docBody = Files.readString(Paths.get(path));

        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode document = parser.getDocumentNode();

        assertTrue(document.getChild(0) instanceof TextNode);
        assertEquals(document.numberOfChildren(), 1);
    }

    @Test
    public void testExample3() throws IOException {
        String path = "src\\test\\resources\\primjer3.txt";
        String docBody = Files.readString(Paths.get(path));

        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode document = parser.getDocumentNode();

        assertTrue(document.getChild(0) instanceof TextNode);
        assertEquals(document.numberOfChildren(), 1);
    }

    @Test
    public void testExample4() throws IOException {
        String path = "src\\test\\resources\\primjer4.txt";
        String docBody = Files.readString(Paths.get(path));

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testExample5() throws IOException {
        String path = "src\\test\\resources\\primjer5.txt";
        String docBody = Files.readString(Paths.get(path));

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testExample6() throws IOException {
        String path = "src\\test\\resources\\primjer6.txt";
        String docBody = Files.readString(Paths.get(path));


        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode document = parser.getDocumentNode();

        assertTrue(document.getChild(0) instanceof TextNode);
        assertTrue(document.getChild(1) instanceof EchoNode);
        assertEquals(document.numberOfChildren(), 2);
    }

    @Test
    public void testExample7() throws IOException {
        String path = "src\\test\\resources\\primjer7.txt";
        String docBody = Files.readString(Paths.get(path));

        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode document = parser.getDocumentNode();

        assertTrue(document.getChild(0) instanceof TextNode);
        assertTrue(document.getChild(1) instanceof EchoNode);
        assertEquals(document.numberOfChildren(), 2);
    }

    @Test
    public void testExample8() throws IOException {
        String path = "src\\test\\resources\\primjer8.txt";
        String docBody = Files.readString(Paths.get(path));

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testExample9() throws IOException {
        String path = "src\\test\\resources\\primjer9.txt";
        String docBody = Files.readString(Paths.get(path));

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

}
