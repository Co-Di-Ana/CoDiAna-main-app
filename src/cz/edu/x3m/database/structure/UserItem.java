package cz.edu.x3m.database.structure;

import java.sql.ResultSet;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class UserItem extends AbstractDataObject {

    private final String lastname, firstname;
    private final int id;



    public UserItem (ResultSet row) {
        super (row);

        id = provider.getInt ("id");
        lastname = provider.getString ("lastname");
        firstname = provider.getString ("firstname");
    }



    /**
     * @return the full name LASTNAME firstname
     */
    public String getFullname () {
        return String.format ("%s %s", lastname.toUpperCase (), firstname);
    }
}
