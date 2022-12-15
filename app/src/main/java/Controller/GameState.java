package Controller;

import FileManager.SaveGame;
import Model.Bullet;
import Model.MovingEntity;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static Controller.Config.*;


public record GameState(ShipController shipController, List<MovingEntity> entities,
                        List<MovingEntity> entitiesToRemove, boolean gamePaused) {


    public GameState handleKeyPressed(KeyCode keyCode) throws IOException {
        if (gamePaused){
            return gameStateWhenPaused(keyCode);
        }
        else {
            return gameStateWhenUnPaused(keyCode);
        }

    }

    @NotNull
    private GameState gameStateWhenUnPaused(KeyCode keyCode) {
        ShipController newShipController;
        switch (keyCode) {
            case UP -> newShipController = shipController.increaseSpeed();
            case DOWN -> newShipController = shipController.stop();
            case LEFT -> newShipController = shipController.changeDirection(-ROTATION_SPEED);
            case RIGHT -> newShipController = shipController.changeDirection(+ROTATION_SPEED);
            case SPACE -> {
                newShipController = this.shipController;
                entities.add(new Bullet("bullet" + UUID.randomUUID(), this.shipController.ship().position(), this.shipController().ship().getTopSpeed() + BULLET_SPEED_MARGIN, this.shipController.ship().degrees(), this.shipController.ship().getWeapon().bulletSize(), this.shipController.ship().getWeapon().type()));
            }
            case P -> {
                newShipController = this.shipController;
                return new GameState(newShipController, this.entities, this.entitiesToRemove, true);
            }
            default -> newShipController = this.shipController;
        }
        return new GameState(newShipController, this.entities, this.entitiesToRemove, gamePaused);
    }

    @NotNull
    private GameState gameStateWhenPaused(KeyCode keyCode) throws IOException {
        ShipController newShipController;
        switch (keyCode) {
            case P -> {
                newShipController = this.shipController;
                return new GameState(newShipController, this.entities, this.entitiesToRemove, false);
            }
            case S -> {
                SaveGame fileWriter = new SaveGame(System.getProperty("user.dir") + "/app/src/main/java/FileManager/SavedGameFile.txt");
                fileWriter.saveGame(this.shipController.ship(), this.entities);
            }
        }
        return this;
    }
}