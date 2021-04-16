package hr.fer.oprpp1.hw04.db;

/**
 * Interface representing a filter. It has one method that will return either true or false for given StudentRecord.
 */
public interface IFilter {

    /**
     * If the filter accepts given record, this method will return true. Otherwise it'll return false.
     * @param record to be filtered
     * @return true if filter accepts the record
     */
    boolean accepts(StudentRecord record);
}
