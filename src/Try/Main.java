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
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main extends Application {

    private static List<Point> points = new ArrayList<>();
    private static int pointIndex = 0;
    private static ImageView imageView;
    private static BufferedImage originalImage;
    BufferedImage result;
    

    @Override
    public void start(Stage primaryStage) {
    	
        BorderPane root = new BorderPane();
        imageView = new ImageView();

        try {
            originalImage = ImageIO.read(new File("imgs//example.jpg"));
            imageView.setImage(SwingFXUtils.toFXImage(originalImage, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int sceneWidth = originalImage.getWidth()-1;
        int sceneHeight = originalImage.getHeight()-1;

        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                points.add(new Point(x, y));

                Circle circle = new Circle(x, y, 2);
                root.getChildren().add(circle);

                if (++pointIndex == 4) {
                	Mat matImg = cropImage();
                	result = Mat2BufferedImage(matImg);
            		try {
            		    ImageIO.write(result, "jpg", new File("imgs//result.jpg"));
            		    BorderPane root2 = new BorderPane();
            		    root2.setCenter(imageView);
            		    imageView.setImage(SwingFXUtils.toFXImage(result, null));
            		    int sceneWidth = result.getWidth()-1;
            	        int sceneHeight = result.getHeight()-1;
            		    Scene scene2 = new Scene(root2, sceneWidth,sceneHeight);
            		    primaryStage.setScene(scene2);
            		} catch (IOException e) {
            		    e.printStackTrace();
            		}
                }
            }
        });

        root.setCenter(imageView);
        Scene scene= new Scene(root, sceneWidth, sceneHeight);        	
        primaryStage.setScene(scene);
        primaryStage.setTitle("Perspective Crop");
        primaryStage.show();
    	
    }

    private static Mat cropImage() {
        Point[] srcPoints = {points.get(0), points.get(1),points.get(2),points.get(3)};
        Rect rect1 = new Rect(srcPoints[0], srcPoints[1]);
        Rect rect2 = new Rect(srcPoints[2], srcPoints[3]);
        Rect rect3 = new Rect(srcPoints[0], srcPoints[2]);
        Rect rect4 = new Rect(srcPoints[1], srcPoints[3]);
        int width = (rect1.width+rect2.width)/2;
        int height = (rect3.height+rect4.height)/2;
        double ratio = (width>height ? (double)width/(double)height : (double)height/(double)width);
        boolean horizontal = width>height;
        
        System.out.println(ratio);
        
        System.out.println();
        points.stream().forEach(System.out::println);
        Mat sourceImage = Imgcodecs.imread("imgs//example.jpg");


        MatOfPoint2f src = new MatOfPoint2f(srcPoints);
        MatOfPoint2f dst;
        if (horizontal) {
        	dst = new MatOfPoint2f(new Point[]{new Point(0, 0), new Point(250*ratio, 0), new Point(0, 250), new Point(250*ratio, 250)});
        }
        else {
        	dst = new MatOfPoint2f(new Point[]{new Point(0, 0), new Point(250, 0), new Point(0, 250*ratio), new Point(250*ratio, 250*ratio)});
        }
        
        Mat homographyMatrix = Imgproc.getPerspectiveTransform(src, dst);
        
        Mat correctedImage = new Mat();
        if (horizontal) {
        	Imgproc.warpPerspective(sourceImage, correctedImage, homographyMatrix, new Size(250*ratio, 250));
        	
        }
        else {
        	Imgproc.warpPerspective(sourceImage, correctedImage, homographyMatrix, new Size(250, 250*ratio));
        	
        }

        return correctedImage;
    }

    public static BufferedImage Mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b);
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  
        return image;
    }
    
    public static void main(String[] args) {
    	System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    	launch(args);
    	
    }
}
