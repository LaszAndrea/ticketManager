package hu.me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        SpringApplication.run(Main.class, args);

        /*Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:D:/sampledata", "sa", "");
        Statement stmt = conn.createStatement();

        String sql =
                "INSERT INTO \"PUBLIC\".\"_4_TIME\" VALUES\n" +
                        "(4, TIMESTAMP '2023-08-18 11:50:30.0', 3);\n" +
        "INSERT INTO \"PUBLIC\".\"_4_TIME\" VALUES\n" +
                "(5, TIMESTAMP '2023-08-20 14:40:30.0', 3);\n"; +
                "INSERT INTO \"PUBLIC\".\"_6_SIGHTS\" VALUES\n" +
                "(1, '4431 Nyíregyháza, Fürdőház tér 2.', 'SIGHT', 'Az Élményfürdő az év minden napján nyitva tart, a szabadtéri Parkfürdő pedig nyáron várja a látogatókat. A két fürdő különlegessége, hogy szomszédosak egymással, a nyári szezonban egy belépőjeggyel látogathatók, itt minden korosztály megtalálja a számára kellemes kikapcsolódási formát. Páratlan növénykultúrával rendelkező Parkfürdőben az úszás szerelmesei egy fantasztikus versenyuszoda habjait szelhetik.', 'Aquarius Élmény- és Parkfürdő - Nyíregyháza');\n"+
                "INSERT INTO \"PUBLIC\".\"_1_USERS\" VALUES\n" +
                "(102, 'alaszlo@gmail.com', 'la-secret', U&'Andrea L\\00e1szl\\00f3', 'ADMIN');\n" +
                "INSERT INTO \"PUBLIC\".\"_1_USERS\" VALUES\n" +
                "(1, 'kisspista@gmail.com', 'kp-secret', 'Kiss Pista', 'USER');\n" +
                "INSERT INTO \"PUBLIC\".\"_2_MOVIE\" VALUES\n" +
                "(1, 12, '1969-et írunk. Indiana Jones úgy dönt, ennyi volt. Miután több mint egy évtizeden át tanított a New York-i Hunter Főiskolán, a neves professzor és régész nyugdíjas éveire készül szerény kis lakásában, ahol egyedül tengeti napjait. ', 'ACTION', 'INDIANA JONES ÉS A SORS TÁRCSÁJA');\n" +
                "INSERT INTO \"PUBLIC\".\"_3_RESERVED\" VALUES\n" +
                "(102, 1);\n" +
                "INSERT INTO \"PUBLIC\".\"_5_SEAT\" VALUES\n" +
                "(1, TRUE, 1);\n" +
                "INSERT INTO \"PUBLIC\".\"_6_SIGHTS\" VALUES\n" +
                "(2, U&'Ny\\00edregyh\\00e1za kis utca ny\\00f3c', 'RESTAURANT', U&'nagyon j\\00f3 hely 10/10', U&'Tr\\00f3fea \\00e9tterem');\n" +
                "INSERT INTO \"PUBLIC\".\"_7_REVIEW\" VALUES\n" +
                "(1, U&'kir\\00e1ly', 5, 1, 1);\n";

        stmt.execute(sql);
        stmt.close();
        conn.close();*/

    }

}