package Model;

import edu.austral.ingsis.starships.ui.ElementColliderType;

import java.io.Serializable;

public class Bullet implements MovingEntity, Serializable {

    private final String id;
    private final Position position;
    private final double speed;
    private final double degrees;
    private final double size;
    private final ElementColliderType colliderType;

    public Bullet(String id, Position position, double speed, double degrees, double size, ElementColliderType colliderType){
        this.id = id;
        this.position = position;
        this.speed = speed;
        this.degrees = degrees;
        this.size = size;
        this.colliderType = colliderType;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public double getSize() {
        return size;
    }

    @Override
    public double getDegrees() {
        return degrees;
    }

    @Override
    public ElementColliderType getColliderType() {
        return colliderType;
    }
}
