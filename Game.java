import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener, MouseListener {
    Timer timer;
    int playerX = 250;
    int playerY = 450;
    int playerWidth = 50;
    int playerHeight = 50;

    ArrayList<Rectangle> enemies;
    int enemySpeed = 4;
    boolean gameOver = false;
    int score = 0;
    Rectangle retryButton;

    public Game() {
        enemies = new ArrayList<>();
        timer = new Timer(20, this);
        timer.start();
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        spawnEnemy();
    }

    public void spawnEnemy() {
        enemies.add(new Rectangle((int) (Math.random() * 500), 0, 50, 50));
    }

    public void resetGame() {
        enemies.clear();
        score = 0;
        playerX = 250;
        gameOver = false;
        timer.start();
        spawnEnemy();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 600);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 20, 30);

        g.setColor(Color.GREEN);
        g.fillRect(playerX, playerY, playerWidth, playerHeight);

        g.setColor(Color.RED);
        for (Rectangle enemy : enemies) {
            g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
        }

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over!", 200, 250);

            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Final Score: " + score, 220, 300);

            g.setColor(Color.YELLOW);
            retryButton = new Rectangle(225, 350, 150, 50);
            g.fillRect(retryButton.x, retryButton.y, retryButton.width, retryButton.height);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("RETRY", 265, 380);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            for (Rectangle enemy : enemies) {
                enemy.y += enemySpeed;
                if (enemy.y > 600) {
                    enemy.y = 0;
                    enemy.x = (int) (Math.random() * 500);
                    score += 10;
                }

                if (enemy.intersects(new Rectangle(playerX, playerY, playerWidth, playerHeight))) {
                    gameOver = true;
                    timer.stop();
                }
            }

            if (Math.random() < 0.02) {
                spawnEnemy();
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && playerX > 0) {
                playerX -= 10;
            }
            if (key == KeyEvent.VK_RIGHT && playerX < getWidth() - playerWidth) {
                playerX += 10;
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameOver && retryButton != null && retryButton.contains(e.getPoint())) {
            resetGame();
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("2D Dodge Game with Score & Retry");
        Game game = new Game();
        frame.add(game);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
