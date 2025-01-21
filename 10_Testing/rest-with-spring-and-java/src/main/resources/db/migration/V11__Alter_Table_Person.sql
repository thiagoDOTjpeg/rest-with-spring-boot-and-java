ALTER TABLE `person`
    ADD COLUMN `enabled` BIT NOT NULL DEFAULT b'1' AFTER `gender`;