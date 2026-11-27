package GUI;

import javax.swing.JOptionPane;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            SharedData.init();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not load saved data:\n" + e.getMessage(),
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new LoginFrame();
    }
}
