// package geometry;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// public class GeometryUtilTest {

//     private GeometryUtil geometryUtil;
//     private static final double DELTA = 1e-9; // Tolerance for double comparisons

//     @BeforeEach
//     public void setUp() {
//         geometryUtil = new GeometryUtil();
//     }

//     // --- Orientation Tests ---

//     @Test
//     public void testOrientationCounterClockwise() {
//         Point p1 = new Point(0, 0);
//         Point p2 = new Point(4, 0);
//         Point p3 = new Point(2, 2);
//         assertEquals(-1, geometryUtil.orientation(p1, p2, p3), "Should be Counter-Clockwise");
//     }

//     @Test
//     public void testOrientationClockwise() {
//         Point p1 = new Point(0, 0);
//         Point p2 = new Point(2, 2);
//         Point p3 = new Point(4, 0);
//         assertEquals(1, geometryUtil.orientation(p1, p2, p3), "Should be Clockwise");
//     }

//     @Test
//     public void testOrientationCollinear() {
//         Point p1 = new Point(0, 0);
//         Point p2 = new Point(2, 2);
//         Point p3 = new Point(4, 4);
//         assertEquals(0, geometryUtil.orientation(p1, p2, p3), "Should be Collinear");
//     }

//     @Test
//     public void testIsCounterClockwiseTrue() {
//         Point p1 = new Point(0, 0);
//         Point p2 = new Point(4, 0);
//         Point p3 = new Point(2, 2);
//         assertTrue(geometryUtil.isCounterClockwise(p1, p2, p3), "Should return true for Counter-Clockwise");
//     }

//     @Test
//     public void testIsCounterClockwiseFalse() {
//         Point p1 = new Point(0, 0);
//         Point p2 = new Point(2, 2);
//         Point p3 = new Point(4, 0);
//         assertFalse(geometryUtil.isCounterClockwise(p1, p2, p3), "Should return false for Clockwise");
//     }

//     @Test
//     public void testIsCounterClockwiseCollinear() {
//         Point p1 = new Point(0, 0);
//         Point p2 = new Point(2, 2);
//         Point p3 = new Point(4, 4);
//         assertFalse(geometryUtil.isCounterClockwise(p1, p2, p3), "Should return false for Collinear");
//     }

//     // --- Angle Calculation Tests ---

//     @Test
//     public void testPolarAngle() {
//         Point reference = new Point(1, 1);
//         assertEquals(0.0, geometryUtil.polarAngle(reference, new Point(3, 1)), DELTA, "Angle should be 0 degrees");
//         assertEquals(45.0, geometryUtil.polarAngle(reference, new Point(3, 3)), DELTA, "Angle should be 45 degrees");
//         assertEquals(90.0, geometryUtil.polarAngle(reference, new Point(1, 3)), DELTA, "Angle should be 90 degrees");
//         assertEquals(135.0, geometryUtil.polarAngle(reference, new Point(-1, 3)), DELTA, "Angle should be 135 degrees");
//         assertEquals(180.0, geometryUtil.polarAngle(reference, new Point(-1, 1)), DELTA, "Angle should be 180 degrees");
//         assertEquals(-135.0, geometryUtil.polarAngle(reference, new Point(-1, -1)), DELTA, "Angle should be -135 degrees");
//         assertEquals(-90.0, geometryUtil.polarAngle(reference, new Point(1, -1)), DELTA, "Angle should be -90 degrees");
//         assertEquals(-45.0, geometryUtil.polarAngle(reference, new Point(3, -1)), DELTA, "Angle should be -45 degrees");
//     }

//     // --- Distance Utility Tests ---

//     @Test
//     public void testSquaredDistance() {
//         Point p1 = new Point(1, 2);
//         Point p2 = new Point(4, 6);
//         // (4-1)^2 + (6-2)^2 = 3^2 + 4^2 = 9 + 16 = 25
//         assertEquals(25.0, geometryUtil.squaredDistance(p1, p2), DELTA, "Squared distance should be 25");
//         assertEquals(0.0, geometryUtil.squaredDistance(p1, p1), DELTA, "Squared distance to self should be 0");
//     }

//     @Test
//     public void testCalculateDistance() {
//         Point p1 = new Point(1, 2);
//         Point p2 = new Point(4, 6);
//         // sqrt(25) = 5
//         assertEquals(5.0, geometryUtil.calculateDistance(p1, p2), DELTA, "Distance should be 5");
//         assertEquals(0.0, geometryUtil.calculateDistance(p1, p1), DELTA, "Distance to self should be 0");
//     }

//     // --- Convex Hull Helper Tests ---

//     @Test
//     public void testFindLowestPoint() {
//         List<Point> points = Arrays.asList(
//             new Point(5, 5),
//             new Point(1, 1), // Lowest y, leftmost
//             new Point(3, 1), // Lowest y, rightmost
//             new Point(2, 8)
//         );
//         Point expected = new Point(1, 1);
//         assertEquals(expected, geometryUtil.findLowestPoint(points), "Should find the lowest, leftmost point");
//     }

//     @Test
//     public void testFindLowestPointEmptyList() {
//         List<Point> points = new ArrayList<>();
//         assertNull(geometryUtil.findLowestPoint(points), "Should return null for empty list");
//     }

//     @Test
//     public void testFindLowestPointNullList() {
//          assertNull(geometryUtil.findLowestPoint(null), "Should return null for null list");
//     }

//     @Test
//     public void testSortPointByPolarAngle() {
//         Point reference = new Point(0, 0);
//         Point p1 = new Point(1, 0);  // 0 degrees
//         Point p2 = new Point(1, 1);  // 45 degrees
//         Point p3 = new Point(0, 1);  // 90 degrees
//         Point p4 = new Point(-1, 1); // 135 degrees
//         Point p5 = new Point(-1, 0); // 180 degrees
//         Point p6 = new Point(2, 0);  // 0 degrees, further

//         List<Point> points = new ArrayList<>(Arrays.asList(p3, p5, p1, p4, p2, p6));
//         geometryUtil.sortPointByPolarAngle(points, reference);

//         List<Point> expectedOrder = Arrays.asList(p1, p6, p2, p3, p4, p5);

//         assertEquals(expectedOrder.size(), points.size(), "List size should remain the same");
//         for (int i = 0; i < expectedOrder.size(); i++) {
//             assertEquals(expectedOrder.get(i), points.get(i), "Point at index " + i + " is incorrect");
//         }
//     }

//      @Test
//     public void testSortPointByPolarAngleWithReferenceRemoval() {
//         Point reference = new Point(0, 0);
//         Point p1 = new Point(1, 0);
//         Point p2 = new Point(1, 1);

//         List<Point> points = new ArrayList<>(Arrays.asList(p2, reference, p1));
//         geometryUtil.sortPointByPolarAngle(points, reference);

//         List<Point> expectedOrder = Arrays.asList(p1, p2);

//         assertEquals(expectedOrder.size(), points.size(), "List size should be 2 after removing reference");
//         assertFalse(points.contains(reference), "Reference point should be removed");
//         assertEquals(expectedOrder.get(0), points.get(0), "First point should be p1");
//         assertEquals(expectedOrder.get(1), points.get(1), "Second point should be p2");
//     }

//     @Test
//     public void testSortPointByPolarAngleEmptyList() {
//         List<Point> points = new ArrayList<>();
//         Point reference = new Point(0, 0);
//         // Should not throw an exception and list should remain empty
//         geometryUtil.sortPointByPolarAngle(points, reference);
//         assertTrue(points.isEmpty(), "List should remain empty");
//     }
// }