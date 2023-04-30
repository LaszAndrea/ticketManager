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

        String sql ="INSERT INTO \"PUBLIC\".\"_6_SIGHTS\" VALUES\n" +
                "(5, 'Cimballom utca 5', 'SIGHT', 'Állatkert nyíregyen', 'Állatkert');\n";/* +
                "(102, 'alaszlo@gmail.com', 'la-secret', U&'Andrea L\\00e1szl\\00f3', 'ADMIN');\n" +
                "INSERT INTO \"PUBLIC\".\"_2_MOVIE\" VALUES\n" +
                "(1, 18, U&'G\\00e9pr\\0151l sz\\00f3l\\00f3 m\\00favi', 'ACTION', U&'A g\\00e9p');\n" +
                "INSERT INTO \"PUBLIC\".\"_3_RESERVED\" VALUES\n" +
                "(102, 1);\n" +
                "INSERT INTO \"PUBLIC\".\"_4_TIME\" VALUES\n" +
                "(1, DATE '2022-08-17', 1);\n" +
                "INSERT INTO \"PUBLIC\".\"_5_SEAT\" VALUES\n" +
                "(1, TRUE, 1);\n" +
                "INSERT INTO \"PUBLIC\".\"_6_SIGHTS\" VALUES\n" +
                "(1, U&'Ny\\00edregyh\\00e1za kis utca ny\\00f3c', 'RESTAURANT', U&'nagyon j\\00f3 hely 10/10', U&'Tr\\00f3fea \\00e9tterem');\n" +
                "INSERT INTO \"PUBLIC\".\"_7_REVIEW\" VALUES\n" +
                "(1, U&'kir\\00e1ly', 5, 1, 0);\n";

        stmt.execute(sql);
        stmt.close();
        conn.close();*/

    }

}