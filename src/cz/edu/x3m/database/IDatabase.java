package cz.edu.x3m.database;

import cz.edu.x3m.database.data.PlagiarismCheckItem;
import cz.edu.x3m.database.data.QueueItem;
import cz.edu.x3m.database.data.AttemptItem;
import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.grading.SolutionGradingResult;
import cz.edu.x3m.plagiarism.PlagiarismResult;
import java.util.List;

/**
 *
 * @author Jan Hybs
 */
public interface IDatabase {

    /**
     * Connects to Database
     *
     * @return true if connection was established or false if database is already connected
     * @throws DatabaseException on connect error
     */
    boolean connect () throws DatabaseException;



    /**
     * Closes Database connection
     *
     * @return true if connection was closed or false if database is already closed
     * @throws DatabaseException on close error
     */
    boolean close () throws DatabaseException;



    /**
     * Loads all queue items
     * Every item is join to particullar task instance
     *
     * @return list of all items
     * @throws DatabaseException on load error
     */
    List<QueueItem> getItems () throws DatabaseException;



    /**
     * Method deletes item from Database
     *
     * @param item to be deleted
     * @return true if deletion was successful otherwise false
     * @throws DatabaseException on error
     */
    boolean deleteItem (QueueItem item) throws DatabaseException;



    /**
     * Gets solution check detail for the given task id and related (student/teacher) id
     *
     * @param taskID codiana id
     * @param userID student/teacher id
     * @return detailed object
     * @throws DatabaseException on error
     */
    AttemptItem getSolutionCheckItem (int taskID, int userID) throws DatabaseException;



    /**
     * Gets plagiarism check detail for the given task id and related (student/teacher) id
     *
     * @param taskID codiana id
     * @param relatedID student/teacher id
     * @return detailed object
     * @throws DatabaseException on error
     */
    PlagiarismCheckItem getPlagiarismCheckItem (int taskID, int relatedID) throws DatabaseException;



    /**
     * Saves grading result to DB
     *
     * @param item to be updated
     * @param result grading result
     * @return true on success false on update
     * @throws DatabaseException on error
     */
    boolean saveGradingResult (QueueItem item, SolutionGradingResult result) throws DatabaseException;



    /**
     * Saves plagiarism result to DB
     *
     * @param item to be updated
     * @param result plagiarism result
     * @return true on success false on update
     * @throws DatabaseException on error
     */
    boolean savePlagCheckResult (QueueItem item, PlagiarismResult result) throws DatabaseException;



    /**
     * Sets Database setting such as username, password, etc.
     *
     * @param settings object containing all settings
     * @throws DatabaseException on empty settings
     */
    void setSettings (DatabaseSetting settings) throws DatabaseException;
}
