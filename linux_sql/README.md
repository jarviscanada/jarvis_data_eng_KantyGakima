# Introduction
The Linux Cluster Monitoring Agent is a system designed to collect hardware
specifications and resource usage data  from a Linux host. 
This system runs locally on each machine and reports those metrics to a PostgreSQL database.
    
The Project is using several core technologies:
```
-Bash for scripting and automation,
-Docker to containerize PostgreSQL instance,
-Git for version control and code review,
-Linux utilities: vmstat, lscpu, df, cronatab for data collection and automation
```
# Quick Start
1. Start a psql instance using psql_docker.sh
```
./scripts/psql_docker.sh create <db_user><db_password>
./scripts/psql_docker.sh start
```
2. Create tables using ddl.sql

* Create host_agent database if it does not exist already using the psql command line:
    ```
    --connect  to the psql instance with postgres as database username
    psql -h localhost -U postgres -W
  
    --list all database
    postgres=# \l
  
    --create host_agent database
    CREATE DATABASE host_agent;
  
    --connect to a database
    postgres=# \c host_agent;
    ```
* Create the tables with ddl.sql
    ```
    psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
    ```
3. Insert hardware specifications data into the Database using host_info.sh
```
./scripts/hots_info.sh localhost 5432 host_agent <db_user><db_password>
```
4. Insert hardware usage data into the Database using host_usage.sh
```
./scropts/host_usage.sh localhost 5432 host_agent <db_user><db_password>
```
5. Crontab setup to automate hardware usage data insertion into the database
```
crontab -e
* * * * * bash /path/to/host_usage.sh localhost 5432 host_agent <db_user> <db_password>
 > /tmp/host_usage.log
```
# Implementation

## Architecture
## Scripts
## Database Modeling
# Test
# Deployment
# Improvements


