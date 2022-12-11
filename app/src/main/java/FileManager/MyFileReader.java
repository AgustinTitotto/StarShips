package FileManager;

import Controller.GameState;
import Controller.ShipController;
import Model.MovingEntity;
import Model.Ship;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Controller.Config.*;

public class MyFileReader {

    private final String fileName;

    public MyFileReader(String fileName) {
        this.fileName = fileName;
    }

    public GameState loadGame() throws IOException, ClassNotFoundException {
        File checkFile = new File(fileName);
        if (checkFile.length() == 0) {
            return new GameState(new ShipController(DEFAULT_SHIP), new ArrayList<>(), new ArrayList<>(), false);
        }
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Ship ship = (Ship) objectInputStream.readObject();
        Integer entitySize = (Integer) objectInputStream.readObject();
        List<MovingEntity> entities = loadEntities(objectInputStream, entitySize);
        return new GameState(new ShipController(ship), entities, new ArrayList<>(), false);
    }

    private List<MovingEntity> loadEntities(ObjectInputStream objectInputStream, Integer entitySize) throws IOException, ClassNotFoundException {
        List<MovingEntity> entities = new ArrayList<>();
        int size = entitySize;
        while (size > 0) {
            MovingEntity entity = (MovingEntity) objectInputStream.readObject();
            entities.add(entity);
            size = size - 1;
        }
        return entities;
    }
}
