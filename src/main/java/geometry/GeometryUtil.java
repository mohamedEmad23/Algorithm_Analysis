package geometry;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class GeometryUtil{

    // Orientation Operations
    public int orientation(Point p1, Point p2, Point p3){
        double slope1 = p2.getY() - p1.getY() / p2.getX() - p1.getX();
        double slope2 = p3.getY() - p2.getY() / p3.getX() - p2.getX();
        if (slope1 < slope2){
            System.out.println("Orientation of Points are Counter-Clockwise");
            return -1;
        }else if (slope1 > slope2){
            System.out.println("Orientation of Points are Clockwise");
            return 1;
        }else{
            System.out.println("Orientations of Points are Collinear");
            return 0;
        }
    }


    public boolean isCounterClockwise(Point p1, Point p2, Point p3){
        return orientation(p1, p2, p3) < 0;
    }

    // Angle Calculations
    public double polarAngle(Point reference, Point target){
        double deltaX = target.getX() - reference.getX();
        double deltaY = target.getY() - reference.getY();
        double angle  = Math.toDegrees(Math.atan2(deltaY, deltaX));
        return angle; 
    }

    public double compareByPolarAngle(Point reference, Point p1, Point p2){
        double angle1 = polarAngle(reference, p1);
        double angle2 = polarAngle(p1, p2);

        if (angle1 < angle2){
            return -1;
        }else if (angle1 > angle2){
            return 1;
        }else{
            double dist1 = squaredDistance(reference, p1);
            double dist2 = squaredDistance(reference, p2);
            return Math.max(dist1, dist2);
        }
    }

    

    // Distance Utilities
    public double squaredDistance(Point p1, Point p2){
        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();
        return Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
    }


    public double calculateDistance(Point p1, Point p2){
        return Math.sqrt(squaredDistance(p1, p2));
    }

    // Convex Hull Helpers
    public Point findLowestPoint(List<Point> points){
        if (points == null || points.isEmpty()){
            return null;
        }
        return Collections.min(points, Comparator.comparing(Point::getY).thenComparing(Point::getX));
    }

    public void sortPointByPolarAngle(List<Point> points, Point referencePoint){
        if (points == null || points.isEmpty()){
            System.out.print("List is Empty");
            return;
        }
        if (points.contains(referencePoint)){
            points.remove(referencePoint);
        }
        // Sorting Based on Polar Angle
        Collections.sort(points, (p1, p2) -> {
            double angle1 = polarAngle(referencePoint, p1);
            double angle2 = polarAngle(referencePoint, p2);
            
            if (Double.compare(angle1, angle2) != 0) {
                return Double.compare(angle1, angle2);
            } else {
                double dist1 = squaredDistance(referencePoint, p1);
                double dist2 = squaredDistance(referencePoint, p2);
                return Double.compare(dist2, dist1);
            }
        });

    }


    // Vector Operations
    // crossProduct(Piont p1, Point p2, Point p3)
    // dotProduct (Point p1, Point p2, Point p3)
}