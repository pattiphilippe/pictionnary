CREATE TABLE Word(
    wId NUMERIC(10) NOT NULL,
    wTxt VARCHAR(20) NOT NULL,
    CONSTRAINT pkWord PRIMARY KEY (wId)
);

INSERT INTO Word (wId, wTxt) VALUES(1, 'Canard');
INSERT INTO Word (wId, wTxt) VALUES(2, 'Vache');
INSERT INTO Word (wId, wTxt) VALUES(3, 'Chat');
INSERT INTO Word (wId, wTxt) VALUES(4, 'Pompiers');
INSERT INTO Word (wId, wTxt) VALUES(5, 'Automobiliste');
INSERT INTO Word (wId, wTxt) VALUES(6, 'Maison');
INSERT INTO Word (wId, wTxt) VALUES(7, 'Voiture');
INSERT INTO Word (wId, wTxt) VALUES(8, 'Camion');
INSERT INTO Word (wId, wTxt) VALUES(9, 'Ile ');
INSERT INTO Word (wId, wTxt) VALUES(10, 'Plage');
INSERT INTO Word (wId, wTxt) VALUES(11, 'Coquillage');
INSERT INTO Word (wId, wTxt) VALUES(12, 'Spaghetti');
INSERT INTO Word (wId, wTxt) VALUES(13, 'Fleur');
INSERT INTO Word (wId, wTxt) VALUES(14, 'Enveloppe');
INSERT INTO Word (wId, wTxt) VALUES(15, 'Hexagone');
INSERT INTO Word (wId, wTxt) VALUES(16, 'Pyramide');
INSERT INTO Word (wId, wTxt) VALUES(17, 'Escarpin');
INSERT INTO Word (wId, wTxt) VALUES(18, 'Escalator');
INSERT INTO Word (wId, wTxt) VALUES(19, 'Escargot');
INSERT INTO Word (wId, wTxt) VALUES(20, 'Lierre');
INSERT INTO Word (wId, wTxt) VALUES(21, 'Vase');
INSERT INTO Word (wId, wTxt) VALUES(22, 'Antenne');
INSERT INTO Word (wId, wTxt) VALUES(23, 'Prise de courant');
INSERT INTO Word (wId, wTxt) VALUES(24, 'Surfeur');
INSERT INTO Word (wId, wTxt) VALUES(25, 'Bouton');
INSERT INTO Word (wId, wTxt) VALUES(26, 'Fauteuil');
INSERT INTO Word (wId, wTxt) VALUES(27, 'Clavier');
INSERT INTO Word (wId, wTxt) VALUES(28, 'Autoradio');
INSERT INTO Word (wId, wTxt) VALUES(29, 'Tournevis');
INSERT INTO Word (wId, wTxt) VALUES(30, 'Punaise');
INSERT INTO Word (wId, wTxt) VALUES(31, 'Horloge');
INSERT INTO Word (wId, wTxt) VALUES(32, 'Tennis');
INSERT INTO Word (wId, wTxt) VALUES(33, 'Football');
INSERT INTO Word (wId, wTxt) VALUES(34, 'Golf');
INSERT INTO Word (wId, wTxt) VALUES(35, 'Avion');
INSERT INTO Word (wId, wTxt) VALUES(36, 'Barque');
INSERT INTO Word (wId, wTxt) VALUES(37, 'Rame');
INSERT INTO Word (wId, wTxt) VALUES(38, 'Etang');
INSERT INTO Word (wId, wTxt) VALUES(39, 'Lac');
INSERT INTO Word (wId, wTxt) VALUES(40, 'Parachute');
INSERT INTO Word (wId, wTxt) VALUES(41, 'Deltaplane');
INSERT INTO Word (wId, wTxt) VALUES(42, 'Elastique');
INSERT INTO Word (wId, wTxt) VALUES(43, 'Ours ');

CREATE TABLE Player(
   pId NUMERIC(10) NOT NULL,
   pName VARCHAR(20) NOT NULL,
   CONSTRAINT pkPlayer PRIMARY KEY (pId),
   CONSTRAINT uPlayerName UNIQUE (pName)
);

CREATE TABLE Game(
    gId NUMERIC(10) NOT NULL,
    gDrawer NUMERIC(10) NOT NULL,
    gPartner NUMERIC(10) NOT NULL,
    gStartTime TIMESTAMP NOT NULL,
    gEndTime TIMESTAMP,
    gStopPlayer NUMERIC(10),
    CONSTRAINT pkGame primary key (gId),
    CONSTRAINT fkDrawer FOREIGN KEY (gDrawer) REFERENCES Player(pId),
    CONSTRAINT fkPartner FOREIGN KEY (gPartner) REFERENCES Player(pId),
    CONSTRAINT fkStopPlayer FOREIGN KEY (gStopPlayer) REFERENCES Player(pId),
    CONSTRAINT ckTime CHECK (gEndTime IS NULL OR gEndTime > gStartTime)
);

create table Sequences (
    sId VARCHAR(30) NOT NULL,
    sValue NUMERIC(10) NOT NULL,
    CONSTRAINT pkSequence PRIMARY KEY (sId)
);

INSERT INTO SEQUENCES VALUES('Word', 43);
INSERT INTO SEQUENCES VALUES('Player', 0);
INSERT INTO SEQUENCES VALUES('Game', 0);

