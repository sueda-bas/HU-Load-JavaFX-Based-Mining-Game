import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * The Elements class represents various elements in the game.
 */
public class Elements {
    private static final int IMAGE_WIDTH = 50;
    private static final int IMAGE_HEIGHT = 50;
    private Image image;
    private ImageView imageView;
    private String name;
    private String imagePath;

    /**
     * Constructs a new Elements object with the specified properties.
     *
     * @param name       The name of the element
     * @param imagePath  The path to the image representing the element
     */

    public Elements(String name, String imagePath){
        this.imagePath = imagePath;
        this.name = name;
        this.image = new Image(imagePath);
        this.imageView = new ImageView(this.image);
        this.imageView.setFitWidth(IMAGE_WIDTH);
        this.imageView.setFitHeight(IMAGE_HEIGHT);
    }

    /**
     * Retrieves the image associated with the element.
     *
     * @return The image of the element
     */
    public Image getImage() {
        return image;
    }
}
