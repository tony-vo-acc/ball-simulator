import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int FPS = 60;
    private static final double DELTA = 1.0 / FPS;

    private final PhysicsManager physicsManager;
    private final Random rng = new Random();
    private Timer timer;

    public Main() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        physicsManager = new PhysicsManager(WIDTH, HEIGHT, 1.0);

        for (int i = 0; i < 6; i++) {
            double radius = 25;
            double x = radius + rng.nextDouble() * (WIDTH - 2 * radius);
            double y = radius + rng.nextDouble() * (HEIGHT - 2 * radius);
            spawnBall(x, y, radius, radius * radius * Math.PI, new Color(255, 255, 255), 500.0);
        }
        timer = new Timer(1000 / FPS, e -> {
            physicsManager.step(DELTA);
            repaint();
        });
        timer.start();
    }

    private void spawnBall(double x, double y, double radius, double mass, Color color, double speed) {
        physicsManager.addBall(new Ball(
                x, y,
                Math.cos(rng.nextDouble() * 2 * Math.PI) * speed,
                Math.sin(rng.nextDouble() * 2 * Math.PI) * speed,
                radius,
                mass,
                color));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Ball b : physicsManager.getBalls()) {
            g2d.setColor(b.color);
            g2d.fillOval((int) (b.x - b.radius), (int) (b.y - b.radius), (int) (b.radius * 2), (int) (b.radius * 2));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ball simulation");
        Main sim = new Main();
        frame.add(sim);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}