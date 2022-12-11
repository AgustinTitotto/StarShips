package Model;

import edu.austral.ingsis.starships.ui.ElementColliderType;

public interface MovingEntity {

    String getId();
    Position getPosition();
    double getDegrees();
    double getSpeed();
    double getSize();
    ElementColliderType getColliderType();

}
