package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StudentDatabaseTest {
    private static final IFilter filterTrue = r -> true;
    private static final IFilter filterFalse = r -> false;

    @Test
    public void testCreateDatabase() {
        List<String> records = new LinkedList<>(Arrays.asList(
                "0000000006	Cvrlje	Ivan	3",
                "0000000007	Čima	Sanjin	4",
                "0000000008	Ćurić	Marko	5",
                "0000000009	Dean	Nataša	2",
                "0000000010	Dokleja	Luka	3"
        ));

        StudentDatabse db = new StudentDatabse(records);
    }

    @Test
    public void testFilterTrue() {
        List<String> records = new LinkedList<>(Arrays.asList(
                "0000000006	Cvrlje	Ivan	3",
                "0000000007	Čima	Sanjin	4",
                "0000000008	Ćurić	Marko	5",
                "0000000009	Dean	Nataša	2",
                "0000000010	Dokleja	Luka	3"
        ));

        StudentDatabse db = new StudentDatabse(records);
        assertEquals(5, db.filter(filterTrue).size());
    }

    @Test
    public void testFilterFalse() {
        List<String> records = new LinkedList<>(Arrays.asList(
                "0000000006	Cvrlje	Ivan	3",
                "0000000007	Čima	Sanjin	4",
                "0000000008	Ćurić	Marko	5",
                "0000000009	Dean	Nataša	2",
                "0000000010	Dokleja	Luka	3"
        ));

        StudentDatabse db = new StudentDatabse(records);
        assertEquals(0, db.filter(filterFalse).size());
    }
}
