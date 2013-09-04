@echo off

echo Importing database user...

rem ##########################################
rem Replace 1234 with your MySQL root password
rem ##########################################

mysql -v -u root -p1234 < db_user.sql

pause
