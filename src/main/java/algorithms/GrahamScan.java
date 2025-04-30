package algorithms;
import geometry.Point;
import geometry.GeometryUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class GrahamScan{
    private GeometryUtil geometryUtil = new GeometryUtil();
    private List<Point> points;
    private List<Point> convexHull;

    public List<Point> readPointsFromFile(String filename) throws IOException {
        points = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = reader.readLine()) != null){
                if (line.trim().isEmpty() || line.startsWith("") || line.startsWith("#")){
                    continue;
                }

                String [] coordinates = line.trim().split("\\s+");
                if (coordinates.length >= 2){
                    double x = Double.parseDouble(coordinates[0]);
                    double y = Double.parseDouble(coordinates[1]);
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    public List<Point> findConvexHull(List<Point> points){
        if (points == null || points.size() < 3){
            return null;
        }
        convexHull = new ArrayList<>();
        // Anchor Point or Reference Point
        Point anchorPoint = geometryUtil.findLowestPoint(points);
        List<Point> copyPoints = new ArrayList<>(points);
        if (copyPoints.contains(anchorPoint)){
            copyPoints.remove(anchorPoint);
        }
        geometryUtil.sortPointByPolarAngle(copyPoints, anchorPoint);
        convexHull.add(anchorPoint);
        convexHull.add(copyPoints.get(0));
        convexHull.add(copyPoints.get(1));

        for (int i = 2; i < copyPoints.size(); i++){
            while(convexHull.size() >= 2 && geometryUtil.isCounterClockwise(convexHull.get(convexHull.size()-2), convexHull.get(convexHull.size()-1), copyPoints.get(i)) != true){ // -1 means as long as its not counter clockwise
                convexHull.remove(convexHull.size() -1); // Remove last element
            }
            convexHull.add(copyPoints.get(i));
        }
        return convexHull;

    }

}