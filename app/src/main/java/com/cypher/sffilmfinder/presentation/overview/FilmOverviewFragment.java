package com.cypher.sffilmfinder.presentation.overview;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cypher.sffilmfinder.R;
import com.cypher.sffilmfinder.presentation.ButterKnifeFragment;
import com.cypher.sffilmfinder.presentation.FilmModel;
import com.cypher.sffilmfinder.presentation.SfFilmFinderApplication;
import com.cypher.sffilmfinder.presentation.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class FilmOverviewFragment extends ButterKnifeFragment implements
        FilmOverviewContract.View,
        FilmsAdapter.FilmClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_LAST_SORT_TYPE =
            "com.cypher.sffilmfinder.presentation.overview.EXTRA_LAST_SORT_TYPE";

    @BindView(R.id.overview_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.overview_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.floating_searchview)
    SearchView searchView;

    @BindView(R.id.floating_overview_bar)
    View floatingOverviewBar;

    @BindView(R.id.overview_error)
    TextView errorView;

    @BindView(R.id.overview_loading)
    ProgressBar loadingView;

    @BindView(R.id.overview_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Inject
    FilmOverviewContract.Presenter presenter;

    private FilmsAdapter filmsAdapter;

    public static Fragment newInstance() {
        return new FilmOverviewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SfFilmFinderApplication.getInstance().getAppComponent().inject(this);
        filmsAdapter = new FilmsAdapter(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_LAST_SORT_TYPE)) {
            lastSortType = (FilmSortType) savedInstanceState.getSerializable(EXTRA_LAST_SORT_TYPE);
            presenter.onSortClick(lastSortType);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.film_overview_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewUtils.setEnabled(false, floatingOverviewBar);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setAdapter(filmsAdapter);
        DividerItemDecoration horizontalDivider =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        horizontalDivider.setDrawable(getResources().getDrawable(R.drawable.film_overview_divider, null));
        recyclerView.addItemDecoration(horizontalDivider);

        presenter.bind(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        String lastQuery = searchView.getQuery().toString();
        if (TextUtils.isEmpty(lastQuery)) {
            presenter.load();
        } else {
            presenter.onSearchQueryChange(lastQuery);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Setting in onResume avoids search triggering on non-user input change (e.g. rotation)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.onSearchQueryChange(newText);
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (lastSortType != null) {
            outState.putSerializable(EXTRA_LAST_SORT_TYPE, lastSortType);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbind();
    }

    @Override
    public void showFilms(List<FilmModel> filmModels) {
        setToolbarCollapsible();
        ViewUtils.setEnabled(true, floatingOverviewBar);

        recyclerView.setVisibility(View.VISIBLE);

        filmsAdapter.setFilmModels(filmModels);
    }

    /* Only set once content has loaded, otherwise toolbar can permanently collapse in error state */
    private void setToolbarCollapsible() {
        AppBarLayout.LayoutParams layoutParams =
                ((AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams());
        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        collapsingToolbarLayout.setLayoutParams(layoutParams);
    }

    @Override
    public void showFilmsLoading(boolean isLoading) {
        if (isLoading) {
            errorView.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
        loadingView.setVisibility(isLoading && !swipeRefreshLayout.isRefreshing()
                ? View.VISIBLE
                : View.GONE);
    }

    @Override
    public void showFilmsLoadingNetworkError() {
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(R.string.film_overview_loading_error_network);
    }

    @Override
    public void showFilmsLoadingSystemError() {
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(R.string.film_overview_loading_error_system);
    }

    @Override
    public void goToFilm(String filmId) {
        getFilmOverviewListener().onGoToFilm(filmId);
    }

    @Override
    public void disableSwipeToRefresh() {
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void enableSwipeToRefresh() {
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void scrollToTop() {
        recyclerView.scrollToPosition(0);
    }

    private FilmSortType lastSortType;

    @OnClick(R.id.floating_sort)
    public void onSortClick(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.TOP | Gravity.END);
        popupMenu.inflate(R.menu.film_overview_sort);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort_by_title) {
                lastSortType = FilmSortType.TITLE;
            } else if (item.getItemId() == R.id.sort_by_year) {
                lastSortType = FilmSortType.YEAR;
            } else {
                throw new IllegalArgumentException("Unknown menu sort: " + item.getTitle());
            }
            presenter.onSortClick(lastSortType);
            return true;
        });
        popupMenu.show();
    }

    @Override
    public void onFilmClick(FilmModel filmModel) {
        presenter.onFilmClick(filmModel);
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
    }

    private FilmOverviewListener getFilmOverviewListener() {
        return (FilmOverviewListener) getActivity();
    }

    public interface FilmOverviewListener {
        void onGoToFilm(String filmId);
    }
}
