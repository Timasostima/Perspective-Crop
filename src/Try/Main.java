package Try;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.dnn.*;

public class Main extends Application {

    private static List<Point> points = new ArrayList<>();
    private static int pointIndex = 0;
    private static ImageView imageView;
    private static BufferedImage originalImage;
    BufferedImage result;
    BufferedImage resultIA;
    

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
            		    
            		    
            		    final String Script_Path = "C:\\Users\\Serious Tim\\eclipse-workspace\\Perspective-Crop\\scripts\\image_upscaler.py";
            		    ProcessBuilder Process_Builder = new ProcessBuilder("python", Script_Path).inheritIO();

            		    Process Demo_Process = Process_Builder.start();
            		    Demo_Process.waitFor();
            		    
            		    BufferedReader Buffered_Reader = new BufferedReader(new InputStreamReader(Demo_Process.getInputStream()));
            		    String Output_line = "";

            		    while ((Output_line = Buffered_Reader.readLine()) != null) {
            		      System.out.println(Output_line);
            		    }
            		    
            		    resultIA = ImageIO.read(new File("imgs//resultIA.jpg"));
            		    BorderPane root2 = new BorderPane();
            		    imageView.setImage(SwingFXUtils.toFXImage(resultIA, null));
            		    root2.setCenter(imageView);
            		    int sceneWidth = resultIA.getWidth()-1;
            	        int sceneHeight = resultIA.getHeight()-1;
            		    Scene scene2 = new Scene(root2, sceneWidth,sceneHeight);
            		    primaryStage.setScene(scene2);
            		} catch (IOException e) {
            		    e.printStackTrace();
            		} catch (InterruptedException e) {
						// TODO Auto-generated catch block
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
        double width = (double)(rect1.width+rect2.width)/2;
        double height = (double)(rect3.height+rect4.height)/2;
        double ratio = (width<height ? width/height : height/width);
        boolean horizontal = width>height;
        
        System.out.println(ratio);
        
        points.stream().forEach(System.out::println);
        Mat sourceImage = Imgcodecs.imread("imgs//example.jpg");


        MatOfPoint2f src = new MatOfPoint2f(srcPoints);
        MatOfPoint2f dst;
        if (!horizontal) {
//        	dst = new MatOfPoint2f(new Point[]{new Point(0, 0), new Point(700*ratio, 0), new Point(0, 700), new Point(700*ratio, 700)});
        	dst = new MatOfPoint2f(new Point[]{new Point(0, 0), new Point(width, 0), new Point(0, height), new Point(width, height)});
        }
        else {
//        	dst = new MatOfPoint2f(new Point[]{new Point(0, 0), new Point(700, 0), new Point(0, 700*ratio), new Point(700, 700*ratio)});
        	dst = new MatOfPoint2f(new Point[]{new Point(0, 0), new Point(width, 0), new Point(0, height), new Point(width, height)});
        }
        
        Mat homographyMatrix = Imgproc.getPerspectiveTransform(src, dst);
        
        Mat correctedImage = new Mat();
        if (!horizontal) {
//        	Imgproc.warpPerspective(sourceImage, correctedImage, homographyMatrix, new Size(700*ratio, 700));
        	Imgproc.warpPerspective(sourceImage, correctedImage, homographyMatrix, new Size(width, height));
        	
        }
        else {
//        	Imgproc.warpPerspective(sourceImage, correctedImage, homographyMatrix, new Size(700, 700*ratio));
        	Imgproc.warpPerspective(sourceImage, correctedImage, homographyMatrix, new Size(width, height));

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
