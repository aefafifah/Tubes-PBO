package tubes.pbo.maven.Classes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SehatFitur {
    private JFrame frame;
    private JPanel panel;

    private static JTextField tingkatNgantukText;
    private static JTextField tingkatCapekText;


    public SehatFitur(SubmitButtonListener submitButtonListener) {
        frame = new JFrame("Health Tracker");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.add(panel);
        placeComponents(submitButtonListener);

        frame.setVisible(true);
    }

    private void placeComponents(SehatFitur.SubmitButtonListener submitButtonListener) {
        // ... (rest of your method)

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(10, 140, 80, 25);
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ... (rest of your actionPerformed method)
                int tingkatCapek = Integer.parseInt(tingkatCapekText.getText());
                int tingkatNgantuk = Integer.parseInt(tingkatNgantukText.getText());

                // Mengganti tingkat ngantuk dan tingkat capek menjadi label 'ya' atau 'tidak'
                boolean ngantukLabel = tingkatNgantuk > 0 ;
                boolean capekLabel = tingkatCapek > 0 ;

                // Call the method from the provided SubmitButtonListener
                submitButtonListener.onSubmitButtonClicked(ngantukLabel ? 1 : 0, capekLabel ? 1 : 0);

            }
        });
    }

    // Interface untuk listener
    public interface SubmitButtonListener {
        void onSubmitButtonClicked(int tingkatNgantuk, int tingkatCapek);
    }


}
