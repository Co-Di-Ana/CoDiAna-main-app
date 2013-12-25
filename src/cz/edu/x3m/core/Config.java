package cz.edu.x3m.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private static final String TAG_PHP_SERVER = "phpserver";
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
    private final String socketMessage;
    private final int socketKnockPort;
    private final List<String> allowedAddresses;
    private String sshUsername;
    private String sshPassword;
    private String dataDirectory;



    private Config () throws JDOMException, IOException, ConfigException {
        SAXBuilder builder = new SAXBuilder ();
        Document document = (Document) builder.build (new File (Config.CONFIG_PATH));
        rootNode = document.getRootElement ();
        languages = new HashMap<> ();

        List<Element> tmp;
        Element element;

        //# languages
        tmp = getItems (TAG_LANGUAGES);
        tmp = tmp.get (0).getChildren (TAG_ITEM);
        for (int i = 0; i < tmp.size (); i++)
            languages.put ((element = tmp.get (i)).getChildText (TAG_EXTENSION), element.getChildText (TAG_CLASSNAME));

        if (languages.isEmpty ())
            throw new ConfigException ("Config file doesn't contain any programming language!");

        //# php server socket setting
        element = getItems (TAG_PHP_SERVER).get (0);
        socketKnockPort = Integer.parseInt (getValue (element, "port"));
        socketMessage = getValue (element, "message");
        tmp = getItems (element, "allow-access-from", false);

        allowedAddresses = new ArrayList<> ();
        if (!tmp.isEmpty ()) {
            element = tmp.get (0);
            tmp = element.getChildren ("address");
            for (int i = 0; i < tmp.size (); i++)
                allowedAddresses.add (tmp.get (i).getText ());
        }

        //# DB details
        element = getItems (TAG_DATABASE).get (0);
        server = getValue (element, "dbserver");
        port = getValue (element, "dbport");
        name = getValue (element, "dbname");
        prefix = getValue (element, "dbprefix");
        username = getValue (element, "username");
        password = getValue (element, "password", false);
        type = getValue (element, "dbtype", false);

        //# ssh details
        tmp = getItems (element, "ssh", false);
        if (!tmp.isEmpty ()) {
            element = tmp.get (0);
            sshUsername = getValue (element, "username");
            sshPassword = getValue (element, "password", false);
        }

        //# data directory
        dataDirectory = getValue (rootNode, "data-directory", true);
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



    public static Config getInstance () throws JDOMException, IOException, ConfigException {
        return instance == null ? instance = new Config () : instance;
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



    public int getSocketKnockPort () {
        return socketKnockPort;
    }



    public List<String> getSocketAllowedAddresses () {
        return allowedAddresses;
    }



    public String getSocketMessage () {
        return socketMessage;
    }



    public String getDataDirectory () {
        return dataDirectory;
    }
}
