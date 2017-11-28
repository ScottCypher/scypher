package com.cypher.sffilmfinder.presentation.detail;


import android.net.Uri;
import android.support.annotation.Nullable;

import com.cypher.sffilmfinder.presentation.BasePresenter;
import com.cypher.sffilmfinder.presentation.FilmModel;

interface FilmDetailContract {
    interface View {
        void showFilmModel(FilmModel filmModel, @Nullable String selectedLocation);

        void showLoadingSystemError();

        void showGoogleMapsUnavailable();

        void startDirections(Uri uri, String packageName);

        void goBack();

        void markLocation(LocationModel locationModel);

        void showLocation(LocationModel locationModel);

        void showLocationLoadNetworkError();

        void showLocationLoadSystemError();
    }

    interface Presenter extends BasePresenter<View> {
        void onBackButtonClick();

        void onRouteClick(@Nullable String selectedLocation);

        void load(String filmId);

        void onDirectionsNotAvailable();

        void onLocationClick(String selectedLocation);
    }
}
