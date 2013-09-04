@echo off

echo Importing tables...
mysql -u cronus -pcronuspassword < db_structure.sql

pause
