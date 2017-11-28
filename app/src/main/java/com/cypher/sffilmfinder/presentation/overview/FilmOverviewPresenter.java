package com.cypher.sffilmfinder.presentation.overview;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cypher.sffilmfinder.data.films.Film;
import com.cypher.sffilmfinder.domain.GetFilmList;
import com.cypher.sffilmfinder.domain.SearchFilmList;
import com.cypher.sffilmfinder.presentation.BasePresenter;
import com.cypher.sffilmfinder.presentation.FilmModel;
import com.cypher.sffilmfinder.presentation.FilmModelMapper;
import com.cypher.sffilmfinder.presentation.RequestModel;
import com.cypher.sffilmfinder.presentation.RxUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

class FilmOverviewPresenter implements FilmOverviewContract.Presenter {

    private static final int MIN_QUERY_LENGTH = 2;
    private final GetFilmList getFilmList;
    private final FilmModelMapper filmModelMapper;
    private final SearchFilmList searchFilmList;

    @Nullable
    private FilmOverviewContract.View view;

    @Nullable
    private List<FilmModel> filmModels;

    @Nullable
    private Disposable searchDisposable;

    @Nullable
    private Disposable loadDisposable;

    private FilmSortType lastSortType;

    @Inject
    FilmOverviewPresenter(GetFilmList getFilmList, FilmModelMapper filmModelMapper, SearchFilmList searchFilmList) {
        this.getFilmList = getFilmList;
        this.filmModelMapper = filmModelMapper;
        this.searchFilmList = searchFilmList;
        lastSortType = FilmSortType.TITLE;
    }

    @Override
    public void bind(FilmOverviewContract.View view) {
        this.view = view;
    }

    @Override
    public void unbind() {
        view = null;
        disposeSearch();
        disposeLoad();
    }

    @Override
    public void onFilmClick(FilmModel filmModel) {
        getViewOrThrow().goToFilm(filmModel.getId());
    }

    @Override
    public void onSortClick(FilmSortType filmSortType) {
        lastSortType = filmSortType;
        if (filmModels != null) {
            Comparator<FilmModel> filmModelComparator = getFilmModelComparator(filmSortType);
            Collections.sort(filmModels, filmModelComparator);
            getViewOrThrow().showFilms(filmModels);
        }
    }

    @Override
    public void load() {
        load(getFilmList.getFilms());
    }

    private void load(Single<List<Film>> single) {
        disposeLoad();

        loadDisposable = single.toObservable()
                .compose(RxUtils.applyCallDefaults())
                .subscribe(requestModel -> {
                    FilmOverviewContract.View view = getViewOrThrow();
                    if (requestModel.status == RequestModel.Status.SUCCESS) {
                        onFilmsLoad(requestModel.value);
                        view.disableSwipeToRefresh();
                    } else if (requestModel.status == RequestModel.Status.FAILURE) {
                        if (BasePresenter.isIoException(requestModel.error)) {
                            view.showFilmsLoadingNetworkError();
                        } else {
                            view.showFilmsLoadingSystemError();
                        }
                        view.enableSwipeToRefresh();
                    }

                    view.showFilmsLoading(requestModel.status == RequestModel.Status.IN_FLIGHT);
                });
    }

    private void onFilmsLoad(List<Film> films) {
        filmModels = filmModelMapper.toFilmModels(films);
        onSortClick(lastSortType);
    }

    @Override
    public void onSearchQueryChange(String query) {
        if (searchDisposable != null) {
            searchDisposable.dispose();
        }
        if (query.length() < MIN_QUERY_LENGTH) {
            load();
        } else {
            searchDisposable = searchFilmList.searchFilms(query).toObservable()
                    .compose(RxUtils.applyCallDefaults())
                    .subscribe(requestModel -> {
                        if (requestModel.status == RequestModel.Status.SUCCESS) {
                            onFilmsLoad(requestModel.value);
                            getViewOrThrow().scrollToTop();
                        }
                        //TODO(scypher) handle search failures
                    });
        }
    }

    @Override
    public void onRefresh() {
        load(getFilmList.retryGetFilms());
    }

    private void disposeSearch() {
        if (searchDisposable != null) {
            searchDisposable.dispose();
        }
    }

    private void disposeLoad() {
        if (loadDisposable != null) {
            loadDisposable.dispose();
        }
    }

    private Comparator<FilmModel> getFilmModelComparator(FilmSortType filmSortType) {
        switch (filmSortType) {
            case YEAR:
                return new YearFilmComparator();
            case TITLE:
                return new TitleFilmComparator();
            default:
                throw new IllegalArgumentException("Unknown film sort type: " + filmSortType);
        }
    }

    @NonNull
    private FilmOverviewContract.View getViewOrThrow() {
        if (view == null) {
            throw new IllegalStateException("View not attached");
        } else {
            return view;
        }
    }
}
