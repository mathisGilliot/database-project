--Peuplage de la BD manuel pour tester les requetes

INSERT INTO CATEGORIES VALUES (1,2,20,2000,30);
INSERT INTO Vehicules VALUES (5,2);
INSERT INTO Abonnes VALUES (123456,'Nougz','Nougz',TO_DATE('04-11-1995','DD-MM-YYYY'),'38000 Grenoble');
INSERT INTO Abonnes VALUES (654321,'Mickey','Mouse',TO_DATE('20-05-1960','DD-MM-YYYY'),'38000 Grenoble');
INSERT INTO STATIONS VALUES ('Imag','center');
INSERT INTO Locations VALUES (2,TO_DATE('16-11-2016','DD-MM-YYYY'),TO_DATE('16-11-2016','DD-MM-YYYY'),15,20,2,'Imag');
INSERT INTO TERMINEA VALUES (4,'Imag');
INSERT INTO Reserve VALUES (3,123456);
INSERT INTO ESTGARE VALUES (5,'Imag');
INSERT INTO Accueille VALUES('Gare',1,5);
--Temps d'utilisation moyen par vehicule par mois --

SELECT id_vehicule, AVG( heure_a + 24*(TO_DATE(date_a,'DD-MM-YYYY')-TO_DATE(date_d,'DD-MM-YYYY'))-heure_d)
FROM Locations
WHERE TO_CHAR(date_a,'MM')=11 AND TO_CHAR(date_d,'MM')=11 AND TO_CHAR(date_d, 'YYYY')=2016
GROUP BY id_vehicule;



--Temps d'utilisation moyen par catégorie de vehicule par mois --
SELECT id_cat, AVG( heure_a + 24*(TO_DATE(date_a,'DD-MM-YYYY')-TO_DATE(date_d,'DD-MM-YYYY'))-heure_d)
FROM Vehicules NATURAL JOIN Locations
WHERE TO_CHAR(date_a,'MM')=11 AND TO_CHAR(date_d,'MM')=11 AND TO_CHAR(date_d, 'YYYY')=2016
GROUP BY id_cat;


--Catégories de véhicules la plus utilisée par tranche de 10 ans --

  SELECT id_cat, SUM( heure_a + 24*(TO_DATE(date_a,'DD-MM-YYYY')-TO_DATE(date_d,'DD-MM-YYYY'))-heure_d) AS somme
  FROM Vehicules NATURAL JOIN Locations NATURAL JOIN RESERVE NATURAL JOIN ABONNES
  WHERE TO_CHAR(ABONNES.DATE_NAISSANCE,'YYYY')<=TO_CHAR(sysdate, 'YYYY')-20 AND TO_CHAR(ABONNES.DATE_NAISSANCE,'YYYY')>=TO_CHAR(sysdate, 'YYYY')-29
  GROUP BY id_cat;


  
--Taux d'occupation de la station sur la journée--
SELECT nom_station, nbpark/total*100 AS Taux_occupation
FROM 
(SELECT nom_station,SUM(nb_places_station) As total
FROM Accueille
GROUP BY nom_station)
NATURAL JOIN
(SELECT nom_station, COUNT(*) AS nbpark
FROM ESTGARE
GROUP BY nom_station)
WHERE nom_station='Imag';

SELECT * FROM  Locations NATURAL JOIN TERMINEA
WHERE (date_d=TO_DATE('16-11-2016','DD-MM-YYYY') AND (station_d='Imag'))OR(date_a=TO_DATE('16-11-2016','DD-MM-YYYY') AND TERMINEA.NOM_STATION='Imag');
 
--Facturation location --

--Recup info sur le vehicule
SELECT id_vehicule, id_cat, duree_max, caution, prix_cat, heure_a + 24*(TO_DATE(date_a,'DD-MM-YYYY')-TO_DATE(date_d,'DD-MM-YYYY'))-heure_d AS TempsLoc
FROM Categories NATURAL JOIN Vehicules NATURAL JOIN Locations
WHERE (Locations.id_location=1);

--Recup info sur abonné
SELECT num_carte, TO_CHAR(sysdate,'YYYY-MM-DD')-TO_CHAR(date_naissance, 'YYYY-MM-DD')
FROM Reserve NATURAL JOIN ABONNES 
WHERE id_location=1;

--Recup info sur le forfait, renvoi vide si c'est pas dans la bonne catégorie de forfait
SELECT * 
FROM Illimites 
WHERE ID_FORFAIT=(SELECT id_forfait 
FROM Souscrire 
WHERE num_carte=0 AND id_cat=1);

SELECT * 
FROM Limites 
WHERE ID_FORFAIT=(SELECT id_forfait
FROM Souscrire 
WHERE num_carte=0 AND id_cat=1);

