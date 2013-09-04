@echo off

echo Importing database struct...
mysql -u cronus -pcronuspassword < db_structure.sql

echo Importing database data...
mysql -u cronus -pcronuspassword cronus_cabal < db_data.sql

pause
