import java.util.*;
import java.lang.*;
import java.sql.*;

public class City {

    public final int pointCount;
    public final int[] distanceTaken, timeTaken;

    public City(int pointCount, int[] adjDistance, int[] adjTime) throws Exception {

        this.pointCount = pointCount;

        if(this.pointCount != adjDistance.length + 1)
            throw new Exception("The Distance between each points for " + this.pointCount + " points is not given");
        else if(this.pointCount != adjTime.length + 1)
            throw new Exception("The Time taken to travel between each points for " + this.pointCount + " points is not given");
        
        this.distanceTaken = new int[this.pointCount];
        CityUtil.fillCumulative(this.pointCount, distanceTaken, adjDistance);

        this.timeTaken = new int[this.pointCount];
        CityUtil.fillCumulative(this.pointCount, timeTaken, adjTime);

    }
    
}

final class CityUtil {

    private CityUtil() {}

    protected static void fillCumulative(int size, int[] srcArray, int[] destArray) {
        for(int sum, i = sum = 0; i < size-1;)
            srcArray[++i] = sum += destArray[i-1];}

    private static int findDistanceBetweenUtil(City city, int p1, int p2) {
        return city.distanceTaken[p2-1] - city.distanceTaken[p1-1];}

    private static int findTimeBetweenUtil(City city, int p1, int p2) {
        return city.timeTaken[p2-1] - city.timeTaken[p1-1];}

    protected static int findDistanceBetween(Taxi taxi, Customer customer) {
        int p1 = Math.min(customer.getComparitivePoint(), taxi.currentPoint),
            p2 = Math.max(customer.getComparitivePoint(), taxi.currentPoint);
        return CityUtil.findDistanceBetweenUtil(taxi.office.city, p1, p2);}

    protected static int findTimeBetween(Taxi taxi, Customer customer) {
        int p1 = Math.min(customer.getComparitivePoint(), taxi.currentPoint),
            p2 = Math.max(customer.getComparitivePoint(), taxi.currentPoint);
        return CityUtil.findTimeBetweenUtil(taxi.office.city, p1, p2);}

}