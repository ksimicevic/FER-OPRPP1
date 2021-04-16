package hr.fer.oprpp1.hw04.db;

import java.util.*;

/**
 * Class used to format the output of query.
 */
public class RecordFormatter {

    /**
     * Takes a list of records, formats it and returns a list of string to be printed to the console.
     *
     * @param records to be printed to the console
     * @return list of formatted strings
     * @throws NullPointerException     if records is null
     * @throws IllegalArgumentException if records are empty
     */
    public static List<String> format(List<StudentRecord> records) {
        if (records == null) {
            throw new NullPointerException("Records are null");
        } else if (records.size() == 0) {
            throw new IllegalArgumentException("Records are empty");
        }

        List<String> formatList = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        int maxJmbagSize = records.stream()
                .map(StudentRecord::getJmbag)
                .mapToInt(String::length)
                .max().getAsInt() + 2;

        int maxLastNameSize = records.stream()
                .map(StudentRecord::getLastName)
                .mapToInt(String::length)
                .max().getAsInt() + 2;

        int maxFirstNameSize = records.stream()
                .map(StudentRecord::getFirstName)
                .mapToInt(String::length)
                .max().getAsInt() + 2;

        String outerLines = sb.append("+")
                .append("=".repeat(maxJmbagSize))
                .append("+")
                .append("=".repeat(maxLastNameSize))
                .append("+")
                .append("=".repeat(maxFirstNameSize))
                .append("+")
                .append("=".repeat(3))
                .append("+").toString();

        formatList.add(outerLines);

        for (StudentRecord record : records) {
            sb.setLength(0);

            sb.append("| ")
                    .append(record.getJmbag())
                    .append(" | ")
                    .append(record.getLastName())
                    .append(" ".repeat(maxLastNameSize - record.getLastName().length() - 1))
                    .append("| ")
                    .append(record.getFirstName())
                    .append(" ".repeat(maxFirstNameSize - record.getFirstName().length() - 1))
                    .append("| ")
                    .append(record.getFinalGrade())
                    .append(" |");

            formatList.add(sb.toString());
        }

        formatList.add(outerLines);

        return formatList;

    }
}
