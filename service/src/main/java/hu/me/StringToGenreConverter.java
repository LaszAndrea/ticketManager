package hu.me;

import hu.me.domain.Genre;
import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;

@Component
public class StringToGenreConverter implements Converter<String, Genre> {

    @Override
    public Genre convert(String source) {
        return Genre.valueOf(source.toUpperCase());
    }

}
