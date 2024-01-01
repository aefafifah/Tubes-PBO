package tubes.pbo.maven.Classes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImanFitur {
    private JFrame frame;
    private JPanel panel;

    private static JTextField levelStressText;
    private static JTextField jauhDariTuhanText;

    public ImanFitur(SubmitButtonListener submitButtonListener) {
        frame = new JFrame("Iman Booster");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.add(panel);
        placeComponents(submitButtonListener);

        frame.setVisible(true);
    }

    private void placeComponents(SubmitButtonListener submitButtonListener) {
        // ... (rest of your method)

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(10, 140, 80, 25);
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ... (rest of your actionPerformed method)
                int levelStress = Integer.parseInt(levelStressText.getText());
                int jauhDariTuhan = Integer.parseInt(jauhDariTuhanText.getText());
                // Call the method from the provided SubmitButtonListener
                submitButtonListener.onSubmitButtonClicked(levelStress,  jauhDariTuhan);
            }
        });
    }


    public interface SubmitButtonListener {
        void onSubmitButtonClicked(int levelStress, int jauhDariTuhan);
    }
}
