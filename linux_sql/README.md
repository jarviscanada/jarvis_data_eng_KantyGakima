# Introduction
The Linux Cluster Monitoring Agent is a system designed to collect hardware
specifications and resource usage data  from a Linux host.
This system runs locally on each machine and reports those metrics to a PostgreSQL database.

The Project is using several core technologies:
- Bash for scripting and automation,
- Docker to containerize PostgreSQL instance,
- SQL to create the host database that will store the metrics
- Git for version control and code review,
- Linux utilities: vmstat, lscpu, df, crontab ,for data collection and automation

# Quick Start
1. Start a psql instance using `psql_docker.sh`
```
./scripts/psql_docker.sh create <db_username><db_password>
./scripts/psql_docker.sh start
```
2. Create tables using `ddl.sql`

* Create host database if it does not exist already using the psql command line:
    ```
    --connect  to the psql instance 
    psql -h localhost -U <db_username> -W
  
    --list all databases
    postgres=# \l
  
    --create host_agent database
    CREATE DATABASE <db_name>;
  
    --connect to a database
    postgres=# \c <db_name>;
    ```
* Create the tables with `ddl.sql`
    ```
    psql -h localhost -U <db_username> -d <db_name> -f sql/ddl.sql
    ```
3. Insert hardware specifications data into the Database using `host_info.sh`
```
./scripts/hots_info.sh localhost pswl_port <db_name> <db_username><db_password>
```
4. Insert hardware usage data into the Database using `host_usage.sh`
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
The linux cluster monitoring agent is implemented using a client server system, each host act as a monitoring agent and
reports its data to a PostgreSQL database.\
The implementation is done in three major parts:
1. **Database setup and containerization:** A postgreSQL instance is created inside a Docker container with `script/psql docker.sh`
    
    sql\ddl.sql script initializes the database schema and created the required tables.
2. **Data collection**: Two bash scripts `host_info.sh` and `host_usage.sh` collect the needed data using Linux utilities such as
`hostname`,`lscpu`,`vmstat` etc.
    - `scripts/host_info.sh` collects hardware specifications and insert them into `host_info` table.
    - `script/host_usage.sh` collects resource usage data and inserts them into `host_usage`table.
3. **Automation with Crontab**: A cron job runs `host_usage.sh` every minute to ensure automatic data collection.
    
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
  collects hardware specifications such as hostname, CPU model, architecture, etc., and inserts them into host_agent
  table.

  Usage:
    ```
    ./scripts/host_info.sh localhost psql_port <db_name> <db_username> <db_password>
    ```
- **host_usage.sh**\
  collects resource usage data, such as disk usage, CPU idle time, and timestamp, and inserts them into the host_info table.

  Usage:
    ```
    ./scripts/host_usage.sh localhost psql_port <db_name> <db_username> <db_password>
    ```
- **Crontab**\
  It is used to collect resource data usage automatically every minute and store it in host_usage table

  Usage:
    ```
    * * * * * bash /path/to/host_usage.sh localhost psql_port <db_name> <db_username> <db_password>
    ```
- **ddl.sql**\
  Initializes the PostgreSQL Database by creating two tables: host_info and host_usage
  It also defines the primary keys, foreign keys, and constraints.

  Usage:
    ```
    psql -h localhost -U <db_username> -d <db_name> -f sql/ddl.sql
    ```

## Database Modeling
- `host_info`
```
| id          | hostname     | cpu_number          | cpu_architecture | cpu_model      | cpu_mhz   | l2_cache                       | "timestamp"                       | total_mem                                    |
| ----------- | ------------ | ------------------- | ---------------- | ---------------| --------  | ------------------------------ | --------------------------------  | -------------------------------------------- |
| Primary key | host machine | Number of CPU cores | eg: x 86_64      | CPU model name | CPU speed | L2 cache size on the CPU in MB | Time  when Data is collected      | Total memory available on host machine in MB | 
```
- `host_usage`
```
| timestamp                    | host_id                                                          | memory_free       | cpu_idle                          | cpu_kernel                   | disk_io                        | disk_available             | 
| ---------------------------- | ---------------------------------------------------------------- | ----------------- | --------------------------------- | ---------------------------- | ------------------------------ | -------------------------- | 
| Time  when Data is collected | reference to `host_info.id` indicating which host for these data | free memory in MB | % of CPU time spent in idle state |% of CPU time spent in kernel | Number of in out operations    | Available disk space in MB | 
```

# Test
Here are the different steps to test the bash scripts:
* **scripts/psql_docker.sh:** use `docker ps -a` to ckeck if the docker container has been created or is running.\
  Test the start and stop commands.
* **sql/sql.ddl:** connect to host_agent database and execute \dt to make sure the tables were created correctly.
  Manually insert sample rows into `host_info` and `host_usage` to verify data formatting and key relationships.
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

* **scrpts/host_info.sh and scripts/host_usage.sh:** First run the linux commands to collect data such as
  `vmstat --unit M`, `lscpu` to verify the expected values.\
  Run the scripts and verify that new entries appear in the tables with the SELECT command as previously.

# Deployment
This project is deployed using GitHub, Docker, a Database, and Crontab.
- GitHub: The project is pushed into a GitHub repository for collaboration, version control, and to be able to reuse the code
- Docker: A PostgreSQL instance is deployed inside a Docker container with `script/psql_docker.sh` that automates  
  container creation, startup, and shutdown.
- Database initialization: `sql/ddl.sql` is executed on host_agent database to create `host_info` and `host_usage` tables
- Crontab: A cron job was added using `crontab -e` to execute host_usage.sh every minute automatically.
# Improvements


