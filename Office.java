import java.util.*;
import java.lang.*;
import java.time.*;
import java.sql.*;

public class Office implements Runnable {

    protected final City city;

    private final OfficeUtil officeUtil = new OfficeUtil(this);

    protected final int taxiCount, initialDistance, initialCharge, usualCharge, minimumDistance;

    protected final Set<Taxi> freeTaxis = new HashSet<Taxi>();

    protected final Queue<Customer> customerQueue = new PriorityQueue<Customer>(new Comparator<Customer>() {
        @Override
        public int compare(Customer customer1, Customer customer2) {
            return customer1.compareTo(customer2);
        }
    });

    public Office(City city, int taxiCount, int initialDistance, int initialCharge, int usualCharge, int minimumDistance) {

        this.city = city;

        this.taxiCount = taxiCount;

        this.initialDistance = initialDistance;
        this.initialCharge = initialCharge;
        this.usualCharge = usualCharge;
        this.minimumDistance = minimumDistance;

        this.initTaxis();

    }

    public void addCustomers(Customer... customers) {
        Arrays.stream(customers).forEach(this.customerQueue::add);}

    private void initTaxis() {
        for(int i = 0; i < this.taxiCount;)
            this.addTaxi(new Taxi(this, ++i));}

    public void addTaxi(Taxi taxi) {
        this.freeTaxis.add(taxi);}

    @Override
    public void run() {
        if(!this.customerQueue.isEmpty())
            this.officeUtil.setTaxisToCustomers();}

}

final class OfficeUtil {

    private final Office office;

    OfficeUtil(Office office) {
        this.office = office;}

    private static LocalTime addLocalTime(LocalTime time0, int delTime) {

        delTime += time0.getHour() * 3600 + time0.getMinute() * 60 + time0.getSecond();
        return LocalTime.of((delTime / 3600) % 24, (delTime % 3600) / 60, (delTime % 3600) % 60);
    
    }

    private void assignTimeAndPoint(Taxi taxi, Customer customer) {

        int time = CityUtil.findTimeBetween(taxi, customer);
        if(customer.getState() == Customer.State.BOOKED) {
            customer.pickUpTime = OfficeUtil.addLocalTime(customer.bookingTime, time);
            taxi.currentPoint = customer.pickUpPoint;}
        else {
            customer.dropDownTime = OfficeUtil.addLocalTime(customer.pickUpTime, time);
            taxi.currentPoint = customer.dropDownPoint;}
        this.office.addCustomers(customer);

    }

    protected void setTaxisToCustomers() {

        Customer customer = null;

        while(!this.office.customerQueue.isEmpty()) {

            customer = this.office.customerQueue.poll();

            switch(customer.getState()) {
                case WAITING:                    
                    this.doWhileWaiting(customer);
                    break;
                
                case BOOKED:
                    this.doWhileBooked(customer);
                    break;
                
                case PICKED:
                    this.doWhilePicked(customer);
                    break;
            }
        }
    }

    private void doWhileWaiting(Customer customer) {

        Taxi taxi = this.getAFreeTaxiFor(customer);
        customer.assignedTaxi = taxi;

        if(taxi == null) {
            customer.setState(Customer.State.CANCELLED);
            System.out.println("\nSince, none of the taxis were either free or closer, TAXI-CANCELLED for\n" + customer);}
        else {
            customer.setState(Customer.State.BOOKED);
            this.assignTimeAndPoint(taxi, customer);}
        
    }

    private void doWhileBooked(Customer customer) {

        customer.setState(Customer.State.PICKED);

        Taxi taxi = customer.assignedTaxi;
        
        int distance = CityUtil.findDistanceBetween(taxi, customer);
        taxi.revenue += customer.amount = this.findCharge(distance);

        this.assignTimeAndPoint(taxi, customer);
        
    }

    private void doWhilePicked(Customer customer) {

        customer.setState(Customer.State.DROPPED);

        System.out.println("\n" + customer);

        this.office.addTaxi(customer.assignedTaxi);
        customer.assignedTaxi = null;
        
    }

    protected Taxi getAFreeTaxiFor(Customer customer) {
        
        Taxi freeTaxi = null;

        for(Taxi taxi: this.office.freeTaxis)
            if(customer.isNotBetterTaxi(taxi, freeTaxi))
                freeTaxi = taxi;
        this.office.freeTaxis.remove(freeTaxi);

        return freeTaxi;
    
    }

    protected int findCharge(int distance) {
        return this.office.initialCharge + Math.max(distance - this.office.initialDistance, 0) * this.office.usualCharge;}

}