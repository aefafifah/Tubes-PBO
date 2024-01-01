package tubes.pbo.maven;

import tubes.pbo.maven.Classes.KonekDatabase3;
import tubes.pbo.maven.Classes.ReminderFitur;
import tubes.pbo.maven.Classes.KonekDatabase;
import tubes.pbo.maven.Classes.KonekDatabase2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StickmanGame extends JFrame {
    private final GamePanel gamePanel;
    private int userId;

    public StickmanGame() {
        setTitle("Stickman Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        this.gamePanel = new GamePanel();
        add(gamePanel);


        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                gamePanel.moveStickman(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        setFocusable(true);

        Timer timer = new Timer(0, e -> {
            gamePanel.updateStickman();
            if (gamePanel.checkCollision()) {
                ((Timer) e.getSource()).stop();

                JOptionPane.showMessageDialog(
                        StickmanGame.this,
                        "Game Over - hayolo ko nabrak!",
                        "Game Over",
                        JOptionPane.INFORMATION_MESSAGE
                );

                handleGameEnd();
                gamePanel.restartGame();
                ((Timer) e.getSource()).start();
            }

            gamePanel.repaint();
        });

        timer.start();
        setVisible(true);
    }

    private void handleGameEnd() {
        int stressLevel = Integer.parseInt(JOptionPane.showInputDialog("Masukkan level stress (1-10)"));
        int jarakTuhanLevel = Integer.parseInt(JOptionPane.showInputDialog("Masukkan level jauh dari tuhan (1-10)"));
        boolean Ngantuk = JOptionPane.showConfirmDialog(null, "Apakah Anda ngantuk?") == JOptionPane.YES_OPTION;
        boolean Capek = JOptionPane.showConfirmDialog(null, "Apakah Anda capek?") == JOptionPane.YES_OPTION;

        KonekDatabase konekDatabase = new KonekDatabase();
        try {
            konekDatabase.getInput(userId,stressLevel, jarakTuhanLevel);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        KonekDatabase2 konekDatabase2 = new KonekDatabase2();
        try {
            konekDatabase2.getInput2(userId, Ngantuk, Capek);
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }

        if (stressLevel > 5 || jarakTuhanLevel > 5 || Ngantuk || Capek) {
            JOptionPane.showMessageDialog(
                    StickmanGame.this,
                    "Kamu perlu beribadah atau istirahat terlebih dahulu.\nGame akan di-freeze selama 15 menit.",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
            );

            Timer freezeTimer = new Timer(15 * 60 * 1000, e -> {
                JOptionPane.showMessageDialog(
                        StickmanGame.this,
                        "Pastikan iman kamu selalu terjaga ya! Selamat bermain kembali!",
                        "Peringatan",
                        JOptionPane.INFORMATION_MESSAGE
                );
                ((Timer) e.getSource()).start();
                requestFocusInWindow();
                setVisible(true);
                setFocusable(true);
            });

            freezeTimer.start();
            setFocusable(false);
        } else {
            requestFocusInWindow();
            setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            boolean isAkun = JOptionPane.showConfirmDialog(null, "Apakah sudah punya akun?") == JOptionPane.YES_OPTION;

            if (isAkun) {
                LoginForm loginForm = new LoginForm(new JFrame());
                loginForm.setVisible(true);
                int authenticatedUserId = loginForm.getAuthenticatedUserId();
                System.out.println("idmu adalah " + authenticatedUserId);
                StickmanGame game = new StickmanGame();
                game.setUserId(authenticatedUserId);
                game.gamePanel.setUserId(authenticatedUserId);
            } else {
                int registeredUserId = showRegistrationForm();
                if (registeredUserId != -1) {
                    StickmanGame game = new StickmanGame();
                    game.setUserId(registeredUserId);
                    game.gamePanel.setUserId(registeredUserId);
                } else {
                    System.out.println("Registration failed. Exiting...");
                }
            }
        });
    }

    private static int showRegistrationForm() {
        form registrationForm = new form();
        registrationForm.setContentPane(registrationForm.tpt);
        registrationForm.setTitle("REGISTRATION FORM....");
        registrationForm.setSize(600, 400);
        registrationForm.setVisible(true);
        registrationForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Wait until the registration form is closed
        registrationForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                int registeredUserId = registrationForm.getRegisteredUserId();
                if (registeredUserId != -1) {
                    StickmanGame game = new StickmanGame();
                    game.setUserId(registeredUserId);
                } else {
                    System.out.println("Registration failed. Exiting...");
                }
            }
        });

        return registrationForm.getRegisteredUserId();
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}

    class GamePanel extends JPanel {
        private int userId;
        private int stickmanX = 50;
        private int stickmanY = 450;
        private int jumpCount = 0;
        private boolean isJumping = false;
        //    private JButton restartButton;
        private boolean gameOver = false;
        private boolean mahkotaDiperoleh = false;
        private final ReminderFitur reminderFitur = new ReminderFitur();
        private final int crownX = 730;
        private final int crownY = 170;
        private boolean hasCrown = false;
        private final int[] starCoordinates = new int[100];
        private final List<Awan> awanList = new ArrayList<>();

        // Tambahkan List untuk menyimpan bounds blok biru
        private final List<Rectangle> obstacleBoundsList = new ArrayList<>();

        public void moveStickman(int keyCode) {
            if (keyCode == KeyEvent.VK_RIGHT) {
                stickmanX += 5;
            }
            if (keyCode == KeyEvent.VK_LEFT) {
                stickmanX -= 5;
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                stickmanY += 5;
            } else if (keyCode == KeyEvent.VK_SPACE && !isJumping) {
                jump();
            }
        }
        public void setUserId(int userId) {
            this.userId = userId;
        }

        public GamePanel() {
            setLayout(null);
            awanList.add(new Awan(100, 50));
            awanList.add(new Awan(300, 100));
            awanList.add(new Awan(500, 30));
            awanList.add(new Awan(200, 200));
            awanList.add(new Awan(100, 100));
            awanList.add(new Awan(20, 20));
            awanList.add(new Awan(400, 300));

            obstacleBoundsList.add(new Rectangle(150, 150, 50, 50));
            obstacleBoundsList.add(new Rectangle(50, 250, 50, 50));
            obstacleBoundsList.add(new Rectangle(50, 350, 50, 50));
            obstacleBoundsList.add(new Rectangle(150, 450, 50, 50));
            obstacleBoundsList.add(new Rectangle(50, 550, 50, 50));

            obstacleBoundsList.add(new Rectangle(150, 550, 50, 50));
            obstacleBoundsList.add(new Rectangle(200, 50, 50, 50));
            obstacleBoundsList.add(new Rectangle(350, 150, 50, 50));
            obstacleBoundsList.add(new Rectangle(450, 150, 50, 50));
            obstacleBoundsList.add(new Rectangle(500, 100, 50, 50));

            obstacleBoundsList.add(new Rectangle(250, 150, 50, 50));
            obstacleBoundsList.add(new Rectangle(160, 350, 50, 50));
            obstacleBoundsList.add(new Rectangle(260, 350, 50, 50));
            obstacleBoundsList.add(new Rectangle(350, 450, 50, 50));
            obstacleBoundsList.add(new Rectangle(440, 450, 50, 50));

            obstacleBoundsList.add(new Rectangle(350, 250, 50, 50));
            obstacleBoundsList.add(new Rectangle(300, 50, 50, 50));
            obstacleBoundsList.add(new Rectangle(560, 150, 50, 50));
            obstacleBoundsList.add(new Rectangle(380, 350, 50, 50));
            obstacleBoundsList.add(new Rectangle(480, 270, 50, 50));

            obstacleBoundsList.add(new Rectangle(490, 350, 50, 50));
            obstacleBoundsList.add(new Rectangle(595, 290, 50, 50));
            obstacleBoundsList.add(new Rectangle(560, 470, 50, 50));
            obstacleBoundsList.add(new Rectangle(610, 360, 50, 50));
            obstacleBoundsList.add(new Rectangle(695, 270, 50, 50));


        }

        //kondisiawal
        public void restartGame() {
            stickmanX = 50;
            stickmanY = 450;
            isJumping = false;
            jumpCount = 0;
            mahkotaDiperoleh = false;
            gameOver = false;
            repaint();
        }

        public void updateStickman() {
            // jika tidak di atas blok biru
            if (isJumping) {
                checkCollision();
                int jumpHeight = 60;
                if (jumpCount < jumpHeight) {
                    stickmanY -= 5;
                    jumpCount += 5;
                } else {
                    isJumping = false;
                }
            }

        }

        public boolean checkCollision() {
            if (stickmanY >= getHeight() - 30 || stickmanY <= 0) {
                resetGame();
                return true;

            }
            if (isCollidingWithObstacle()) {
                return true;
            }
            if (!mahkotaDiperoleh && isCollidingWithCrown()) {
                getMahkota(userId);
                mahkotaDiperoleh = true;
                return false;
            }

            return false;
        }

        public void jump() {
            if (!isJumping) {
                isJumping = true;
                jumpCount = 0;
            }
        }

        public void resetGame() {
            stickmanX = 50;
            stickmanY = 450;
            isJumping = false;
            jumpCount = 0;
            mahkotaDiperoleh = false;
            gameOver = false;
            hasCrown = false;  // tambahkan baris ini jika diperlukan
            // atur ulang posisi atau properti lainnya yang perlu direset
            repaint();  // panggil repaint untuk memperbarui tampilan setelah reset
        }

        private boolean isCollidingWithObstacle() {
            Rectangle stickmanBounds = new Rectangle(stickmanX, stickmanY, 30, 70);

            // Cek collision dengan setiap blok biru
            for (Rectangle obstacleBounds : obstacleBoundsList) {
                if (stickmanBounds.intersects(obstacleBounds)) {
                    // Stickman bersentuhan dengan blok biru, tertabrak
                    return true;  // Mengembalikan true untuk memicu game over
                }
            }
            return false;
        }

        private boolean isCollidingWithCrown() {
            Rectangle stickmanBounds = new Rectangle(stickmanX, stickmanY, 30, 70);
            Rectangle crownBounds = new Rectangle(crownX, crownY, 20, 10);


            return stickmanBounds.intersects(crownBounds);
        }

        private void getMahkota(int userId) {
            hasCrown = true;
            JOptionPane.showMessageDialog(this, "Selamat! Stickman mendapatkan mahkota.", "Game Berhasil", JOptionPane.INFORMATION_MESSAGE);
            String title = JOptionPane.showInputDialog("Masukkan judul pengingat: ");
            int hour = Integer.parseInt(JOptionPane.showInputDialog("Masukkan jam pengingat: "));
            int minute = Integer.parseInt(JOptionPane.showInputDialog("Masukkan menit pengingat: "));
            reminderFitur.addReminder(title, hour, minute);

            // Add this block to connect to the database and save the user ID
            KonekDatabase3 konekDatabase3 = new KonekDatabase3();
            try {
                konekDatabase3.saveReminder(userId, title, hour, minute);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            Runnable task = reminderFitur::checkReminders;
            executorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
            resetGame();
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(new Color(75, 0, 130));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            for (int i = 0; i < starCoordinates.length; i += 2) {
                int x = (int) (Math.random() * getWidth());
                int y = (int) (Math.random() * getHeight());
                g.fillOval(x, y, 3, 3);

                starCoordinates[i] = x;
                starCoordinates[i + 1] = y;
            }

            if (mahkotaDiperoleh) {
                g.setColor(Color.RED);
                g.drawString("Game Over - Stickman mendapatkan mahkota!", 300, 300);
                resetGame();
            }

            if (gameOver) {
                g.setColor(Color.RED);
                g.drawString("Game Over - Stickman tersandung!", 300, 300);
                repaint();
            }

            g.setColor(Color.YELLOW);
            g.fillOval(650, 50, 100, 100);

            g.setColor(Color.YELLOW);
            // Sinar atas (vertikal)
            g.setColor(Color.YELLOW);
            g.drawLine(700, 50, 700, 0);

            // Sinar bawah (vertikal)
            g.drawLine(700, 50, 700, 200);
            g.drawLine(680, 90, 860, 90); // Sinar kanan
            g.drawLine(680, 90, 580, 90); // Sinar kiri
            int tanda = 1;
            for (Rectangle obstacleBounds : obstacleBoundsList) {
                g.setColor(Color.BLUE);
                g.fillRect((int) obstacleBounds.getX(), (int) obstacleBounds.getY(), (int) obstacleBounds.getWidth(), (int) obstacleBounds.getHeight());

                // Gambar tanda di posisi objek

                g.setColor(Color.RED);
                g.drawString("Tanda " + tanda, (int) obstacleBounds.getX(), (int) obstacleBounds.getY());
                tanda++;
            }


            if (!hasCrown) {
                g.setColor(Color.RED);
//            g.fillOval(crownX, crownY, 20, 10);
                g.fillOval(crownX, crownY, 20, 10);
            }

            g.setColor(Color.BLACK);
            g.setColor(Color.RED);
            // g.fillOval(700, 150, 20, 10);

            g.setColor(new Color(29, 161, 242));

            g.fillOval(stickmanX + 10, stickmanY + 20, 20, 30);
            g.fillOval(stickmanX + 15, stickmanY + 10, 15, 15);
            g.setColor(Color.WHITE);
            g.fillOval(stickmanX + 18, stickmanY + 12, 4, 4);

            int[] sayapX = {stickmanX + 18, stickmanX + 10, stickmanX + 10};
            int[] sayapY = {stickmanY + 22, stickmanY + 27, stickmanY + 47};
            g.fillPolygon(sayapX, sayapY, 3);

            int[] ekorX = {stickmanX + 18, stickmanX + 23, stickmanX + 23};
            int[] ekorY = {stickmanY + 22, stickmanY + 27, stickmanY + 47};
            g.fillPolygon(ekorX, ekorY, 3);


            for (Awan awan : awanList) {
                awan.draw(g);

                if (mahkotaDiperoleh) {
                    g.setColor(Color.RED);
                    g.drawString("Game Over - Stickman mendapatkan mahkota!", 300, 300);
                }

                if (gameOver) {
                    g.setColor(Color.RED);
                    g.drawString("Game Over - Stickman tersandung!", 300, 300);

                    g.setColor(Color.RED);
                    // g.fillRect(lautBounds.x, lautBounds.y, lautBounds.width, lautBounds.height);
                    // g.fillRect(lautBounds.x, lautBounds.y, lautBounds.width, lautBounds.height);

                }
            }
        }

        public static class Awan {
            private final int x;
            private final int y;

            public Awan(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public void draw(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillOval(x, y, 30, 15);
                g.fillOval(x + 10, y - 5, 30, 15);
                g.fillOval(x + 20, y, 30, 15);
            }
        }
    }





