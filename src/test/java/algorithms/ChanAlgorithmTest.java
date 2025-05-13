package algorithms;

import geometry.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChanAlgorithmTest {

    private ChanAlgorithm chanAlgorithm;

    @BeforeEach
    public void setUp() {
        chanAlgorithm = new ChanAlgorithm();
    }

    @Test
    public void testNullInput() {
        List<Point> result = chanAlgorithm.findConvexHull(null);
        assertNull(result, "Null input should return null");
    }

    @Test
    public void testLessThanThreePoints() {
        List<Point> points = Arrays.asList(new Point(0, 0), new Point(1, 1));
        List<Point> result = chanAlgorithm.findConvexHull(points);
        assertNull(result, "Less than 3 points should return null");
    }

    @Test
    public void testTriangle() {
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(4, 0),
            new Point(2, 2)
        );

        List<Point> hull = chanAlgorithm.findConvexHull(points);

        assertNotNull(hull, "Hull should not be null");
        assertEquals(3, hull.size(), "Hull should have 3 points");
        assertTrue(containsAllPoints(hull, points), "All input points should be in the hull");
    }

    @Test
    public void testSquare() {
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(0, 2),
            new Point(2, 2),
            new Point(2, 0)
        );

        List<Point> hull = chanAlgorithm.findConvexHull(points);

        assertNotNull(hull, "Hull should not be null");
        assertEquals(4, hull.size(), "Hull should have 4 points");
        assertTrue(containsAllPoints(hull, points), "All input points should be in the hull");
    }

    @Test
    public void testWithInteriorPoints() {
        List<Point> points = new ArrayList<>();
        // Square vertices
        points.add(new Point(0, 0));
        points.add(new Point(10, 0));
        points.add(new Point(10, 10));
        points.add(new Point(0, 10));

        // Interior points
        points.add(new Point(5, 5));
        points.add(new Point(3, 3));
        points.add(new Point(7, 7));

        List<Point> hull = chanAlgorithm.findConvexHull(points);

        assertNotNull(hull, "Hull should not be null");
        assertEquals(4, hull.size(), "Hull should have 4 points");

        // Verify hull points
        assertTrue(containsPoint(hull, new Point(0, 0)), "Hull should contain (0,0)");
        assertTrue(containsPoint(hull, new Point(10, 0)), "Hull should contain (10,0)");
        assertTrue(containsPoint(hull, new Point(10, 10)), "Hull should contain (10,10)");
        assertTrue(containsPoint(hull, new Point(0, 10)), "Hull should contain (0,10)");

        // Verify interior points are not in hull
        assertFalse(containsPoint(hull, new Point(5, 5)), "Hull should not contain interior point (5,5)");
    }

    @Test
    public void testLargePointSet() {
        // Create a larger set of points (including some interior points)
        List<Point> points = new ArrayList<>();
        
        // Create a large convex polygon (circle-like)
        int numPoints = 30;
        double radius = 10.0;
        double centerX = 10.0;
        double centerY = 10.0;
        
        for (int i = 0; i < numPoints; i++) {
            double angle = 2 * Math.PI * i / numPoints;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            points.add(new Point(x, y));
        }
        
        // Add some interior points
        for (int i = 0; i < 20; i++) {
            double r = Math.random() * (radius * 0.8);
            double angle = Math.random() * 2 * Math.PI;
            double x = centerX + r * Math.cos(angle);
            double y = centerY + r * Math.sin(angle);
            points.add(new Point(x, y));
        }
        
        // Compute the convex hull
        List<Point> hull = chanAlgorithm.findConvexHull(points);
        
        assertNotNull(hull, "Hull should not be null");
        assertTrue(hull.size() <= numPoints, "Hull size should not exceed number of outer points");
        assertTrue(hull.size() >= 3, "Hull should have at least 3 points");
    }

    @Test
    public void testCircularPointSet() {
        // Create a perfect circle of points
        List<Point> points = new ArrayList<>();
        
        int numPoints = 24;  // Matches the sample-points.txt file
        double radius = 5.0;
        double centerX = 10.0;
        double centerY = 10.0;
        
        // Generate points in a perfect circle
        for (int i = 0; i < numPoints; i++) {
            double angle = 2 * Math.PI * i / numPoints;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            points.add(new Point(x, y));
        }
        
        // Compute the convex hull with Chan's algorithm
        List<Point> chanHull = chanAlgorithm.findConvexHull(points);
        
        // Also compute with Graham Scan for comparison
        GrahamScan grahamScan = new GrahamScan();
        List<Point> grahamHull = grahamScan.findConvexHull(points);
        
        // Tests
        assertNotNull(chanHull, "Hull should not be null");
        assertEquals(numPoints, chanHull.size(), "For a circle, all points should be in the hull");
        assertEquals(grahamHull.size(), chanHull.size(), "Chan and Graham should produce same size hull");
        
        // Verify that all points are included in the hull
        for (Point p : points) {
            assertTrue(containsPoint(chanHull, p), "Hull should contain all points in a circle");
        }
    }

    @Test
    public void testReadPointsFromFile() throws IOException {
        List<Point> points = chanAlgorithm.readPointsFromFile("/root/Algorithm_Analysis/convex-hull/src/main/resources/sample-points.txt");
        assertNotNull(points, "Points should be read from file");
        assertFalse(points.isEmpty(), "Points list should not be empty");
    }

    @Test
    public void compareWithOtherAlgorithms() throws IOException {
        // Load the same set of points for all algorithms
        List<Point> points = chanAlgorithm.readPointsFromFile("/root/Algorithm_Analysis/convex-hull/src/main/resources/sample-points.txt");
        
        // Compute convex hulls using different algorithms
        List<Point> chanHull = chanAlgorithm.findConvexHull(points);
        
        GrahamScan grahamScan = new GrahamScan();
        List<Point> grahamHull = grahamScan.findConvexHull(points);
        
        JarvisMarch jarvisMarch = new JarvisMarch();
        List<Point> jarvisHull = jarvisMarch.findConvexHull(points);
        
        // All algorithms should produce hulls with the same number of points
        assertEquals(grahamHull.size(), chanHull.size(), 
                    "Graham Scan and Chan's Algorithm should produce hulls of the same size");
        assertEquals(jarvisHull.size(), chanHull.size(), 
                    "Jarvis March and Chan's Algorithm should produce hulls of the same size");
        
        // Each point in one hull should be in the other hulls
        for (Point p : grahamHull) {
            assertTrue(containsPoint(chanHull, p), 
                     "All points from Graham Scan hull should be in Chan's Algorithm hull");
        }
        
        for (Point p : jarvisHull) {
            assertTrue(containsPoint(chanHull, p), 
                     "All points from Jarvis March hull should be in Chan's Algorithm hull");
        }
    }

    // Helper methods
    private boolean containsPoint(List<Point> points, Point target) {
        for (Point p : points) {
            if (p.equals(target)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAllPoints(List<Point> container, List<Point> targets) {
        for (Point target : targets) {
            if (!containsPoint(container, target)) {
                return false;
            }
        }
        return true;
    }
}