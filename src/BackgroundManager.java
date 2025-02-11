import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The BackgroundManager class manages the creation of the background elements for the game.
 */
public class BackgroundManager {
    //arraylists to determine drilling objects
    public static ArrayList<Valuables> valuablesList = new ArrayList<>();
    public static ArrayList<Integer> blocks = new ArrayList<Integer>();
    public static ArrayList<String> checkList = new ArrayList<>();
    public static ArrayList<Integer> images = new ArrayList<Integer>();
    public static ArrayList<Integer> obstacles = new ArrayList<Integer>();
    public static ArrayList<Integer> lavas = new ArrayList<Integer>();
    public static ArrayList<Integer> amazonite = new ArrayList<Integer>();
    public static ArrayList<Integer> bronzium = new ArrayList<Integer>();
    public static ArrayList<Integer> diamond = new ArrayList<Integer>();
    public static ArrayList<Integer> einsteinium = new ArrayList<Integer>();
    public static ArrayList<Integer> emerald = new ArrayList<Integer>();
    public static ArrayList<Integer> goldium = new ArrayList<Integer>();
    public static ArrayList<Integer> ironium = new ArrayList<Integer>();
    public static ArrayList<Integer> platinium = new ArrayList<Integer>();
    public static ArrayList<Integer> ruby = new ArrayList<Integer>();
    public static ArrayList<Integer> silverium = new ArrayList<Integer>();
    public static String fuelText = new String("fuel:" + Drill.fuelLevel);
    public static String haulText = new String("haul:" + Drill.haul);
    public static String moneyText = new String("money:" + Drill.money);

    /**
     * Creates the background for the game.
     *
     * @param root        The root GridPane
     * @param scene       The Scene of the game
     * @param primaryStage The primary stage of the game
     */
    public void createBackground(GridPane root,Scene scene, Stage primaryStage) {
        createEarth(root);
        createSky(root);
        placeTop(root);
        placeSolid(root);
        placeObstacle(root);
        placeValuables(root);
        placeLavas(root);
        placeDrill(root,scene,primaryStage);
        addInfo(root);
    }

    /**
     * Adds textual information to the game screen.
     *
     * @param root The root GridPane
     */
    public void addInfo(GridPane root){
        //removes the current text and update the text nodes
        root.getChildren().removeIf(node -> node instanceof Text);
        fuelText ="fuel:" + Drill.fuelLevel;
        addTextToPane(root, fuelText, 0, 0, 16);
        addTextToPane(root, haulText, 0, 1,16);
        addTextToPane(root, moneyText, 0, 2,16);
    }

    /**
     * Creates the sky portion of the background.
     *
     * @param root The root GridPane
     */
    private void createSky(GridPane root) {
        //makes the first 3 rows from bottom blue for sky look
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 16; column++) {
                Rectangle cell = new Rectangle(50, 50, Color.DARKBLUE);
                StackPane stackPane = new StackPane(cell);
                if (row == 3 ) {
                    cell = new Rectangle(50, 50, Color.CHOCOLATE);
                    stackPane = new StackPane(cell);
                    //colors blue the fourth row from up to 5 pixels in order to ensure integrity
                    Rectangle extraSpace = new Rectangle(50, 5, Color.DARKBLUE);
                    StackPane extraSpaceStack = new StackPane(extraSpace);
                    extraSpaceStack.setTranslateY(-25);
                    stackPane.getChildren().add(extraSpaceStack);
                }
                root.add(stackPane, column, row);
            }
        }
    }

    /**
     * Creates the earth portion of the background.
     *
     * @param root The root GridPane
     */
    private void createEarth(GridPane root){
        //makes the last 13 rows from bottom chocolate for earth look
        for(int row = 3; row < 16; row++){
            for(int column = 0; column < 16; column++){
                Rectangle cell = new Rectangle(50, 50, Color.CHOCOLATE);
                StackPane stackPane = new StackPane(cell);
                root.add(stackPane, column, row);
            }
        }
    }

    /**
     * Places solid elements on the earth portion of the background.
     *
     * @param root The root GridPane
     */
    private void placeSolid(GridPane root){
        //places solid blocks on everywhere of earth
        Elements soil = new Elements("soil", "file:src/assets/underground/soil_01.png");
        for(int row = 4; row < 16; row++){
            for(int column = 0; column < 16 ; column ++){
                ImageView soilImageView = new ImageView(soil.getImage());
                root.add(soilImageView, column, row);
            }
        }
    }
    /**
     * Places top elements on the top layer of the background.
     *
     * @param root The root GridPane
     */
    private void placeTop(GridPane root){
        //top of the earth has grass
        Elements top = new Elements("top", "file:src/assets/underground/top_02.png");
        for(int column = 0; column < 16; column ++){
            ImageView topImageView = new ImageView(top.getImage());
            root.add(topImageView, column, 3);
        }
    }

    /**
     * Places obstacles on the earth portion of the background.
     *
     * @param root The root GridPane
     */
    private void placeObstacle(GridPane root){
        Elements obstacle = new Elements ("obstacle", "file:src/assets/underground/obstacle_01.png");
        //creates borders from obstacles
        for(int row = 4; row < 16; row++){
            ImageView obstacleImageView1 = new ImageView(obstacle.getImage());
            int index = 16 * row + 0;
            root.add(obstacleImageView1, 0, row);
            recordImages("obstacle", index);

            ImageView obstacleImageView2 = new ImageView(obstacle.getImage());
            index = 16 * row + 15;
            root.add(obstacleImageView2, 15, row);
            recordImages("obstacle", index);

        }for(int column = 0; column < 16; column ++){
            ImageView obstacleImageView3 = new ImageView(obstacle.getImage());
            int index = 16 * 15 + column;
            root.add(obstacleImageView3, column, 15);

            recordImages("obstacle", index);
        }
        //places obstacles ob random places as a barrier of the move of drill machine
        Random rand = new Random();
        int numberOfObjects = rand.nextInt(3) + 1;
        int i = 0;
        while (i < numberOfObjects){
            int row = rand.nextInt(11) + 4;
            int column = rand.nextInt(14) + 1;
            int index = 16 * row + column;
            ImageView obstacleImageView4 = new ImageView(obstacle.getImage());
            root.add(obstacleImageView4, column, row);
            recordImages("obstacle", index);
            i += 1;
        }
    }

    /**
     * Creates a list of Valuables objects.
     * Each Valuables object represents a valuable item with its name, image file name,
     * value, and weight.
     *
     * @return A list containing Valuables objects.
     */
    private List<Valuables> createValuables(){
        //creates valuables objects and adds to list
        valuablesList.add(new Valuables("amazonite","file:src/assets/underground/valuable_amazonite.png", 500000,120));
        valuablesList.add(new Valuables("bronzium","file:src/assets/underground/valuable_bronzium.png", 60, 10));
        valuablesList.add(new Valuables("diamond","file:src/assets/underground/valuable_diamond.png", 100000, 100));
        valuablesList.add(new Valuables("einsteinium","file:src/assets/underground/valuable_einsteinium.png", 2000, 40));
        valuablesList.add(new Valuables("emerald","file:src/assets/underground/valuable_emerald.png", 5000, 60));
        valuablesList.add(new Valuables("goldium","file:src/assets/underground/valuable_goldium.png", 250, 20));
        valuablesList.add(new Valuables("ironium","file:src/assets/underground/valuable_ironium.png",30, 10));
        valuablesList.add(new Valuables("platinium","file:src/assets/underground/valuable_platinium.png", 750, 30));
        valuablesList.add(new Valuables("ruby","file:src/assets/underground/valuable_ruby.png", 20000, 80));
        valuablesList.add(new Valuables("silverium","file:src/assets/underground/valuable_silverium.png", 100, 10));

        return valuablesList;
    }

    /**
     * Creates a list of valuables available in the game and places them on the background.
     *
     * @param root The root GridPane
     */
    private void placeValuables(GridPane root){
        //places valuable mines on random places in random counts
        List<Valuables> valuables = createValuables();
        Collections.shuffle(valuables);
        Random rand = new Random();
        int numberOfObjects = rand.nextInt(5) + 3;
        List<Valuables> selectedValuables = valuables.subList(0, numberOfObjects);
        for ( Valuables valuable : selectedValuables){
            int countOfObjects = rand.nextInt(4) + 2;
            int i = 0;
            while (i < countOfObjects){
                int row = rand.nextInt(11) + 4;
                int column = rand.nextInt(14) + 1;
                ImageView valuableImageView = new ImageView(valuable.getImage());
                int index = 16 * row + column;
                String name = valuable.getName();
                if(isEmpty(index)){
                    root.add(valuableImageView, column, row);
                    recordImages(name, index);
                    i += 1;
                }
            }
        }
    }

    /**
     * Places lava elements on the earth portion of the background.
     *
     * @param root The root GridPane
     */
    private void placeLavas(GridPane root){
        //places lavas blocks to over the game
        Elements lava = new Elements("lavas","file:src/assets/underground/lava_01.png");
        Random rand = new Random();
        int numberOfLavas = rand.nextInt(4) + 1;
        int i = 0;
        while ( i < numberOfLavas){
            int row = rand.nextInt(11) + 4;
            int column = rand.nextInt(14) + 1;
            ImageView lavasImageView = new ImageView(lava.getImage());
            int index = 16 * row + column;
            if(isEmpty(index)){
                root.add(lavasImageView, column, row);
                recordImages("lavas", index);
                i += 1;
            }
        }
    }

    /**
     * Places the drill (player's character) on the background and starts the game.
     *
     * @param root        The root GridPane
     * @param scene       The Scene of the game
     * @param primaryStage The primary stage of the game
     */
    private void placeDrill(GridPane root,Scene scene, Stage primaryStage){
        //places the drill machine on the up part of the screen on the  random column
        Drill drill = new Drill("file:src/assets/drill/drill_01.png");
        Random rand = new Random();
        int column = rand.nextInt(15);
        ImageView drillImageView = drill.getImageView();
        drillImageView.setFitWidth(50);
        drillImageView.setFitHeight(50);
        root.add(drillImageView, column, 2);

        PlayGame playGame = new PlayGame();
        playGame.startGame(root, drillImageView,drill, scene, primaryStage);
    }

    /**
     * Records the images of different elements at specified indices.
     *
     * @param name  The name of the element
     * @param index The index where the element is placed
     */
    private void recordImages(String name , Integer index){
        //saves the indexes of images placed the game pane
        images.add(index);
        switch (name) {
            case "amazonite":
                amazonite.add(index);
                break;
            case "bronzium":
                bronzium.add(index);
                break;
            case "diamond":
                diamond.add(index);
                break;
            case "einsteinium":
                einsteinium.add(index);
                break;
            case "emerald":
                emerald.add(index);
                break;
            case "goldium":
                goldium.add(index);
                break;
            case "ironium":
                ironium.add(index);
                break;
            case "platinium":
                platinium.add(index);
                break;
            case "ruby":
                ruby.add(index);
                break;
            case "silverium":
                silverium.add(index);
                break;
            case "obstacle":
                obstacles.add(index);
                break;
            case "lavas":
                lavas.add(index);
                break;
        }
        for (int i = 48; i <= 240; i++){
            blocks.add(i);
            checkList.add(String.valueOf(i));
        }
    }

    /**
     * Checks if a given index in the background is empty.
     *
     * @param index The index to check
     * @return True if the index is empty, otherwise false
     */
    private boolean isEmpty(int index){
        for(int cellNum : images){
            if(cellNum == index){
                return false;
            }
        }
        return true;
    }

    /**
     * Adds textual information to a specified location in the GridPane.
     *
     * @param pane        The GridPane
     * @param text        The text to be displayed
     * @param column      The column index
     * @param row         The row index
     * @param columnSpan  The column span of the text
     */
    private void addTextToPane(GridPane pane, String text, int column, int row,  int columnSpan) {
        //creates a text node and adds it to pane
        Text textNode = new Text(text);
        textNode.setFill(Color.WHITE);
        textNode.setFont(Font.font(textNode.getFont().getFamily(), 35));
        pane.add(textNode, column, row);
        GridPane.setColumnSpan(textNode, columnSpan);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        pane.getRowConstraints().add(rowConstraints);
    }
}
