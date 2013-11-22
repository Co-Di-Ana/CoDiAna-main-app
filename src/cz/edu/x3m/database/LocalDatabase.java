package cz.edu.x3m.database;

import cz.edu.x3m.core.Config;
import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.data.PlagiarismCheckItem;
import cz.edu.x3m.database.data.QueueItem;
import cz.edu.x3m.database.data.SolutionCheckItem;
import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.grading.SolutionGradingResult;
import cz.edu.x3m.plagiarism.PlagiarismResult;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            String sql = String.format (
                    "SELECT * FROM %scodiana_queue ORDER BY priority",
                    Globals.getConfig ().getPrefix ());

            PreparedStatement statement = connection.prepareStatement (sql);
            ResultSet resultSet = statement.executeQuery ();
            List<QueueItem> result = new ArrayList<> ();

            while (resultSet.next ())
                result.add (new QueueItem (resultSet));

            return result;

        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public boolean deleteItem (QueueItem item) throws DatabaseException {
        try {
            String sql = String.format (
                    "DELETE FROM %scodiana_queue WHERE taskID = ? LIMIT 1",
                    Globals.getConfig ().getPrefix ());

            PreparedStatement statement = connection.prepareStatement (sql);
            statement.setInt (1, item.getId ());
            
            return statement.executeUpdate () == 1;
        } catch (Exception e) {
            throw new DatabaseException (e);
        }
    }



    @Override
    public SolutionCheckItem getSolutionCheckItem (int taskID, int relatedID) throws DatabaseException {
        throw new UnsupportedOperationException ("Not supported yet.");
    }



    @Override
    public PlagiarismCheckItem getPlagiarismCheckItem (int taskID, int relatedID) throws DatabaseException {
        throw new UnsupportedOperationException ("Not supported yet.");
    }



    @Override
    public boolean saveGradingResult (QueueItem item, SolutionGradingResult result) throws DatabaseException {
        throw new UnsupportedOperationException ("Not supported yet.");
    }



    @Override
    public boolean savePlagCheckResult (QueueItem item, PlagiarismResult result) throws DatabaseException {
        throw new UnsupportedOperationException ("Not supported yet.");
    }



    @Override
    public void setSettings (DatabaseSetting settings) throws DatabaseException {
        throw new UnsupportedOperationException ("Not supported yet.");
    }
}
