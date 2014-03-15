package cz.edu.x3m.core;

import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.logging.Log;
import cz.edu.x3m.utils.DataProvider;
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
    private static final String TAG_DATABASE = "database";
    private static final String TAG_PHP_SERVER = "phpserver";
    //
    private static Config instance;
    private static Element rootNode;
    private static final int LIMIT_TIME = 60;
    private static final int LIMIT_MEMORY = 500 * 1000;



    /**
     * @return the rootNode
     */
    public static Element getRootNode () {
        return rootNode;
    }
    private final Map<String, String> langSupportClassname;
    private final Map<String, String> langPlagsClassname;
    private final Map<String, String> langExtension;
    //
    private final String server;
    private final String port;
    private final String name;
    private final String username;
    private final String password;
    private final String prefix;
    private final String type;
    private final String socketMessage;
    private final String runScript;
    private final String dataDirectory;
    private String sshUsername;
    private String sshPassword;
    //
    private final int socketKnockPort;
    private final List<String> allowedAddresses;
    private final Map<String, String> pluginConfig = new HashMap<> ();
    private boolean pluginConfigLoaded;



    private Config () throws JDOMException, IOException, ConfigException {
        SAXBuilder builder = new SAXBuilder ();
        Document document = (Document) builder.build (new File (Config.CONFIG_PATH));
        rootNode = document.getRootElement ();
        langSupportClassname = new HashMap<> ();
        langPlagsClassname = new HashMap<> ();
        langExtension = new HashMap<> ();

        List<Element> tmp;
        Element element;

        //# languages
        tmp = getItems (TAG_LANGUAGES);
        tmp = tmp.get (0).getChildren (TAG_ITEM);
        for (int i = 0; i < tmp.size (); i++) {
            element = tmp.get (i);
            langSupportClassname.put (element.getChildText ("name"), element.getChildText ("supportLib"));
            langPlagsClassname.put (element.getChildText ("name"), element.getChildText ("plagsLib"));
            langExtension.put (element.getChildText ("name"), element.getChildText ("extension"));
        }

        if (langSupportClassname.isEmpty ())
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

        //# data directory and run script location
        dataDirectory = getValue (rootNode, "data-directory", true);
        runScript = getValue (rootNode, "run-script", true);
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



    public String getLanguageSupportClassname (String languageName) {
        return getLangSupportClassname ().containsKey (languageName) ? getLangSupportClassname ().get (languageName) : null;
    }



    public String getLanguagePlagsClassname (String languageName) {
        return getLangPlagsClassname ().containsKey (languageName) ? getLangPlagsClassname ().get (languageName) : null;
    }



    public static Config getInstance () throws JDOMException, IOException, ConfigException {
        return instance == null ? instance = new Config () : instance;
    }



    /**
     * @return map where key is language name (Java6, Java7) and value is classname of support library
     */
    public Map<String, String> getLangSupportClassname () {
        return langSupportClassname;
    }



    /**
     * @return map where key is language name (Java6, Java7) and value is classname of plags library
     */
    public Map<String, String> getLangPlagsClassname () {
        return langPlagsClassname;
    }



    /**
     * @return map where key is language name (Java6, Java7) and value its extension (java)
     */
    public Map<String, String> getLangExtension () {
        return langExtension;
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



    public String getRunScript () {
        return runScript;
    }



    public int getLimitTime () {
        if (!pluginConfigLoaded)
            loadPluginConfig ();
        String value = pluginConfig.get ("limittime");
        return value == null || value.isEmpty () ? LIMIT_TIME : Integer.parseInt (value);
    }



    public int getLimitMemory () {
        if (!pluginConfigLoaded)
            loadPluginConfig ();
        String value = pluginConfig.get ("limitmemory");
        return value == null || value.isEmpty () ? LIMIT_MEMORY : Integer.parseInt (value);
    }



    private void loadPluginConfig () {
        try {
            Map<String, String> config = Globals.getDatabase ().loadPluginConfig ();
            pluginConfig.clear ();
            pluginConfig.putAll (config);
            pluginConfigLoaded = true;
        } catch (DatabaseException e) {
            Log.err (e);
        }
    }



    public void clearCache () {
        DataProvider.clearCache ();
        pluginConfigLoaded = false;
        pluginConfig.clear ();
        System.gc ();
    }
}
