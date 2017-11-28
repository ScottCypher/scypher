package com.cypher.sffilmfinder.presentation.detail;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cypher.sffilmfinder.R;
import com.cypher.sffilmfinder.presentation.FilmModel;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

class FilmDetailBottomSheetViewHolder {

    @BindViews({R.id.film_detail_description,
            R.id.film_detail_actor_title,
            R.id.film_detail_actor_1,
            R.id.film_detail_actor_2,
            R.id.film_detail_actor_3,
            R.id.film_detail_locations_recycler,
            R.id.film_detail_locations_title})
    List<View> nonPeekViews;

    @BindView(R.id.film_detail_title)
    TextView filmTitle;

    @BindView(R.id.film_detail_description)
    TextView filmDescription;

    @BindView(R.id.film_detail_actor_1)
    TextView filmActor1;

    @BindView(R.id.film_detail_actor_2)
    TextView filmActor2;

    @BindView(R.id.film_detail_actor_3)
    TextView filmActor3;

    @BindView(R.id.film_detail_locations_recycler)
    RecyclerView locationsRecycler;

    @BindView(R.id.film_detail_locations_title)
    TextView locationsTitle;

    private NestedScrollView bottomSheetView;
    private FilmLocationsAdapter filmLocationsAdapter;
    private BottomSheetBehavior bottomSheetBehavior;

    FilmDetailBottomSheetViewHolder(NestedScrollView bottomSheetView,
                                    FilmLocationsAdapter.LocationSelectionListener locationSelectionListener) {
        this.bottomSheetView = bottomSheetView;
        ButterKnife.bind(this, bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                FilmDetailBottomSheetViewHolder.this.onStateChanged(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        locationsRecycler.setNestedScrollingEnabled(false);
        filmLocationsAdapter = new FilmLocationsAdapter(locationSelectionListener);
    }

    private void onStateChanged(int newState) {
        int nonPeekVis = newState == BottomSheetBehavior.STATE_COLLAPSED
                ? View.INVISIBLE
                : View.VISIBLE;
        for (View view : nonPeekViews) {
            if (view.getVisibility() != View.GONE) {
                view.setVisibility(nonPeekVis);
            }
        }
    }

    void show(FilmModel filmModel, @Nullable String selectedLocation) {
        filmTitle.setText(filmModel.getTitle());

        setText(filmDescription, filmModel.getFunFacts());
        setText(filmActor1, filmModel.getActor1());
        setText(filmActor2, filmModel.getActor2());
        setText(filmActor3, filmModel.getActor3());

        List<String> locations = filmModel.getLocations();
        int locationsVis = locations.isEmpty() ? View.GONE : View.VISIBLE;
        locationsRecycler.setVisibility(locationsVis);
        locationsTitle.setVisibility(locationsVis);
        filmLocationsAdapter.setLocations(locations, selectedLocation);
        locationsRecycler.setAdapter(filmLocationsAdapter);

        onStateChanged(bottomSheetBehavior.getState());
    }

    private void setText(TextView textView, @Nullable String text) {
        textView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        textView.setText(text);
    }

    int getCurrentHeight() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED
                ? bottomSheetBehavior.getPeekHeight()
                : bottomSheetView.getHeight();
    }

    @OnClick(R.id.film_detail_title)
    public void onTitleClick() {
        int newState = bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED
                ? BottomSheetBehavior.STATE_EXPANDED
                : BottomSheetBehavior.STATE_COLLAPSED;
        bottomSheetBehavior.setState(newState);
        bottomSheetView.scrollTo(0, 0);
    }
}
