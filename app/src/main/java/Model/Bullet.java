package Model;

import edu.austral.ingsis.starships.ui.ElementColliderType;

import java.io.Serializable;

public record Bullet(String id, Position position, double speed, double degrees, double size,
                     ElementColliderType colliderType) implements MovingEntity, Serializable {

}
