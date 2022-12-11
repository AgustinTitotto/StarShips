package Controller;

import Model.Position;
import Model.Ship;
import Model.Weapon;
import edu.austral.ingsis.starships.ui.ElementColliderType;

public class Config {

    public static final double X_SIZE = 800.0;
    public static final double Y_SIZE = 800.0;

    public static final double X_MARGIN = 65.0;
    public static final double Y_MARGIN = 135.0;

    public static final double BULLET_SPEED_MARGIN = 3;
    public static final double NORMAL_BULLET_SIZE = 5.0;

    public static final double ASTEROID_SPAWN_CHANCE = 0.005;
    public static final double ASTEROID_MAX_SIZE = 100;
    public static final double ASTEROID_MIN_SIZE = 40;
    public static final double ASTEROID_MAX_SPEED = 0.8;
    public static final double ASTEROID_MIN_SPEED = 0.2;

    public static final double DEFAULT_TOP_SPEED = 0.8;
    public static final double DEFAULT_ACCELERATION = 0.2;
    public static final double DEFAULT_STARTING_SPEED = 0.0;
    public static final double DEFAULT_WEAPON_DAMAGE = 10.0;
    public static final int DEFAULT_LIVES = 3;
    public static final double DEFAULT_STARTING_X = 300.0;
    public static final double DEFAULT_STARTING_Y = 300.0;
    public static final double DEFAULT_STARTING_ROTATION = 90;
    public static final double DEFAULT_STARSHIP_SIZE = 40.0;
    public static final ElementColliderType DEFAULT_COLLIDER_TYPE = ElementColliderType.Triangular;
    public static final Ship DEFAULT_SHIP = new Ship("starship", DEFAULT_TOP_SPEED, DEFAULT_ACCELERATION, DEFAULT_STARTING_SPEED, new Weapon(DEFAULT_WEAPON_DAMAGE, ElementColliderType.Rectangular),
            DEFAULT_LIVES, new Position(DEFAULT_STARTING_X, DEFAULT_STARTING_Y), DEFAULT_STARTING_ROTATION, DEFAULT_STARSHIP_SIZE, DEFAULT_COLLIDER_TYPE, 0);

    public static double ROTATION_SPEED = 15;

}
