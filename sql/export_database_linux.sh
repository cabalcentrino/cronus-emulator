#!/bin/bash

MYSQLDUMP_OPTIONS="--skip-extended-insert --no-create-info --skip-create-options --skip-add-drop-table -u cronus -pcronuspassword"
OUTPUT_NAME="db_data.sql"
DATABASE_NAME="cronus_cabal"
TABLES_NAME="item_data item_name item_type level_up_condition mapa modelo_equipamento modelo_personagem quest_group quest_name weapon_rank_exp"

echo "Realizando backup dos dados..."
mysqldump $MYSQLDUMP_OPTIONS $DATABASE_NAME $TABLES_NAME > $OUTPUT_NAME
