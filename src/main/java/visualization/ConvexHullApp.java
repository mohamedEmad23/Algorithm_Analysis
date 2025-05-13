package visualization;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

import algorithms.GrahamScan;
import algorithms.ChanAlgorithm;
import algorithms.JarvisMarch;
import geometry.Point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvexHullApp extends Application {

    private Canvas canvas;
    private GraphicsContext gc;
    private GrahamScan grahamScan;
    private JarvisMarch jarvisMarch;
    private ChanAlgorithm chanAlgorithm;
    private List<Point> points;
    private List<Point> convexHull;
    
    // Algorithm selection
    private String currentAlgorithm = "Graham Scan";
    
    // Constants for visualization
    private static final int POINT_RADIUS = 5;
    private static final Color POINT_COLOR = Color.BLUE;
    private static final Color HULL_COLOR = Color.RED;
    private static final Color BACKGROUND_COLOR = Color.LIGHTGRAY;
    
    // Status information
    private Label statusLabel;

    // Coordinate system parameters
    private double xOffset = 200; // Increased to accommodate negative coordinates
    private double yOffset = 200; // Increased to accommodate negative coordinates
    private double scale = 1.0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Convex Hull Visualization");
        
        // Initialize algorithms and point lists
        grahamScan = new GrahamScan();
        jarvisMarch = new JarvisMarch();
        chanAlgorithm = new ChanAlgorithm();
        points = new ArrayList<>();
        convexHull = new ArrayList<>();

        // 1. Setup Layout
        BorderPane root = new BorderPane();
        canvas = new Canvas(600, 500);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        // 2. Setup Controls (Buttons etc.)
        Button loadButton = new Button("Load Points");
        Button runButton = new Button("Run Algorithm");
        Button clearButton = new Button("Clear");
        
        // Algorithm selection dropdown
        Label algorithmLabel = new Label("Algorithm:");
        ComboBox<String> algorithmSelector = new ComboBox<>();
        algorithmSelector.getItems().addAll("Graham Scan", "Jarvis March", "Chan's Algorithm");
        algorithmSelector.setValue("Graham Scan");
        algorithmSelector.setOnAction(e -> currentAlgorithm = algorithmSelector.getValue());
        
        // Layout for algorithm selection
        HBox algorithmBox = new HBox(10, algorithmLabel, algorithmSelector);
        algorithmBox.setPadding(new Insets(10));
        
        // Status label for algorithm information
        statusLabel = new Label("Status: Ready");
        statusLabel.setMinWidth(200);
        statusLabel.setWrapText(true);
        
        // Add info labels for coordinates when hovering
        Label pointInfoLabel = new Label("Point: N/A");
        
        // Add mouse move handler to show coordinates
        canvas.setOnMouseMoved(e -> {
            double worldX = (e.getX() - xOffset) / scale;
            double worldY = (canvas.getHeight() - yOffset - e.getY()) / scale;
            pointInfoLabel.setText(String.format("Mouse at: (%.1f, %.1f)", worldX, worldY));
        });
        
        // Vertical box for all controls
        VBox controls = new VBox(10, algorithmBox, loadButton, runButton, clearButton, statusLabel, pointInfoLabel);
        controls.setPadding(new Insets(10));
        root.setLeft(controls);

        // 3. Add Event Handlers (Implement these methods)
        loadButton.setOnAction(e -> handleLoadPoints(primaryStage));
        runButton.setOnAction(e -> handleRunScan());
        clearButton.setOnAction(e -> handleClear());

        // 4. Create and Show Scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // 5. Initial Drawing
        drawBackground();
        drawCoordinateSystem();
    }

    // --- Event handler implementations ---

    private void handleLoadPoints(Stage ownerStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Points File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        
        File selectedFile = fileChooser.showOpenDialog(ownerStage);
        if (selectedFile != null) {
            try {
                // Clear existing points
                points.clear();
                convexHull.clear();
                
                // Load points from file
                points = grahamScan.readPointsFromFile(selectedFile.getAbsolutePath());
                
                // Adjust scale based on points
                adjustScale();
                
                // Redraw
                drawBackground();
                drawCoordinateSystem();
                drawPoints();
                
                System.out.println("Loaded " + points.size() + " points");
            } catch (IOException e) {
                System.err.println("Error loading points: " + e.getMessage());
            }
        }
    }

    private void handleRunScan() {
        if (points == null || points.size() < 3) {
            System.out.println("Not enough points to compute convex hull (need at least 3)");
            return;
        }
        
        // Use the selected algorithm
        long startTime = System.nanoTime();
        
        switch (currentAlgorithm) {
            case "Graham Scan":
                convexHull = grahamScan.findConvexHull(points);
                break;
            case "Jarvis March":
                convexHull = jarvisMarch.findConvexHull(points);
                break;
            case "Chan's Algorithm":
                convexHull = chanAlgorithm.findConvexHull(points);
                break;
            default:
                convexHull = grahamScan.findConvexHull(points);
        }
        
        long endTime = System.nanoTime();
        double elapsedTimeMs = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
        
        // Redraw with hull
        drawBackground();
        drawCoordinateSystem();
        drawPoints();
        drawHull();
        
        // Update status label
        String status = currentAlgorithm + " computed hull with " + convexHull.size() + 
                        " vertices in " + String.format("%.2f", elapsedTimeMs) + " ms";
        statusLabel.setText("Status: " + status);
        
        System.out.println(status);
    }

    private void handleClear() {
        points.clear();
        convexHull.clear();
        drawBackground();
        drawCoordinateSystem();
        System.out.println("Cleared all points and hull");
    }

    // --- Drawing methods ---

    private void drawBackground() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    private void drawCoordinateSystem() {
        // Draw axes
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        // Calculate the position of the origin in screen coordinates
        double originX = xOffset; // X origin is at xOffset
        double originY = canvas.getHeight() - yOffset; // Y origin is at canvas.getHeight() - yOffset
        
        // Draw coordinate grid and labels - before axes to keep grid behind
        gc.setLineWidth(0.3);
        gc.setStroke(Color.LIGHTGRAY);
        
        // Draw grid
        int gridSpacing = 25; // pixels between grid lines
        int numLines = 20; // number of grid lines in each direction
        
        // Horizontal grid lines
        for (int i = -numLines; i <= numLines; i++) {
            double y = originY - i * gridSpacing;
            if (y >= 0 && y <= canvas.getHeight()) {
                gc.strokeLine(0, y, canvas.getWidth(), y);
            }
        }
        
        // Vertical grid lines
        for (int i = -numLines; i <= numLines; i++) {
            double x = originX + i * gridSpacing;
            if (x >= 0 && x <= canvas.getWidth()) {
                gc.strokeLine(x, 0, x, canvas.getHeight());
            }
        }
        
        // Draw axes on top of grid
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.5);
        
        // X-axis
        gc.strokeLine(0, originY, canvas.getWidth(), originY);
        
        // Y-axis
        gc.strokeLine(originX, 0, originX, canvas.getHeight());
                     
        // Axis labels
        gc.setFill(Color.BLACK);
        gc.fillText("X", canvas.getWidth() - 20, originY - 10);
        gc.fillText("Y", originX + 10, 20);
        
        // Draw origin label
        gc.setFill(Color.BLUE);
        gc.fillText("(0,0)", originX + 5, originY + 15);
        
        // Draw tick marks on axes
        int tickSpacing = 50; // pixels between ticks
        int numTicks = 10; // number of ticks in each direction
        
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        
        // X-axis ticks
        for (int i = -numTicks; i <= numTicks; i++) {
            if (i == 0) continue; // Skip origin
            double x = originX + i * tickSpacing;
            gc.strokeLine(x, originY - 5, x, originY + 5);
            
            // Only show label if within canvas bounds
            if (x >= 0 && x <= canvas.getWidth()) {
                gc.fillText(String.valueOf((int)(i * tickSpacing / scale)), x - 5, originY + 20);
            }
        }
        
        // Y-axis ticks
        for (int i = -numTicks; i <= numTicks; i++) {
            if (i == 0) continue; // Skip origin
            double y = originY - i * tickSpacing;
            gc.strokeLine(originX - 5, y, originX + 5, y);
            
            // Only show label if within canvas bounds
            if (y >= 0 && y <= canvas.getHeight()) {
                gc.fillText(String.valueOf((int)(i * tickSpacing / scale)), originX - 30, y + 5);
            }
        }
    }

    private void drawPoints() {
        if (points == null || points.isEmpty()) return;
        
        gc.setFill(POINT_COLOR);
        
        for (Point p : points) {
            // Convert from world coordinates to screen coordinates
            double screenX = worldToScreenX(p.getX());
            double screenY = worldToScreenY(p.getY());
            
            gc.fillOval(screenX - POINT_RADIUS, screenY - POINT_RADIUS, 
                      POINT_RADIUS * 2, POINT_RADIUS * 2);
        }
    }

    private void drawHull() {
        if (convexHull == null || convexHull.size() < 3) return;
        
        gc.setStroke(HULL_COLOR);
        gc.setLineWidth(2);
        
        // Draw hull edges
        for (int i = 0; i < convexHull.size(); i++) {
            Point current = convexHull.get(i);
            Point next = convexHull.get((i + 1) % convexHull.size());
            
            double x1 = worldToScreenX(current.getX());
            double y1 = worldToScreenY(current.getY());
            double x2 = worldToScreenX(next.getX());
            double y2 = worldToScreenY(next.getY());
            
            gc.strokeLine(x1, y1, x2, y2);
        }
    }
    
    private void adjustScale() {
        if (points == null || points.isEmpty()) return;
        
        // Find bounds of the points
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        
        for (Point p : points) {
            minX = Math.min(minX, p.getX());
            maxX = Math.max(maxX, p.getX());
            minY = Math.min(minY, p.getY());
            maxY = Math.max(maxY, p.getY());
        }
        
        // Calculate the range
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;
        
        // Add padding to ensure all points are visible (10% padding)
        double paddingX = Math.max(rangeX * 0.1, 1);  // Ensure at least 1 unit padding
        double paddingY = Math.max(rangeY * 0.1, 1);
        
        minX -= paddingX;
        maxX += paddingX;
        minY -= paddingY;
        maxY += paddingY;
        
        // Update ranges with padding
        rangeX = maxX - minX;
        rangeY = maxY - minY;
        
        // Calculate available space, taking into account the offsets for the axes
        double availableWidth = canvas.getWidth() - 2 * xOffset;
        double availableHeight = canvas.getHeight() - 2 * yOffset;
        
        // Calculate required scale
        if (rangeX > 0 && rangeY > 0) {
            double scaleX = availableWidth / rangeX;
            double scaleY = availableHeight / rangeY;
            
            // Use the smaller scale to ensure everything fits
            scale = Math.min(scaleX, scaleY) * 0.8; // 80% to add some margin
        } else {
            // Default scale if all points are on a line
            scale = 20.0; // Default to a reasonable scale
        }
        
        // Print the range of coordinates to help with debugging
        System.out.println("Point range: X[" + minX + " to " + maxX + "], Y[" + minY + " to " + maxY + "]");
        System.out.println("Scale: " + scale);
        
        // Update status label with range information
        statusLabel.setText("Status: Points loaded. Range X[" + String.format("%.1f", minX) + 
                          " to " + String.format("%.1f", maxX) + "], Y[" + 
                          String.format("%.1f", minY) + " to " + String.format("%.1f", maxY) + "]");
    }

    // Helper methods for coordinate conversion
    /**
     * Converts world X coordinate to screen X coordinate.
     * Positive world X points are rendered to the right of the origin.
     * Negative world X points are rendered to the left of the origin.
     */
    private double worldToScreenX(double worldX) {
        // Origin is at xOffset, so add worldX * scale to xOffset
        return xOffset + worldX * scale;
    }
    
    /**
     * Converts world Y coordinate to screen Y coordinate.
     * Positive world Y points are rendered above the origin.
     * Negative world Y points are rendered below the origin.
     * Note: In screen coordinates, Y increases downward, but in world coordinates, Y increases upward.
     */
    private double worldToScreenY(double worldY) {
        // Origin is at canvas.getHeight() - yOffset, so subtract worldY * scale from that
        return (canvas.getHeight() - yOffset) - worldY * scale;
    }

    public static void main(String[] args) {
        launch(args);
    }
}