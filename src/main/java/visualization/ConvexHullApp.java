// filepath: /root/Algorithm_Analysis/convex-hull/src/main/java/visualizations/ConvexHullApp.java
package visualization;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane; // Or another layout
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
// Add other necessary imports

public class ConvexHullApp extends Application {

    private Canvas canvas;
    private GraphicsContext gc;
    // Add references to GrahamScan, points list, hull list etc.

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Convex Hull Visualization (Graham Scan)");

        // 1. Setup Layout
        BorderPane root = new BorderPane();
        canvas = new Canvas(600, 500); // Adjust size
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        // 2. Setup Controls (Buttons etc.)
        Button loadButton = new Button("Load Points");
        Button runButton = new Button("Run Graham Scan");
        Button clearButton = new Button("Clear");
        VBox controls = new VBox(10, loadButton, runButton, clearButton); // Simple vertical layout for buttons
        root.setLeft(controls); // Put controls on the left

        // 3. Add Event Handlers (Implement these methods)
        loadButton.setOnAction(e -> handleLoadPoints(primaryStage));
        runButton.setOnAction(e -> handleRunScan());
        clearButton.setOnAction(e -> handleClear());

        // 4. Create and Show Scene
        Scene scene = new Scene(root, 800, 600); // Overall window size
        primaryStage.setScene(scene);
        primaryStage.show();

        // 5. Initial Drawing (Optional)
        drawBackground(); // e.g., draw axes or grid
    }

    // --- Placeholder methods for event handlers and drawing ---

    private void handleLoadPoints(Stage ownerStage) {
        System.out.println("Load Points button clicked");
        // TODO: Implement FileChooser logic here
        // Read points using GrahamScan.readPointsFromFile
        // Store the points
        // Draw the loaded points
    }

    private void handleRunScan() {
        System.out.println("Run Scan button clicked");
        // TODO: Check if points are loaded
        // Run GrahamScan.findConvexHull
        // Store the hull points
        // Draw the hull (lines connecting hull points)
    }

    private void handleClear() {
        System.out.println("Clear button clicked");
        // TODO: Clear points and hull lists
        // Clear the canvas
        drawBackground(); // Redraw background if needed
    }

    private void drawBackground() {
        // Optional: Draw axes, grid, etc.
         gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear canvas
         gc.setFill(javafx.scene.paint.Color.LIGHTGRAY); // Example background
         gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawPoints() {
        // TODO: Iterate through the loaded points list
        // Draw each point as a small circle or dot on the canvas using gc
    }

     private void drawHull() {
        // TODO: Iterate through the hull points list
        // Draw lines connecting consecutive hull points using gc
        // Connect the last point back to the first point
    }


    public static void main(String[] args) {
        launch(args);
    }
}