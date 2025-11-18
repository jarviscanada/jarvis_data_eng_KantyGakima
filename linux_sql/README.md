# Introduction
The Linux Cluster Monitoring Agent is a system designed to collect hardware
specifications and resource usage data  from a Linux host. 
This system runs locally on each machine and reports those metrics to a PostgreSQL database.
    
The Project is using several core technologies:
- Bash for scripting and automation,
- Docker to containerize PostgreSQL instance, 
- SQL to create the host database that will store the metrics
- Git for version control and code review, 
- Linux utilities: vmstat, lscpu, df, cronatab for data collection and automation

# Quick Start
1. Start a psql instance using psql_docker.sh
```
./scripts/psql_docker.sh create <db_username><db_password>
./scripts/psql_docker.sh start
```
2. Create tables using ddl.sql

* Create host database if it does not exist already using the psql command line:
    ```
    --connect  to the psql instance 
    psql -h localhost -U <db_username> -W
  
    --list all database
    postgres=# \l
  
    --create host_agent database
    CREATE DATABASE <db_name>;
  
    --connect to a database
    postgres=# \c <db_name>;
    ```
* Create the tables with ddl.sql
    ```
    psql -h localhost -U <db_username> -d <db_name> -f sql/ddl.sql
    ```
3. Insert hardware specifications data into the Database using host_info.sh
```
./scripts/hots_info.sh localhost pswl_port <db_name> <db_username><db_password>
```
4. Insert hardware usage data into the Database using host_usage.sh
```
./scripts/host_usage.sh localhost psql_port <db_name> <db_username><db_password>
```
5. Crontab setup to automate hardware usage data insertion into the database
```
bash> crontab -e

* * * * * bash /path/to/host_usage.sh localhost psql_port <db_name> <db_username> <db_password>
 > /tmp/host_usage.log
```
# Implementation
## Architecture
![Architecture Cluster Diagram](assets/Architecture.svg)
## Scripts
- **psql_docker.sh**\
    manages the PostgreSQL Docker container lifecycle. It creates the database container with a contanier volume,
    starts or stops the contaniner.

    Usage:
    ```
    ./scripts/psql_docker.sh create <db_username> <db_password>
    ./scripts/psql_docker.sh start
    ./scripts/psql_docker.sh stop
    ```
- **host_info.sh**\
    collects hardware specifications such as hostname, CPU model, architecture, etc. and insert them into host_agent
    table.
   
    Usage:
    ```
    ./scripts/host_info.sh localhost psql_port <db_name> <db_username> <db_password>
    ```
- **host_usage.sh**\
    collects resource usage data such as disk usage CPU idle time, timestamp etc. and inserts them into host_info table.

    Usage:
    ```
    ./scripts/host_usage.sh localhost psql_port <db_name> <db_username> <db_password>
    ```
- **Crontab**\
    Is used to collect resource data usage automatically every minute and store it into host_usage table
    
    Usage:
    ```
    * * * * * bash /path/to/host_usage.sh localhost psql_port <db_name> <db_username> <db_password>
    ```
- **ddl.sql**\
    Initializes the PostgreSQL Database by creating two tables: host_info and host_usage
    It also defines the primary keys, foreign keys and constraints.
    Usage:
    ```
    psql -h localhost -U <db_username> -d <db_name> -f sql/ddl.sql
    ```

## Database Modeling

# Test
Here are the different steps to test the bash scripts:
* **scripts/psql_docker.sh:** use docker ps -a to ckeck if the docker container has been created or is running.\
Test the start and stop commands.
* **sql/sql.ddl:** connect to host_agent database and execute \dt to make sure the tables \
  were created correctly.
  Manually Insert samaple rows into host_info and host_usage to verify data formatting and key relationships.
```
Ex:
INSERT INTO host_info (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, "timestamp",
total_mem) VALUES(1, 'jrvs-remote-desktop-centos7-6.us-central1-a.c.spry-framework-236416.internal', 1, 'x86_64', 
'Intel(R) Xeon(R) CPU @ 2.30GHz', 2300, 256, '2019-05-29 17:49:53.000', 601324);

INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) 
VALUES('2019-05-29 15:00:00.000', 1, 300000, 90, 4, 2, 3);

--Verify the inserted data
SELECT * FROM host_info;
SELECT * FROM host_usage;
```
  
* **scrpts/host_info.sh and scripts/host_usage.sh:** First run the linux commands to collect data such as 'vmstat --unit M', 'lscpu' to verify \
the expected values.\
  Run the scripts and verify that new entries appears in the tables with the SELECT command as previously.

# Deployment
# Improvements


