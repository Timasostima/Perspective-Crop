package Try;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main2 extends Application {

    private List<Point2D> points = new ArrayList<>();
    private int pointIndex = 0;
    private ImageView imageView;
    private BufferedImage originalImage;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        imageView = new ImageView();

        try {
            originalImage = ImageIO.read(new File("C:\\Users\\Serious Tim\\eclipse-workspace\\PerspectiveCrop\\src\\Try\\me.jpg"));
            imageView.setImage(SwingFXUtils.toFXImage(originalImage, null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                points.add(new Point2D(x, y));

                Circle circle = new Circle(x, y, 5);
                root.getChildren().add(circle);

                if (++pointIndex == 4) {
                    cropImage();
                }
            }
        });

        root.setCenter(imageView);

        Scene scene = new Scene(root, originalImage.getWidth()-1, originalImage.getHeight()-1);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Perspective Crop");
        primaryStage.show();
    }

    private void cropImage() {
        double minX = points.get(0).getX();
        double minY = points.get(0).getY();
        double maxX = points.get(0).getX();
        double maxY = points.get(0).getY();

        for (Point2D point : points) {
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }

        double width = maxX - minX;
        double height = maxY - minY;

        
        BufferedImage croppedImage = originalImage.getSubimage((int) minX, (int) minY, (int) width, (int) height);

        // Save or display the cropped image as needed
        try {
            ImageIO.write(croppedImage, "jpg", new File("C:\\Users\\Serious Tim\\eclipse-workspace\\PerspectiveCrop\\src\\\\Try\\result.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
