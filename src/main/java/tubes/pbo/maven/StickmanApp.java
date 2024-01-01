import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StickmanApp extends JFrame {

    private int healthTracker = 100;
    private int imanBooster = 100;
    private boolean isGameRunning = false;
    private boolean hasCrown = false;

    public StickmanApp() {
        setTitle("Stickman Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JButton startButton = new JButton("Mulai Game");
        startButton.setBounds(150, 100, 100, 30);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);

        setVisible(true);
    }

    private void startGame() {
        isGameRunning = true;
        JOptionPane.showMessageDialog(this, "Game dimulai! Stickman berjalan...", "Info Game", JOptionPane.INFORMATION_MESSAGE);

        while (isGameRunning) {
            // Simulasi pergerakan stickman

            // Simulasi kondisi tertabrak
            if (Math.random() < 0.05) {
                stickmanTersandung();
            }

            // Simulasi mendapatkan mahkota
            if (Math.random() < 0.02 && !hasCrown) {
                getMahkota();
            }

            // Simulasi pengingat jadwal
            checkJadwal();
        }
    }

    private void stickmanTersandung() {
        isGameRunning = false;
        JOptionPane.showMessageDialog(this, "Stickman tersandung! Stickman mati.", "Game Over", JOptionPane.WARNING_MESSAGE);

        if (healthTracker < 50 || imanBooster < 50) {
            JOptionPane.showMessageDialog(this, "Silahkan makan atau beristirahat terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
            disableGameFor15Minutes();
        } else {
            // Jika kondisi kesehatan dan iman baik, user dapat melanjutkan permainan
            JOptionPane.showMessageDialog(this, "Stickman akan melanjutkan perjalanan setelah istirahat sejenak.", "Info", JOptionPane.INFORMATION_MESSAGE);
            // Melanjutkan permainan setelah beberapa saat istirahat
            isGameRunning = true;
        }
    }

    private void disableGameFor15Minutes() {
        isGameRunning = false;
        Timer timer = new Timer(900000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isGameRunning = true;
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void getMahkota() {
        hasCrown = true;
        JOptionPane.showMessageDialog(this, "Selamat! Stickman mendapatkan mahkota.", "Game Over - Menang", JOptionPane.INFORMATION_MESSAGE);
        enableScheduleReminder();
    }

    private void enableScheduleReminder() {
        Timer scheduleTimer = new Timer(60000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String schedule = JOptionPane.showInputDialog("Masukkan jadwal Anda (hh:mm):");
                if (!schedule.isEmpty()) {
                    JOptionPane.showMessageDialog(StickmanApp.this, "Waktu sekarang untuk " + schedule, "Jadwal", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        scheduleTimer.start();
    }

    private void checkJadwal() {
        String schedule = JOptionPane.showInputDialog("Masukkan jadwal Anda (hh:mm):");
        if (!schedule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Waktu sekarang untuk " + schedule, "Jadwal", JOptionPane.INFORMATION_MESSAGE);
            isGameRunning = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StickmanApp());
    }
}
