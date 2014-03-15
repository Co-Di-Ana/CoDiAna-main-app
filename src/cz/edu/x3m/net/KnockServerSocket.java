package cz.edu.x3m.net;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.logging.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class KnockServerSocket implements Runnable {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private static KnockServerSocket instance;
    private KnockServerConnection connection;



    public static KnockServerSocket getInstance () throws IOException {
        return instance == null ? instance = new KnockServerSocket () : instance;
    }



    private KnockServerSocket () throws IOException {
        serverSocket = new ServerSocket (Globals.getConfig ().getSocketKnockPort ());
    }



    public KnockServerConnection getConnection () {
        return connection;
    }



    private void listen () throws KnockSocketException {
        try {
            // waiting to connection
            clientSocket = serverSocket.accept ();

            // check for address, wheter is in white list
            String address = clientSocket.getInetAddress ().getHostAddress ();
            List<String> addresses = Globals.getConfig ().getSocketAllowedAddresses ();
            boolean notAllowed = addresses == null || addresses.indexOf (address) == -1;
            connection.setInetAddressAllowed (address, !notAllowed);

            if (!connection.isAddressAllowed ()) {
                clientSocket.close ();
                return;
            }

            //PrintWriter out = new PrintWriter (clientSocket.getOutputStream (), true);
            BufferedReader in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ()));
            String message = in.readLine ();
            connection.setClientMessage (message, message.indexOf (Globals.getConfig ().getSocketMessage ()) == 0);

            if (!connection.isCorrectMessage ()) {
                clientSocket.close ();
                return;
            }

            clientSocket.close ();
            return;

        } catch (Exception e) {
            throw new KnockSocketException (e);
        }
    }



    @Override
    public void run () {
        connection = new KnockServerConnection ();
        try {
            listen ();
        } catch (KnockSocketException ex) {
            Log.err (ex);
        }
    }
}
