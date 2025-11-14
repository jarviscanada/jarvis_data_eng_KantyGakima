
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# check the number of arguments
if ["$#" -ne 5]; then
  echo "Illegal number of parameters"
  exit 1
fi

# Save hardware specifications and current machine hostname into variables
specs=`lscpu`
hostname=$(hostname -f)

# Retrieve hardware specification variables
cpu_number=$(echo "$specs" | egrep "^CPU\(S\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$specs"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model= $(echo "$specs" | awk -F': +' '/^Model name:/ {print $2}' | xargs)
cpu_mhz=$(awk -F': +' '/^cpu MHz/ {print $2}' /proc/cpuinfo | uniq)
l2_cache=$(echo "specs" | awk -F': +' '/^L2 cache:/ {print $2}' | xargs)
total_mem=$(vmstat --unit M | awk '{print $4}' | tail -n1 | xargs)
timestamp=$(vmstat -t | awk '{print $18, $19}' | tail -n1 | xargs)



#PSQL command to insert server usage data into host_usage table
insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache,"timestamp",
              total_mem)
              VALUES('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache',
              '$timestamp, '$total_mem)";

# set up env var for psql command
export PGPASSWORD=$psql_password

#insert data into the database
psql -h $psql_host -p psql_port -d $db_name -U $psq_user -c "$insert_stmt"

exit $?
