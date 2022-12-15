package Model;


import edu.austral.ingsis.starships.ui.ElementColliderType;

import java.io.Serializable;

public class Asteroid implements MovingEntity, Serializable {

    private final String id;
    private final Position position;
    private final double speed;
    private final double degrees;
    private final double health;
    private final ElementColliderType colliderType;

    public Asteroid(String id, Position position, double speed, double degrees, double health, ElementColliderType colliderType){
        this.id = id;
        this.health = health;
        this.speed = speed;
        this.position = position;
        this.degrees = degrees;
        this.colliderType = colliderType;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public double speed() {
        return speed;
    }

    @Override
    public double size() {
        return health;
    }

    @Override
    public Position position() {
        return position;
    }

    @Override
    public double degrees() {
        return degrees;
    }

    @Override
    public ElementColliderType colliderType() {
        return colliderType;
    }
}
