SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `cronus_cabal` ;
CREATE SCHEMA IF NOT EXISTS `cronus_cabal` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `cronus_cabal` ;

-- -----------------------------------------------------
-- Table `account`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `account` (
  `acc_account_id` INT NOT NULL AUTO_INCREMENT ,
  `acc_login` VARCHAR(16) NOT NULL ,
  `acc_password` CHAR(32) NOT NULL ,
  PRIMARY KEY (`acc_account_id`) ,
  UNIQUE INDEX `con_usuario_UNIQUE` (`acc_login` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `world`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `world` (
  `wld_world_id` TINYINT NOT NULL AUTO_INCREMENT ,
  `wld_name` VARCHAR(60) NOT NULL ,
  PRIMARY KEY (`wld_world_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `nation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `nation` (
  `nat_nation_id` TINYINT NOT NULL ,
  `nac_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`nat_nation_id`) )
ENGINE = InnoDB
MAX_ROWS = 4
MIN_ROWS = 4;


-- -----------------------------------------------------
-- Table `rank_weapon`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `rank_weapon` (
  `rka_rank_weapon_id` TINYINT NOT NULL AUTO_INCREMENT ,
  `rka_name` VARCHAR(45) NULL ,
  PRIMARY KEY (`rka_rank_weapon_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `server`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `server` (
  `ser_server_id` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `ser_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`ser_server_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `character`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `character` (
  `chr_character_id` INT UNSIGNED NOT NULL ,
  `chr_ser_server_id` TINYINT UNSIGNED NOT NULL ,
  `chr_acc_account_id` INT NOT NULL ,
  `chr_wld_world_id` TINYINT NOT NULL ,
  `chr_nat_nation_id` TINYINT NOT NULL DEFAULT 0 ,
  `chr_rka_magic_rank_id` TINYINT NOT NULL DEFAULT 1 ,
  `chr_rka_sword_rank_id` TINYINT NOT NULL DEFAULT 1 ,
  `chr_style` INT UNSIGNED NOT NULL ,
  `chr_name` VARCHAR(16) NOT NULL ,
  `chr_level` TINYINT UNSIGNED NOT NULL DEFAULT 1 ,
  `chr_str` SMALLINT NOT NULL ,
  `chr_int` SMALLINT NOT NULL ,
  `chr_dex` SMALLINT NOT NULL ,
  `chr_stats_points` SMALLINT NOT NULL DEFAULT 0 ,
  `chr_honor_points` INT UNSIGNED NOT NULL DEFAULT 0 ,
  `chr_world_x` SMALLINT UNSIGNED NOT NULL ,
  `chr_world_y` SMALLINT UNSIGNED NOT NULL ,
  `chr_alz` INT NOT NULL DEFAULT 0 ,
  `chr_warp_mask` INT NOT NULL DEFAULT 0 ,
  `chr_world_mask` INT NOT NULL ,
  `chr_experience` INT UNSIGNED NOT NULL DEFAULT 0 ,
  `chr_hp` INT UNSIGNED NOT NULL ,
  `chr_max_hp` SMALLINT UNSIGNED NOT NULL ,
  `chr_mp` INT UNSIGNED NOT NULL ,
  `chr_max_mp` INT UNSIGNED NOT NULL ,
  `chr_sp` INT UNSIGNED NOT NULL DEFAULT 0 ,
  `chr_max_sp` SMALLINT UNSIGNED NOT NULL DEFAULT 0 ,
  `chr_magic_exp` INT UNSIGNED NOT NULL DEFAULT 0 ,
  `chr_sword_exp` INT UNSIGNED NOT NULL DEFAULT 0 ,
  `chr_rank_exp` INT UNSIGNED NOT NULL DEFAULT 257 COMMENT '????' ,
  `chr_create_date` DATETIME NOT NULL ,
  `chr_last_update` TIMESTAMP NULL ,
  PRIMARY KEY (`chr_character_id`, `chr_ser_server_id`) ,
  INDEX `fk_character_account_idx` (`chr_acc_account_id` ASC) ,
  INDEX `fk_personagem_mapa1_idx` (`chr_wld_world_id` ASC) ,
  INDEX `fk_personagem_nacao1_idx` (`chr_nat_nation_id` ASC) ,
  INDEX `fk_personagem_rank_arma1_idx` (`chr_rka_magic_rank_id` ASC) ,
  INDEX `fk_personagem_rank_arma2_idx` (`chr_rka_sword_rank_id` ASC) ,
  INDEX `fk_personagem_server1_idx` (`chr_ser_server_id` ASC) ,
  CONSTRAINT `fk_character_account`
    FOREIGN KEY (`chr_acc_account_id` )
    REFERENCES `account` (`acc_account_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_personagem_mapa1`
    FOREIGN KEY (`chr_wld_world_id` )
    REFERENCES `world` (`wld_world_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_personagem_nacao1`
    FOREIGN KEY (`chr_nat_nation_id` )
    REFERENCES `nation` (`nat_nation_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_personagem_rank_arma1`
    FOREIGN KEY (`chr_rka_magic_rank_id` )
    REFERENCES `rank_weapon` (`rka_rank_weapon_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_personagem_rank_arma2`
    FOREIGN KEY (`chr_rka_sword_rank_id` )
    REFERENCES `rank_weapon` (`rka_rank_weapon_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_personagem_server1`
    FOREIGN KEY (`chr_ser_server_id` )
    REFERENCES `server` (`ser_server_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `classes`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `classes` (
  `cla_class_id` TINYINT NOT NULL ,
  `cla_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`cla_class_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `item_name`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `item_name` (
  `itn_item_id` INT UNSIGNED NOT NULL ,
  `itn_name` VARCHAR(128) NOT NULL ,
  PRIMARY KEY (`itn_item_id`) )
ENGINE = InnoDB
MAX_ROWS = 4094;


-- -----------------------------------------------------
-- Table `equipment_slot`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `equipment_slot` (
  `esl_equipment_slot_id` TINYINT NOT NULL ,
  `esl_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`esl_equipment_slot_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `equipment_template`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `equipment_template` (
  `etp_cla_class_id` TINYINT NOT NULL ,
  `etp_esl_equipment_slot_id` TINYINT NOT NULL ,
  `etp_itn_item_id` INT UNSIGNED NOT NULL ,
  `etp_item_opt1` INT NOT NULL ,
  `etp_item_opt2` INT NOT NULL ,
  `etp_item_opt3` INT NOT NULL ,
  INDEX `fk_modelo_equipamento_classe1_idx` (`etp_cla_class_id` ASC) ,
  INDEX `fk_modelo_equipamento_item1_idx` (`etp_itn_item_id` ASC) ,
  INDEX `fk_modelo_equipamento_equipamento_slot1_idx` (`etp_esl_equipment_slot_id` ASC) ,
  PRIMARY KEY (`etp_cla_class_id`, `etp_esl_equipment_slot_id`) ,
  CONSTRAINT `fk_modelo_equipamento_classe1`
    FOREIGN KEY (`etp_cla_class_id` )
    REFERENCES `classes` (`cla_class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_modelo_equipamento_item1`
    FOREIGN KEY (`etp_itn_item_id` )
    REFERENCES `item_name` (`itn_item_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_modelo_equipamento_equipamento_slot1`
    FOREIGN KEY (`etp_esl_equipment_slot_id` )
    REFERENCES `equipment_slot` (`esl_equipment_slot_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `equipment`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `equipment` (
  `eqp_chr_character_id` INT UNSIGNED NOT NULL ,
  `eqp_chr_ser_server_id` TINYINT UNSIGNED NOT NULL ,
  `eqp_esl_equipment_slot_id` TINYINT NOT NULL ,
  `eqp_itn_item_id` INT UNSIGNED NOT NULL ,
  `eqp_item_opt1` INT UNSIGNED NOT NULL ,
  `eqp_item_opt2` INT NOT NULL COMMENT 'Quantidade, Aprimoramento do Slot' ,
  `eqp_item_opt3` INT NOT NULL COMMENT 'Slots' ,
  INDEX `fk_equipamento_item1_idx` (`eqp_itn_item_id` ASC) ,
  PRIMARY KEY (`eqp_chr_character_id`, `eqp_chr_ser_server_id`, `eqp_esl_equipment_slot_id`) ,
  INDEX `fk_equipamento_personagem1_idx` (`eqp_chr_character_id` ASC, `eqp_chr_ser_server_id` ASC) ,
  CONSTRAINT `fk_equipamento_item1`
    FOREIGN KEY (`eqp_itn_item_id` )
    REFERENCES `item_name` (`itn_item_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_equipamento_equipamento_slot1`
    FOREIGN KEY (`eqp_esl_equipment_slot_id` )
    REFERENCES `equipment_slot` (`esl_equipment_slot_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_equipamento_personagem1`
    FOREIGN KEY (`eqp_chr_character_id` , `eqp_chr_ser_server_id` )
    REFERENCES `character` (`chr_character_id` , `chr_ser_server_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `general_inventory_template`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `general_inventory_template` (
  `git_slot` SMALLINT UNSIGNED NOT NULL ,
  `git_itn_item_id` INT UNSIGNED NOT NULL ,
  `git_item_opt1` INT UNSIGNED NOT NULL ,
  `git_item_opt2` INT UNSIGNED NOT NULL ,
  `git_item_opt3` INT UNSIGNED NOT NULL ,
  INDEX `fk_modelo_bag_geral_item1_idx` (`git_itn_item_id` ASC) ,
  PRIMARY KEY (`git_slot`) ,
  CONSTRAINT `fk_modelo_bag_geral_item1`
    FOREIGN KEY (`git_itn_item_id` )
    REFERENCES `item_name` (`itn_item_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inventory_template`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `inventory_template` (
  `ivt_cla_class_id` TINYINT NOT NULL ,
  `ivt_slot` SMALLINT NOT NULL ,
  `ivt_itn_item_id` INT UNSIGNED NOT NULL ,
  `ivt_item_opt1` INT UNSIGNED NOT NULL ,
  `ivt_item_opt2` INT UNSIGNED NOT NULL ,
  `ivt_item_opt3` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`ivt_cla_class_id`, `ivt_slot`) ,
  INDEX `fk_modelo_bag_item1_idx` (`ivt_itn_item_id` ASC) ,
  CONSTRAINT `fk_modelo_bag_classe1`
    FOREIGN KEY (`ivt_cla_class_id` )
    REFERENCES `classes` (`cla_class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_modelo_bag_item1`
    FOREIGN KEY (`ivt_itn_item_id` )
    REFERENCES `item_name` (`itn_item_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `character_template`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `character_template` (
  `ctp_cla_class_id` TINYINT NOT NULL ,
  `ctp_wld_world_id` TINYINT NOT NULL ,
  `ctp_str` SMALLINT NULL DEFAULT 0 ,
  `ctp_int` SMALLINT NULL DEFAULT 0 ,
  `ctp_dex` SMALLINT NULL DEFAULT 0 ,
  `ctp_world_x` SMALLINT UNSIGNED NOT NULL ,
  `ctp_world_y` SMALLINT UNSIGNED NOT NULL ,
  `ctp_world_mask` INT UNSIGNED NULL DEFAULT 3 ,
  `ctp_hp` INT UNSIGNED NOT NULL ,
  `ctp_mp` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`ctp_cla_class_id`) ,
  INDEX `fk_modelo_personagem_mapa1_idx` (`ctp_wld_world_id` ASC) ,
  CONSTRAINT `fk_modelo_personagem_classe1`
    FOREIGN KEY (`ctp_cla_class_id` )
    REFERENCES `classes` (`cla_class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_modelo_personagem_mapa1`
    FOREIGN KEY (`ctp_wld_world_id` )
    REFERENCES `world` (`wld_world_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `inventory`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `inventory` (
  `inv_chr_character_id` INT UNSIGNED NOT NULL ,
  `inv_chr_ser_server_id` TINYINT UNSIGNED NOT NULL ,
  `inv_slot` SMALLINT NOT NULL ,
  `inv_itn_item_id` INT UNSIGNED NOT NULL ,
  `inv_item_opt1` INT UNSIGNED NOT NULL ,
  `inv_item_opt2` INT UNSIGNED NOT NULL ,
  `inv_item_opt3` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`inv_chr_character_id`, `inv_chr_ser_server_id`, `inv_slot`) ,
  INDEX `fk_bolsa_item1_idx` (`inv_itn_item_id` ASC) ,
  INDEX `fk_bolsa_personagem1_idx` (`inv_chr_character_id` ASC, `inv_chr_ser_server_id` ASC) ,
  CONSTRAINT `fk_bolsa_item1`
    FOREIGN KEY (`inv_itn_item_id` )
    REFERENCES `item_name` (`itn_item_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bolsa_personagem1`
    FOREIGN KEY (`inv_chr_character_id` , `inv_chr_ser_server_id` )
    REFERENCES `character` (`chr_character_id` , `chr_ser_server_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `character_count`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `character_count` (
  `cct_ser_server_id` TINYINT UNSIGNED NOT NULL ,
  `cct_acc_account_id` INT NOT NULL ,
  `cct_count` TINYINT NOT NULL ,
  PRIMARY KEY (`cct_ser_server_id`, `cct_acc_account_id`) ,
  INDEX `fk_quantidade_personagens_conta1_idx` (`cct_acc_account_id` ASC) ,
  CONSTRAINT `fk_quantidade_personagens_server1`
    FOREIGN KEY (`cct_ser_server_id` )
    REFERENCES `server` (`ser_server_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quantidade_personagens_conta1`
    FOREIGN KEY (`cct_acc_account_id` )
    REFERENCES `account` (`acc_account_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `skill`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `skill` (
  `ski_skill_id` SMALLINT UNSIGNED NOT NULL ,
  `ski_name` VARCHAR(45) NULL ,
  PRIMARY KEY (`ski_skill_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `general_skillbook_template`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `general_skillbook_template` (
  `gst_slot` SMALLINT UNSIGNED NOT NULL COMMENT 'Skill Slot' ,
  `gst_ski_skill_id` SMALLINT UNSIGNED NOT NULL COMMENT 'Skill Id' ,
  `gst_level` TINYINT UNSIGNED NOT NULL COMMENT 'Skill Start Level' ,
  PRIMARY KEY (`gst_slot`) ,
  INDEX `fk_modelo_skillbook_geral_skill1_idx` (`gst_ski_skill_id` ASC) ,
  CONSTRAINT `fk_modelo_skillbook_geral_skill1`
    FOREIGN KEY (`gst_ski_skill_id` )
    REFERENCES `skill` (`ski_skill_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `skill_book`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `skill_book` (
  `skb_chr_character_id` INT UNSIGNED NOT NULL ,
  `skb_chr_ser_server_id` TINYINT UNSIGNED NOT NULL ,
  `skb_slot` SMALLINT UNSIGNED NOT NULL ,
  `skb_ski_skill_id` SMALLINT UNSIGNED NOT NULL ,
  `skb_level` TINYINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`skb_chr_character_id`, `skb_chr_ser_server_id`, `skb_slot`) ,
  INDEX `fk_skill_book_skill1_idx` (`skb_ski_skill_id` ASC) ,
  CONSTRAINT `fk_skill_book_personagem1`
    FOREIGN KEY (`skb_chr_character_id` , `skb_chr_ser_server_id` )
    REFERENCES `character` (`chr_character_id` , `chr_ser_server_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_skill_book_skill1`
    FOREIGN KEY (`skb_ski_skill_id` )
    REFERENCES `skill` (`ski_skill_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `skillbook_template`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `skillbook_template` (
  `sbt_cla_class_id` TINYINT NOT NULL ,
  `sbt_slot` SMALLINT UNSIGNED NOT NULL ,
  `sbt_ski_skill_id` SMALLINT UNSIGNED NOT NULL ,
  `sbt_level` TINYINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`sbt_cla_class_id`, `sbt_slot`) ,
  INDEX `fk_modelo_skill_classe1_idx` (`sbt_cla_class_id` ASC) ,
  INDEX `fk_modelo_skill_skill1_idx` (`sbt_ski_skill_id` ASC) ,
  CONSTRAINT `fk_modelo_skill_classe1`
    FOREIGN KEY (`sbt_cla_class_id` )
    REFERENCES `classes` (`cla_class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_modelo_skill_skill1`
    FOREIGN KEY (`sbt_ski_skill_id` )
    REFERENCES `skill` (`ski_skill_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aura`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aura` (
  `aur_aura_id` INT NOT NULL COMMENT 'FIXME: Unk data type' ,
  `aur_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`aur_aura_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reputation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `reputation` (
  `rep_reputation_id` INT NOT NULL COMMENT 'FIXME: Unk Data Type' ,
  `rep_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`rep_reputation_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quest_group`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `quest_group` (
  `qgp_quest_group_id` INT UNSIGNED NOT NULL ,
  `qgp_name` VARCHAR(80) NOT NULL ,
  PRIMARY KEY (`qgp_quest_group_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quest_name`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `quest_name` (
  `qsn_quest_id` INT NOT NULL ,
  `qsn_name` VARCHAR(80) NOT NULL ,
  PRIMARY KEY (`qsn_quest_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `level_up_condition`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `level_up_condition` (
  `luc_cod_level_up_condition` TINYINT UNSIGNED NOT NULL ,
  `luc_exp` BIGINT UNSIGNED NOT NULL ,
  `luc_accuexp` BIGINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`luc_cod_level_up_condition`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weapon_rank_exp`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `weapon_rank_exp` (
  `wre_rka_weapon_rank_id` TINYINT NOT NULL ,
  `wre_cla_class_id` TINYINT NOT NULL ,
  `wre_sword_exp` INT UNSIGNED NOT NULL ,
  `wre_magic_exp` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`wre_rka_weapon_rank_id`, `wre_cla_class_id`) ,
  INDEX `fk_weapon_rank_exp_classe1_idx` (`wre_cla_class_id` ASC) ,
  CONSTRAINT `fk_weapon_rank_exp_rank_arma1`
    FOREIGN KEY (`wre_rka_weapon_rank_id` )
    REFERENCES `rank_weapon` (`rka_rank_weapon_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_weapon_rank_exp_classe1`
    FOREIGN KEY (`wre_cla_class_id` )
    REFERENCES `classes` (`cla_class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `item_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `item_type` (
  `itp_item_type_id` INT UNSIGNED NOT NULL ,
  `itp_name` VARCHAR(80) NOT NULL ,
  PRIMARY KEY (`itp_item_type_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `item_property`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `item_property` (
  `ipr_item_property_id` INT UNSIGNED NOT NULL ,
  `ipr_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`ipr_item_property_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `item_size`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `item_size` (
  `isi_item_size_id` INT NOT NULL ,
  `isi_name` CHAR(4) NOT NULL ,
  `isi_width` INT NOT NULL ,
  `isi_height` INT NOT NULL ,
  PRIMARY KEY (`isi_item_size_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `item_data`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `item_data` (
  `idt_itn_cod_item` INT UNSIGNED NOT NULL ,
  `idt_itp_cod_item_type` INT UNSIGNED NOT NULL ,
  `idt_cla_cod_classe` TINYINT NOT NULL ,
  `idt_cla_cod_classe1` TINYINT NOT NULL ,
  `idt_cla_cod_classe2` TINYINT NOT NULL ,
  `idt_ipr_cod_item_property` INT UNSIGNED NOT NULL ,
  `idt_isi_cod_item_size` INT NOT NULL ,
  `idt_limit_level` INT NOT NULL ,
  `idt_limit_reputation` INT UNSIGNED NOT NULL ,
  `idt_stat_str2` INT UNSIGNED NOT NULL ,
  `idt_stat_dex2` INT UNSIGNED NOT NULL ,
  `idt_stat_int2` INT UNSIGNED NOT NULL ,
  `idt_price_sell` INT UNSIGNED NOT NULL ,
  `idt_value_lv` INT UNSIGNED NOT NULL ,
  `idt_att_rate` INT UNSIGNED NOT NULL ,
  `idt_def_rate` INT UNSIGNED NOT NULL ,
  `idt_def_lev_lmt_mag_att_val` INT UNSIGNED NOT NULL ,
  `idt_max_core` INT UNSIGNED NOT NULL ,
  `idt_str_lmt1_opt2` INT UNSIGNED NOT NULL ,
  `idt_dex_lmt1_opt2_val` INT UNSIGNED NOT NULL ,
  `idt_int_lmt1_opt3` INT UNSIGNED NOT NULL ,
  `idt_str_lmt2_opt3_val` INT UNSIGNED NOT NULL ,
  `idt_dex_lmt2_opt4` INT UNSIGNED NOT NULL ,
  `idt_int_lmt2_opt4_val` INT UNSIGNED NOT NULL ,
  `idt_stat_str1` INT UNSIGNED NOT NULL ,
  `idt_stat_dex1` INT UNSIGNED NOT NULL ,
  `idt_stat_int1` INT UNSIGNED NOT NULL ,
  `idt_grade` TINYINT UNSIGNED NOT NULL ,
  `idt_enchant_code_lnk` TINYINT UNSIGNED NOT NULL ,
  `idt_period_type` TINYINT UNSIGNED NOT NULL ,
  `idt_period_use` TINYINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`idt_itn_cod_item`) ,
  INDEX `fk_item_data_item_type1_idx` (`idt_itp_cod_item_type` ASC) ,
  INDEX `fk_item_data_classe1_idx` (`idt_cla_cod_classe` ASC) ,
  INDEX `fk_item_data_classe2_idx` (`idt_cla_cod_classe1` ASC) ,
  INDEX `fk_item_data_classe3_idx` (`idt_cla_cod_classe2` ASC) ,
  INDEX `fk_item_data_item_property1_idx` (`idt_ipr_cod_item_property` ASC) ,
  INDEX `fk_item_data_item_size1_idx` (`idt_isi_cod_item_size` ASC) ,
  CONSTRAINT `fk_item_data_item_name1`
    FOREIGN KEY (`idt_itn_cod_item` )
    REFERENCES `item_name` (`itn_item_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_data_item_type1`
    FOREIGN KEY (`idt_itp_cod_item_type` )
    REFERENCES `item_type` (`itp_item_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_data_classe1`
    FOREIGN KEY (`idt_cla_cod_classe` )
    REFERENCES `classes` (`cla_class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_data_classe2`
    FOREIGN KEY (`idt_cla_cod_classe1` )
    REFERENCES `classes` (`cla_class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_data_classe3`
    FOREIGN KEY (`idt_cla_cod_classe2` )
    REFERENCES `classes` (`cla_class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_data_item_property1`
    FOREIGN KEY (`idt_ipr_cod_item_property` )
    REFERENCES `item_property` (`ipr_item_property_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_data_item_size1`
    FOREIGN KEY (`idt_isi_cod_item_size` )
    REFERENCES `item_size` (`isi_item_size_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `monster`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `monster` (
  `mon_cod_monster` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`mon_cod_monster`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `monster_name`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `monster_name` (
  `mna_mon_monster_id` INT UNSIGNED NOT NULL ,
  `mna_name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`mna_mon_monster_id`) ,
  CONSTRAINT `fk_monster_name_monster1`
    FOREIGN KEY (`mna_mon_monster_id` )
    REFERENCES `monster` (`mon_cod_monster` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- procedure p_character_createNew
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_character_createNew` (characterId INT UNSIGNED, serverId TINYINT UNSIGNED, accountId INT, class TINYINT, style INT, characterName VARCHAR(16))
MODIFIES SQL DATA
root:BEGIN
	-- TODO: Verificar palavrão ( Retorno 0x04 )
	DECLARE errorFound BOOLEAN DEFAULT FALSE;

	-- , SQLWARNING, NOT FOUND
    DECLARE EXIT HANDLER
        FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;  
            SELECT 1 FROM DUAL;
        END;

	/* Check if already exits a character with that id (slot) */
	SELECT
		TRUE INTO errorFound
	FROM
		`character`
	WHERE
		chr_character_id = characterId
		AND chr_ser_server_id = serverId
	LIMIT 1;

	/* If found a character in slot return error already exist (0x02) */
	IF(errorFound) THEN
		SELECT 2 FROM DUAL;
		LEAVE root;
	END IF;
    
    SET autocommit = 0;
    START TRANSACTION;

	/* Check if exists character with entered name in server */
	SELECT
		TRUE INTO errorFound
	FROM
		`character`
	WHERE
		chr_ser_server_id = serverId
		AND chr_name = characterName		
	LIMIT 1;

	/* When exists a character with that name in server return error (0x03) */
	IF(errorFound) THEN
		SELECT 3 FROM DUAL;
		LEAVE root;
	END IF;
	
	/* 
	 * Update character count for account 
	 */
	CALL p_account_updateCharacterCount(accountId, serverId, 1);

    /* Inserte new character */
    INSERT INTO
        `character`
        ( 
			chr_character_id, chr_ser_server_id,
            chr_acc_account_id,  chr_style, chr_name, chr_create_date, 
            chr_wld_world_id, chr_str, chr_int, chr_dex, 
            chr_world_x, chr_world_y, chr_world_mask, chr_hp, 
			chr_max_hp, chr_mp, chr_max_mp
        )
    SELECT
		characterId,
		serverId,
        accountId,
        style,
        characterName,
        NOW(),
        ctp.ctp_wld_world_id,
        ctp.ctp_str,
        ctp.ctp_int,
        ctp.ctp_dex,
        ctp.ctp_world_x,
        ctp.ctp_world_y,
        ctp.ctp_world_mask,
        ctp.ctp_hp,
		ctp.ctp_hp, -- Max HP
        ctp.ctp_mp,
		ctp.ctp_mp	-- Max MP
    FROM
        `character_template` ctp
    WHERE
        ctp.ctp_cla_class_id = class
    LIMIT 1;
    
    /* Add default equipments */
    INSERT INTO
        `equipment`
        (
            eqp_chr_character_id, eqp_chr_ser_server_id, eqp_esl_equipment_slot_id, 
            eqp_itn_item_id, eqp_item_opt1, eqp_item_opt2, eqp_item_opt3
        )
    SELECT
        characterId,
		serverId,
        etp_esl_equipment_slot_id,
        etp_itn_item_id,
        etp_item_opt1,
        etp_item_opt2,
        etp_item_opt3
    FROM
        `equipment_template`
    WHERE
        etp_cla_class_id = class;
    
    /* Add default and specifique items to inventory */
    INSERT INTO
        `inventory`
        (
            inv_chr_character_id,
			inv_chr_ser_server_id,
            inv_slot,
            inv_itn_item_id,
            inv_item_opt1,
            inv_item_opt2,
            inv_item_opt3
        )
    SELECT
        characterId,
		serverId,
        git_slot,
        git_itn_item_id,
        git_item_opt1,
        git_item_opt2,
        git_item_opt3
    FROM
        `general_inventory_template`
    
    UNION

    SELECT
        characterId,
		serverId,
        ivt_slot,
        ivt_itn_item_id,
        ivt_item_opt1,
        ivt_item_opt2,
        ivt_item_opt3
    FROM
        `inventory_template`
    WHERE
        ivt_cla_class_id = class;


	/* Add default and specifyc skills */
	INSERT INTO

		`skill_book`
		(
			skb_chr_character_id,
			skb_chr_ser_server_id,
			skb_ski_skill_id,
			skb_level,
			skb_slot
		)

	SELECT
		characterId,
		serverId,
		gst_ski_skill_id,
		gst_level,
		gst_slot
	FROM
		`general_skillbook_template`

	UNION

	SELECT
		characterId,
		serverId,
		sbt_ski_skill_id,
		sbt_level,
		sbt_slot
	FROM
		`skillbook_template`
	WHERE
		sbt_cla_class_id = class;

    SELECT
        160 | class -- 0xA1 ~ 0xA6
    FROM 
        DUAL;

    COMMIT;
END



$$

-- -----------------------------------------------------
-- procedure p_account_updateCharacterCount
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_account_updateCharacterCount` (accountId INT, serverId TINYINT, addCount TINYINT)
MODIFIES SQL DATA
BEGIN
	UPDATE
		`character_count`
	SET
		cct_count = cct_count + addCount
	WHERE
		cct_acc_account_id = accountId
		AND cct_ser_server_id = serverId;

	/* Verifica se foi atualizado*/
	IF(ROW_COUNT() = 0) THEN
		INSERT INTO
			`character_count`
		VALUES
		(
			serverId,
			accountId,
			addCount
		);
	END IF;
END
$$

-- -----------------------------------------------------
-- procedure p_account_getCharactersForSelection
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_account_getCharactersForSelection` (accountId INT UNSIGNED, serverId TINYINT)
READS SQL DATA
BEGIN
	SELECT
		chr_character_id,
		chr_name,
		chr_level,
		chr_style,		
		chr_rka_sword_rank_id,
		chr_rka_magic_rank_id,
		chr_nat_nation_id,
		chr_alz,
		chr_wld_world_id,
		chr_world_x,
		chr_world_y
	FROM
		`character`
	WHERE
		chr_acc_account_id = accountId
		AND chr_ser_server_id = serverId
	ORDER BY
		1
	LIMIT 6;
END
$$

-- -----------------------------------------------------
-- procedure p_equipment_getAllCharacterItems
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_equipment_getAllCharacterItems` (characterId INT UNSIGNED, serverId TINYINT)
READS SQL DATA
BEGIN
	SELECT 
		eqp_item_opt1, 
		eqp_item_opt2, 
		eqp_item_opt3, 
		eqp_esl_equipment_slot_id
	FROM 
		`equipment` 
	WHERE 
		eqp_chr_character_id = characterId 
		AND eqp_chr_ser_server_id = serverId 
	ORDER BY 
		4;
END$$

-- -----------------------------------------------------
-- procedure p_character_getComplementaryInfo
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_character_getComplementaryInfo` (characterId INT UNSIGNED, serverId TINYINT)
READS SQL DATA
BEGIN
	SELECT
		chr_str,
		chr_int,
		chr_dex,
		chr_stats_points,
		chr_honor_points,
		chr_warp_mask,
		chr_world_mask,
		chr_experience,
		chr_hp,
		chr_max_hp,
		chr_mp,
		chr_max_mp,
		chr_sp,
		chr_max_sp,
		chr_magic_exp,
		chr_sword_exp,
		chr_rank_exp    
	FROM
		`character`
	WHERE
		chr_character_id = characterId
		AND chr_ser_server_id = serverId
	LIMIT 1;
END$$

-- -----------------------------------------------------
-- procedure p_character_getAllInfo
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_character_getAllInfo` (characterId INT UNSIGNED, serverId TINYINT)
READS SQL DATA
BEGIN
	SELECT
        chr_str,                      	-- 1
        chr_int,               			-- 2
        chr_dex,                   		-- 3
        chr_stats_points,               -- 4
        chr_honor_points,               -- 5
        chr_warp_mask,               	-- 6
        chr_world_mask,              	-- 7
        chr_experience,                	-- 8
        chr_hp,                         -- 9
		chr_max_hp,						-- 10
        chr_mp,                         -- 11
		chr_max_mp,						-- 12
        chr_sp,                         -- 13
		chr_max_sp,						-- 14
        chr_magic_exp,          		-- 15
        chr_sword_exp,         			-- 16
        chr_rank_exp,           		-- 17
        -- Extras
        chr_wld_world_id,               -- 18
        chr_nat_nation_id,              -- 19
        chr_rka_magic_rank_id, 			-- 20
        chr_rka_sword_rank_id, 			-- 21
        chr_style,                     	-- 22
        chr_name,                       -- 23
        chr_level,                      -- 24
        chr_world_x,                  	-- 25
        chr_world_y,                  	-- 26
        chr_alz                         -- 27
	FROM
			`character`
	WHERE
			chr_character_id = characterId
			AND chr_ser_server_id = serverId
	LIMIT 1;
END$$

-- -----------------------------------------------------
-- procedure p_character_inventory_getAllItems
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_character_inventory_getAllItems` (characterId INT, serverId TINYINT)
READS SQL DATA
BEGIN
	SELECT
		inv_item_opt1    AS 'ItemId',
		inv_item_opt2    AS 'ItemOpt1',
		inv_item_opt3    AS 'ItemOpt2',
		inv_slot         AS 'InventorySlot'
	FROM
		`inventory`
	WHERE
		inv_chr_character_id = characterId
		AND inv_chr_ser_server_id = serverId
	ORDER BY 
		4;
END


$$

-- -----------------------------------------------------
-- procedure p_character_skillbook_getSkills
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_character_skillbook_getSkills` (characterId INT UNSIGNED, serverId TINYINT UNSIGNED)
READS SQL DATA
BEGIN

	SELECT
		skb_ski_skill_id   	AS 'SkillId',
		skb_level           AS 'SkillLevel',
		skb_slot            AS 'SkillBookSlot'
	FROM
		`skill_book`
	WHERE
		skb_chr_character_id = characterId
		AND skb_chr_ser_server_id = serverId
	ORDER BY
		3;

END$$

-- -----------------------------------------------------
-- procedure p_character_inventory_removeAll
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_character_inventory_removeAll` (characterId INT UNSIGNED, serverId SMALLINT UNSIGNED)
MODIFIES SQL DATA
BEGIN
	DELETE FROM 
		`inventory` 
	WHERE 
		inv_chr_character_id = characterId
		AND inv_chr_ser_server_id = serverId;
END

$$

-- -----------------------------------------------------
-- procedure p_character_inventory_addItem
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_character_inventory_addItem` (characterId INT UNSIGNED, serverId TINYINT UNSIGNED, inventorySlot SMALLINT UNSIGNED, itemId INT UNSIGNED, itemOpt1 INT UNSIGNED, itemOpt2 INT UNSIGNED, itemOpt3 INT UNSIGNED)
MODIFIES SQL DATA
BEGIN
	INSERT INTO 
		`inventory` 
	VALUES
	(
		characterId,
		serverId,
		inventorySlot,
		itemId,
		itemOpt1,
		itemOpt2,
		itemOpt3		
	);
END

$$

-- -----------------------------------------------------
-- procedure p_character_equipment_addItem
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_character_equipment_addItem` (characterId INT UNSIGNED, serverId TINYINT UNSIGNED, equipmentSlot SMALLINT UNSIGNED, itemId INT UNSIGNED, itemOpt1 INT UNSIGNED, itemOpt2 INT UNSIGNED, itemOpt3 INT UNSIGNED)
MODIFIES SQL DATA
BEGIN
	INSERT INTO 
		`equipment` 
	VALUES 
	(
		characterId,
		serverId,
		equipmentSlot,
		itemId,
		itemOpt1,
		itemOpt2,
		itemOpt3
	);            
END$$

-- -----------------------------------------------------
-- procedure p_character_equipment_deleteAll
-- -----------------------------------------------------

DELIMITER $$
USE `cronus_cabal`$$
CREATE PROCEDURE `p_character_equipment_deleteAll` (characterId INT UNSIGNED, serverId TINYINT UNSIGNED)
MODIFIES SQL DATA
BEGIN
	DELETE FROM 
		`equipment` 
	WHERE 
		eqp_chr_character_id = characterId 
		AND eqp_chr_ser_server_id = serverId;
END$$


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `account`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `account` (`acc_account_id`, `acc_login`, `acc_password`) VALUES (1, 'root', '123');

COMMIT;

-- -----------------------------------------------------
-- Data for table `nation`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `nation` (`nat_nation_id`, `nac_name`) VALUES (0, 'No nation');
INSERT INTO `nation` (`nat_nation_id`, `nac_name`) VALUES (1, 'Capella');
INSERT INTO `nation` (`nat_nation_id`, `nac_name`) VALUES (2, 'Procyon');
INSERT INTO `nation` (`nat_nation_id`, `nac_name`) VALUES (3, 'Gamemaster');

COMMIT;

-- -----------------------------------------------------
-- Data for table `rank_weapon`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (1, 'Novice');
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (2, 'Apprentice');
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (3, 'Regular');
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (4, 'Expert');
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (5, 'A. Expert');
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (6, 'Master');
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (7, 'A. Master');
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (8, 'G. Master');
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (9, 'Completer');
INSERT INTO `rank_weapon` (`rka_rank_weapon_id`, `rka_name`) VALUES (10, 'Transcender');

COMMIT;

-- -----------------------------------------------------
-- Data for table `server`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `server` (`ser_server_id`, `ser_name`) VALUES (NULL, 'Development');

COMMIT;

-- -----------------------------------------------------
-- Data for table `classes`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `classes` (`cla_class_id`, `cla_name`) VALUES (1, 'Warrior');
INSERT INTO `classes` (`cla_class_id`, `cla_name`) VALUES (2, 'Blader');
INSERT INTO `classes` (`cla_class_id`, `cla_name`) VALUES (3, 'Wizard');
INSERT INTO `classes` (`cla_class_id`, `cla_name`) VALUES (4, 'Force Archer');
INSERT INTO `classes` (`cla_class_id`, `cla_name`) VALUES (5, 'Force Shielder');
INSERT INTO `classes` (`cla_class_id`, `cla_name`) VALUES (6, 'Force Blader');
INSERT INTO `classes` (`cla_class_id`, `cla_name`) VALUES (0, 'All');

COMMIT;

-- -----------------------------------------------------
-- Data for table `equipment_slot`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (0, 'Head');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (1, 'Body');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (2, 'Hand');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (3, 'Feet');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (4, 'Left Hand');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (5, 'Right Hand');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (6, 'Epaulet');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (7, 'Amuleto');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (8, 'Upper Left Ring');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (9, 'Upper Right Ring');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (10, 'Bike / Board');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (11, 'Pet');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (12, 'Pet Extra ???');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (13, 'Right Earring');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (14, 'Left Earring');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (15, 'Right Bracellet');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (16, 'Left Bracellet');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (17, 'Lower Right Ring');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (18, 'Lower Left Ring');
INSERT INTO `equipment_slot` (`esl_equipment_slot_id`, `esl_name`) VALUES (19, 'Belt');

COMMIT;

-- -----------------------------------------------------
-- Data for table `skill`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (2, 'Estocada Impactante');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (333, 'Aura de Batalha');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (340, 'O Invencivel');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (341, 'O Ceifador');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (400, 'Arremesso');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (380, '-');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (381, '-');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (382, '-');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (383, '-');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (1, 'Saque Relampago');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (421, 'Tecnica de Combo da Prancha');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (422, 'Tecnica de Combo da Moto');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (474, 'Espada Memorial');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (481, 'Ataque de Machado A');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (482, 'Ataque de Machado B');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (483, 'Especialidade com Machado Level 1');
INSERT INTO `skill` (`ski_skill_id`, `ski_name`) VALUES (484, 'Especialidade com Machado Level 2');

COMMIT;

-- -----------------------------------------------------
-- Data for table `general_skillbook_template`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `general_skillbook_template` (`gst_slot`, `gst_ski_skill_id`, `gst_level`) VALUES (70, 333, 1);
INSERT INTO `general_skillbook_template` (`gst_slot`, `gst_ski_skill_id`, `gst_level`) VALUES (102, 421, 1);
INSERT INTO `general_skillbook_template` (`gst_slot`, `gst_ski_skill_id`, `gst_level`) VALUES (103, 422, 1);
INSERT INTO `general_skillbook_template` (`gst_slot`, `gst_ski_skill_id`, `gst_level`) VALUES (104, 474, 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `skillbook_template`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 0, 2, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 72, 340, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 73, 341, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 75, 400, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 78, 380, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 79, 381, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 80, 382, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 81, 383, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 1, 1, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 105, 481, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 106, 482, 1);
INSERT INTO `skillbook_template` (`sbt_cla_class_id`, `sbt_slot`, `sbt_ski_skill_id`, `sbt_level`) VALUES (1, 107, 483, 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `aura`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `aura` (`aur_aura_id`, `aur_name`) VALUES (0, 'None');
INSERT INTO `aura` (`aur_aura_id`, `aur_name`) VALUES (1, 'Earth');
INSERT INTO `aura` (`aur_aura_id`, `aur_name`) VALUES (2, 'Water');
INSERT INTO `aura` (`aur_aura_id`, `aur_name`) VALUES (3, 'Wing');
INSERT INTO `aura` (`aur_aura_id`, `aur_name`) VALUES (4, 'Fire');
INSERT INTO `aura` (`aur_aura_id`, `aur_name`) VALUES (5, 'Ice');
INSERT INTO `aura` (`aur_aura_id`, `aur_name`) VALUES (6, 'Thunder');

COMMIT;

-- -----------------------------------------------------
-- Data for table `item_property`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (0, 'Normal');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (1, 'Drop not allowed');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (2, 'Trade not allowed');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (3, 'Account bound');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (4, 'Cannot sell');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (5, 'Cannot sell, cannot drop');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (6, 'Cannot sell, drop or trade');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (7, 'Character bound, cannot sell');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (8, 'Cannot store');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (9, 'Cannot drop or store');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (10, 'Cannot drop or trade');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (11, 'Character bound');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (12, 'Cannot sell or store');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (13, 'Cannot drop, sell or store');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (14, 'Cannot sell, store or trade');
INSERT INTO `item_property` (`ipr_item_property_id`, `ipr_name`) VALUES (15, 'Character bound, cannot sell');

COMMIT;

-- -----------------------------------------------------
-- Data for table `item_size`
-- -----------------------------------------------------
START TRANSACTION;
USE `cronus_cabal`;
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (0, '1x1', 1, 1);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (1, '1x2', 1, 2);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (2, '1x3', 1, 3);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (3, '1x4', 1, 4);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (4, '2x1', 2, 1);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (5, '2x2', 2, 2);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (6, '2x3', 2, 3);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (7, '2x4', 2, 4);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (8, '3x1', 3, 1);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (9, '3x2', 3, 2);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (10, '3x3', 3, 3);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (11, '3x4', 3, 4);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (12, '4x1', 4, 1);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (13, '4x2', 4, 2);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (14, '4x3', 4, 3);
INSERT INTO `item_size` (`isi_item_size_id`, `isi_name`, `isi_width`, `isi_height`) VALUES (15, '4x4', 4, 4);

COMMIT;
