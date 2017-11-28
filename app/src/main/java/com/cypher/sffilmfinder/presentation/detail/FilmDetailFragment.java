package com.cypher.sffilmfinder.presentation.detail;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cypher.sffilmfinder.R;
import com.cypher.sffilmfinder.presentation.ButterKnifeFragment;
import com.cypher.sffilmfinder.presentation.FilmModel;
import com.cypher.sffilmfinder.presentation.SfFilmFinderApplication;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import junit.framework.Assert;

import javax.inject.Inject;

import butterknife.BindBool;
import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.OnClick;

public class FilmDetailFragment extends ButterKnifeFragment implements
        FilmDetailContract.View, FilmLocationsAdapter.LocationSelectionListener, OnMapReadyCallback {

    private static final String EXTRA_FILM_ID =
            "com.cypher.sffilmfinder.presentation.detail.EXTRA_FILM_ID";

    @BindBool(R.bool.film_bottom_sheet_start_expanded)
    boolean filmBottomSheetStartExpanded;
    @BindFloat(R.dimen.zoomed_in_google_map_zoom)
    float zoomedInGoogleMapZoom;
    @BindFloat(R.dimen.zoomed_out_google_map_zoom)
    float zoomedOutGoogleMapZoom;

    @BindView(R.id.film_detail_bottom_sheet)
    NestedScrollView bottomSheet;

    @Inject
    FilmDetailContract.Presenter presenter;

    private FilmDetailBottomSheetViewHolder filmDetailBottomSheetViewHolder;
    private String selectedLocation;
    private LocationModel lastLocation;
    private boolean markAndAnimateLastLocation;

    @Nullable
    private GoogleMap googleMap;

    public static FilmDetailFragment newInstance(String filmId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_FILM_ID, filmId);
        FilmDetailFragment fragment = new FilmDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SfFilmFinderApplication.getInstance().getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.film_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filmDetailBottomSheetViewHolder = new FilmDetailBottomSheetViewHolder(bottomSheet, this);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        if (filmBottomSheetStartExpanded && savedInstanceState == null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        presenter.bind(this);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Assert.assertTrue(getArguments().containsKey(EXTRA_FILM_ID));
        String filmId = getArguments().getString(EXTRA_FILM_ID);
        presenter.load(filmId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbind();
        googleMap = null;
    }

    @Override
    public void showFilmModel(FilmModel filmModel, @Nullable String selectedLocation) {
        this.selectedLocation = selectedLocation;
        filmDetailBottomSheetViewHolder.show(filmModel, selectedLocation);
    }

    @Override
    public void showLoadingSystemError() {
        //TODO(scypher) display invalid ID load
    }

    @Override
    public void showGoogleMapsUnavailable() {
        Snackbar.make(getViewOrThrow(), R.string.film_detail_google_maps_unavailable, Snackbar.LENGTH_SHORT)
                .show();
    }

    private View getViewOrThrow() {
        View view = getView();
        if (view == null) {
            throw new IllegalStateException("View does not exist");
        } else {
            return view;
        }
    }

    @Override
    public void startDirections(Uri uri, String packageName) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage(packageName);
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) == null) {
            presenter.onDirectionsNotAvailable();
        } else {
            startActivity(mapIntent);
        }
    }

    @Override
    public void goBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void markLocation(LocationModel locationModel) {
        lastLocation = locationModel;
        markAndAnimateLastLocation = true;
        if (googleMap != null) {
            animateToAndMarkLocation(googleMap, lastLocation);
        }
    }

    @Override
    public void showLocation(LocationModel locationModel) {
        lastLocation = locationModel;
        markAndAnimateLastLocation = false;
        if (googleMap != null) {
            showLocation(googleMap, locationModel);
        }
    }

    @Override
    public void showLocationLoadNetworkError() {
        displayLocationLoadError(R.string.film_detail_location_load_network_error);
    }

    @Override
    public void showLocationLoadSystemError() {
        displayLocationLoadError(R.string.film_detail_location_load_system_error);
    }

    private void displayLocationLoadError(@StringRes int strRes) {
        Snackbar.make(getViewOrThrow(), strRes, Snackbar.LENGTH_LONG)
                .setAction(R.string.film_detail_location_load_retry, v -> presenter.onLocationClick(selectedLocation))
                .show();
    }

    @OnClick(R.id.film_detail_fab)
    public void onFabClick() {
        presenter.onRouteClick(selectedLocation);
    }

    @OnClick(R.id.film_bottom_sheet_back)
    public void onBackClick() {
        presenter.onBackButtonClick();
    }

    @Override
    public void onLocationSelected(String location) {
        this.selectedLocation = location;
        presenter.onLocationClick(location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setPadding(0, 0, 0, filmDetailBottomSheetViewHolder.getCurrentHeight());
        if (lastLocation != null) {
            if (markAndAnimateLastLocation) {
                animateToAndMarkLocation(googleMap, lastLocation);
            } else {
                showLocation(googleMap, lastLocation);
            }
        }
    }

    private void animateToAndMarkLocation(GoogleMap googleMap, LocationModel locationModel) {
        LatLng location = new LatLng(locationModel.getLatitude(), locationModel.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(location));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, zoomedInGoogleMapZoom);
        googleMap.animateCamera(cameraUpdate);
    }

    private void showLocation(GoogleMap googleMap, LocationModel locationModel) {
        LatLng location = new LatLng(locationModel.getLatitude(), locationModel.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, zoomedOutGoogleMapZoom);
        googleMap.moveCamera(cameraUpdate);
    }
}
