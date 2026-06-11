import java.util.ArrayList;
import java.util.List;

public class PhysicsManager {
  private final int WIDTH;
  private final int HEIGHT;
  private final double MOMENTUM;
  private final List<Ball> balls = new ArrayList<>();

  public PhysicsManager(int width, int height, double momentum) {
    this.WIDTH = width;
    this.HEIGHT = height;
    this.MOMENTUM = momentum;
  }

  public void addBall(Ball ball) {
    balls.add(ball);
  }

  public List<Ball> getBalls() {
    return balls;
  }

  public void integrate(double delta) {
    for (Ball b : balls) {
      b.x += b.vx * delta;
      b.y += b.vy * delta;
    }
  }

  public void step(double delta) {
    integrate(delta);
    checkWalls();
    resolveBallCollisions();
  }

  private void checkWalls() {
    for (Ball b : balls) {
      if (b.x - b.radius < 0) {
        b.x = b.radius;
        b.vx = Math.abs(b.vx) * MOMENTUM;
      }

      if (b.x + b.radius > WIDTH) {
        b.x = WIDTH - b.radius;
        b.vx = -Math.abs(b.vx) * MOMENTUM;
      }

      if (b.y - b.radius < 0) {
        b.y = b.radius;
        b.vy = Math.abs(b.vy) * MOMENTUM;
      }

      if (b.y + b.radius > HEIGHT) {
        b.y = HEIGHT - b.radius;
        b.vy = -Math.abs(b.vy) * MOMENTUM;
      }
    }
  }

  private void resolveBallCollisions() {
    for (int i = 0; i < balls.size(); i++) {
      for (int x = i + 1; x < balls.size(); x++) {
        Ball b1 = balls.get(i);
        Ball b2 = balls.get(x);

        double dx = b1.x - b2.x;
        double dy = b1.y - b2.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double minDist = b1.radius + b2.radius;

        if (dist < minDist && dist > 0) {
          double overlap = minDist - dist;
          double nx = dx / dist;
          double ny = dy / dist;

          b1.x += nx * overlap;
          b1.y += ny * overlap;
          b2.x -= nx * overlap;
          b2.y -= ny * overlap;

          double relativeVelocityX = b1.vx - b2.vx;
          double relativeVelocityY = b1.vy - b2.vy;
          double velocityNormal = relativeVelocityX * nx + relativeVelocityY * ny;

          if (velocityNormal > 0) {
            double impulse = -(1 + MOMENTUM) * velocityNormal / (1.0 / b1.mass + 1.0 / b2.mass);
            b1.vx += impulse / b1.mass * nx;
            b1.vy += impulse / b1.mass * ny;
            b2.vx -= impulse / b2.mass * nx;
            b2.vy -= impulse / b2.mass * ny;
          }
        }
      }
    }
  }
}
