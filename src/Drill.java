import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * The Drill class represents the player's character in the game.
 */
public class Drill {
    private ImageView imageView;
    private Image image;


    private static final float BASE_FUEL_DECREASE_RATE = 0.01f;
    private static final float INCREASED_FUEL_DECREASE_RATE = 0.2f;
    private static float currentDecreaseRate = BASE_FUEL_DECREASE_RATE;

    public static float fuelLevel = 10000;
    public static long haul = 0;
    public static long money = 0;
    private String imagePath;

    /**
     * Constructs a new Drill object with the specified image path.
     *
     * @param imagePath The path to the image representing the drill
     */
    public Drill(String imagePath) {
        this.imagePath = imagePath;
        this.image = new Image(imagePath);
        this.imageView = new ImageView(this.image);
    }

    /**
     * Decreases the fuel level based on the current decrease rate.
     * If fuel level drops below 0, the game is over.
     */
    public static void decreaseFuel() {
        //decreases the fuel level until it reaches the 0 value
        fuelLevel -= currentDecreaseRate;
        if (fuelLevel < 0) {
            fuelLevel = 0;
            PlayGame.isGameOver = true;
        }
    }

    /**
     * Increases the fuel decrease rate, making the fuel deplete faster.
     */
    public static void increaseFuelDecreaseRate() {
        //in some cases the fuel level must decrease more rapidly, the method manages it
        currentDecreaseRate = INCREASED_FUEL_DECREASE_RATE;
    }

    /**
     * Retrieves the ImageView of the drill.
     *
     * @return The ImageView of the drill
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Updates the image of the drill with the specified image path.
     *
     * @param imagePath The path to the new image
     */
    public void updateImageView(String imagePath){
        Image newImage = new Image(imagePath);
        this.imageView.setImage(newImage);
        this.imageView.setFitWidth(50);
        this.imageView.setFitHeight(50);
    }
}
