-- Script de remplissage de la BD


-- Remplissage Categories --

-- Catégorie 1 : Voitures, 2h de location max, 2000€ de caution, 5€/h
INSERT INTO Categories VALUES(1, 5, 2, 2000, 5);

-- Catégorie 2 : Vélos, 3h de location max, 250€ de caution, 1€/h de location
INSERT INTO Categories VALUES(2, 1, 3, 250, 1);

-- Catégorie 3 : Vélos électriques, 2h de location max, 350€ de caution, 2€/h
INSERT INTO Categories VALUES(3, 1, 2, 350, 2);

-- Catégorie 4 : Vélos à remorque, 2h de location max, 400€ de caution, 3€/h
INSERT INTO Categories VALUES(4, 1, 2, 400, 3);

-- Catégorie 5 : petits utilitaires, 2h de location max, 1000€ de caution, 5€/h
INSERT INTO Categories VALUES(5, 2, 2, 1000, 5);


-- Remplissage VEHICULES --

-- 10 Vehicules

INSERT INTO Vehicules VALUES (1, 1);
INSERT INTO Vehicules VALUES (2, 2);
INSERT INTO Vehicules VALUES (3, 1);
INSERT INTO Vehicules VALUES (4, 2);
INSERT INTO Vehicules VALUES (5, 2);
INSERT INTO Vehicules VALUES (6, 2);
INSERT INTO Vehicules VALUES (7, 3);
INSERT INTO Vehicules VALUES (8, 3);
INSERT INTO Vehicules VALUES (9, 4);
INSERT INTO Vehicules VALUES (10, 4);

-- Remplissage ABONNES --

-- 3 Abonnés

INSERT INTO Abonnes VALUES (1, 'Smith', 'John', TO_DATE('22-JAN-1994', 'DD-MON-YYYY'), '13100');
INSERT INTO Abonnes VALUES (2, 'Conte', 'Thomas', TO_DATE('17-JUN-1995', 'DD-MON-YYYY'), '21000');
INSERT INTO Abonnes VALUES (3, 'Rana', 'Mickey', TO_DATE('11-MAR-1995', 'DD-MON-YYYY'), '38000');


------------------
INSERT INTO Stations VALUES ('imag', 'rue de la passerelle');
INSERT INTO Stations VALUES ('gustave rivet', 'rue gambetta');
INSERT INTO Stations VALUES ('flandrin', 'rue de la liberation');

INSERT INTO Locations VALUES (1, TO_DATE('15-JAN-2016', 'DD-MON-YYYY'), TO_DATE('15-JAN-2016', 'DD-MON-YYYY'), 4, 6, 1, 'imag');
INSERT INTO Locations VALUES (2, TO_DATE('16-JAN-2016', 'DD-MON-YYYY'), TO_DATE('16-JAN-2016', 'DD-MON-YYYY'), 2, 4, 2, 'gustave rivet');
INSERT INTO Locations VALUES (3, TO_DATE('18-JAN-2016', 'DD-MON-YYYY'), TO_DATE('18-JAN-2016', 'DD-MON-YYYY'), 4, 6, 3, 'imag');
INSERT INTO Locations VALUES (4, TO_DATE('05-MAR-2016', 'DD-MON-YYYY'), TO_DATE('05-MAR-2016', 'DD-MON-YYYY'), 1, 2, 4, 'flandrin');

INSERT INTO Forfaits VALUES (1);
INSERT INTO Forfaits VALUES (2);
INSERT INTO Forfaits VALUES (3);
INSERT INTO Forfaits VALUES (4);

INSERT INTO Limites VALUES (1, 100, 3);
INSERT INTO Limites VALUES (2, 100, 3);

INSERT INTO Illimites VALUES (3, 2, TO_DATE('18-JAN-2016', 'DD-MON-YYYY'), 100, 0);
INSERT INTO Illimites VALUES (4, 2, TO_DATE('15-JAN-2016', 'DD-MON-YYYY'), 100, 0);

INSERT INTO Estgare VALUES (1, 'imag');
INSERT INTO Estgare VALUES (2, 'imag');
INSERT INTO Estgare VALUES (3, 'flandrin');
INSERT INTO Estgare VALUES (5, 'imag');
INSERT INTO Estgare VALUES (6, 'gustave rivet');
INSERT INTO Estgare VALUES (7, 'flandrin');
INSERT INTO Estgare VALUES (8, 'imag');
INSERT INTO Estgare VALUES (9, 'imag');
INSERT INTO Estgare VALUES (10, 'imag');




INSERT INTO Terminea VALUES (1, 'imag');
INSERT INTO Terminea VALUES (2, 'imag');
INSERT INTO Terminea VALUES (3, 'flandrin');


INSERT INTO Accueille VALUES ('imag', 1, 10);
INSERT INTO Accueille VALUES ('imag', 2, 10);
INSERT INTO Accueille VALUES ('imag', 3, 10);
INSERT INTO Accueille VALUES ('imag', 4, 10);
INSERT INTO Accueille VALUES ('imag', 5, 10);

INSERT INTO Accueille VALUES ('gustave rivet', 1, 10);
INSERT INTO Accueille VALUES ('gustave rivet', 2, 10);
INSERT INTO Accueille VALUES ('gustave rivet', 3, 10);
INSERT INTO Accueille VALUES ('gustave rivet', 4, 10);

INSERT INTO Accueille VALUES ('flandrin', 1, 10);
INSERT INTO Accueille VALUES ('flandrin', 2, 10);
INSERT INTO Accueille VALUES ('flandrin', 3, 10);
INSERT INTO Accueille VALUES ('flandrin', 4, 10);

INSERT INTO Reserve VALUES (1, 1);
INSERT INTO Reserve VALUES (2, 2);
INSERT INTO Reserve VALUES (3, 3);
INSERT INTO Reserve VALUES (4, 1);

INSERT INTO Souscrire VALUES (1, 1, 2);
INSERT INTO Souscrire VALUES (2, 2, 1);
INSERT INTO Souscrire VALUES (3, 3, 1);
