package hu.me;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class Statistics {

    private int numberOfUserShows;
    private int numberOfUserWrittenReviews;


    public Statistics(int numberOfUserShows, int numberOfUserWrittenReviews) {
        this.numberOfUserShows = numberOfUserShows;
        this.numberOfUserWrittenReviews = numberOfUserWrittenReviews;
    }

    public int getNumberOfUserShows() {
        return numberOfUserShows;
    }

    public void setNumberOfUserShows(int numberOfUserShows) {
        this.numberOfUserShows = numberOfUserShows;
    }

    public int getNumberOfUserWrittenReviews() {
        return numberOfUserWrittenReviews;
    }

    public void setNumberOfUserWrittenReviews(int numberOfUserWrittenReviews) {
        this.numberOfUserWrittenReviews = numberOfUserWrittenReviews;
    }

}
