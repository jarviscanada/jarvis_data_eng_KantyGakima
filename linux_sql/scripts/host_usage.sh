
#Capture CLI commands
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# check the number of arguments
if [ "$#" -ne 5 ]; then
  echo "Illegal number of parameters"
  exit 1
fi

# Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# Retrieve hardware specification variables
memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -n1)
cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}' | tail -n1)
cpu_kernel=$(echo "$vmstat_mb" | awk '{print $14}' | tail -n1)
disk_io=$(vmstat -d | awk '{print $11}' | tail -n1)
disk_available=$(df -BM / | awk '{print $4}'| sed 's/M//' | tail -n1 ) #sed 's/M//'  for parsing the M as we ony need int
timestamp=$(vmstat -t | awk '{print $18, $19}' | tail -n1)

# Subquery to find machine id in host_info table
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

#PSQL command to insert server usage data into host_usage table
insert_stmt="INSERT INTO host_usage("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
              VALUES('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel ,$disk_io, $disk_available)";

# provide password to psql using env variable
export PGPASSWORD=$psql_password

#insert data into the database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?
