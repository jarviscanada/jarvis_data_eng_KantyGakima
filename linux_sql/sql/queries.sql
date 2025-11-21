
--Solutions to practice query questions

--Question1
INSERT INTO cd.facilities
 (
    facid,name,membercost,guestcost,initialoutlay,monthlymaintenance
  )
VALUES
  (9, 'Spa', 20, 30, 100000, 800);
--Question2
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
--Question3
UPDATE cd.facilities
    set initialoutlay = 10000
    where facid = 1;

--Question4
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
--Question5
TRUNCATE TABLE cd.bookings
--question6
DELETE FROM
  cd.members
where
  memid = 37;