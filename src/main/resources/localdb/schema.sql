CREATE SEQUENCE events_sequence START WITH 100;

CREATE TABLE IF NOT EXISTS RATING (
  id   INT PRIMARY KEY,
  name VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS EVENTS (
  id         INT PRIMARY KEY,
  name       VARCHAR(255),
  base_price DECIMAL(5, 2),
  rating_id  INT,
  FOREIGN KEY (rating_id) REFERENCES RATING (id)
);
CREATE TABLE IF NOT EXISTS AUDITORIUMS (
  id   INT PRIMARY KEY,
  name VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS SEATS (
  id             INT PRIMARY KEY,
  row            INT,
  place          INT,
  vip            BOOLEAN,
  auditoriumm_id INT,
  FOREIGN KEY (auditoriumm_id) REFERENCES AUDITORIUM (id)
);
CREATE TABLE IF NOT EXISTS USERS (
  id       INT PRIMARY KEY,
  name     VARCHAR(255),
  email    VARCHAR(255),
  birthday DATE
)
CREATE TABLE IF NOT EXISTS TICKETS (
  id       INT PRIMARY KEY,
  event_id INT,
  datetime TIMESTAMP,
  seat_id  INT,
  user_id  INT,
  booked   BOOLEAN,
  FOREIGN KEY (event_id) REFERENCES EVENTS (id),
  FOREIGN KEY (seat_id) REFERENCES SEATS (id),
  FOREIGN KEY (user_id) REFERENCES USERS (id)
)
