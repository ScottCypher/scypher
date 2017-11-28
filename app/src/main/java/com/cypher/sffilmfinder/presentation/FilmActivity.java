package com.cypher.sffilmfinder.presentation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cypher.sffilmfinder.R;
import com.cypher.sffilmfinder.presentation.detail.FilmDetailFragment;
import com.cypher.sffilmfinder.presentation.overview.FilmOverviewFragment;

public class FilmActivity extends AppCompatActivity implements
        FilmOverviewFragment.FilmOverviewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_activity);

        if (savedInstanceState == null) {
            Fragment fragment = FilmOverviewFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
        }
    }

    @Override
    public void onGoToFilm(String filmId) {
        Fragment fragment = FilmDetailFragment.newInstance(filmId);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment, fragment)
                .commit();
    }
}
