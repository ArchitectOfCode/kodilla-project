#!/usr/bin/env bash

stop_tomcat()
{
  service tomcat8 stop
}

start_tomcat()
{
  service tomcat8 start
  end
}

rename() {
  rm build/libs/crud.war
  if mv build/libs/tasks-0.0.1-SNAPSHOT.war build/libs/crud.war; then
     echo "Successfully renamed file"
  else
     echo "Cannot rename file"
     fail
  fi
}

copy_file() {
  if cp build/libs/crud.war /var/lib/tomcat8/webapps; then
     start_tomcat
  else
     fail
  fi
}

fail() {
  echo "There were errors"
}

end() {
  echo "Work is finished"
}

if ./gradlew build; then
   rename
   copy_file
else
   stop_tomcat
   fail
fi