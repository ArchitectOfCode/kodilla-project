#!/usr/bin/env bash

openInBrowser() {
  if firefox "http://localhost:8080/crud/v1/task/getTasks"; then
    echo "Deployment finished successfully. Firefox should open automatically."
  else
    echo "Something went wrong. Deployment not finished successfully."
  fi
}

fail() {
  echo "Cannot continue script!"
}

if ./runcrud.sh;
  then
  echo "Starting script..."
  openInBrowser
else
  echo "Error: Script runcrud.sh couldn't be found!"
  fail
fi