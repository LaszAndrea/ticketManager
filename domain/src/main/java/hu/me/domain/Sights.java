package hu.me.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="_6_Sights")
public class Sights {

    @Id
    //@GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable = false, length = 60)
    private String name;
    @Column(nullable = false, length = 1000)
    private String description;
    @Column(nullable = false, length = 100)
    private String address;
    @Enumerated(EnumType.STRING)
    private Category category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sight")
    private List<Review> reviews;

/*@Column(name = "first_pic")
    private String firstPic;

    @Column(name = "second_pic")
    private String secondPic;

    @Column(name = "third_pic")
    private String thirdPic;

    @Column(name = "fourth_pic")
    private String fourthPic;

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    public String getSecondPic() {
        return secondPic;
    }

    public void setSecondPic(String secondPic) {
        this.secondPic = secondPic;
    }

    public String getThirdPic() {
        return thirdPic;
    }

    public void setThirdPic(String thirdPic) {
        this.thirdPic = thirdPic;
    }

    public String getFourthPic() {
        return fourthPic;
    }

    public void setFourthPic(String fourthPic) {
        this.fourthPic = fourthPic;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
