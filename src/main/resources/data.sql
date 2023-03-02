DELETE FROM hospital;
INSERT INTO hospital (idhospital, hosp_name, latitude, longitude, free_beds) VALUES
  (1, 'Hopital Saint Vincent de Paul', 50.620312, 3.077438, 2),
  (2, 'Centre Hospitalier Universitaire de Lille', 50.610937, 3.034687, 0),
  (3, 'Hopital privé La Louvière', 50.646438,3.083563, 5);

DELETE FROM specialty;
INSERT INTO specialty (idspecialty,spec_name) VALUES
  (1, 'Cardiologie'),
  (2, 'Immunologie'),
  (3, 'Neuropathologie diagnostique'),
  (4, 'Néphrologie');

DELETE FROM hospital_specialty;
INSERT INTO hospital_specialty (hospitalid, specialtyid) VALUES
  (1, 1),
  (1, 2),
  (2, 1),
  (2, 2),
  (2, 3),
  (2, 4),
  (3, 2),
  (3, 3);
  