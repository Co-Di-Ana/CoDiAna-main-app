package cz.edu.x3m.database.impl;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.AbstractDatabase;
import cz.edu.x3m.database.DatabaseSetting;
import cz.edu.x3m.database.structure.PlagItem;
import cz.edu.x3m.database.structure.QueueItem;
import cz.edu.x3m.database.data.GradeMethod;
import static cz.edu.x3m.database.data.GradeMethod.BEST;
import static cz.edu.x3m.database.data.GradeMethod.FIRST;
import static cz.edu.x3m.database.data.GradeMethod.LAST;
import cz.edu.x3m.database.data.PlagsCheckStateType;
import cz.edu.x3m.database.exception.InvalidArgument;
import cz.edu.x3m.database.data.types.AttemptStateType;
import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.database.structure.AttemptItem;
import cz.edu.x3m.database.structure.TaskItem;
import cz.edu.x3m.database.structure.UserItem;
import cz.edu.x3m.grading.ISolutionGradingResult;
import cz.edu.x3m.plagiarism.IPlagResult;
import cz.edu.x3m.plagiarism.IPlagPair;
import cz.edu.x3m.processing.IRunEvaluation;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Jan Hybs
 */
public class LocalDatabase extends AbstractDatabase {

    private static final String DRIVER = "org.gjt.mm.mysql.Driver";
    private static final String DATABASE = Globals.getConfig ().getName ();
    private static final String URL = String.format ("jdbc:mysql://%s:%s/%s", Globals.getConfig ().getServer (), Globals.getConfig ().getPort (), DATABASE);
    private static final String USERNAME = Globals.getConfig ().getUsername ();
    private static final String PASSWORD = Globals.getConfig ().getPassword ();
    private static final Charset utf8 = Charset.forName ("utf-8");
    private static boolean connected;
    private static Connection connection;
    private static PreparedStatement statement;
    private static int statementIndex;
    //
    private static final String SAVE_GRADING_RESULT_FROM_EXEC = Strings.createAndReplace (
            "UPDATE                                 ",
            "       ::codiana_attempt               ",
            "SET                                    ",
            "    runtime = ?,                       ",
            "    runoutput = ?,                     ",
            "    runmemory = ?,                     ",
            //
            "    resulttime = ?,                    ",
            "    resultoutput = ?,                  ",
            "    resultmemory = ?,                  ",
            "    resultfinal = ?,                   ",
            //
            "    resultnote = ?,                    ",
            "    state = ?,                         ",
            "    ordinal = ?                        ",
            "WHERE (                                ",
            "    id = ?                             ",
            ")                                      ",
            "LIMIT 1                                ");
    private static final String SAVE_GRADING_RESULT_STATE = Strings.createAndReplace (
            "UPDATE                                 ",
            "       ::codiana_attempt               ",
            "SET                                    ",
            "    state = ?,                         ",
            "    resultnote = ?                     ",
            "WHERE (                                ",
            "    id = ?                             ",
            ")                                      ",
            "LIMIT 1                                ");
    private static final String SAVE_GRADING_RESULT_FROM_GRADING = Strings.createAndReplace (
            "UPDATE                                 ",
            "       ::codiana_attempt               ",
            "SET                                    ",
            "    runtime = ?,                       ",
            "    runoutput = ?,                     ",
            "    runmemory = ?,                     ",
            //
            "    resulttime = ?,                    ",
            "    resultoutput = ?,                  ",
            "    resultmemory = ?,                  ",
            "    resultfinal = ?,                   ",
            //
            "    resultnote = ?,                    ",
            "    state = ?                          ",
            "WHERE (                                ",
            "    id = ?                             ",
            ")                                      ",
            "LIMIT 1                                ");
    private static final String SAVE_PLAG_CHECK_RESULT = Strings.createAndReplace (
            "INSERT INTO                            ",
            "       ::codiana_plags                 ",
            "(                                      ",
            "    taskid,                            ",
            "    firstid,                           ",
            "    secondid,                          ",
            "    result,                            ",
            "    details                            ",
            ") VALUES                               ");
    private static final String SAVE_MEASUREMENT_RESULT = Strings.createAndReplace (
            "UPDATE                                 ",
            "       ::codiana                       ",
            "SET                                    ",
            "    limittimefalling = ?,              ",
            "    limittimenothing = ?,              ",
            "    limitmemoryfalling = ?,            ",
            "    limitmemorynothing = ?             ",
            "WHERE (                                ",
            "    id = ?                             ",
            ")                                      ",
            "LIMIT 1                                ");
    private static final String GET_SOLUTION_CHECK_ITEM = Strings.createAndReplace (
            "SELECT                         ",
            "       A.id,                   ",
            "       A.ordinal,              ",
            "       A.state,                ",
            "       A.language,             ",
            "       A.detail,               ",
            "       A.timesent,             ",
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
    private static final String DELETE_QUEUE_ITEM = Strings.createAndReplace (
            "DELETE FROM                    ",
            "       ::codiana_queue         ",
            "WHERE                          ",
            "       id = ?                  ",
            "LIMIT 1                        ");
    private static final String GET_ITEMS = Strings.createAndReplace (
            "SELECT                         ",
            "       Q.*                     ",
            "FROM                           ",
            "       ::codiana_queue Q       ",
            "ORDER BY Q.priority DESC       ");
    private static final String GET_USER_BY_ID = Strings.createAndReplace (
            "SELECT *                       ",
            "       FROM ::user             ",
            "WHERE (                        ",
            "       id = ?                  ",
            ") LIMIT 1                      ");
    private static final String GET_TASK_BY_ID = Strings.createAndReplace (
            "SELECT *                       ",
            "       FROM ::codiana          ",
            "WHERE (                        ",
            "       id = ?                  ",
            ") LIMIT 1                      ");
    private static final String GET_ATTEMPT_BY_ID = Strings.createAndReplace (
            "SELECT *                       ",
            "       FROM ::codiana_attempt  ",
            "WHERE (                        ",
            "       id = ?                  ",
            ") LIMIT 1                      ");
    private static final String SET_ATTEMPT_PLAG_STATE = Strings.createAndReplace (
            "UPDATE ::codiana_attempt         ",
            "       SET plagscheckstate = ? ",
            "WHERE (                        ",
            "       id = ?                  ",
            ") LIMIT 1                      ");
    private static final String SET_TASKS_PLAG_STATE = Strings.createAndReplace (
            "UPDATE ::codiana                 ",
            "       SET plagscheckstate = ? ",
            "WHERE (                        ",
            "       id = ?                  ",
            ") LIMIT 1                      ");
    private static final String GET_PLUGIN_CONFIG_LIMIT = Strings.createAndReplace (
            "SELECT *                       ",
            "       FROM ::config_plugins   ",
            "WHERE (                        ",
            "       plugin = 'codiana' AND  ",
            "       name LIKE '%limit%'     ",
            ")                              ");



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
            final Statement statement = connection.createStatement ();
            statement.executeQuery (String.format ("USE `%s`", DATABASE));
            statement.executeQuery ("SET NAMES UTF8");
            //statement.executeQuery("SET NAMES utf8 COLLATE utf8_czech_ci");
            connected = true;
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
    public List<QueueItem> getQueueItems () throws DatabaseException {
        try {
            createPreparedStatement (GET_ITEMS);

            ResultSet resultSet = statement.executeQuery ();
            List<QueueItem> result = new ArrayList<> ();

            while (resultSet.next ())
                result.add (new QueueItem (resultSet));

            return result;

        } catch (SQLException | InvalidArgument e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean deleteQueueItem (QueueItem item) throws DatabaseException {
        try {
            createPreparedStatement (DELETE_QUEUE_ITEM);

            statement.setInt (1, item.getId ());

            return statement.executeUpdate () == 1;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public AttemptItem getAttemptItem (int taskID, int userID) throws DatabaseException {
        try {
            createPreparedStatement (GET_SOLUTION_CHECK_ITEM);

            statement.setInt (1, taskID);
            statement.setInt (2, userID);
            ResultSet resultSet = statement.executeQuery ();

            resultSet.next ();
            return new AttemptItem (resultSet);
        } catch (SQLException | InvalidArgument e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public PlagItem getPlagItem (int taskID, int teacherID, GradeMethod gradeMethod) throws DatabaseException {

        final String sql = Strings.createAndReplace (
                "SELECT                         ",
                "       A.*                     ",
                "FROM                           ",
                "       ::codiana_attempt A     ",
                "WHERE (                        ",
                "    taskid = ? AND             ",
                "    ordinal != -1 AND          ",
                String.format ("state = %d", AttemptStateType.MEASUREMENT_OK.value ()),
                ")                              ",
                "GROUP BY A.userid             ",
                "ORDER BY                       ",
                getOrderQuery ("A", gradeMethod));

        try {
            createPreparedStatement (sql);

            statement.setInt (next (), taskID);

            ResultSet resultSet = statement.executeQuery ();
            return new PlagItem (taskID, teacherID, resultSet);

        } catch (SQLException | InvalidArgument e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean saveGradingResult (QueueItem item, ISolutionGradingResult result, AttemptStateType attemptStateType) throws DatabaseException {
        try {
            createPreparedStatement (SAVE_GRADING_RESULT_FROM_GRADING);

            statement.setInt (next (), result.getTimeGradeResult ().getRunTime ());
            statement.setInt (next (), result.getOutputGradeResult ().getCorrectLines ());
            statement.setInt (next (), result.getMemoryGradeResult ().getMemoryPeak ());

            statement.setInt (next (), result.getTimeGradeResult ().getPercent ());
            statement.setInt (next (), result.getOutputGradeResult ().getPercent ());
            statement.setInt (next (), result.getMemoryGradeResult ().getPercent ());
            statement.setInt (next (), result.getPercent ());

            statement.setNull (next (), Types.VARCHAR);
            statement.setInt (next (), attemptStateType.value ());

            statement.setInt (next (), item.getAttemptID ());

            return statement.executeUpdate () == 1;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean saveMeasurementResult (TaskItem item, IRunEvaluation result) throws DatabaseException {
        try {
            createPreparedStatement (SAVE_MEASUREMENT_RESULT);

            statement.setInt (next (), result.getRunTime ());
            statement.setInt (next (), result.getRunTime () * 2);
            statement.setInt (next (), result.getMemoryAvg ());
            statement.setInt (next (), result.getMemoryAvg () * 4);
            statement.setInt (next (), item.getID ());

            return statement.executeUpdate () == 1;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean savePlagCheckResult (QueueItem item, IPlagResult result) throws DatabaseException {
        try {
            final int size = result.getPlags ().size ();
            boolean insertResult = true;
            if (size > 0) {
                StringBuilder sqlBuilder = new StringBuilder (SAVE_PLAG_CHECK_RESULT);

                // creates sql query VALUES (?, ?, ?, ?, ?), (?, ?, ?, ?, ?), ... , (?, ?, ?, ?, ?); 
                for (int i = 0; i < size; i++)
                    sqlBuilder.append ("(?, ?, ?, ?, ?), ");
                sqlBuilder.setCharAt (sqlBuilder.length () - 2, ';');

                // prepare statements
                createPreparedStatement (sqlBuilder.toString ());

                // fill statements
                for (IPlagPair pair : result.getPlags ()) {
                    statement.setInt (next (), result.getQueueItem ().getTaskID ());
                    statement.setInt (next (), pair.getFirst ().getUserID ());
                    statement.setInt (next (), pair.getSecond ().getUserID ());
                    final int resultValue = (int) (pair.getDifference ().getIdenticalLikelihood () * 100);
                    statement.setInt (next (), resultValue);
                    statement.setNull (next (), Types.VARCHAR);
                }
                // execute
                insertResult = statement.executeUpdate () > 0;
            }

            if (result.isSimpleCheck ()) {
                createPreparedStatement (SET_ATTEMPT_PLAG_STATE);
                statement.setInt (next (), size == 0 ? PlagsCheckStateType.NO_PLAGS_FOUND.value () : PlagsCheckStateType.PLAGS_FOUND.value ());
                statement.setInt (next (), result.getQueueItem ().getAttemptID ());
                return insertResult && executeUpdate (statement);
            } else {
                createPreparedStatement (SET_TASKS_PLAG_STATE);
                statement.setInt (next (), size == 0 ? PlagsCheckStateType.NO_PLAGS_FOUND.value () : PlagsCheckStateType.PLAGS_FOUND.value ());
                statement.setInt (next (), result.getQueueItem ().getTaskID ());
                return insertResult && executeUpdate (statement);
            }

        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean saveGradingResult (QueueItem queueItem, AttemptStateType state, String details) throws DatabaseException {
        try {
            createPreparedStatement (SAVE_GRADING_RESULT_STATE);

            statement.setInt (next (), state.value ());
            if (details == null || details.isEmpty ())
                statement.setNull (next (), Types.VARCHAR);
            else
                statement.setString (next (), details);

            statement.setInt (next (), queueItem.getAttemptID ());

            return executeUpdate (statement);
        } catch (SQLException e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean saveGradingResult (AttemptItem item, IRunEvaluation result) throws DatabaseException {
        try {
            createPreparedStatement (SAVE_GRADING_RESULT_FROM_EXEC);

            statement.setInt (next (), result.getRunTime ());
            statement.setInt (next (), result.getLineCount ());
            statement.setInt (next (), result.getMemoryAvg ());

            statement.setInt (next (), 100);
            statement.setInt (next (), 100);
            statement.setInt (next (), 100);
            statement.setInt (next (), 100);

            statement.setString (next (), "Values measurement");
            statement.setInt (next (), AttemptStateType.MEASUREMENT_OK.value ());

            statement.setInt (next (), -1);
            statement.setInt (next (), item.getId ());

            return executeUpdate (statement);
        } catch (SQLException ex) {
            throw new DatabaseException (ex);
        }
    }



    private static boolean executeUpdate (PreparedStatement statement) throws DatabaseException {
        try {
            return statement.executeUpdate () == 1;
        } catch (SQLException ex) {
            throw new DatabaseException (ex);
        }
    }



    private static PreparedStatement createPreparedStatement (String sql) throws SQLException {
        statementIndex = 1;
        return statement = connection.prepareStatement (sql);
    }



    private static ResultSet executeQuery () throws SQLException {
        ResultSet set = statement.executeQuery ();
        set.next ();
        return set;
    }



    private static int next () {
        return statementIndex++;
    }



    private static String getOrderQuery (String id, GradeMethod gradeMethod) throws DatabaseException {
        switch (gradeMethod) {
            case FIRST:
                return String.format ("%s.timesent ASC", id);
            case LAST:
                return String.format ("%s.timesent DESC", id);
            case BEST:
                return String.format ("%s.resultfinal DESC", id);
        }
        throw new DatabaseException ("unknown gradeMethod %s", gradeMethod);
    }



    @Override
    public void setSettings (DatabaseSetting settings) throws DatabaseException {
        throw new UnsupportedOperationException ("Not supported yet.");
    }



    @Override
    public UserItem getUserItem (int id) throws DatabaseException {
        try {
            createPreparedStatement (GET_USER_BY_ID);
            statement.setInt (next (), id);
            return new UserItem (executeQuery ());
        } catch (SQLException ex) {
            throw new DatabaseException (ex);
        }
    }



    @Override
    public TaskItem getTaskItem (int id) throws DatabaseException {
        try {
            createPreparedStatement (GET_TASK_BY_ID);
            statement.setInt (next (), id);
            return new TaskItem (executeQuery ());
        } catch (SQLException | InvalidArgument ex) {
            throw new DatabaseException (ex);
        }
    }



    @Override
    public AttemptItem getAttemptItem (int id) throws DatabaseException {
        try {
            createPreparedStatement (GET_ATTEMPT_BY_ID);
            statement.setInt (next (), id);
            return new AttemptItem (executeQuery ());
        } catch (SQLException | InvalidArgument ex) {
            throw new DatabaseException (ex);
        }
    }



    @Override
    public Map<String, String> loadPluginConfig () throws DatabaseException {
        try {
            createPreparedStatement (GET_PLUGIN_CONFIG_LIMIT);

            ResultSet resultSet = statement.executeQuery ();
            Map<String, String> result = new HashMap<> ();

            while (resultSet.next ())
                result.put (resultSet.getString ("name"), resultSet.getString ("value"));

            return result;
        } catch (SQLException e) {
            throw new DatabaseException (e);
        }

    }
}
