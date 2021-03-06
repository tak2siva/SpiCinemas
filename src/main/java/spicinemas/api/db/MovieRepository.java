package spicinemas.api.db;

import spicinemas.api.model.Movie;
import spicinemas.api.type.MovieListingType;
import spicinemas.db.gen.tables.records.MovieRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static spicinemas.db.gen.tables.Movie.MOVIE;

@Repository
@Transactional
public class MovieRepository {
    @Autowired
    private DSLContext dsl;

    public List<Movie> getNowShowingMovies() {
         return dsl.selectFrom(MOVIE).fetchMap(MOVIE.ID)
                 .values()
                 .stream().map(Movie::new).collect(toList());
    }

    public void addMovie(Movie movie) {
        dsl.insertInto(MOVIE)
                .columns(MOVIE.NAME, MOVIE.EXPERIENCES, MOVIE.LISTING_TYPE)
                .values(movie.getName(), movie.getExperiences(), movie.getListingType().toString())
                .execute();
    }

    public Movie getMovie(String name) {
        MovieRecord movieRecord = dsl.select()
                .from(MOVIE)
                .where(MOVIE.NAME.eq(name))
                .fetchOne()
                .into(MovieRecord.class);
        return new Movie(movieRecord);
    }
}
