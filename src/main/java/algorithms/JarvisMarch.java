package algorithms;

import geometry.Point;
import geometry.GeometryUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Jarvis March (Gift Wrapping) algorithm for computing the convex hull of a set of points.
 * Time complexity: O(nh) where n is the number of points and h is the number of points on the hull.
 */
public class JarvisMarch {
    private GeometryUtil geometryUtil = new GeometryUtil();
    private List<Point> convexHull;
    

    public List<Point> readPointsFromFile(String filename) throws IOException {
        List<Point> points = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Handle comma-separated or space-separated formats
                String[] coordinates;
                if (line.contains(",")) {
                    coordinates = line.trim().split("\\s*,\\s*");
                } else {
                    coordinates = line.trim().split("\\s+");
                }
                
                if (coordinates.length >= 2) {
                    try {
                        double x = Double.parseDouble(coordinates[0]);
                        double y = Double.parseDouble(coordinates[1]);
                        points.add(new Point(x, y));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid point: " + line);
                    }
                }
            }
        }
        return points;
    }
    
   
    public List<Point> findConvexHull(List<Point> points) {
        // Basic validations
        if (points == null || points.size() < 3) {
            return null; // Not enough points to form a convex hull
        }
        
        convexHull = new ArrayList<>();
        
        // Find the leftmost point (with the smallest x-coordinate)
        Point startPoint = findLeftmostPoint(points);
        
        // Start from the leftmost point and keep finding the next point
        Point currentPoint = startPoint;
        
        do {
            // Add the current point to the convex hull
            convexHull.add(currentPoint);
            
            // Find the next point with the smallest polar angle
            Point nextPoint = points.get(0);
            
            // If the next candidate is the same as our current point, select another candidate
            if (nextPoint.equals(currentPoint) && points.size() > 1) {
                nextPoint = points.get(1);
            }
            
            // Find the point with the smallest polar angle from the current point
            for (Point candidate : points) {
                // Skip the current point
                if (candidate.equals(currentPoint)) {
                    continue;
                }
                
                // If next is not yet set or the candidate is better, update next
                int orientation = geometryUtil.orientation(currentPoint, nextPoint, candidate);
                
                // If candidate is more counter-clockwise than next
                if (nextPoint.equals(currentPoint) ||
                    orientation == -1 || // Counter-clockwise turn
                    // If collinear, take the farthest point
                    (orientation == 0 && geometryUtil.squaredDistance(currentPoint, candidate) > 
                     geometryUtil.squaredDistance(currentPoint, nextPoint))) {
                    nextPoint = candidate;
                }
            }
            
            // Next becomes our new current
            currentPoint = nextPoint;
            
        } while (!currentPoint.equals(startPoint)); // Continue until we reach our starting point
        
        return convexHull;
    }
    
    
    private Point findLeftmostPoint(List<Point> points) {
        Point leftmost = points.get(0);
        
        for (Point p : points) {
            // If p is more to the left than the current leftmost
            if (p.getX() < leftmost.getX() || 
                (p.getX() == leftmost.getX() && p.getY() < leftmost.getY())) {
                leftmost = p;
            }
        }
        
        return leftmost;
    }
}
