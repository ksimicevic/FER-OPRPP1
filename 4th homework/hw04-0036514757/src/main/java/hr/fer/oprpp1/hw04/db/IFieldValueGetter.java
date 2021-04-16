package hr.fer.oprpp1.hw04.db;

/**
 * Interface representing a field getter.
 */
public interface IFieldValueGetter {
    /**
     * For given record, returns the appropriate field.
     * @param record whose field this method will return
     * @return appropriate field
     */
    String get(StudentRecord record);
}
