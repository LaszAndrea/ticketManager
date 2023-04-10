package hu.me.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.awt.image.BufferedImage;

@Entity
public class Sights {

    @Id
    private Long id;

    private String name;

    private String description;

    //private BufferedImage img;

}
