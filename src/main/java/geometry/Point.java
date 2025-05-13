package geometry;

public class Point implements Comparable<Point> {
    private double x;
    private double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Double.hashCode(x);
        result = prime * result + Double.hashCode(y);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (Double.compare(x, other.x) != 0)
            return false;
        if (Double.compare(y, other.y) != 0)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }
    
    // Add Comparable implementation for sorting points
    public int compareTo(Point other) {
        if (this.x != other.x) {
            return Double.compare(this.x, other.x);
        }
        return Double.compare(this.y, other.y);
    }
    
    // Method to calculate distance squared between two points
    public double distanceSquared(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return dx * dx + dy * dy;
    }
}