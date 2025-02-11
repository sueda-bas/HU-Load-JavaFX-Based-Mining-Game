import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The PlayGame class manages the gameplay mechanics.
 */
public class PlayGame {
    private int currentIndex = 0;
    public static boolean isUpMoving;
    public static boolean isGameOver;
    private AnimationTimer fuelTimer;

    /**
     * Starts the game by initializing the drill and starting the fuel timer.
     * @param root The root GridPane of the game scene.
     * @param drillImageView The ImageView of the drill.
     * @param drill The instance of the Drill class.
     * @param scene The Scene of the game.
     * @param primaryStage The primary Stage of the application.
     */
    public void startGame ( GridPane root, ImageView drillImageView, Drill drill, Scene scene, Stage primaryStage){
        drillMover( root, drillImageView, drill, scene, primaryStage);
        startFuelTimer(root, primaryStage);
    }

    /**
     * Starts the fuel timer, which continuously decreases the fuel level.
     * @param root The root GridPane of the game scene.
     * @param primaryStage The primary Stage of the application.
     */
    public void startFuelTimer(GridPane root, Stage primaryStage) {
        fuelTimer = new AnimationTimer() {
            private long lastTime = 0;
            @Override
            public void handle(long now) {
                if ((now - lastTime) >= 1_000_000) {
                    Drill.decreaseFuel();
                    BackgroundManager backgroundManager = new BackgroundManager();
                    //while fuel Level is decreasing, add new value of it to pane
                    backgroundManager.addInfo(root);
                    lastTime = now;
                    if(isGameOver){
                        //if fuel is over, game over
                        displayGameOverPane(createGameOverPane(false),primaryStage);
                        stopFuelTimer();
                    }
                }
            }
        };
        fuelTimer.start();
    }

    /**
     * Stops the fuel timer.
     */
    public void stopFuelTimer() {
        if (fuelTimer != null) {
            fuelTimer.stop();
        }
    }

    /**
     * Increases the fuel decrease rate when a block is removed and the machine is moving.
     */
    public void increaseFuelDecreaseRateOnBlockRemoval() {
        Drill.increaseFuelDecreaseRate();
    }

    /**
     * Handles the movement of the drill.
     * @param root The root GridPane of the game scene.
     * @param drillImageView The ImageView of the drill.
     * @param drill The instance of the Drill class.
     * @param scene The Scene of the game.
     * @param primaryStage The primary Stage of the application.
     */
    public void drillMover(GridPane root, ImageView drillImageView, Drill drill, Scene scene, Stage primaryStage) {

        AnimationTimer animationTimer = new AnimationTimer() {
            private long lastTime = 0;
            @Override
            public void handle(long now) {
                long interval = 600_000_000;
                if ((now - lastTime) >= interval){
                    fallDown(root, String.valueOf(currentIndex + 16), drill );
                    lastTime = now;
                }
            }
        };
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            int currentRow = root.getRowIndex(drillImageView);
            int currentColumn = root.getColumnIndex(drillImageView);
            int newRow = currentRow;
            int newColumn = currentColumn;
            int newIndex = 0;
            KeyCode keyCode = event.getCode();
            isUpMoving= false;
            switch (keyCode) {
                case UP:
                    isUpMoving = true;
                    newRow = Math.max(0, currentRow - 1);
                    newIndex = 16 * newRow + newColumn;
                    drill.updateImageView("file:src/assets/drill/drill_51.png");
                    if(isEmpty(String.valueOf(newIndex))){
                        root.setRowIndex(drill.getImageView(), newRow);
                        root.setColumnIndex(drill.getImageView(), newColumn);
                    }
                    break;
                case DOWN:
                    newRow = Math.min(15 - 1, currentRow + 1);
                    newIndex = 16 * newRow + newColumn;
                    drill.updateImageView("file:src/assets/drill/drill_40.png");
                    break;
                case LEFT:
                    newColumn = Math.max(0, currentColumn - 1);
                    newIndex = 16 * newRow + newColumn;
                    drill.updateImageView("file:src/assets/drill/drill_01.png");
                    break;
                case RIGHT:
                    newColumn = Math.min(16 - 1, currentColumn + 1);
                    newIndex = 16 * newRow + newColumn;
                    drill.updateImageView("file:src/assets/drill/drill_60.png");
                    break;
                default:
                    break;
            }

            if(!isUpMoving){
                //if it is not upMoving it should drill some blocks
                if(!determineDrillingBlock(newIndex).equals("obstacles")){
                    drillingBlocks(root, newIndex, drill);
                    root.setRowIndex(drill.getImageView(), newRow);
                    root.setColumnIndex(drill.getImageView(), newColumn);

                    if(determineDrillingBlock(newIndex).equals("lavas")){
                        displayGameOverPane(createGameOverPane(true),primaryStage);
                    }for(int index : BackgroundManager.blocks){
                        if(index == newIndex){
                            updateValues(newIndex, root);
                        }
                    }
                }
            }
            increaseFuelDecreaseRateOnBlockRemoval();
            setCurrentIndex(newIndex);
            animationTimer.start();
        });
    }

    /**
     * Handles the update of the current index for the drill.
     * @param currentIndex The current index for the drill.
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    /**
     * Moves the drill take-down when triggered by a timer.
     * @param root The root GridPane of the game scene.
     * @param index The current index of the drill.
     * @param drill The instance of the Drill class.
     */

    public void fallDown(GridPane root, String index, Drill drill){
        //it ensures the gravity
        int i = Integer.parseInt(index);
        if(isEmpty(String.valueOf(i))){
            int newRow = i / 16 ;
            int newColumn = i % 16 ;
            if(!determineDrillingBlock(Integer.parseInt(index)).equals("obstacles")){
                root.setRowIndex(drill.getImageView(), newRow);
                root.setColumnIndex(drill.getImageView(), newColumn);
                currentIndex = 16 * newRow + newColumn;
            }
        }
    }

    /**
     * Checks if a given index in the GridPane is empty.
     * @param index The index to check.
     * @return True if the index is empty, otherwise false.
     */
    public boolean isEmpty(String index){
        if(BackgroundManager.checkList.contains(index)){
            return false;
        }
        return true;
    }

    /**
     * Drills blocks based on the drill's movement.
     * @param root The root GridPane of the game scene.
     * @param index The index of the block being drilled.
     * @param drill The instance of the Drill class.
     */
    public void drillingBlocks(GridPane root, int index, Drill drill){
        //in some parts there are 2 block imageView in a row so the method is called two times
        if(isMustDrill(index)){
            removeBlock(root, index, 16, drill);
            removeBlock(root, index, 16, drill);
        }
    }

    /**
     * Checks if a block must be drilled.
     * @param index The index of the block.
     * @return True if the block must be drilled, otherwise false.
     */
    private boolean isMustDrill(int index){
        for(int blockNum : BackgroundManager.blocks){
            if(blockNum == index){
                return true;
            }
        }return false;
    }

    /**
     * Determines the type of block being drilled.
     * @param index The index of the block being drilled.
     * @return The type of block being drilled.
     */
    private String determineDrillingBlock(int index){
        String result = "";
        for(int blockNum : BackgroundManager.lavas){
            if(blockNum == index){
                result = "lavas";
            }
        }
        for(int blockNum : BackgroundManager.obstacles){
            if(blockNum == index){
                result = "obstacles";
            }

        }
        for(int blockNum : BackgroundManager.einsteinium){
            if(blockNum == index){
                result = "einsteinium";
            }

        }
        for(int blockNum : BackgroundManager.diamond){
            if(blockNum == index){
                result = "diamond";
            }

        }
        for(int blockNum : BackgroundManager.amazonite){
            if(blockNum == index){
                result = "amazonite";
            }

        }
        for(int blockNum : BackgroundManager.bronzium){
            if(blockNum == index){
                result = "bronzium";
            }

        }
        for(int blockNum : BackgroundManager.emerald){
            if(blockNum == index){
                result = "emerald";
            }

            }
        for(int blockNum : BackgroundManager.goldium){
            if(blockNum == index){
                result = "goldium";
            }

        }
        for(int blockNum : BackgroundManager.ironium){
            if(blockNum == index){
                result = "ironium";
            }

        }
        for(int blockNum : BackgroundManager.platinium){
            if(blockNum == index){
                result = "platinium";
            }

        }
        for(int blockNum : BackgroundManager.ruby){
            if(blockNum == index){
                result = "ruby";
            }

        }
        for(int blockNum : BackgroundManager.silverium){
            if(blockNum == index){
                result = "silverium";
            }

        }
        return result;
    }

    /**
     * Retrieves the node in the GridPane corresponding to a cell index.
     * @param cellIndex The index of the cell.
     * @param gridPane The GridPane in which the cell resides.
     * @param numColumns The number of columns in the GridPane.
     * @param drill The instance of the Drill class.
     * @return The node corresponding to the cell index.
     */
    private Node getNodeByCellIndex(int cellIndex, GridPane gridPane, int numColumns, Drill drill) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        int row = cellIndex / numColumns;
        int column = cellIndex % numColumns;

        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column && node instanceof ImageView) {
                if(node!= drill.getImageView()){
                    result = node;
                    break;
                }

            }
        }
        return result;
    }

    /**
     * Removes a block from the GridPane.
     * @param root The root GridPane of the game scene.
     * @param index The index of the block to be removed.
     * @param column The number of columns in the GridPane.
     * @param drill The instance of the Drill class.
     */
    private void removeBlock(GridPane root, int index, int column, Drill drill) {
        Node node = getNodeByCellIndex(index, root, column, drill);
        if (node != null) {
            root.getChildren().remove(node);
            increaseFuelDecreaseRateOnBlockRemoval();
        }
    }

    /**
     * Creates a StackPane for the game over screen.
     * @param isLost Indicates if the game was lost.
     * @return The StackPane for the game over screen.
     */
    public StackPane createGameOverPane(boolean isLost) {
        StackPane stackPane = new StackPane();
        Label label = new Label("GAME OVER");
        label.setFont(Font.font(label.getFont().getFamily(), 50));
        label.setTextFill(Color.WHITE);
        if(isLost){
            //if the lost of the game is about lavas there mustn't be info about collected money
            stackPane.setBackground(new Background(new BackgroundFill(Color.DARKRED, null, null)));
        }else{
            stackPane.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            Label moneyLabel = new Label("Collected Money: " + Drill.money);
            moneyLabel.setFont(Font.font(label.getFont().getFamily(), 50));
            moneyLabel.setTextFill(Color.WHITE);
            stackPane.getChildren().add(moneyLabel);
            moneyLabel.setTranslateY(50);
        }
        stackPane.getChildren().add(label);

        return stackPane;
    }
    /**
     * Displays the game over screen.
     * @param stackPane The StackPane containing the game over message.
     * @param primaryStage The primary Stage of the application.
     */
    public void displayGameOverPane(StackPane stackPane, Stage primaryStage) {
        primaryStage.setTitle("HU-Load");
        primaryStage.setScene(new Scene(stackPane, 800, 800));
        primaryStage.show();
    }
    /**
     * Updates the game values after drilling a block.
     * @param index The index of the drilled block.
     * @param root The root GridPane of the game scene.
     */
    public void updateValues(int index, GridPane root){
        //updates haul and money values according to type of drilled blocks
        if(determineDrillingBlock(index).equals("amazonite")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("amazonite")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }
                }
            }
        }else if(determineDrillingBlock(index).equals("bronzium")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("bronzium")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }

                }
            }

        }else if(determineDrillingBlock(index).equals("diamond")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("diamond")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }

                }
            }

        }else if(determineDrillingBlock(index).equals("einsteinium")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("einsteinium")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }

                }
            }

        }else if(determineDrillingBlock(index).equals("emerald")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("emerald")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }

                }
            }

        }else if(determineDrillingBlock(index).equals("goldium")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("goldium")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }

                }
            }

        }else if(determineDrillingBlock(index).equals("ironium")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("ironium")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }

                }
            }

        }else if(determineDrillingBlock(index).equals("platinium")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("platinium")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }

                }
            }

        }else if(determineDrillingBlock(index).equals("ruby")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("ruby")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }

                }
            }

        }else if(determineDrillingBlock(index).equals("silverium")){
            for(Valuables valuable : BackgroundManager.valuablesList){
                if(valuable.getName().equals("silverium")){
                    if(BackgroundManager.checkList.contains(String.valueOf(index))){
                        Drill.haul += valuable.getWeight();
                        Drill.money += valuable.getWorth();
                        BackgroundManager.images.removeIf(number -> number == index);
                    }

                }
            }

        }
        BackgroundManager backgroundManager = new BackgroundManager();
        backgroundManager.fuelText = "fuel:" + Drill.fuelLevel;
        backgroundManager.haulText = "haul:" + Drill.haul;
        backgroundManager.moneyText= "money:" + Drill.money;
        backgroundManager.addInfo(root);
        BackgroundManager.checkList.remove(String.valueOf(index));

    }
}





