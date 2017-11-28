package com.cypher.sffilmfinder.presentation.overview;


import com.cypher.sffilmfinder.presentation.BasePresenter;
import com.cypher.sffilmfinder.presentation.FilmModel;

import java.util.List;

interface FilmOverviewContract {
    interface View {
        void showFilms(List<FilmModel> filmModels);

        void showFilmsLoading(boolean isLoading);

        void showFilmsLoadingNetworkError();

        void showFilmsLoadingSystemError();

        void goToFilm(String id);

        void disableSwipeToRefresh();

        void enableSwipeToRefresh();

        void scrollToTop();
    }

    interface Presenter extends BasePresenter<View> {
        void onFilmClick(FilmModel filmModel);

        void onSortClick(FilmSortType filmSortType);

        void load();

        void onSearchQueryChange(String query);

        void onRefresh();
    }
}
