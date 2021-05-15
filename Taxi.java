import java.util.*;
import java.sql.*;

public class Taxi {

    public static enum State {
        FREE, ASSIGNED, PICKED}

    private Taxi.State state = Taxi.State.FREE;

    public final Office office;

    public final int idNumber;
    
    public int currentPoint = 1, revenue = 0;

    public Taxi(Office office, int idNumber) {
        this.office = office;
        this.idNumber = idNumber;}

    @Override
    public String toString() {
        return "Taxi[idNumber=" + this.idNumber + ",currentPoint=" + this.currentPoint + ",revenue=" + this.revenue + "]";}

}