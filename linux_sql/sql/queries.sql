--Solutions to practice query questions

--Question 1: Insert values into cd.facilities table

INSERT INTO
  cd.facilities (
    facid,name,membercost,guestcost,initialoutlay,monthlymaintenance
  )
VALUES
  (9, 'Spa', 20, 30, 100000, 800);
```

-- Question 2: Insert values into cd.facilities with automatic generation of facid numbers
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

--Question 3: Update initial outlay for tenis court 2 in cd.facilities table
sql
UPDATE cd.facilities
    set initialoutlay = 10000
    where facid = 1;

--Question 4: alter the price of the second tennis court so that it costs 10% more than the first one.

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

--Question 5: delete all bookings from the cd.bookings table

TRUNCATE TABLE cd.bookings

--Question 6: Remove member 37, who has never made a booking from the database

DELETE FROM
  cd.members 
where 
  memid = 37;
```
--Question 7 : List facilities with member cost > 0 and less than 1/50 of maintenance cosy
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

--Question 8 : List facilities with "Tennis" in the name

SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  name like '%Tennis%';

--Question 9 : List the details of facilities with Id 1 and 5 without OR operator

SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  facid in (1, 5);

--Question 10 : List members who joined after September 1st, 2012

SELECT 
  memid, 
  surname, 
  firstname, 
  joindate 
FROM 
  cd.members 
WHERE 
  joindate >= '2012-09-01';

-- Question 11 : List all surnames and facility names

SELECT 
  surname 
FROM 
  cd.members 
UNION 
SELECT 
  name 
FROM
  cd.facilities;

-- Question 12 : list start times for bookings made by "David Farrell"

SELECT
  bookings.starttime 
FROM
  cd.bookings bookings 
  inner join cd.members members on members.memid = bookings.memid 
WHERE
  members.firstname = 'David' 
  and members.surname = 'Farrell';

-- Question 13 : List all the Tennis court bookings on 2012-09-21 ordered by the time

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

-- Question 14 : List all members with the individual who recommended them

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

--Question 15 : List all members who have recommended other members

SELECT 
  distinct M2.firstname, 
  M2.surname 
FROM 
  cd.members M1 
  JOIN cd.members M2 on M1.recommendedby = M2.memid 
ORDER by 
  M2.surname, 
  M2.firstname;


--Question 16 : List all members with the individual who recommended tem without using any join

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

-- Question 17 : Count the number of recommendations each member has made

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

-- Question 18 : List total slots booked per facility.

SELECT 
  facid, 
  sum(slots) as "Total slots" 
FROM 
  cd.bookings 
group by 
  facid 
Order by 
  facid;

--Question 19 : List total slots booked per facility in September, 2012.

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

--Question 19 : List total slots booked per facility in 2012 with the month of the booking

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

--Question 20 : List total number of members and guests who have made at least one booking

SELECT 
  count(distinct memid) 
from 
  cd.bookings;

--Question #21: List each member's first booking after september 1st 2012

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

-- Question 22 : list of member names with the count of total members

SELECT 
  count(*) over(), 
  firstname, 
  surname 
FROM 
  cd.members 
order by 
  joindate

--Question 23 : Produce a monotonically increasing numbered list of members ordered by date of joining

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

-- Question 24 : Output the facility id that has the highest number of slots booked

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

-- Question 25 : Output the names of all members, formatted as 'Surname, Firstname'

SELECT 
  surname || ', ' || firstname as name 
FROM 
  cd.members;

--Question 26 : Find all the telephone numbers with parentheses

SELECT 
  memid, 
  telephone 
FROM 
  cd.members 
WHERE 
  telephone ~ '[()]';


-- Question 27 : A count of members whose surname starts with each letter of the alphabet sorted by letter

SELECT 
  substr(members.surname, 1, 1) as letter, 
  count(*) as count 
FROM 
  cd.members members 
group by 
  letter 
order by 
  letter;
