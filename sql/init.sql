DROP TABLE IF EXISTS repairs;
DROP TABLE IF EXISTS cars;

CREATE TABLE cars (
    id          SERIAL PRIMARY KEY,
    plate       VARCHAR(15)  NOT NULL UNIQUE,
    brand       VARCHAR(50)  NOT NULL,
    model       VARCHAR(50)  NOT NULL,
    year        INTEGER      NOT NULL CHECK (year BETWEEN 1950 AND 2100),
    owner_name  VARCHAR(100) NOT NULL
);

CREATE TABLE repairs (
    id           SERIAL PRIMARY KEY,
    car_id       INTEGER       NOT NULL REFERENCES cars (id) ON DELETE CASCADE,
    description  VARCHAR(200)  NOT NULL,
    price        NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    repair_date  DATE          NOT NULL,
    status       VARCHAR(20)   NOT NULL DEFAULT 'Нове'
                 CHECK (status IN ('Нове', 'В роботі', 'Виконано'))
);

INSERT INTO cars (plate, brand, model, year, owner_name) VALUES
    ('AA1234BB', 'Toyota',     'Camry',   2018, 'Іваненко Петро'),
    ('BC5678CE', 'Skoda',      'Octavia', 2015, 'Коваль Олена'),
    ('KA9012AX', 'Volkswagen', 'Golf',    2012, 'Шевченко Андрій'),
    ('BE3456HH', 'Renault',    'Duster',  2020, 'Бондаренко Марія'),
    ('AI7890KK', 'Ford',       'Focus',   2010, 'Мельник Сергій');

INSERT INTO repairs (car_id, description, price, repair_date, status) VALUES
    (1, 'Заміна моторної оливи та фільтра', 1200.00, '2026-10-05', 'Виконано'),
    (1, 'Діагностика ходової',               400.00, '2026-10-20', 'Виконано'),
    (2, 'Заміна гальмівних колодок',         1800.00, '2026-10-28', 'Виконано'),
    (3, 'Розвал-сходження',                   700.00, '2026-11-03', 'В роботі'),
    (3, 'Заміна ременя ГРМ',                 4500.00, '2026-11-05', 'Нове'),
    (4, 'Технічне обслуговування (ТО-1)',    2200.00, '2026-11-08', 'Нове'),
    (5, 'Заміна свічок запалювання',          900.00, '2026-11-10', 'В роботі'),
    (5, 'Ремонт глушника',                   3100.00, '2026-11-11', 'Нове');
