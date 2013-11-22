package cz.edu.x3m.core;

import cz.edu.x3m.database.exception.DatabaseException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Jan Hybs
 */
public class Config {

    private static final String CONFIG_PATH = "config.xml";
    private static final String TAG_LANGUAGES = "languages";
    private static final String TAG_ITEM = "item";
    private static final String TAG_EXTENSION = "extension";
    private static final String TAG_CLASSNAME = "classname";
    private static final String TAG_DATABASE = "database";
    //
    private static Config instance;
    private static Element rootNode;



    /**
     * @return the rootNode
     */
    public static Element getRootNode () {
        return rootNode;
    }
    private final Map<String, String> languages;
    //
    private final String server;
    private final String port;
    private final String name;
    private final String username;
    private final String password;
    private final String prefix;
    private final String type;
    private String sshUsername;
    private String sshPassword;



    private Config () throws JDOMException, IOException, ConfigException {
        SAXBuilder builder = new SAXBuilder ();
        Document document = (Document) builder.build (new File (Config.CONFIG_PATH));
        rootNode = document.getRootElement ();
        languages = new HashMap<> ();

        List<Element> tmp;
        Element elem;

        //# languages
        tmp = getItems (TAG_LANGUAGES);
        tmp = tmp.get (0).getChildren (TAG_ITEM);
        for (int i = 0; i < tmp.size (); i++)
            languages.put ((elem = tmp.get (i)).getChildText (TAG_EXTENSION), elem.getChildText (TAG_CLASSNAME));

        if (languages.isEmpty ())
            throw new ConfigException ("Config file doesn't contain any programming language!");


        //# DB details
        elem = getItems (TAG_DATABASE).get (0);
        server = getValue (elem, "dbserver");
        port = getValue (elem, "dbport");
        name = getValue (elem, "dbname");
        prefix = getValue (elem, "dbprefix");
        username = getValue (elem, "username");
        password = getValue (elem, "password", false);
        type = getValue (elem, "dbtype", false);

        //# ssh details
        tmp = getItems (elem, "ssh", false);
        if (!tmp.isEmpty ()) {
            elem = tmp.get (0);
            sshUsername = getValue (elem, "username");
            sshPassword = getValue (elem, "password", false);
        }


    }



    private List<Element> getItems (Element root, String name, boolean required) throws ConfigException {
        List<Element> result = root.getChildren (name);
        if (result.isEmpty () && required)
            throw new ConfigException ("Config file doesn't contain section '%s'", name);
        return result;
    }



    private String getValue (Element root, String name, boolean required) throws ConfigException {
        String value = root.getChildText (name);
        if (value == null || (value.isEmpty () && required))
            throw new ConfigException ("Value '%s' is required and cannot be empty", name);

        return value;
    }



    private List<Element> getItems (String name) throws ConfigException {
        return getItems (getRootNode (), name, true);
    }



    private String getValue (Element root, String name) throws ConfigException {
        return getValue (root, name, true);
    }



    public String getLanguageClassName (String languageExtension) {
        return getLanguages ().containsKey (languageExtension) ? getLanguages ().get (languageExtension) : null;
    }



    public static Config loadConfig () throws JDOMException, IOException, ConfigException {
        return instance = new Config ();
    }



    /**
     * @return the supported languages
     */
    public Map<String, String> getLanguages () {
        return languages;
    }



    /**
     * @return the database server
     */
    public String getServer () {
        return server;
    }



    /**
     * @return the database port
     */
    public String getPort () {
        return port;
    }



    /**
     * @return the database name
     */
    public String getName () {
        return name;
    }



    /**
     * @return the database username
     */
    public String getUsername () {
        return username;
    }



    /**
     * @return the database password
     */
    public String getPassword () {
        return password;
    }



    /**
     * @return the database prefix
     */
    public String getPrefix () {
        return prefix;
    }



    /**
     * @return the database type (local/remote)
     */
    public String getType () {
        return type;
    }



    /**
     * @return the sshUsername
     */
    public String getSSHUsername () {
        return sshUsername;
    }



    /**
     * @return the sshPassword
     */
    public String getSSHPassword () {
        return sshPassword;
    }
}
