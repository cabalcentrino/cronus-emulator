#!/bin/bash

GAME_DATA_FILE="db_data.sql"
DATABASE="cronus_cabal"

MYSQL_USER="cronus"
MYSQL_PASS="cronuspassword"

echo "Importing tables..."
mysql -u $MYSQL_USER -p$MYSQL_PASS < db_structure.sql

echo "Importing data $GAME_DATA_FILE..."
mysql -u $MYSQL_USER -p$MYSQL_PASS $DATABASE < $GAME_DATA_FILE
