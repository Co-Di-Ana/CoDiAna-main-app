package cz.edu.x3m.net;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class KnockServerConnection {

    private String address;
    private boolean addressAllowed;
    private String message;
    private boolean correctMessage;



    public void setInetAddressAllowed (String address, boolean addressAllowed) {
        this.address = address;
        this.addressAllowed = addressAllowed;
    }



    public String getAddress () {
        return address;
    }



    public boolean isAddressAllowed () {
        return addressAllowed;
    }



    public String getMessage () {
        return message;
    }



    public boolean isCorrectMessage () {
        return correctMessage;
    }



    public boolean isValid () {
        return addressAllowed && correctMessage;
    }



    public String getErrorDetails () {
        if (!isAddressAllowed ())
            return String.format ("Address '%s' is not allowed", address);

        if (!isCorrectMessage ())
            return String.format ("Recived message '%s' is incorrect", message);

        return null;
    }



    public void setClientMessage (String message, boolean correctMessage) {
        this.message = message;
        this.correctMessage = correctMessage;
    }
}
