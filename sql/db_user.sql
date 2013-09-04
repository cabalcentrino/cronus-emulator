CREATE USER 'cronus'@'localhost' IDENTIFIED BY 'cronuspassword';

DROP SCHEMA IF EXISTS `cronus_cabal` ;
CREATE SCHEMA IF NOT EXISTS `cronus_cabal` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;

GRANT ALL ON cronus_cabal.* TO 'cronus'@'localhost';