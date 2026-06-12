import java.awt.Color;

public class Ball {
  public double x, y;
  public double vx, vy;
  public double radius;
  public double mass;
  public Color color;

  public double maxHealth;
  public double health;
  public boolean isAlive;

  public Ball(double x, double y, double vx, double vy, double radius, double mass, double health, Color color) {
    this.x = x;
    this.y = y;
    this.vx = vx;
    this.vy = vy;
    this.radius = radius;
    this.mass = mass;
    this.maxHealth = health;
    this.health = health;
    this.isAlive = true;
    this.color = color;
  }

  public double getSpeed() {
    return Math.sqrt(vx * vx + vy * vy);
  }

  public double getPotentialDamage() {
    return getSpeed() / 300;
  }
}