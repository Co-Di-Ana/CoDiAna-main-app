package cz.edu.x3m.logging;

import javax.swing.JOptionPane;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Alert {

    private Alert () {
    }



    public static void showInfo (String message, Object... args) {
        JOptionPane.showMessageDialog (
                null,
                String.format (message, args),
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }



    public static void showAlert (String message, Object... args) {
        JOptionPane.showMessageDialog (
                null,
                String.format (message, args),
                "Warning",
                JOptionPane.WARNING_MESSAGE);
    }



    public static void showError (String message, Object... args) {
        JOptionPane.showMessageDialog (
                null,
                String.format (message, args),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
