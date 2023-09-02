package hu.me.transformer;

import hu.me.domain.Movie;
import hu.me.domain.Sights;
import hu.me.model.MovieModel;
import hu.me.model.SightModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieTransformer {

    public List<MovieModel> transformMovieListToMovieModelList(List<Movie> movieList) {
        List<MovieModel> movieModelList = new ArrayList<>();
        for (Movie movie : movieList) {
            movieModelList.add(transformMovieToMovieModel(movie));
        }
        return movieModelList;
    }

    public MovieModel transformMovieToMovieModel(Movie movie) {
        MovieModel movieModel = new MovieModel();
        if(movie != null) {
            movieModel.setName(movie.getName());
            movieModel.setDescription(movie.getDescription());
            movieModel.setAge(movie.getAge());
            movieModel.setGenre(movie.getGenre());
            //movieModel.setUser(movie.getUser());
            movieModel.setTimes(movie.getTimes());
            if (movie.getId() != null)
                movieModel.setId(movie.getId());
        }
        return movieModel;
    }

    public Movie transformMovieModelToMovie(MovieModel movieModel) {
        Movie movie = new Movie();
        if(movieModel.getId()!=null)
            movie.setId(movieModel.getId());
        movie.setName(movieModel.getName());
        movie.setDescription(movieModel.getDescription());
        movie.setAge(movieModel.getAge());
        movie.setTimes(movieModel.getTimes());
        movie.setGenre(movieModel.getGenre());
        //movie.setUser(movieModel.getUser());
        return movie;
    }

}
