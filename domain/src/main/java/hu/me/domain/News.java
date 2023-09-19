package hu.me.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="_8_News")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String pictureURL;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String hrefLink;

    public String getHrefLink() {
        return hrefLink;
    }

    public void setHrefLink(String hrefLink) {
        this.hrefLink = hrefLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return id.equals(news.id) && name.equals(news.name) && description.equals(news.description) && pictureURL.equals(news.pictureURL) && date.equals(news.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, pictureURL, date);
    }

    public News(String name, String description, String pictureURL, String date, String hrefLink) {
        this.name = name;
        this.description = description;
        this.pictureURL = pictureURL;
        this.date = date;
        this.hrefLink = hrefLink;
    }

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

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
