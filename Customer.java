import java.util.*;
import java.time.*;
import java.lang.*;
import java.sql.*;

public class Customer implements Comparable<Customer> {
    
    public static enum State {
        WAITING, CANCELLED, BOOKED, PICKED, DROPPED}

    private Customer.State state = Customer.State.WAITING;

    protected final String name;

    public final int pickUpPoint, dropDownPoint;
    public int amount = 0;

    public final LocalTime bookingTime;
    public LocalTime pickUpTime;
    public LocalTime dropDownTime;

    public Taxi assignedTaxi = null;

    public Customer(String name, int pickUpPoint, int dropDownPoint, String bookingTime) throws IllegalArgumentException {

        this.name = name;
        this.pickUpPoint = pickUpPoint;
        this.dropDownPoint = dropDownPoint;

        this.bookingTime = CustomerUtil.setTime(bookingTime);

    }

    public void setState(Customer.State state) {
        this.state = state;}
    
    public Customer.State getState() {
        return this.state;}

    public boolean isNotBetterTaxi(Taxi taxi0, Taxi taxi1) {
        return CustomerUtil.isNotBetterTaxi(taxi0, taxi1, this);}

    public int getComparitivePoint() {
        return this.getState() == Customer.State.BOOKED || this.getState() == Customer.State.WAITING ? this.pickUpPoint : this.dropDownPoint;}

    @Override
    public int compareTo(Customer customer) {
        
        LocalTime time1 = CustomerUtil.getComparitiveTime(this),
                  time2 = CustomerUtil.getComparitiveTime(customer);
        
        if(time1.isBefore(time2))
            return -1;
        
        else if(time1.isAfter(time2))
            return 1;
        
        return customer.getState().ordinal() - this.getState().ordinal();}

    @Override
    public String toString() {
        return "Customer[name=" + this.name +
               ",\n\t assignedTaxi-" + this.assignedTaxi +
               ",\n\t amountPaid=" + this.amount +
               ",\n\t bookingTime=" + this.bookingTime +
               ",\n\t pickUpTime=" + this.pickUpTime +
               ",\n\t dropDownTime=" + this.dropDownTime + "]\n\n";}

}

final class CustomerUtil {

    private static int hour, minute, second;

    private CustomerUtil() {}

    public static LocalTime setTime(String time) throws IllegalArgumentException {

        if(time.length() != 8)
            throw new IllegalArgumentException();

        CustomerUtil.hour = Integer.valueOf(time.substring(0, 2));
        CustomerUtil.minute = Integer.valueOf(time.substring(3, 5));
        CustomerUtil.second = Integer.valueOf(time.substring(6, 8));

        return LocalTime.of(CustomerUtil.hour, CustomerUtil.minute, CustomerUtil.second);

    }

    public static LocalTime getComparitiveTime(Customer customer) {

        switch(customer.getState()) {
            case WAITING:
                return customer.bookingTime;
            case BOOKED:
                return customer.pickUpTime;
            case PICKED:
                return customer.dropDownTime;}
        return null;

    }

    public static boolean isNotBetterTaxi(Taxi taxi0, Taxi taxi1, Customer customer) {
        
        if(taxi0 == null)
            return false;
        
        int d0 = CityUtil.findDistanceBetween(taxi0, customer),
            d1 = taxi1 != null ? CityUtil.findDistanceBetween(taxi1, customer) : Integer.MAX_VALUE,
            r0 = taxi0.revenue,
            r1 = taxi1 != null ? taxi1.revenue : Integer.MAX_VALUE;
        
        if(d0 > taxi0.office.minimumDistance || d0 > d1)
            return false;
        else if(d0 < d1)
            return true;
        else if(r0 > r1)
            return false;
        else if(r0 < r1)
            return true;
        return taxi0.idNumber < taxi1.idNumber;
        
    }

}