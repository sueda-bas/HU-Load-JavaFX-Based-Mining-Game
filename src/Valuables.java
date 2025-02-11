import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * Represents valuable items in the game.
 */
public class Valuables {
    private static final int IMAGE_WIDTH = 50;
    private static final int IMAGE_HEIGHT = 50;
    private Image image;
    private ImageView imageView;
    private String name;
    private int worth;
    private int weight;
    private String imagePath;

    /**
     * Constructs a new Valuables object.
     * @param name The name of the valuable item.
     * @param imagePath The file path to the image representing the valuable item.
     * @param worth The monetary worth of the valuable item.
     * @param weight The weight of the valuable item.
     */
    public Valuables(String name, String imagePath, int worth, int weight){
        this.imagePath = imagePath;
        this.name = name;
        this.worth = worth;
        this.weight = weight;
        this.image = new Image(imagePath);
        this.imageView = new ImageView(this.image);
        this.imageView.setFitWidth(IMAGE_WIDTH);
        this.imageView.setFitHeight(IMAGE_HEIGHT);
    }

    /**
     * Gets the name of the valuable item.
     * @return The name of the valuable item.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the image representing the valuable item.
     * @return The image representing the valuable item.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the monetary worth of the valuable item.
     * @return The monetary worth of the valuable item.
     */
    public int getWorth() {return worth;}

    /**
     * Gets the weight of the valuable item.
     * @return The weight of the valuable item.
     */
    public int getWeight() {return weight;}
}
