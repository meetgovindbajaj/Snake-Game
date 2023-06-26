import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 *
 * @author govind
 */
public class GamePanel extends JPanel implements ActionListener {
    static final int screen_width = 600;
    static final int screen_height = 600;
    static final int unit_size = 15;
    static final int game_units = (screen_width * screen_height) / unit_size;
    final int[] x = new int[game_units];
    final int[] y = new int[game_units];
    int delay = 100;
    int bodyParts = 5;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    void startGame() {
        newFood();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    void draw(Graphics g) {
        if (running) {
            // for (int i = 0; i < screen_height / unit_size; i++) {
            // g.drawLine(i * unit_size, 0, i * unit_size, screen_height);
            // g.drawLine(0, i * unit_size, screen_width, i * unit_size);
            // }
            g.setColor(Color.YELLOW);
            g.fillOval(foodX, foodY, unit_size, unit_size);
            for (int i = 0; i < bodyParts; i++) {
                g.setColor(i == 0 ? Color.green
                        : new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                g.fillRect(x[i], y[i], unit_size, unit_size);
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + foodEaten, (screen_width - metrics.stringWidth("Score: " + foodEaten)) / 2, 30);
        } else {
            gameOver(g);
        }
    }

    void newFood() {
        foodX = random.nextInt((int) (screen_width / unit_size)) * unit_size;
        foodY = random.nextInt((int) (screen_height / unit_size)) * unit_size;
    }

    void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] -= unit_size;
                break;
            case 'D':
                y[0] += unit_size;
                break;
            case 'L':
                x[0] -= unit_size;
                break;
            case 'R':
                x[0] += unit_size;
                break;
        }
    }

    void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            bodyParts += 1;
            foodEaten += 1;
            newFood();
            if (delay > 12) {
                delay -= 2;
                timer.setDelay(delay);
            }
        }
    }

    void checkCollisions() {
        // check if head collides with left border || right border || top border ||
        // bottom border
        if (x[0] < 0 || x[0] > screen_width || y[0] < 0 || y[0] > screen_height)
            running = false;
        // check if head collides with body
        for (int i = bodyParts; i > 0; i--)
            if ((x[0] == x[i]) && (y[0] == y[i]))
                running = false;
        if (!running)
            timer.stop();
    }

    void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + foodEaten, (screen_width - metrics1.stringWidth("Score: " + foodEaten)) / 2, 30);
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screen_width - metrics.stringWidth("Game Over")) / 2, screen_height / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if (direction != 'R' && direction != 'L')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (direction != 'L' && direction != 'R')
                        direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if (direction != 'D' && direction != 'U')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (direction != 'U' && direction != 'D')
                        direction = 'D';
                    break;
            }
        }
    }

}
