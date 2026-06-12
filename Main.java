import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main extends JPanel implements MouseListener {
    private static final int WIDTH = 800;
    private static final int PANEL_WIDTH = 200;
    private static final int HEIGHT = 800;
    private static final int FPS = 60;
    private static final double DELTA = 1.0 / FPS;

    // tmp variable
    private static final double ballSpeed = 700;

    private final PhysicsManager physicsManager;
    private final Random rng = new Random();
    private Timer timer;

    private Ball selectedBall = null;

    public Main() {
        setPreferredSize(new Dimension(PANEL_WIDTH + WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addMouseListener(this);

        physicsManager = new PhysicsManager(WIDTH, HEIGHT, 1.0);

        /*
         * for (int i = 0; i < 6; i++) {
         * double radius = 50;
         * double x = radius + rng.nextDouble() * (WIDTH - 2 * radius);
         * double y = radius + rng.nextDouble() * (HEIGHT - 2 * radius);
         * spawnBall(x, y, radius, radius * radius * Math.PI, new Color(255, 255, 255),
         * ballSpeed, 100);
         * }
         */
        timer = new Timer(1000 / FPS, e -> {
            physicsManager.step(DELTA);
            repaint();
        });
        timer.start();
    }

    private void spawnBall(double x, double y, double radius, double mass, Color color, double speed, double health) {
        physicsManager.addBall(new Ball(
                x, y,
                Math.cos(rng.nextDouble() * 2 * Math.PI) * speed,
                Math.sin(rng.nextDouble() * 2 * Math.PI) * speed,
                radius,
                mass,
                health,
                color));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Ball b : physicsManager.getBalls()) {
            // draw the ball
            if (selectedBall == b) {
                g2d.setColor(new Color(125, 255, 173));
                g2d.fillOval((int) (b.x - b.radius), (int) (b.y - b.radius), (int) (b.radius * 2),
                        (int) (b.radius * 2));
            } else {
                g2d.setColor(b.color);
                g2d.fillOval((int) (b.x - b.radius), (int) (b.y - b.radius), (int) (b.radius * 2),
                        (int) (b.radius * 2));
            }

            // draw health
            String healthText = String.valueOf((int) Math.ceil(b.health));
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (int) b.x - fm.stringWidth(healthText) / 2;
            int textY = (int) b.y + fm.getAscent() / 2 - 2;
            g2d.drawString(healthText, textX, textY);
        }

        // border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(WIDTH, 0, WIDTH, HEIGHT);

        // stats panel
        g2d.setColor(Color.WHITE);
        g2d.fillRect(WIDTH, 0, PANEL_WIDTH, HEIGHT);

        g2d.setColor(new Color(60, 60, 60));
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("BALL STATS", WIDTH + 16, 30);
        g2d.drawLine(WIDTH + 10, 38, WIDTH + PANEL_WIDTH - 10, 38);

        if (selectedBall == null) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            g2d.drawString("No ball selected", WIDTH + 16, 60);
        } else {
            Ball b = selectedBall;

            if (b.health <= 0) {
                selectedBall = null;
                return;
            }

            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            double speed = b.getSpeed();
            double potentialDamage = b.getPotentialDamage();

            String[] labels = { "Health", "Speed", "Damage" };
            String[] values = {
                    String.format("%.1f / %.1f", b.health, b.maxHealth),
                    String.format("%.1f px/s", speed),
                    String.format("%.1f", potentialDamage),
            };

            int startY = 55;
            int lineHeight = 45;
            for (int i = 0; i < labels.length; i++) {
                int rowY = startY + i * lineHeight;

                g2d.setColor(Color.BLACK);
                g2d.drawString(labels[i] + ":", WIDTH + 16, rowY);

                // g2d.setColor(new Color(200, 200, 200));
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.drawString(values[i], WIDTH + 16, rowY + 16);
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));

                // g2d.setColor(new Color(35, 35, 35));
                g2d.drawLine(WIDTH + 10, rowY + 24, WIDTH + PANEL_WIDTH - 10, rowY + 24);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() < WIDTH) {
            Ball hit = null;
            for (Ball b : physicsManager.getBalls()) {
                double dx = e.getX() - b.x;
                double dy = e.getY() - b.y;
                if (Math.sqrt(dx * dx + dy * dy) <= b.radius) {
                    hit = b;
                    break;
                }
            }

            if (hit != null) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    selectedBall = hit;
                } else {
                    physicsManager.removeBall(hit);
                    selectedBall = null;
                }
            } else {
                double radius = 50;
                physicsManager.addBall(new Ball(e.getX(), e.getY(),
                        Math.sin(rng.nextDouble() * 2 * Math.PI) * ballSpeed,
                        Math.cos(rng.nextDouble() * 2 * Math.PI) * ballSpeed,
                        radius,
                        radius * radius * Math.PI,
                        100,
                        new Color(255, 255, 255)));
            }
        }
    }

    public void mouseListener(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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