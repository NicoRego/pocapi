
INSERT INTO hospital (hosp_name, latitude, longitude, free_beds) VALUES
  ('Hôpital Saint Vincent de Paul', 50.620312, 3.077438, 2),
  ('Centre Hospitalier Universitaire de Lille', 50.610937, 3.034687, 0),
  ('Hôpital privé La Louvière', 50.646438,3.083563, 5);

INSERT INTO specialty (spec_name) VALUES
  ('Cardiologie'),
  ('Immunologie'),
  ('Neuropathologie diagnostique'),
  ('Néphrologie');
  

INSERT INTO hospital_specialty (hospital_id, specialty_id) VALUES
  (1, 1),
  (1, 2),
  (2, 1),
  (2, 2),
  (2, 3),
  (2, 4),
  (3, 2),
  (3, 3);
  