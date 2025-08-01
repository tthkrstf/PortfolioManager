CREATE TABLE IF NOT EXISTS Passwords(
   id int auto_increment primary key,
   name varchar(200) not null,
   loginid int not null,
   password varchar(200) not null,
   url varchar(200),
   notes varchar(200),
   deleteddate date,
   creationdate date not null,
   modificationdate date
);

CREATE TABLE IF NOT EXISTS Certificates(
   id int auto_increment primary key,
   name varchar(200) not null,
   servername varchar(200) not null,
   certificate varchar(200) not null,
   notes varchar(200),
   deleteddate date,
   creationdate date not null,
   modificationdate date
);

INSERT INTO Passwords values(null, 'name1', 30, 'password1', 'url1', 'notes1', null, '2025-05-05', '2025-05-06');
INSERT INTO Passwords values(null, 'name2', 20, 'password21', 'url2', 'notes1', null, '2025-05-05', '2025-05-06');
INSERT INTO Passwords values(null,'name3', 10, 'password31', 'url3', 'notes1', null, '2025-05-05', '2025-05-06');

INSERT INTO Certificates values(null, 'name1', 'servername', 'certificate','notes1', null, '2025-05-05', '2025-05-06');
INSERT INTO Certificates values(null, 'name2', 'servername', 'certificate', 'notes1', null, '2025-05-05', '2025-05-06');
INSERT INTO Certificates values(null,'name3', 'servername', 'certificate', 'notes1', '2015-05-05', '2025-05-05', '2025-05-06');
