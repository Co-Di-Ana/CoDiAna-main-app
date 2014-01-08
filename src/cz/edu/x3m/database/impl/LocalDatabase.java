package cz.edu.x3m.database.impl;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.AbstractDatabase;
import cz.edu.x3m.database.DatabaseSetting;
import cz.edu.x3m.database.data.PlagiarismCheckItem;
import cz.edu.x3m.database.data.QueueItem;
import cz.edu.x3m.database.data.AttemptItem;
import cz.edu.x3m.database.data.InvalidArgument;
import cz.edu.x3m.database.data.TaskItem;
import cz.edu.x3m.database.data.types.AttemptStateType;
import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.grading.ISolutionGradingResult;
import cz.edu.x3m.plagiarism.IPlagiarismResult;
import cz.edu.x3m.processing.execution.IExecutionResult;
import cz.edu.x3m.utils.Strings;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Jan Hybs
 */
public class LocalDatabase extends AbstractDatabase {

    private static final String DRIVER = "org.gjt.mm.mysql.Driver";
    private static final String URL = String.format ("jdbc:mysql://%s:%s/mysql", Globals.getConfig ().getServer (), Globals.getConfig ().getPort ());
    private static final String DATABASE = Globals.getConfig ().getName ();
    private static final String USERNAME = Globals.getConfig ().getUsername ();
    private static final String PASSWORD = Globals.getConfig ().getPassword ();
    private static final Charset utf8 = Charset.forName ("utf-8");
    private static boolean connected;
    private static Connection connection;
    private static Statement statement;



    @Override
    public boolean connect () throws DatabaseException {
        if (connected)
            return false;

        try {
            Class.forName (DRIVER);
            Properties info = new Properties ();
            info.put ("user", USERNAME);
            info.put ("password", PASSWORD);

            connection = DriverManager.getConnection (URL, info);
            statement = connection.createStatement ();
            statement.executeQuery (String.format ("USE `%s`", DATABASE));
            statement.executeQuery ("SET NAMES UTF8");
            connected = true;
            //statement.executeQuery("SET NAMES utf8 COLLATE utf8_czech_ci");
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            throw new DatabaseException (ex);
        }
    }



    @Override
    public boolean close () throws DatabaseException {
        if (!connected)
            return false;

        try {
            connection.close ();
            connected = false;
            return true;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public List<QueueItem> getItems () throws DatabaseException {
        try {
            String sql = Strings.createAndReplace (
                    "SELECT                                                 ",
                    "       Q.id,                                           ",
                    "       Q.taskid,                                       ",
                    "       Q.userid,                                       ",
                    "       Q.attemptid,                                    ",
                    "       Q.type, Q.priority,                             ",
                    "       C.name AS taskname,                             ",
                    "       C.mainfilename AS taskmainfilename,             ",
                    "       C.difficulty AS taskdifficulty,                 ",
                    "       C.outputmethod AS taskoutputmethod,             ",
                    "       C.grademethod AS taskgrademethod,               ",
                    "       C.languages AS tasklanguages,                   ",
                    "       C.limittimefalling AS tasklimittimefalling,     ",
                    "       C.limittimenothing AS tasklimittimenothing,     ",
                    "       C.limitmemoryfalling AS tasklimitmemoryfalling, ",
                    "       C.limitmemorynothing AS tasklimitmemorynothing  ",
                    "FROM                                                   ",
                    "       ::codiana_queue Q                               ",
                    "LEFT JOIN                                              ",
                    "       ::codiana C ON (                                ",
                    "    Q.taskid = C.id                                    ",
                    ")                                                      ",
                    "ORDER BY Q.priority DESC                               ");

            PreparedStatement statement = connection.prepareStatement (sql);
            ResultSet resultSet = statement.executeQuery ();
            List<QueueItem> result = new ArrayList<> ();

            while (resultSet.next ())
                result.add (new QueueItem (resultSet));

            return result;

        } catch (SQLException | DatabaseException | InvalidArgument e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean deleteQueueItem (QueueItem item) throws DatabaseException {
        try {
            String sql = Strings.createAndReplace (
                    "DELETE FROM                    ",
                    "       ::codiana_queue         ",
                    "WHERE                          ",
                    "       id = ?                  ",
                    "LIMIT 1                        ");

            PreparedStatement statement = connection.prepareStatement (sql);
            statement.setInt (1, item.getId ());

            return statement.executeUpdate () == 1;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean deleteAttemptItem (QueueItem item) throws DatabaseException {
        try {
            String sql = Strings.createAndReplace (
                    "DELETE FROM                    ",
                    "       ::codiana_attempt       ",
                    "WHERE                          ",
                    "       id = ?                  ",
                    "LIMIT 1                        ");

            PreparedStatement statement = connection.prepareStatement (sql);
            statement.setInt (1, item.getAttemptID ());

            return statement.executeUpdate () == 1;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public AttemptItem getSolutionCheckItem (int taskID, int userID) throws DatabaseException {
        try {
            String sql = Strings.createAndReplace (
                    "SELECT                         ",
                    "       A.id,                   ",
                    "       A.ordinal,              ",
                    "       A.state,                ",
                    "       A.language,             ",
                    "       A.detail,               ",
                    "       U.firstname,            ",
                    "       U.lastname              ",
                    "FROM                           ",
                    "       ::codiana_attempt A     ",
                    "LEFT JOIN                      ",
                    "       ::user U ON (           ",
                    "           U.id = A.userid     ",
                    ")                              ",
                    "WHERE (                        ",
                    "    A.taskid = ? AND           ",
                    "    A.userid = ?               ",
                    ")                              ",
                    "LIMIT 1                        ");

            PreparedStatement statement = connection.prepareStatement (sql);
            statement.setInt (1, taskID);
            statement.setInt (2, userID);
            ResultSet resultSet = statement.executeQuery ();

            resultSet.next ();
            return new AttemptItem (taskID, userID, resultSet);
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public PlagiarismCheckItem getPlagiarismCheckItem (int taskID, int relatedID) throws DatabaseException {
        throw new UnsupportedOperationException ("Not supported yet.");
    }



    @Override
    public boolean saveGradingResult (QueueItem item, ISolutionGradingResult result) throws DatabaseException {
        try {
            String sql = Strings.createAndReplace (
                    "UPDATE                                 ",
                    "       ::codiana_attempt               ",
                    "SET                                    ",
                    "    runtime = ?,                       ",
                    "    runoutput = ?,                     ",
                    //"    runmemory = ?,                   ",
                    "    resulttime = ?,                    ",
                    "    resultoutput = ?,                  ",
                    //"    resultmemory = ?,                ",
                    "    resultfinal = ?,                   ",
                    "    resultnote = ?,                    ",
                    "    state = ?                          ",
                    "                                       ",
                    "WHERE (                                ",
                    "    id = ?                             ",
                    ")                                      ",
                    "LIMIT 1                                ");

            PreparedStatement statement = connection.prepareStatement (sql);
            int i = 1;

            statement.setInt (i++, result.getTimeGradeResult ().getRunTime ());
            statement.setInt (i++, result.getOutputGradeResult ().getCorrectLines ());
            //statement.setInt (i++, result.getMemoryGradeResult ().getMemoryPeak ());

            statement.setInt (i++, result.getTimeGradeResult ().getPercent ());
            statement.setInt (i++, result.getOutputGradeResult ().getPercent ());
            //statement.setInt (i++, result.getMemoryGradeResult ().getPercent ());

            statement.setInt (i++, result.getPercent ());
            statement.setNull (i++, Types.VARCHAR);

            // correct result/state
            statement.setInt (i++, 1);

            statement.setInt (i++, item.getAttemptID ());

            return statement.executeUpdate () == 1;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean saveMeasurementResult (TaskItem item, IExecutionResult result) throws DatabaseException {
        try {
            String sql = Strings.createAndReplace (
                    "UPDATE                                 ",
                    "       ::codiana                       ",
                    "SET                                    ",
                    "    limittimefalling = ?,              ",
                    "    limittimenothing = ?,              ",
                    "    limitmemoryfalling = ?,            ",
                    "    limitmemorynothing = ?             ",
                    "                                       ",
                    "WHERE (                                ",
                    "    id = ?                             ",
                    ")                                      ",
                    "LIMIT 1                                ");

            PreparedStatement statement = connection.prepareStatement (sql);
            int i = 1;

            statement.setInt (i++, result.getRunTime ());
            statement.setInt (i++, result.getRunTime () * 2);
            statement.setInt (i++, result.getMemoryPeak ());
            statement.setInt (i++, result.getMemoryPeak () * 2);
            statement.setInt (i++, item.getID ());

            return statement.executeUpdate () == 1;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean savePlagCheckResult (QueueItem item, IPlagiarismResult result) throws DatabaseException {
        throw new UnsupportedOperationException ("Not supported yet.");
    }



    @Override
    public void setSettings (DatabaseSetting settings) throws DatabaseException {
        throw new UnsupportedOperationException ("Not supported yet.");
    }



    @Override
    public boolean saveGradingResult (QueueItem queueItem, AttemptStateType state, String details) throws DatabaseException {
        try {
            String sql = Strings.createAndReplace (
                    "UPDATE                                 ",
                    "       ::codiana_attempt               ",
                    "SET                                    ",
                    "    state = ?,                         ",
                    "    resultnote = ?                     ",
                    "                                       ",
                    "WHERE (                                ",
                    "    id = ?                             ",
                    ")                                      ",
                    "LIMIT 1                                ");

            PreparedStatement statement = connection.prepareStatement (sql);
            int i = 1;

            statement.setInt (i++, state.value ());
            if (details == null || details.isEmpty ())
                statement.setNull (i++, Types.VARCHAR);
            else
                statement.setString (i++, details);

            statement.setInt (i++, 1);
            statement.setInt (i++, queueItem.getAttemptID ());

            return statement.executeUpdate () == 1;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }
}
