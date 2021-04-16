package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * This class represents a record for each student.
 */
public class StudentRecord {
    private String jmbag;
    private String lastName;
    private String firstName;
    private int finalGrade;

    public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.finalGrade = finalGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRecord that = (StudentRecord) o;
        return jmbag.equals(that.jmbag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jmbag);
    }

    public String getJmbag() {
        return jmbag;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    @Override
    public String toString() {
        return this.jmbag + " " + this.lastName + " " + this.firstName + " " + this.getFinalGrade();
    }
}
