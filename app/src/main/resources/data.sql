INSERT INTO  Felhasznalok(id, full_name, role, login_name, password) VALUES
    (000, 'Vendég felhasználó', 'USER', 'vendeg@gmail.com', 'vendeg'),
    (102, 'Andrea László', 'ADMIN', 'alaszlo@gmail.com', 'la-secret');
INSERT INTO  Movie(id, name, age, description, genre) VALUES
    (1,'A gép', '18', 'Gépről szóló múvi', 'ACTION');
INSERT INTO  Time(id, time_date, movie_id) VALUES
    (1, '2022-08-17', 1);
INSERT INTO Sights(id, name, description, address, category) VALUES
    (1, 'Trófea étterem', 'nagyon jó hely 10/10', 'Nyíregyháza kis utca nyóc', 'RESTAURANT');
INSERT INTO Review(id, rating, comment, user_id, sight_id) VALUES
    (1, '5', 'király', 000, '1');
INSERT INTO Seat(id, free, time_date_id) VALUES
    (1, true, '1');
INSERT INTO Reserved(user_id, movie_id) VALUES
    (102,1);