package Model;

import edu.austral.ingsis.starships.ui.ElementColliderType;

public interface MovingEntity {

    String id();
    Position position();
    double degrees();
    double speed();
    double size();
    ElementColliderType colliderType();

    /**/
    MovingEntity move(double posX, double posY);
}
