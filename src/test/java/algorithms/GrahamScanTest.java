// package algorithms;

// import geometry.Point;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// public class GrahamScanTest {

//     private GrahamScan grahamScan;

//     @BeforeEach
//     public void setUp() {
//         grahamScan = new GrahamScan();
//     }

//     @Test
//     public void testNullInput() {
//         List<Point> result = grahamScan.findConvexHull(null);
//         assertNull(result, "Null input should return null");
//     }

//     @Test
//     public void testLessThanThreePoints() {
//         List<Point> points = Arrays.asList(new Point(0, 0), new Point(1, 1));
//         List<Point> result = grahamScan.findConvexHull(points);
//         assertNull(result, "Less than 3 points should return null");
//     }

//     @Test
//     public void testTriangle() {
//         List<Point> points = Arrays.asList(
//             new Point(0, 0),
//             new Point(4, 0),
//             new Point(2, 2)
//         );

//         List<Point> hull = grahamScan.findConvexHull(points);

//         assertNotNull(hull, "Hull should not be null");
//         assertEquals(3, hull.size(), "Hull should have 3 points");
//         assertTrue(containsAllPoints(hull, points), "All input points should be in the hull");
//     }

//     @Test
//     public void testSquare() {
//         List<Point> points = Arrays.asList(
//             new Point(0, 0),
//             new Point(0, 2),
//             new Point(2, 2),
//             new Point(2, 0)
//         );

//         List<Point> hull = grahamScan.findConvexHull(points);

//         assertNotNull(hull, "Hull should not be null");
//         assertEquals(4, hull.size(), "Hull should have 4 points");
//         assertTrue(containsAllPoints(hull, points), "All input points should be in the hull");
//     }

//     @Test
//     public void testWithInteriorPoints() {
//         List<Point> points = new ArrayList<>();
//         // Square vertices
//         points.add(new Point(0, 0));
//         points.add(new Point(10, 0));
//         points.add(new Point(10, 10));
//         points.add(new Point(0, 10));

//         // Interior points
//         points.add(new Point(5, 5));
//         points.add(new Point(3, 3));
//         points.add(new Point(7, 7));

//         List<Point> hull = grahamScan.findConvexHull(points);

//         assertNotNull(hull, "Hull should not be null");
//         assertEquals(4, hull.size(), "Hull should have 4 points");

//         // Verify hull points
//         assertTrue(containsPoint(hull, new Point(0, 0)), "Hull should contain (0,0)");
//         assertTrue(containsPoint(hull, new Point(10, 0)), "Hull should contain (10,0)");
//         assertTrue(containsPoint(hull, new Point(10, 10)), "Hull should contain (10,10)");
//         assertTrue(containsPoint(hull, new Point(0, 10)), "Hull should contain (0,10)");

//         // Verify interior points are not in hull
//         assertFalse(containsPoint(hull, new Point(5, 5)), "Hull should not contain interior point (5,5)");
//     }

//     @Test
//     public void testCollinearPoints() {
//         List<Point> points = Arrays.asList(
//             new Point(0, 0),
//             new Point(1, 1),
//             new Point(2, 2),
//             new Point(3, 3),
//             new Point(4, 4)
//         );

//         List<Point> hull = grahamScan.findConvexHull(points);

//         assertNotNull(hull, "Hull should not be null");
//         assertEquals(2, hull.size(), "Hull should have 2 points for collinear points");

//         assertTrue(containsPoint(hull, new Point(0, 0)), "Hull should contain first point (0,0)");
//         assertTrue(containsPoint(hull, new Point(4, 4)), "Hull should contain last point (4,4)");
//     }

//     @Test
//     public void testRandomConvexPolygon() {
//         List<Point> points = Arrays.asList(
//             new Point(3, 0),
//             new Point(5, 2),
//             new Point(4, 5),
//             new Point(1, 5),
//             new Point(0, 2),
//             // Interior points
//             new Point(3, 3),
//             new Point(2, 2),
//             new Point(3, 2)
//         );

//         List<Point> expected = Arrays.asList(
//             new Point(3, 0),
//             new Point(5, 2),
//             new Point(4, 5),
//             new Point(1, 5),
//             new Point(0, 2)
//         );

//         List<Point> hull = grahamScan.findConvexHull(points);

//         assertNotNull(hull, "Hull should not be null");
//         assertEquals(5, hull.size(), "Hull should have 5 points");

//         for (Point p : expected) {
//             assertTrue(containsPoint(hull, p), "Hull should contain point " + p);
//         }
//     }

//     @Test
//     public void testReadPointsFromFile() throws IOException {
//         List<Point> points = grahamScan.readPointsFromFile("/root/Algorithm_Analysis/convex-hull/src/main/resources/sample-points.txt");
//         assertNotNull(points, "Points should be read from file");
//         assertFalse(points.isEmpty(), "Points list should not be empty");
//     }

//     // Helper methods
//     private boolean containsPoint(List<Point> points, Point target) {
//         for (Point p : points) {
//             if (p.equals(target)) {
//                 return true;
//             }
//         }
//         return false;
//     }

//     private boolean containsAllPoints(List<Point> container, List<Point> targets) {
//         for (Point target : targets) {
//             if (!containsPoint(container, target)) {
//                 return false;
//             }
//         }
//         return true;
//     }
// }