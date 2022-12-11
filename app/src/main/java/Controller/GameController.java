package Controller;

import Model.*;
import edu.austral.ingsis.starships.ui.ElementColliderType;
import edu.austral.ingsis.starships.ui.ElementModel;
import javafx.collections.ObservableMap;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static Controller.Config.*;


public record GameController(GameState gameState) {

    public GameController moveElements() {
        if (!this.gameState.gamePaused()){
            Ship ship = this.gameState().shipController().ship();
            generateAsteroids();
            List<MovingEntity> entities = moveEntities(this.gameState.entities());
            List<MovingEntity> entitiesToRemove = entitiesToRemove(entities);
            return moveShip(ship, entities, entitiesToRemove);
        }
        else return new GameController(this.gameState);
    }

    private void generateAsteroids() {
        if (Math.random() < ASTEROID_SPAWN_CHANCE) {
            Position asteriodSpawnPosition = asteroidSpawnPosition();
            double asteroidSize = Math.random() * (ASTEROID_MAX_SIZE - ASTEROID_MIN_SIZE) + ASTEROID_MIN_SIZE;
            this.gameState.entities().add(new Asteroid("asteroid" + UUID.randomUUID(),
                    asteriodSpawnPosition, setAsteroidSpeed(asteroidSize), getDegreesFromSpawningPosition(asteriodSpawnPosition), asteroidSize, ElementColliderType.Elliptical));
        }
    }

    private double setAsteroidSpeed(double asteroidSize) {
        double a = ((ASTEROID_MIN_SPEED - ASTEROID_MAX_SPEED) / (ASTEROID_MAX_SIZE - ASTEROID_MIN_SIZE));
        return a * asteroidSize + (ASTEROID_MAX_SPEED - ASTEROID_MIN_SIZE * a);
    }

    private double getDegreesFromSpawningPosition(Position asteriodSpawnPosition) {
        if (asteriodSpawnPosition.getX() == 0)
            return Math.random() * 179.9 + 0.01;
        else if (asteriodSpawnPosition.getY() == Y_SIZE - Y_MARGIN)
            return Math.random() * 179.9 + 90.01;
        else if (asteriodSpawnPosition.getX() == X_SIZE - X_MARGIN)
            return Math.random() * 179.9 + 180.01;
        else
            return Math.random() * 179.9 + 270.01;
    }

    private Position asteroidSpawnPosition() {
        double randomNumber = Math.random();
        if (randomNumber < 0.25)
            return new Position(0, Y_SIZE * Math.random());
        else if (randomNumber < 0.5)
            return new Position(X_SIZE * Math.random(), Y_SIZE - Y_MARGIN);
        else if (randomNumber < 0.75)
            return new Position(X_SIZE - X_MARGIN, Y_SIZE * Math.random());
        else
            return new Position(X_SIZE * Math.random(), 0);
    }

    @NotNull
    private GameController moveShip(Ship ship, List<MovingEntity> entities, List<MovingEntity> entitiesToRemove) {
        if (shipIsOutOfBound(ship)) {
            return new GameController(new GameState(new ShipController(ship), entities, entitiesToRemove, false));
        } else {
            return new GameController(new GameState(new ShipController(new Ship(ship.getId(), ship.getTopSpeed(), ship.getAcceleration(), ship.getSpeed(), ship.getWeapon(), ship.getLives(), new Position(getPosition(ship.getPosition().getX(), calculateXCoefficient(ship.getDegrees()), ship.getSpeed()), getPosition(ship.getPosition().getY(), calculateYCoefficient(ship.getDegrees()), ship.getSpeed())), ship.getDegrees(), ship.getSize(), ship.getColliderType(), ship.getPoints())), entities, entitiesToRemove, false));
        }
    }


    private List<MovingEntity> moveEntities(List<MovingEntity> entities) {
        List<MovingEntity> newEntities = new ArrayList<>();
        for (MovingEntity entity : entities) {
            if (entity instanceof Bullet) {
                newEntities.add(new Bullet(entity.getId(), new Position(getPosition(entity.getPosition().getX(), calculateXCoefficient(entity.getDegrees()), entity.getSpeed()), getPosition(entity.getPosition().getY(), calculateYCoefficient(entity.getDegrees()), entity.getSpeed())), entity.getSpeed(), entity.getDegrees(), entity.getSize(), entity.getColliderType()));
            } else {
                newEntities.add(new Asteroid(entity.getId(), new Position(getPosition(entity.getPosition().getX(), calculateXCoefficient(entity.getDegrees()), entity.getSpeed()), getPosition(entity.getPosition().getY(), calculateYCoefficient(entity.getDegrees()), entity.getSpeed())), entity.getSpeed(), entity.getDegrees(), entity.getSize(), entity.getColliderType()));
            }

        }
        return newEntities;
    }

    private List<MovingEntity> entitiesToRemove(List<MovingEntity> entities) {
        List<MovingEntity> newEntitiesToRemove = new ArrayList<>();
        for (MovingEntity entity : entities) {
            if (entity.getPosition().getX() >= X_SIZE - X_MARGIN || entity.getPosition().getY() >= Y_SIZE - Y_MARGIN ||
                    entity.getPosition().getX() <= 0 || entity.getPosition().getY() <= 0) {
                newEntitiesToRemove.add(entity);
            }
        }
        return newEntitiesToRemove;
    }

    private boolean shipIsOutOfBound(Ship ship) {           // Hacer para que le pueda pasar cualquier entidad (Ast, bala o nave)
        return getPosition(ship.getPosition().getX(), calculateXCoefficient(ship.getDegrees()), ship.getSpeed()) >= X_SIZE - X_MARGIN ||
                getPosition(ship.getPosition().getY(), calculateYCoefficient(ship.getDegrees()), ship.getSpeed()) >= Y_SIZE - Y_MARGIN ||
                getPosition(ship.getPosition().getX(), calculateXCoefficient(ship.getDegrees()), ship.getSpeed()) <= 0 ||
                getPosition(ship.getPosition().getY(), calculateYCoefficient(ship.getDegrees()), ship.getSpeed()) <= 0;
    }

    private double getPosition(double position, double coefficient, double speed) {
        return position + coefficient * speed;
    }

    public GameController handleKeyPressed(KeyCode keyCode) throws IOException {
        return new GameController(gameState.handleKeyPressed(keyCode));
    }

    private double calculateYCoefficient(double rotation) {
        return -Math.cos(Math.toRadians(rotation));
    }

    private double calculateXCoefficient(double rotation) {
        return Math.sin(Math.toRadians(rotation));
    }

    @NotNull
    public GameController handleCollision(@NotNull String element1Id, @NotNull String element2Id, ObservableMap<String, ElementModel> elements) {
        MovingEntity element1 = findEntityById(element1Id);
        MovingEntity element2 = findEntityById(element2Id);
        if (element1.getId().startsWith("s") && element2.getId().startsWith("a")) {
            Ship ship = gameState.shipController().ship();
            return new GameController(new GameState(new ShipController(new Ship(ship.getId(), ship.getTopSpeed(), ship.getAcceleration(), ship.getSpeed(), ship.getWeapon(), ship.getLives() - 1, ship.getPosition(), ship.getDegrees(), ship.getSize(), ship.getColliderType(), ship.getPoints())), gameState.entities(), gameState.entitiesToRemove(), false));
        } else if (element1.getId().startsWith("a") && element2.getId().startsWith("b")) {
            List<MovingEntity> entities = gameState.entities();
            entities.remove(element1);
            entities.remove(element2);
            if ((element1.getSize() - gameState.shipController().ship().getWeapon().getDamage()) < ASTEROID_MIN_SIZE) {
                elements.remove(element1.getId());
            } else {
                entities.add(new Asteroid(element1.getId(), element1.getPosition(), element1.getSpeed(), element1.getDegrees(), element1.getSize() - gameState.shipController().ship().getWeapon().getDamage(), element1.getColliderType()));
            }
            elements.remove(element2.getId());
            Ship ship = gameState.shipController().ship();
            return new GameController(new GameState(new ShipController(new Ship(ship.getId(), ship.getTopSpeed(), ship.getAcceleration(), ship.getSpeed(), ship.getWeapon(), ship.getLives(), ship.getPosition(), ship.getDegrees(), ship.getSize(), ship.getColliderType(), ship.getPoints() + 20)), entities, gameState.entitiesToRemove(), false));
        } else return this;
    }

    @NotNull
    private MovingEntity findEntityById(String elementId) {
        for (int i = 0; i < gameState.entities().size(); i++) {
            if (gameState.entities().get(i).getId().equalsIgnoreCase(elementId)) {
                return gameState.entities().get(i);
            }
        }
        return gameState.shipController().ship();
    }

    @NotNull
    public GameController cleanElements(GameController gameController) {
        List<MovingEntity> newMovingEntities = new ArrayList<>();
        for (MovingEntity entity : gameController.gameState.entities()) {
            if (!gameController.gameState.entitiesToRemove().contains(entity)) {
                newMovingEntities.add(entity);
            }
        }
        return new GameController(new GameState(gameController.gameState.shipController(), newMovingEntities, gameController.gameState.entitiesToRemove(), gameState.gamePaused()));
    }
}
