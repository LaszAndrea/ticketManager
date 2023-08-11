package hu.me.model;

import hu.me.domain.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MovieListModel {

    List<MovieModel> movieModel;

}
