package Model;

import edu.austral.ingsis.starships.ui.ElementColliderType;

import java.io.Serializable;

public class Ship implements MovingEntity, Serializable {

    private final String id;
    private final double topSpeed;
    private final double acceleration;
    private final double currentSpeed;
    private final Weapon weapon;
    private final int lives;
    private final Position position;
    private final double degrees;
    private final double size;
    private final ElementColliderType colliderType;
    private final double points;

    public Ship(String id, double topSpeed, double acceleration, double currentSpeed, Weapon weapon, int lives, Position position, double degrees, double size, ElementColliderType colliderType, double points){
        this.id = id;
        this.topSpeed = topSpeed;
        this.acceleration = acceleration;
        this.currentSpeed = currentSpeed;
        this.weapon = weapon;
        this.lives = lives;
        this.position = position;
        this.degrees = degrees;
        this.size = size;
        this.colliderType = colliderType;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getSpeed() {
        return currentSpeed;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getLives() {
        return lives;
    }

    public Position getPosition() {
        return position;
    }

    public double getDegrees() {
        return degrees;
    }

    public double getSize() {
        return size;
    }

    public ElementColliderType getColliderType() {
        return colliderType;
    }

    public double getPoints() {
        return points;
    }
}
