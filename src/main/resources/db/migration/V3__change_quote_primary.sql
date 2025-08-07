delete from quote; -- any changes for this table will be deleted to not cause any issues in chaning

ALTER TABLE quote ADD UNIQUE (id);

ALTER TABLE quote DROP PRIMARY KEY;

ALTER TABLE quote DROP COLUMN id;

ALTER TABLE quote ADD PRIMARY KEY (symbol, creation_date);