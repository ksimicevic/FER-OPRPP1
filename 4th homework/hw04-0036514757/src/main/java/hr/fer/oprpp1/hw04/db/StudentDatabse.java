package hr.fer.oprpp1.hw04.db;

import java.util.*;

/**
 * This class represents a database storing student records.
 */
public class StudentDatabse {
    HashMap<String, StudentRecord> indexedRecords;

    /**
     * For given list of Strings, constructor will attempt to parse the contents of each String to be able to create objects of class
     * StudentRecord. For each successfully parsed String, they will be added to the database.
     *
     * @param strings data to be parsed and added to database
     * @throws IllegalArgumentException if there is already a stored record with the same jmbag or grade isn't between 1 or 5 or the method
     *                                  simply failed to parse the given String
     */
    public StudentDatabse(List<String> strings) {
        this.indexedRecords = new HashMap<>();

        for (String s : strings) {
            String[] sArray = s.split("\\t");

            if (indexedRecords.containsKey(sArray[0])) {
                throw new IllegalArgumentException("There is already a student record with given jmbag");
            }

            try {
                int grade = Integer.parseInt(sArray[sArray.length - 1]);
                if (grade < 1 || grade > 5) {
                    throw new IllegalArgumentException("Grade needs to be between 1 and 5");
                }
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Expected a grade as 4th argument but didn't find it");
            }

            StudentRecord sr;
            if(sArray.length == 4) {
                sr = new StudentRecord(sArray[0], sArray[1], sArray[2], Integer.parseInt(sArray[3]));
            } else {
                throw new IllegalArgumentException("Couldn't parse student record");
            }
            indexedRecords.put(sArray[0], sr);
        }
    }

    /**
     * Returns StudentRecord for given jmbag.
     * @param jmbag of student whose record is to be returned
     * @return StudentRecord for given jmbag
     */
    public StudentRecord forJMBAG(String jmbag) {
        return indexedRecords.get(jmbag);
    }

    /**
     * Returns the list of StudentRecords that pass the filter.
     * @param filter object that will determine for each record will it be found in the filtered list or not
     * @return list of filtered StudentRecords
     * @throws NullPointerException if database is uninitialized or empty
     */
    public List<StudentRecord> filter(IFilter filter) {
        LinkedList<StudentRecord> filteredRecords = new LinkedList<>();

        if (this.indexedRecords == null) {
            throw new NullPointerException("Records haven't been initialized yet");
        }

        for (StudentRecord record : this.indexedRecords.values()) {
            if (filter.accepts(record)) {
                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }


}
