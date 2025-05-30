-- Figure 1: University Database
DROP TABLE Professor CASCADE;
DROP TABLE Project CASCADE;
DROP TABLE Work_In CASCADE;
DROP TABLE Department CASCADE;
DROP TABLE Work_Department CASCADE;
DROP TABLE Graduate CASCADE;
DROP TABLE Work_Project CASCADE;

CREATE TABLE Professor (ssn CHAR(11) NOT NULL,
                        name CHAR(32),
                        age INTEGER, 
                        rank INTEGER, 
                        specialty CHAR(24),
                        PRIMARY KEY (ssn));
                        
CREATE TABLE Project (pno INTEGER NOT NULL, 
                      sponsor CHAR(24), 
                      start_date CHAR(10), -- 01/01/2001
                      end_date CHAR(10), 
                      budget float, 
                      ssn CHAR(11) NOT NULL, 
                      PRIMARY KEY (pno), 
                      FOREIGN KEY (ssn) REFERENCES Professor(ssn));

CREATE TABLE Work_In(ssn CHAR(11), 
                     pno INTEGER, 
                     PRIMARY KEY (ssn, pno),
                     FOREIGN KEY (ssn) REFERENCES Professor(ssn), 
                     FOREIGN KEY (pno) REFERENCES Project(pno));

CREATE TABLE Department(dno INTEGER NOT NULL, 
                        dname CHAR(24), 
                        office CHAR(24), 
                        ssn CHAR(11), 
                        PRIMARY KEY (dno), 
                        FOREIGN KEY (ssn) REFERENCES Professor(ssn));

CREATE TABLE Work_Department(ssn CHAR(11), 
                             dno INTEGER, 
                             time_pc float, 
                             PRIMARY KEY (ssn, dno), 
                             FOREIGN KEY (ssn) REFERENCES Professor(ssn), 
                             FOREIGN KEY (dno) REFERENCES Department(dno));

CREATE TABLE Graduate(ssn CHAR(11) NOT NULL,
                      name CHAR(11),
                      age INTEGER,
                      deg_pg CHAR(32),
                      ssn_advisor CHAR(11),
                      dno INTEGER,
                      PRIMARY KEY (ssn),
                      FOREIGN KEY (ssn_advisor) REFERENCES Graduate(ssn),
                      FOREIGN KEY (dno) REFERENCES Department(dno));

CREATE TABLE Work_Project(pno INTEGER NOT NULL,
                          ssn CHAR(11),
                          since CHAR(10),
                          ssn_professor CHAR(11),
                          PRIMARY KEY (ssn),
                          FOREIGN KEY (ssn_professor) REFERENCES Professor(ssn));

SELECT * FROM Professor;
SELECT * FROM Project;
SELECT * FROM Work_In;
SELECT * FROM Department;
SELECT * FROM Work_Department;
SELECT * FROM Graduate;
SELECT * FROM Work_Project;

-- Figure 2: Notown Records
DROP TABLE Place CASCADE;
DROP TABLE Telephone CASCADE;
DROP TABLE Musicians CASCADE;
DROP TABLE Lives CASCADE;
DROP TABLE Instrument CASCADE;
DROP TABLE Plays CASCADE;
DROP TABLE Songs CASCADE;
DROP TABLE Perform CASCADE;
DROP TABLE Album CASCADE;

CREATE TABLE Place(address TEXT NOT NULL,
                   PRIMARY KEY(address));
                   
CREATE TABLE Telephone(phone_no CHAR(14) NOT NULL, -- '(123) 456-7890' 14 chars
                       address TEXT,
                       PRIMARY KEY (address, phone_no),
                       FOREIGN KEY(address) REFERENCES Place(address) ON DELETE CASCADE);

CREATE TABLE Musicians(ssn CHAR(11) NOT NULL,
                       name text,
                       PRIMARY KEY (ssn));

CREATE TABLE Lives(ssn CHAR(11),
                   address TEXT,
                   PRIMARY KEY (ssn, address),
                   FOREIGN KEY (ssn) REFERENCES Musicians(ssn),
                   FOREIGN KEY (address) REFERENCES Place(address));

CREATE TABLE Instrument(instrid INTEGER NOT NULL,
                        key text,
                        dname text,
                        PRIMARY KEY (instrid));

CREATE TABLE Plays(ssn CHAR(11),
                   instrid INTEGER,
                   PRIMARY KEY (ssn, instrid),
                   FOREIGN KEY (ssn) REFERENCES Musicians(ssn),
                   FOREIGN KEY (instrid) REFERENCES Instrument(instrid));

CREATE TABLE Album(albumidentifier INTEGER NOT NULL,
                   copyright text,
                   speed float,
                   title text,
                   ssn_producer CHAR(11),
                   PRIMARY KEY(albumidentifier),
                   FOREIGN KEY (ssn_producer) REFERENCES Musicians(ssn));

CREATE TABLE Songs(songid INTEGER NOT NULL,
                   title text,
                   author text,
                   albumidentifier INTEGER,
                   PRIMARY KEY (songid),
                   FOREIGN KEY (albumidentifier) REFERENCES Album(albumidentifier));
                   
CREATE TABLE Perform(ssn CHAR(11),
                     songid INTEGER,
                     PRIMARY KEY (ssn, songid),
                     FOREIGN KEY (ssn) REFERENCES Musicians(ssn),
                     FOREIGN KEY (songid) REFERENCES Songs(songid));

SELECT * FROM Place;
SELECT * FROM Telephone;
SELECT * FROM Musicians;
SELECT * FROM Lives;
SELECT * FROM Instrument;
SELECT * FROM Plays;
SELECT * FROM Songs;
SELECT * FROM Perform;
SELECT * FROM Album;