package hu.me.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 1)
    private int rating;
    @Column(nullable = true, length = 300)
    private String comment;
    @ManyToOne
    private User user;
    @ManyToOne
    private Sights sight;


}
