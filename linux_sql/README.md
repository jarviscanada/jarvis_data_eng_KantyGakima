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

# Deployment
# Improvements


