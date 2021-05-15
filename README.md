# Call-Taxi-Booking
This project is all about booking a Call Taxi in a Lane, with some number of Taxis and Customers.

Each of the nodes are represented with unique integer called 'point'.
Thus, a customer has a pickUp-point and dropDown-point.
And each of the taxis have their current position as current-point.

When a request is made from a Customer at a given instance of time called, 'pickUpTime'.

The program searches(from the class 'Office') for a free and closely available taxi in the 'City' (or) 'lane'.
When such a taxi is available, it is assigned to the customer, else the Taxi be cancelled for the customer.
