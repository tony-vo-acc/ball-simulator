import java.awt.Color;

public class Ball {
  public double x, y;
  public double vx, vy;
  public double radius;
  public double mass;
  public Color color;

  public Ball(double x, double y, double vx, double vy, double radius, double mass, Color color) {
    this.x = x;
    this.y = y;
    this.vx = vx;
    this.vy = vy;
    this.radius = radius;
    this.mass = mass;
    this.color = color;
  }
}