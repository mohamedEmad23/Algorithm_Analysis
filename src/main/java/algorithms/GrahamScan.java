package algorithms;
import geometry.Point;
import geometry.GeometryUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GrahamScan {
    private GeometryUtil geometryUtil = new GeometryUtil();
    private List<Point> points;
    private List<Point> convexHull;

    public List<Point> readPointsFromFile(String filename) throws IOException {
        points = new ArrayList<>();

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
                        // Skip invalid entries
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
            return null;
        }
        
        // Handle special case: all points are collinear
        boolean allCollinear = true;
        if (points.size() >= 3) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            
            for (int i = 2; i < points.size(); i++) {
                if (geometryUtil.orientation(p1, p2, points.get(i)) != 0) {
                    allCollinear = false;
                    break;
                }
            }
        }
        
        if (allCollinear) {
            // For collinear points, return just the endpoints
            Point minX = points.get(0);
            Point maxX = points.get(0);
            
            for (Point p : points) {
                if (p.getX() < minX.getX() || (p.getX() == minX.getX() && p.getY() < minX.getY())) {
                    minX = p;
                }
                if (p.getX() > maxX.getX() || (p.getX() == maxX.getX() && p.getY() > maxX.getY())) {
                    maxX = p;
                }
            }
            
            List<Point> hull = new ArrayList<>();
            hull.add(minX);
            if (!minX.equals(maxX)) {
                hull.add(maxX);
            }
            return hull;
        }
        
        // Standard Graham Scan implementation for non-collinear points
        convexHull = new ArrayList<>();
        
        // Find the point with lowest y-coordinate (anchor point)
        Point anchorPoint = geometryUtil.findLowestPoint(points);
        
        // Create a copy of points and remove anchor point
        List<Point> sortedPoints = new ArrayList<>(points);
        sortedPoints.remove(anchorPoint);
        
        // Sort points by polar angle with respect to anchor point
        geometryUtil.sortPointByPolarAngle(sortedPoints, anchorPoint);
        
        // Start with first three points
        Stack<Point> stack = new Stack<>();
        stack.push(anchorPoint);
        
        // Handle edge cases with few points
        if (sortedPoints.size() > 0) {
            stack.push(sortedPoints.get(0));
        }
        
        // Process remaining points
        for (int i = 1; i < sortedPoints.size(); i++) {
            Point top = stack.pop();
            
            // Remove points that make a non-left turn
            while (!stack.isEmpty() && geometryUtil.orientation(stack.peek(), top, sortedPoints.get(i)) >= 0) {
                top = stack.pop();
            }
            
            stack.push(top);
            stack.push(sortedPoints.get(i));
        }
        
        // Convert stack to list
        convexHull = new ArrayList<>(stack);
        return convexHull;

    }

}