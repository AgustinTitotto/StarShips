package Model;

import edu.austral.ingsis.starships.ui.ElementColliderType;

import java.io.Serializable;

public record Bullet(String id, Position position, double speed, double degrees, double size,
                     ElementColliderType colliderType) implements MovingEntity, Serializable {


    @Override
    public MovingEntity move(double posX, double posY) {
        return new Bullet(this.id, new Position(posX, posY), this.speed, this.degrees, this.size, this.colliderType);
    }

  //  newEntities.add(new Bullet(entity.id(), new Position(getPosition(entity.position().x(), calculateXCoefficient(entity.degrees()), entity.speed()), getPosition(entity.position().y(), calculateYCoefficient(entity.degrees()), entity.speed())), entity.speed(), entity.degrees(), entity.size(), entity.colliderType()));

}
