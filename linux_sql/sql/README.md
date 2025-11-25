# Introduction
##### Design
This project was primarily a structured learning activity focused on strengthening SQL fundamentals. The objective was to
work with a Docker containerized PostgreSQL database and solve a wide range of SQL exercises  touching key database
concepts, including joins, aggregation, filtering, window functions, string manipulation and subqueries.
##### Implementation
Approximately 30 SQL exercises were completed, each targeting different real-world query scenarios. All queries were
written and executed on a local PostgreSQL running inside a Docker container. The solution to each exercise is
documented in this README for reference.
##### Testing
The exercises are from an online SQL practice platform that provides automatic validation for each query. Queries were
also executed on the local Docker PostgreSQL instance.
##### Deployment
Each exercise includes a link to the original question along with its solution.
```
#Create Docker container using the script in /scripts directory.
./scripts/psql_docker.sh create <db_username> <db_password>

#start docker container
./scripts/psql_docker.sh start

#run clubdata.sql in the PostgreSQL instance to create the exercises database
psql -h localhost -p 5432 -U <db_username> -d postgres -f clubdata.sql

#connect to exercises database
\c exercises
```

# SQL Queries
##### Table Setup (DDL)
We have a dataset of a country club, with a list of members, facilities (tennis courts, fitness rooms, etc.),
and booking history linking members to the facilities they use.\
Below are the SQL DDL statements to create all required tables.
* **cd.members**: This table contains personal details of each club member, including an optional reference to another
  member who recommended them. Each member has a unique member ID (memid)
    ```sql
    CREATE TABLE cd.members
    (
        memid integer NOT NULL,
        surname character varying(200) NOT NULL,
        firstname character varying(200) NOT NULL,
        adress character varying(300) NOT NULL,
        zipcode integer NOT NULL,
        telephone  character varying(20) NOT NULL,
        recommendedby integer,
        joindate timestamp without timezone NOT NULL,
        CONSTRAINT members_pk PRIMARY KEY (memid),
        CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
            REFERENCES cd.members(memid) on DELETE SET NULL
    );      
    ```
* **cd.bookings**: This table records every booking made by club members. It links each booking to a specific facility
  and a specific member through foreign keys. Each booking has a unique  booking ID `bookid`.
    ```sql
    CREATE TABLE cd.bookings
    (
        bookid integer NOT NULL,
        facid integer NOT NULL,
        memid integer NOT NULL,
        starttime timestamp without timezone NOT NULL,
        slots integer NOT NULL,
        CONSTRAINT bookings_pk PRIMARY KEY (bookid),
        CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
        CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
    );   
    ```
* **cd.facilities**: This table stores the list of facilities available at the club(e.g, tennis courts, spa. It includes pricing details for members and guests and other financial attributes. Each facility has a unique
  `facid` that links to `cd.bookings`.
    ```sql
    CREATE TABLE cd.facilities
    (
        facid integer NOT NULL,
        name character varying(100) NOT NULL,
        membercost numeric NOT NULL,
        guestcost numeric NOT NULL,
        initialoutlay numeric NOT NULL,
        monthlymaintenance numeric NOT NULL,
        CONSTRAINT facilities_pk PRIMARY KEY (facid)
    );
    ```
####
###### Question 1: Insert values into cd.facilities table [link](https://pgexercises.com/questions/updates/insert.html)

```sql
INSERT INTO
  cd.facilities (
    facid,name,membercost,guestcost,initialoutlay,monthlymaintenance
  )
VALUES
  (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 2: Insert values into cd.facilities with automatic generation of facid numbers [link](https://pgexercises.com/questions/updates/insert3.html)
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

###### Question 3: Update initial outlay for tennis court 2 in cd.facilities table[link](https://pgexercises.com/questions/updates/update.html)
```sql
UPDATE cd.facilities
    set initialoutlay = 10000
    where facid = 1;
```
###### Question 4: Alter the price of the second tennis court so that it costs 10% more than the first one.[link](https://pgexercises.com/questions/updates/updatecalculated.html)
```sql
update
  cd.facilities f1
set
  membercost = (
    select
      membercost * 1.1
    from
      cd.facilities
    where
      facid = 0
  ),
  guestcost = (
    select
      guestcost * 1.1
    from
      cd.facilities
    where
      facid = 0
  )
where
  f1.facid = 1;
```
###### Question 5: Delete all bookings from the cd.bookings table [link](https://pgexercises.com/questions/updates/delete.html)
```sql
TRUNCATE TABLE cd.bookings
```
###### Question 6: Remove member 37, who has never made a booking, from the database [link](https://pgexercises.com/questions/updates/deletewh.html)
```sql
DELETE FROM 
  cd.members 
where 
  memid = 37;
```
###### Question 7 : List facilities with member cost > 0 and less than 1/50 of maintencae cosy [link](https://pgexercises.com/questions/basic/where2.html)
```sql
SELECT 
  facid, 
  name, 
  membercost, 
  monthlymaintenance 
FROM 
  cd.facilities 
WHERE 
  membercost > 0 
  and membercost < (monthlymaintenance / 50);
```
###### Question 8: List facilities with "Tennis" in the name.[link](https://pgexercises.com/questions/basic/where3.html)
```sql
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  name like '%Tennis%';
```
###### Question 9: List the details of facilities with IDs 1 and 5 without the `OR` operator [link](https://pgexercises.com/questions/basic/where4.html)
```sql
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  facid in (1, 5);
```
###### Question 10 : List members who joined after September 1st, 2012 [link](https://pgexercises.com/questions/basic/date.html)
```sql
SELECT 
  memid, 
  surname, 
  firstname, 
  joindate 
FROM 
  cd.members 
WHERE 
  joindate >= '2012-09-01';
```
###### Question 11: List all surnames and facility names [link](https://pgexercises.com/questions/basic/union.html)
```sql
SELECT 
  surname 
FROM 
  cd.members 
UNION 
SELECT 
  name 
FROM
  cd.facilities;
```
###### Question 12: List start times for bookings made by "David Farrell"[link](https://pgexercises.com/questions/joins/simplejoin.html)
```sql
SELECT
  bookings.starttime 
FROM
  cd.bookings bookings 
  inner join cd.members members on members.memid = bookings.memid 
WHERE
  members.firstname = 'David' 
  and members.surname = 'Farrell';
```
###### Question 13 : List all the Tennis court bookings on 2012-09-21 ordered by the time [lnk](https://pgexercises.com/questions/joins/simplejoin2.html)
```sql
select 
  bookings.starttime as start, 
  facilities.name as name 
from 
  cd.facilities facilities 
  inner join cd.bookings bookings on facilities.facid = bookings.facid 
where 
  facilities.name like 'Tennis Court%' 
  and bookings.starttime >= '2012-09-21' 
  and bookings.starttime < '2012-09-22' 
order by 
  bookings.starttime;
```
###### Question 14 : List all members with the individual who recommended them [link](https://pgexercises.com/questions/joins/self2.html)
```sql
SELECT 
  M1.firstname as memberfn, 
  M1.surname as membersn, 
  M2.firstname as recommendedbyfn, 
  M2.surname as recommendedbysn 
FROM 
  cd.members M1 
  LEFT JOIN cd.members M2 ON M1.recommendedby = M2.memid 
order by 
  membersn, 
  memberfn;
```
###### Question 15 : List all members who have recommended other members [link](https://pgexercises.com/questions/joins/self.html)
```sql
SELECT 
  distinct M2.firstname, 
  M2.surname 
FROM 
  cd.members M1 
  JOIN cd.members M2 on M1.recommendedby = M2.memid 
ORDER by 
  M2.surname, 
  M2.firstname;
```

###### Question 16 : List all members with the individual who recommended them without using any join [link](https://pgexercises.com/questions/joins/sub.html)
```sql
SELECT 
  DISTINCT M.firstname || ' ' || M.surname as member, 
  (
    SELECT 
      R.firstname || ' ' || R.surname as recommender 
    FROM 
      cd.members R 
    WHERE 
      R.memid = M.recommendedby
  ) 
FROM 
  cd.members M 
order by 
  member;
```
###### Question 17: Count the number of recommendations each member has made [link](https://pgexercises.com/questions/aggregates/count3.html)
```sql
SELECT 
  recommendedby, 
  count(*) 
FROM 
  cd.members 
WHERE 
  recommendedby is not null 
group by 
  recommendedby 
Order by 
  recommendedby;
```
###### Question 18: List total slots booked per facility. [link](https://pgexercises.com/questions/aggregates/fachours.html)
```sql
SELECT 
  facid, 
  sum(slots) as "Total slots" 
FROM 
  cd.bookings 
group by 
  facid 
Order by 
  facid;

```
###### Question 19: List total slots booked per facility in September 2012. [link](https://pgexercises.com/questions/aggregates/fachoursbymonth.html)
```sql
SELECT 
  facid, 
  sum(slots) as "Total Slots" 
FROM 
  cd.bookings 
WHERE 
  starttime >= '2012-09-01' 
  and starttime < '2012-10-01' 
group by 
  facid 
Order by 
  "Total Slots";
```
###### Question 19: List total slots booked per facility in 2012 with the month of the booking [link](https://pgexercises.com/questions/aggregates/fachoursbymonth2.html)
```sql
SELECT 
  facid, 
  extract(
    month 
    from 
      starttime
  ) as month, 
  sum(slots) as "Total Slots" 
FROM 
  cd.bookings 
WHERE 
  extract(
    year 
    from 
      starttime
  ) = 2012 
group by 
  facid, 
  month 
order by 
  facid, 
  month;
```
###### Question 20: List the total number of members and guests who have made at least one booking [link](https://pgexercises.com/questions/aggregates/members1.html)
```sql
SELECT 
  count(distinct memid) 
from 
  cd.bookings;

```
###### Question #21: List each member's first booking after september 1st 2012 [link](https://pgexercises.com/questions/aggregates/nbooking.html)
```sql
SELECT 
  members.surname, 
  members.firstname, 
  members.memid, 
  min(bookings.starttime) as starttime 
FROM 
  cd.bookings bookings 
  JOIN cd.members members on members.memid = bookings.memid 
WHERE 
  starttime >= '2012-09-01' 
group by 
  members.surname, 
  members.firstname, 
  members.memid 
order by 
  members.memid;
```
###### Question 22: List of member names with the count of total members [link](https://pgexercises.com/questions/aggregates/countmembers.html)
```sql
SELECT 
  count(*) over(), 
  firstname, 
  surname 
FROM 
  cd.members 
order by 
  joindate

```
###### Question 23: Produce a monotonically increasing numbered list of members ordered by date of joining [link](https://pgexercises.com/questions/aggregates/nummembers.html)
```sql
SELECT 
  row_number() over (
    order by 
      joindate
  ), 
  firstname, 
  surname 
FROM 
  cd.members 
order by 
  joindate;
```
###### Question 24: Output the facility ID that has the highest number of slots booked [link](https://pgexercises.com/questions/aggregates/fachours4.html)
```sql
SELECT 
  facid, 
  total 
from 
  (
    SELECT 
      facid, 
      sum(slots) total, 
      rank() over (
        order by 
          sum(slots) desc
      ) rank 
    from 
      cd.bookings 
    group by 
      facid
  ) as ranked 
where 
  rank = 1;
```
###### Question 25: Output the names of all members, formatted as 'Surname, Firstname' [link](https://pgexercises.com/questions/string/concat.html )
```sql
SELECT 
  surname || ', ' || firstname as name 
FROM 
  cd.members;
```
###### Question 26: Find all the telephone numbers with parentheses [link](https://pgexercises.com/questions/string/reg.html)
```sql
SELECT 
  memid, 
  telephone 
FROM 
  cd.members 
WHERE 
  telephone ~ '[()]';

```
###### Question 27: A count of members whose surname starts with each letter of the alphabet, sorted by letter [link](https://pgexercises.com/questions/string/substr.html)
```sql
SELECT 
  substr(members.surname, 1, 1) as letter, 
  count(*) as count 
FROM 
  cd.members members 
group by 
  letter 
order by 
  letter;
```


