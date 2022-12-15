package edu.austral.ingsis.starships

import Controller.Config.*
import Controller.GameController
import Controller.GameState
import Controller.ShipController
import FileManager.LoadGame
import Model.MovingEntity
import Model.Ship
import edu.austral.ingsis.starships.ui.*
import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.ComboBox
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

        fun setOnClickLabelStyle(loadGame: Label) {
            loadGame.textFill = Color.WHITE
            loadGame.style = "-fx-font-family: 'Agency FB'; -fx-font-size: 40"
            loadGame.setOnMouseEntered {
                loadGame.textFill = Color.RED
                loadGame.cursor = Cursor.HAND
            }
            loadGame.setOnMouseExited {
                loadGame.textFill = Color.WHITE
            }
        }

        fun startNewGame(primaryStage: Stage,  value: String) {
            gameController = GameController(initialState())
            val starship = ElementModel("starship", DEFAULT_STARTING_X, DEFAULT_STARTING_Y, DEFAULT_STARSHIP_SIZE, DEFAULT_STARSHIP_SIZE, DEFAULT_STARTING_ROTATION + 180, DEFAULT_COLLIDER_TYPE, getImageRef(value))
            startGame(starship, primaryStage, value)
        }

        fun startGame(starship: ElementModel, primaryStage: Stage, value: String) {
            facade = ElementsViewFacade(imageResolver)
            keyTracker = KeyTracker()
            facade.elements["starship"] = starship

            val layout = VBox()
            val labels = HBox(50.0)
            labels.alignment = Pos.TOP_CENTER

            val lives = Label("Lives")
            lives.alignment = Pos.CENTER
            lives.textFill = Color.WHITE
            lives.style = "-fx-font-family: 'Agency FB'; -fx-font-size: 40"

            val points = Label("Points")
            points.alignment = Pos.CENTER
            points.textFill = Color.WHITE
            points.style = "-fx-font-family: 'Agency FB'; -fx-font-size: 40"

            labels.children.addAll(lives, points)
            layout.children.addAll(labels, facade.view)

            layout.style = ("-fx-background-image: url('background_image.jpg')")
            val scene = Scene(layout)
            keyTracker.scene = scene

            setScene(primaryStage, scene)

            facade.timeListenable.addEventListener(MyTimeListener(primaryStage, lives, points, value))
            facade.collisionsListenable.addEventListener(MyCollisionListener())
            keyTracker.keyPressedListenable.addEventListener(MyPressKeyListener())

            facade.start()
            keyTracker.start()
            primaryStage.show()
        }

        private fun setScene(primaryStage: Stage, scene: Scene) {
            primaryStage.scene = scene
            primaryStage.height = Y_SIZE
            primaryStage.width = X_SIZE
        }

        fun getImageRef(value: String): ImageRef {
            when(value) {
                "Grey" -> return GREY_IMAGE_REF
                "Blue" -> return BLUE_IMAGE_REF
                "Green" -> return GREEN_IMAGE_REF
                "Red" -> return RED_IMAGE_REF
            }
            return GREY_IMAGE_REF
        }
    }

    override fun start(primaryStage: Stage) {
        val layout = VBox(100.0)
        layout.alignment = Pos.CENTER
        val name = Label("StarShip")
        name.textFill = Color.WHITE
        name.style = "-fx-font-family: 'Agency FB'; -fx-font-size: 100"

        val options = HBox(100.0)
        options.alignment = Pos.CENTER

        val skins: ComboBox<String> = ComboBox<String>()
        skins.style = "-fx-font-family: 'Agency FB'; -fx-font-size: 30  "
        skins.items.addAll(
            "Grey",
            "Blue",
            "Green",
            "Red"
        )
        skins.selectionModel.select(0)

        val startNewGame = Label("Start new game")
        setOnClickLabelStyle(startNewGame)
        startNewGame.setOnMouseClicked {
            startNewGame(primaryStage, skins.value)
        }

        val loadGame = Label("Load Game")
        setOnClickLabelStyle(loadGame)
        loadGame.setOnMouseClicked {
            loadGame(primaryStage, skins.value)
        }

        options.children.addAll(startNewGame, loadGame)
        layout.children.addAll(name, options, skins)

        layout.style = ("-fx-background-image: url('background_image.jpg')")
        val scene = Scene(layout)
        setScene(primaryStage, scene)

        primaryStage.show()
    }

    private fun loadGame(primaryStage: Stage, value: String) {
        val fileReader =
            LoadGame(System.getProperty("user.dir") + "/app/src/main/java/FileManager/SavedGameFile.txt")
        gameController = GameController(fileReader.loadGame())
        val ship = gameController.gameState.shipController.ship
        val starship = ElementModel("starship", ship.position().x(), ship.position().y(), ship.size(), ship.size(), ship.degrees() + 180, ship.colliderType(), getImageRef(value))
        startGame(starship, primaryStage, value)
    }

    class MyTimeListener(private val primaryStage: Stage, private val lives: Label, private val points: Label, private val skin: String) : EventListener<TimePassed> {
        override fun handle(event: TimePassed) {
            gameController = gameController.moveElements()
            updateElements()
            updateGameScore(gameController.gameState.shipController.ship, lives, points)
            checkDefeat(gameController.gameState.shipController.ship, primaryStage, skin)
            gameController = gameController.cleanElements(gameController)
        }

        private fun updateGameScore(ship: Ship, lives: Label, points: Label) {
            lives.text = "Lives: " + ship.lives.toString()
            points.text = "Points: " + ship.points.toString()
        }

        private fun checkDefeat(ship: Ship, primaryStage: Stage, skin : String) {
            if (ship.lives <= 0) {
                val layout = VBox(100.0)
                layout.alignment = Pos.CENTER
                val name = Label("Game Over")
                name.textFill = Color.WHITE
                name.style = "-fx-font-family: 'Agency FB'; -fx-font-size: 60"

                val options = HBox(100.0)
                options.alignment = Pos.CENTER

                val startNewGame = Label("Start new game")
                setOnClickLabelStyle(startNewGame)
                startNewGame.setOnMouseClicked {
                    startNewGame(primaryStage, skin)
                }
                options.children.add(startNewGame)
                layout.children.addAll(name, options)

                layout.style = ("-fx-background-image: url('background_image.jpg')")
                val scene = Scene(layout)
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
            addNewElements(entities)
            entitiesToRemove.forEach{
                facade.elements.remove(it.id())
            }
            moveElements(ship)
        }

        private fun moveElements(ship: Ship) {
            facade.elements.forEach {
                val (key, element) = it
                when (key) {
                    "starship" -> {
                        element.x.set(ship.position().x())
                        element.y.set(ship.position().y())
                        element.rotationInDegrees.set(ship.degrees() + 180)
                    }

                    else -> {
                        element.x.set(element.x.value)
                        element.y.set(element.y.value)
                        element.rotationInDegrees.set(element.rotationInDegrees.value)
                    }
                }
            }
        }

        private fun addNewElements(entities: MutableList<MovingEntity>) {
            entities.forEach {
                val entity: ElementModel = if (it.id().startsWith("a", ignoreCase = true)) {
                    ElementModel(
                        it.id(), it.position().x(), it.position().y(), it.size(), it.size(), it.degrees() + 180.0,
                        it.colliderType(), ASTEROID
                    )
                } else {
                    ElementModel(
                        it.id(), it.position().x(), it.position().y(), it.size(), it.size(), it.degrees() + 180.0,
                        it.colliderType(), null
                    )
                }
                facade.elements[it.id()] = entity
            }
        }
    }

    class MyCollisionListener : EventListener<Collision> {
        override fun handle(event: Collision) {
            println("${event.element1Id} ${event.element2Id}")
            gameController = gameController.handleCollision(event.element1Id, event.element2Id, facade.elements)
        }
    }

    class MyPressKeyListener : EventListener<KeyPressed> {
        override fun handle(event: KeyPressed) {
            gameController = gameController.handleKeyPressed(event.key)
        }
    }

}


private fun initialState(): GameState {
    val ship = DEFAULT_SHIP
    val shipController = ShipController(ship)
    return GameState(shipController, ArrayList(), ArrayList(), false)
}