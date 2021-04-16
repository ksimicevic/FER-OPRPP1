package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Main program.
 */
public class StudentDB {

    public static void main (String... args) throws IOException {
        Scanner sc = new Scanner(System.in);
        List<String> lines = Files.readAllLines(
                Paths.get("src/resources/database.txt"),
                StandardCharsets.UTF_8
        );
        StudentDatabse db = new StudentDatabse(lines);

        while(true) {
            String query = sc.nextLine();

            if(query.toLowerCase().equals("exit")) {
                System.out.println("Goodbye!");
                System.exit(0);
            }

            QueryParser parser = new QueryParser(query);
            List<String> results;

            if(parser.isDirectQuery()) {
                if(db.forJMBAG(parser.getQueriedJMBAG()) != null) {
                    System.out.println("Using index for record retrieval.");
                    results = RecordFormatter.format(new LinkedList<>(Collections.singletonList(db.forJMBAG(parser.getQueriedJMBAG()))));
                    results.forEach(System.out::println);
                    System.out.println("Records selected: 1");
                } else {
                    System.out.println("Records selected: 0");
                }
            } else {
                List<StudentRecord> queryResult = db.filter(new QueryFilter(parser.getQuery()));

                if(queryResult.size() > 0) {
                    results = RecordFormatter.format(queryResult);
                    results.forEach(System.out::println);
                }
                System.out.println("Records selected: " + queryResult.size());
            }

        }

    }
}
