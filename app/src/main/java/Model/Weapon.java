package Model;

import edu.austral.ingsis.starships.ui.ElementColliderType;

import java.io.Serializable;

public record Weapon(double damage, ElementColliderType type, double bulletSize) implements Serializable {


}
