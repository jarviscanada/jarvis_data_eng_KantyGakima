# Introcduction

# SQL Queries

###### Table Setup (DDL)

###### Question 1: Insert values into cd.facilites table

```sql
INSERT INTO
  cd.facilities (
    facid,name,membercost,guestcost,initialoutlay,monthlymaintenance
  )
VALUES
  (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 2: Insert values into cd.facilities with automatic generation of facid numbers
```sql
INSERT INTO cd.facilities 
 (
  facid, name, membercost, guestcost, initialoutlay, monthlymaintenance
  )   
SELECT 
  (
    SELECT 
      max(facid) 
    FROM 
      cd.facilities
  )+ 1, 'Spa', 20, 30, 100000, 800;
```
###### Question 3: Update initial outlay for tenis court 2 in cd.facilities table
```sql
UPDATE cd.facilities
    set initialoutlay = 10000
    where facid = 1;
```
###### Question 4: alter the price of the second tennis court so that it costs 10% more than the first one.
```sql

```
###### Question 5: delete all bookings from the cd.bookings table
```sqli
TRUNCATE TABLE cd.bookings
```
###### Question 6: Remove meber 37, who has never made a booking from the database
```sql
DELETE FROM 
  cd.members 
where 
  memid = 37;
```
