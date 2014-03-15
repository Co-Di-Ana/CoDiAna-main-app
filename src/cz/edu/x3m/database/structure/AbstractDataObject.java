package cz.edu.x3m.database.structure;

import cz.edu.x3m.utils.DataProvider;
import java.sql.ResultSet;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class AbstractDataObject {

    protected final DataProvider provider;



    public AbstractDataObject (ResultSet row) {
        provider = new DataProvider (row);
    }
}
