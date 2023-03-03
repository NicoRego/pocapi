DELETE FROM hospital;
INSERT INTO hospital (id, name, latitude, longitude, free_beds) VALUES
  (1, 'Hopital Saint Vincent de Paul', 50.620312, 3.077438, 2),
  (2, 'Centre Hospitalier Universitaire de Lille', 50.610937, 3.034687, 0),
  (3, 'Hopital prive La Louviere', 50.646438, 3.083563, 5);

DELETE FROM specialty;
INSERT INTO specialty (id, name) VALUES
  (1, 'Cardiologie'),
  (2, 'Immunologie'),
  (3, 'Neuropathologie diagnostique'),
  (4, 'Nephrologie');

DELETE FROM hospital_specialty;
INSERT INTO hospital_specialty (idhospital, idspecialty) VALUES
  (1, 1),
  (1, 2),
  (2, 1),
  (2, 2),
  (2, 3),
  (2, 4),
  (3, 2),
  (3, 3);
  