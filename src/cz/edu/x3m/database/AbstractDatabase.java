package cz.edu.x3m.database;

import cz.edu.x3m.database.exception.DatabaseException;

/**
 *
 * @author Jan Hybs
 */
abstract class AbstractDatabase implements IDatabase {

    protected DatabaseSetting settings;



    @Override
    public void setSettings (DatabaseSetting settings) throws DatabaseException {
        if (settings == null)
            throw new DatabaseException ("Settings cannot be null");

        this.settings = settings;
    }
}
