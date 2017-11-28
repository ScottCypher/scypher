package com.cypher.sffilmfinder.presentation.detail;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cypher.sffilmfinder.domain.GetCoordinates;
import com.cypher.sffilmfinder.domain.GetFilm;
import com.cypher.sffilmfinder.presentation.BasePresenter;
import com.cypher.sffilmfinder.presentation.FilmModel;
import com.cypher.sffilmfinder.presentation.FilmModelMapper;
import com.cypher.sffilmfinder.presentation.RequestModel;
import com.cypher.sffilmfinder.presentation.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

class FilmDetailPresenter implements FilmDetailContract.Presenter {

    private static final String NEAR_SF_QUERY_F = "geo:%s,%s?q=%s";
    private static final String GOOGLE_MAPS_PACKAGE_NAME = "com.google.android.apps.maps";

    private final GetFilm getFilm;
    private final FilmModelMapper filmModelMapper;
    private final GetCoordinates getCoordinates;
    private final CoordinatesModelMapper coordinatesModelMapper;

    @Nullable
    private Disposable loadDisposable;

    @Nullable
    private Disposable geoDisposable;

    @Nullable
    private FilmDetailContract.View view;

    @Inject
    FilmDetailPresenter(GetFilm getFilm, FilmModelMapper filmModelMapper,
                        GetCoordinates getCoordinates, CoordinatesModelMapper coordinatesModelMapper) {
        this.getFilm = getFilm;
        this.filmModelMapper = filmModelMapper;
        this.getCoordinates = getCoordinates;
        this.coordinatesModelMapper = coordinatesModelMapper;
    }

    @Override
    public void bind(FilmDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void unbind() {
        view = null;
        disposeLoad();
        disposeGeo();
    }

    @Override
    public void onBackButtonClick() {
        getViewOrThrow().goBack();
    }

    @Override
    public void onRouteClick(String selectedLocation) {
        String location = selectedLocation == null
                ? getCoordinates.getDefaultLocationName()
                : selectedLocation;
        String uri = String.format(NEAR_SF_QUERY_F,
                getCoordinates.getDefaultLocation().getLatitude(),
                getCoordinates.getDefaultLocation().getLongitude(),
                location);
        Uri gmmIntentUri = Uri.parse(uri);
        getViewOrThrow().startDirections(gmmIntentUri, GOOGLE_MAPS_PACKAGE_NAME);
    }

    @Override
    public void load(String filmId) {
        disposeLoad();

        showDefaultLocation();

        loadDisposable = getFilm.getFilm(filmId)
                .subscribe(film -> {
                    FilmDetailContract.View view = getViewOrThrow();
                    if (film == null) {
                        view.showLoadingSystemError();
                    } else {
                        FilmModel filmModel = filmModelMapper.toFilmModel(film);
                        String selectedLocation = null;
                        if (!filmModel.getLocations().isEmpty()) {
                            selectedLocation = filmModel.getLocations().get(0);
                        }
                        view.showFilmModel(filmModel, selectedLocation);
                        loadLocation(selectedLocation);
                    }
                });
    }

    private void showDefaultLocation() {
        LocationModel locationModel =
                coordinatesModelMapper.toLocationModel(getCoordinates.getDefaultLocation());
        getViewOrThrow().showLocation(locationModel);
    }

    @Override
    public void onDirectionsNotAvailable() {
        getViewOrThrow().showGoogleMapsUnavailable();
    }

    @Override
    public void onLocationClick(String selectedLocation) {
        loadLocation(selectedLocation);
    }

    private void loadLocation(String selectedLocation) {
        geoDisposable = getCoordinates.getCoordinates(selectedLocation).toObservable()
                .compose(RxUtils.applyCallDefaults())
                .subscribe(requestModel -> {
                    FilmDetailContract.View view = getViewOrThrow();
                    if (requestModel.status == RequestModel.Status.SUCCESS) {
                        LocationModel locationModel =
                                coordinatesModelMapper.toLocationModel(requestModel.value);
                        view.markLocation(locationModel);
                    } else if (requestModel.status == RequestModel.Status.FAILURE) {
                        showDefaultLocation();
                        if (BasePresenter.isIoException(requestModel.error)) {
                            view.showLocationLoadNetworkError();
                        } else {
                            view.showLocationLoadSystemError();
                        }
                    }
                    //TODO(scypher) handle loading to improve UX
                });
    }

    private void disposeLoad() {
        if (loadDisposable != null) {
            loadDisposable.dispose();
        }
    }

    private void disposeGeo() {
        if (geoDisposable != null) {
            geoDisposable.dispose();
        }
    }

    @NonNull
    private FilmDetailContract.View getViewOrThrow() {
        if (view == null) {
            throw new IllegalStateException("View not attached");
        } else {
            return view;
        }
    }
}
