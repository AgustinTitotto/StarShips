package Controller;

import Model.Ship;

public record ShipController(Ship ship) {

    public ShipController increaseSpeed() {
        if (ship.speed() < ship.getTopSpeed()) {
            return new ShipController(new Ship(ship.id(), ship.getTopSpeed(), ship.getAcceleration(), ship.speed() + ship.getAcceleration(), ship.getWeapon(), ship.getLives(), ship.position(), ship.degrees(), ship.size(), ship.colliderType(), ship.getPoints()));
        } else {
            return this;
        }
    }

    public ShipController stop() {
        if (ship.speed() > 0.0) {
            return new ShipController(new Ship(ship.id(), ship.getTopSpeed(), ship.getAcceleration(), 0, ship.getWeapon(), ship.getLives(), ship.position(), ship.degrees(), ship.size(), ship.colliderType(), ship.getPoints()));
        } else {
            return this;
        }
    }

    public ShipController changeDirection(double degrees) {
        return new ShipController(new Ship(ship.id(), ship.getTopSpeed(), ship.getAcceleration(), ship.speed(), ship.getWeapon(), ship.getLives(), ship.position(), ship.degrees() + degrees,  ship.size(), ship.colliderType(), ship.getPoints()));
    }
}
