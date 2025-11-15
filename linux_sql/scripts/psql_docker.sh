#!/bin/sh

# Script to start, stop and create a docker container

# Capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

# Start docker
# || : If the command on the left fails then run the one on the right
sudo systemctl status docker || sudo systemctl start docker

# Check container status
container_status=$? #$? holds the exit code of the last command(0 for success, non-zero for failure)

# Switch case to handle create|stop|start 
case $cmd in
  create)

  # Check if the container is already created
  if [ $container_status -eq 0 ]; then
	  echo 'Container already exists'
	  exit 1
  fi

  # Check # of CLI arguments
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi

  # Create container
  docker volume create pgdata

  # Start the container
  docker run --name jrvs-psql \
	  -e POSTGRES_USER=$db_username -e POSTGRES_PASSWORD=$db_password\
	  -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine

  exit $?

	;;

  start|stop)
  # Check instance status; exit 1 if container has not been created
  if [ $container_status -ne 0 ]; then
    echo 'Container has not been created'
    exit 1
  fi

  # Start or stop the container
  docker container $cmd jrvs-psql
  exit $?
  ;;

  *)
	  echo 'Illegal command'
	  echo 'Commands: start|stop|create'
	  exit 1
	  ;;
#To do: I have to quit the cli to get error message, fix
esac
