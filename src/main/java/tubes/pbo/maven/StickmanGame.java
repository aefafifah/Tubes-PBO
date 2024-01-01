package tubes.pbo.maven;
import Classes.ReminderFitur;
import tubes.pbo.maven.Classes.KonekDatabase;
import tubes.pbo.maven.Classes.KonekDatabase2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StickmanGame extends JFrame {
    private GamePanel gamePanel;

    private StickmanGame game;
    private boolean imanActive = false;

    private boolean healthTrack = false;


    public StickmanGame() {
        setTitle("Stickman Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        this.game = this;

        gamePanel = new GamePanel();
        add(gamePanel);
        imanActive = false;
        healthTrack = false;

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

        Timer timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.updateStickman();
                if (gamePanel.checkCollision()) {
                    ((Timer) e.getSource()).stop();
                    imanActive = true;
                    healthTrack = true;


                    JOptionPane.showMessageDialog(StickmanGame.this, "Game Over - Stickman Tersandung!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    int stressLevel = Integer.valueOf(JOptionPane.showInputDialog("Masukkan level stress (1-10)"));
                    int jarakTuhanLevel = Integer.valueOf(JOptionPane.showInputDialog("Masukkan level jauh dari tuhan (1-10)"));
                    boolean Ngantuk = JOptionPane.showConfirmDialog(null, "Apakah Anda ngantuk?") == JOptionPane.YES_OPTION;
                    boolean Capek = JOptionPane.showConfirmDialog(null, "Apakah Anda capek?") == JOptionPane.YES_OPTION;

                    KonekDatabase konekDatabase = new KonekDatabase();
                    try {
                        konekDatabase.getInput(stressLevel, jarakTuhanLevel);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    KonekDatabase2 konekDatabase2 = new KonekDatabase2();
                    try{
                        konekDatabase2.getInput2(Ngantuk,Capek);
                    } catch (SQLException exc){
                        throw new RuntimeException(exc);
                    }

                    if (stressLevel > 5 || jarakTuhanLevel > 5 || Ngantuk || Capek) {
                        JOptionPane.showMessageDialog(StickmanGame.this, "Kamu perlu beribadah atau istirahat terlebih dahulu.\nGame akan di-freeze selama 15 menit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        long freezeStartTime = System.currentTimeMillis();
                        Timer freezeTimer = new Timer(15 * 60 * 1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Unfreeze the game after 15 minutes
                                JOptionPane.showMessageDialog(StickmanGame.this, "Pastikan iman kamu selalu terjaga ya! Selamat bermain kembali!", "Peringatan", JOptionPane.INFORMATION_MESSAGE);
                                ((Timer) e.getSource()).start();
                                requestFocusInWindow();
                                setVisible(true);
                                setFocusable(true);
                            }

                        });

                        freezeTimer.start();
                        setFocusable(false);

                    } else {
                        imanActive = false;
                        healthTrack = false;
                        requestFocusInWindow();
                        setVisible(true);


                    }


                    gamePanel.restartGame();
                    ((Timer) e.getSource()).start();
                }


                gamePanel.repaint();
            }
        });
        timer.start();

        setVisible(true);


    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StickmanGame game = new StickmanGame();
            LoginForm loginForm = new LoginForm(game); // Gunakan instance game
            loginForm.setVisible(true); // Menampilkan form login terlebih dahulu

            // Contoh panggilan metode saat tombol "Masuk" atau "Register" diklik
            // ...
            // Kemudian, tutup form login dan mulai menjalankan game Stickman
            loginForm.dispose();
        });
    }

    class GamePanel extends JPanel {
        private int stickmanX = 50;
        private int stickmanY = 450;
        private int jumpHeight = 60;
        private int jumpCount = 0;
        private boolean isJumping = false;
        //    private JButton restartButton;
        private boolean gameOver = false;
        private int obstacleX = 300;
        private int obstacleY = 420;
        private boolean mahkotaDiperoleh = false;
        private ReminderFitur reminderFitur = new ReminderFitur();
        private Calendar calendar = Calendar.getInstance();
        private int crownX = 700;
        private int crownY = 150;
        private boolean hasCrown = false;
        private int[] starCoordinates = new int[100];
        private List<Awan> awanList = new ArrayList<>();
        private boolean isOnBlock = false;

        // Tambahkan List untuk menyimpan bounds blok biru
        private List<Rectangle> obstacleBoundsList = new ArrayList<>();

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

        public GamePanel() {
            setLayout(null);
            awanList.add(new Awan(100, 50));
            awanList.add(new Awan(300, 100));
            awanList.add(new Awan(500, 30));
            awanList.add(new Awan(200, 200));
            awanList.add(new Awan(100, 100));
            awanList.add(new Awan(20, 20));
            awanList.add(new Awan(400, 300));


            obstacleBoundsList.add(new Rectangle(100, 300, 50, 50));
            obstacleBoundsList.add(new Rectangle(500, 400, 50, 50));
            obstacleBoundsList.add(new Rectangle(700, 300, 50, 50));
            // Tambahkan blok biru tambahan jika diperlukan
            obstacleBoundsList.add(new Rectangle(100, 200, 50, 50));
            obstacleBoundsList.add(new Rectangle(30, 40, 50, 50));
            obstacleBoundsList.add(new Rectangle(60, 70, 50, 50));
            obstacleBoundsList.add(new Rectangle(170, 80, 50, 50));
            obstacleBoundsList.add(new Rectangle(600, 80, 50, 50));
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
                getMahkota();
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

        private void getMahkota() {
            hasCrown = true;
            JOptionPane.showMessageDialog(this, "Selamat! Stickman mendapatkan mahkota.", "Game Berhasil", JOptionPane.INFORMATION_MESSAGE);
            String title = JOptionPane.showInputDialog("Masukkan judul pengingat: ");
            int hour = Integer.parseInt(JOptionPane.showInputDialog("Masukkan jam pengingat: "));
            int minute = Integer.parseInt(JOptionPane.showInputDialog("Masukkan menit pengingat: "));
            reminderFitur.addReminder(title, hour, minute);
            //reminderFitur.checkReminders();
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            Runnable task = () -> reminderFitur.checkReminders();
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
            g.setColor(Color.BLUE);
            g.fillRect(obstacleX, obstacleY, 50, 50);
            // Tambahkan blok biru tambahan
            g.fillRect(200, 400, 50, 50);
            g.fillRect(100, 300, 50, 50);
            g.fillRect(500, 400, 50, 50);
            g.fillRect(700, 300, 50, 50);

            // Tambahkan lebih banyak blok sesuai kebutuhan
            g.fillRect(100, 200, 50, 50);
            g.fillRect(30, 40, 50, 50);
            g.fillRect(60, 70, 50, 50);
            g.fillRect(170, 80, 50, 50);
            g.fillRect(600, 80, 50, 50);

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

        public class Awan {
            private int x;
            private int y;

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
}



