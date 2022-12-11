package edu.austral.ingsis.starships

import Controller.Config.*
import Controller.GameController
import Controller.GameState
import Controller.ShipController
import FileManager.MyFileReader
import Model.MovingEntity
import Model.Position
import Model.Ship
import Model.Weapon
import edu.austral.ingsis.starships.ui.*
import edu.austral.ingsis.starships.ui.ElementColliderType.Triangular
import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage

private val imageResolver = CachedImageResolver(DefaultImageResolver())
private var facade = ElementsViewFacade(imageResolver)
private var keyTracker = KeyTracker()
private var gameController = GameController(null)

fun main() {
    launch(MyApp::class.java)
}


class MyApp : Application() {


    companion object {
        val STARSHIP_IMAGE_REF = ImageRef("starship", 70.0, 70.0) // Esto es la definicion

        fun setLabelStyle(loadGame: Label) {
            loadGame.textFill = Color.BLACK
            loadGame.style = "-fx-font-family: 'Agency FB'; -fx-font-size: 40"
            loadGame.setOnMouseEntered {
                loadGame.textFill = Color.RED
                loadGame.cursor = Cursor.HAND
            }
            loadGame.setOnMouseExited {
                loadGame.textFill = Color.BLACK
            }
        }

        fun startNewGame(primaryStage: Stage) {
            gameController = GameController(initialState())
            val starship = ElementModel("starship", DEFAULT_STARTING_X, DEFAULT_STARTING_Y, DEFAULT_STARSHIP_SIZE, DEFAULT_STARSHIP_SIZE, DEFAULT_STARTING_ROTATION + 180, DEFAULT_COLLIDER_TYPE, STARSHIP_IMAGE_REF)
            startGame(starship, primaryStage)
        }

        fun startGame(starship: ElementModel, primaryStage: Stage) {
            facade = ElementsViewFacade(imageResolver);
            keyTracker = KeyTracker()
            facade.elements["starship"] = starship

            val scene = Scene(facade.view)
            keyTracker.scene = scene

            primaryStage.scene = scene
            primaryStage.height = Y_SIZE
            primaryStage.width = X_SIZE

            facade.timeListenable.addEventListener(MyTimeListener(primaryStage))
            facade.collisionsListenable.addEventListener(MyCollisionListener());
            keyTracker.keyPressedListenable.addEventListener(MyPressKeyListener())

            facade.start()
            keyTracker.start()
            primaryStage.show()
        }
    }

    override fun start(primaryStage: Stage) {
        val layout = VBox(100.0)
        layout.alignment = Pos.CENTER
        val name = Label("StarShip")
        name.textFill = Color.BLACK
        name.style = "-fx-font-family: 'Agency FB'; -fx-font-size: 100"

        val options = HBox(100.0)
        options.alignment = Pos.CENTER

        val startNewGame = Label("Start new game")
        setLabelStyle(startNewGame)
        startNewGame.setOnMouseClicked {
            startNewGame(primaryStage)
        }

        val loadGame = Label("Load Game")
        setLabelStyle(loadGame)
        loadGame.setOnMouseClicked {
            loadGame(primaryStage)
        }

        options.children.addAll(startNewGame, loadGame)
        layout.children.addAll(name, options)

        val scene = Scene(layout)
        primaryStage.scene = scene
        primaryStage.height = Y_SIZE
        primaryStage.width = X_SIZE

        primaryStage.show()
    }

    private fun loadGame(primaryStage: Stage) {
        val fileReader = MyFileReader(System.getProperty("user.dir") + "/app/src/main/java/FileManager/SavedGameFile.txt")
        gameController = GameController(fileReader.loadGame());
        val ship = gameController.gameState.shipController.ship
        val starship = ElementModel("starship", ship.position.x, ship.position.y, ship.size, ship.size, ship.degrees + 180, ship.colliderType, STARSHIP_IMAGE_REF)
        startGame(starship, primaryStage)
    }

    class MyTimeListener(private val primaryStage: Stage) : EventListener<TimePassed> {
        override fun handle(event: TimePassed) {
            gameController = gameController.moveElements();
            updateElements()
            checkDefeat(gameController.gameState.shipController.ship, primaryStage)
            gameController = gameController.cleanElements(gameController)
        }

        private fun checkDefeat(ship: Ship, primaryStage: Stage) {
            if (ship.lives <= 0) {
                val layout = VBox(100.0)
                layout.alignment = Pos.CENTER
                val name = Label("Game Over")
                name.textFill = Color.BLACK
                name.style = "-fx-font-family: 'Agency FB'; -fx-font-size: 60"

                val options = HBox(100.0)
                options.alignment = Pos.CENTER

                val startNewGame = Label("Start new game")
                setLabelStyle(startNewGame)
                startNewGame.setOnMouseClicked {
                    startNewGame(primaryStage)
                }

                options.children.add(startNewGame)
                layout.children.addAll(name, options);

                val scene = Scene(layout);
                primaryStage.scene = scene
                primaryStage.height = Y_SIZE
                primaryStage.width = X_SIZE

                facade.stop()
                keyTracker.stop()
                primaryStage.show()
            }
        }

        private fun updateElements() {
            val ship = gameController.gameState().shipController().ship()
            val entities = gameController.gameState().entities()
            val entitiesToRemove = gameController.gameState().entitiesToRemove()
            entities.forEach{
                if (!it.id.equals("starship", ignoreCase = true)){
                    val entity = ElementModel(it.id, it.position.x, it.position.y, it.size, it.size, it.degrees + 180.0,
                        it.colliderType, null)
                    facade.elements[it.id] = entity;
                }
            }
            entitiesToRemove.forEach{
                facade.elements.remove(it.id)
            }
            facade.elements.forEach {
                val (key, element) = it
                when (key) {
                    "starship" -> {
                        element.x.set(ship.position.x)
                        element.y.set(ship.position.y)
                        element.rotationInDegrees.set(ship.degrees + 180)
                    }
                    else -> {
                        element.x.set(element.x.value)
                        element.y.set(element.y.value)
                        element.rotationInDegrees.set(element.rotationInDegrees.value)
                    }
                }
            }
        }
    }

    class MyCollisionListener() : EventListener<Collision> {
        override fun handle(event: Collision) {
            println("${event.element1Id} ${event.element2Id}")
            gameController = gameController.handleCollision(event.element1Id, event.element2Id, facade.elements);
        }
    }

    class MyPressKeyListener() : EventListener<KeyPressed> {
        override fun handle(event: KeyPressed) {
            gameController = gameController.handleKeyPressed(event.key);
        }
    }

}


private fun initialState(): GameState {
    val ship = Ship("starship", 0.8, 0.2, 0.0, Weapon(10.0, 2.0), 3, Position(300.0, 300.0), 90.0, 40.0, Triangular);
    val shipController = ShipController(ship);
    val gameState = GameState(shipController, ArrayList<MovingEntity>(), ArrayList<MovingEntity>(), false);
    return gameState
}