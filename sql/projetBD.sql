-- Script de création du schéma relationnel

-- Entités

CREATE TABLE Categories (
            id_cat INT PRIMARY KEY,
            nb_places_veh INT, CHECK (nb_places_veh>0),
            duree_max INT CONSTRAINT DureePositive CHECK (duree_max>0),
            caution INT CONSTRAINT cautionPositive CHECK (caution>0),
            prix_cat INT CONSTRAINT prixCatPositif CHECK (prix_cat>0)
)

CREATE TABLE Vehicules (
            id_vehicule INT PRIMARY KEY,
            id_cat INT REFERENCES Categories
)

Create Table Stations (  
            nom_station VARCHAR(50) PRIMARY KEY,
            adresse VARCHAR(255)
)

          
CREATE TABLE Locations (
        id_location INT PRIMARY KEY,
        date_d DATE,
        date_a DATE,
        heure_d INT CONSTRAINT HeureExistenteD check ((heure_d >= 0) AND (heure_d < 24)),
        heure_a INT CONSTRAINT HeureExistenteA check ((heure_a >= 0) AND (heure_a < 24)),
        id_vehicule INT REFERENCES Vehicules,
        station_d VARCHAR(50) REFERENCES Stations
)

Create Table Abonnes (
            num_carte INT PRIMARY KEY,
            nom_abonne VARCHAR(50),
            prenom VARCHAR(50),
            date_naissance DATE,
            adresse VARCHAR(50)
)

CREATE TABLE Forfaits (
          id_forfait INT PRIMARY KEY
)


CREATE TABLE Illimites (
            id_forfait INT not NULL,
            duree INT,
            debut DATE,
            prix_illim INT,
            remise INT,
            CONSTRAINT uniq_id
              FOREIGN KEY (id_forfait)
              REFERENCES Forfaits(id_forfait)
)

CREATE TABLE Limites (
        id_forfait INT not NULL,
        prix_lim INT,
        Nb_loc_max INT,
        CONSTRAINT uniq_id2 
                FOREIGN KEY (id_forfait)
                REFERENCES Forfaits(id_forfait)
)

-- Associations binaires 0..1

CREATE TABLE ESTGARE (
  id_vehicule REFERENCES Vehicules,
  nom_station varchar(50) REFERENCES Stations,
  CONSTRAINT KestGare PRIMARY KEY(id_vehicule, nom_station)
)

CREATE TABLE TERMINEA (
  id_location INT REFERENCES Locations,
  nom_station VARCHAR(50) REFERENCES Stations,
  CONSTRAINT Ktermine PRIMARY KEY(id_location, nom_station)
)

-- Associations binaires 0..*

CREATE TABLE Reserve (
  id_location INT REFERENCES Locations,
  num_carte INT REFERENCES Abonnes,
  CONSTRAINT Kreservation PRIMARY KEY(id_location, num_carte)
)
  
-- Associations binaires 1..*

CREATE TABLE Accueille (
  nom_station VARCHAR(50) REFERENCES Stations,
  id_cat INT REFERENCES Categories,
  nb_places_station INT constraint NbPlacesStationPositif check (nb_places_station>0),
  CONSTRAINT Kaccueil PRIMARY KEY(nom_station, id_cat)
)

-- Associations ternaires

CREATE TABLE SOUSCRIRE (
  num_carte INT REFERENCES Abonnes,
  id_forfait INT REFERENCES Forfaits,
  id_cat INT REFERENCES Categories,
  CONSTRAINT Ksouscription PRIMARY KEY(num_carte, id_forfait, id_cat)
) 
commit;
