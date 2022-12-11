package Model;

import edu.austral.ingsis.starships.ui.ElementColliderType;

import java.io.Serializable;

public class Weapon implements Serializable {


    private final double damage;
    private final ElementColliderType type;

    public Weapon(double damage, ElementColliderType fireRate){
        this.damage = damage;
        this.type = fireRate;
    }

    public double getDamage() {
        return damage;
    }

    public ElementColliderType getType() {
        return type;
    }
}
