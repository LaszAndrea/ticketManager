package hu.me.transformer;

import hu.me.domain.Review;
import hu.me.model.ReviewModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class ReviewTransformer {

    public ReviewModel transformReviewToReviewModel(Review review) {
        ReviewModel reviewModel = new ReviewModel();
        if(review!=null) {
            reviewModel.setUser(review.getUser());
            reviewModel.setComment(review.getComment());
            reviewModel.setSights(review.getSight());
            reviewModel.setRating(review.getRating());
            if (review.getId() != null)
                reviewModel.setId(review.getId());
        }
        return reviewModel;
    }

    public Review transformReviewModelToReview(ReviewModel reviewModel) {
        Review review = new Review();
        review.setUser(reviewModel.getUser());
        review.setSight(reviewModel.getSights());
        review.setComment(reviewModel.getComment());
        review.setRating(reviewModel.getRating());
        if(reviewModel.getId()!=null)
            review.setId(reviewModel.getId());
        return review;
    }


}
