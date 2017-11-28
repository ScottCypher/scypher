package com.cypher.sffilmfinder.presentation;


import io.reactivex.ObservableTransformer;

public class RxUtils {
    public static <T> ObservableTransformer<T, RequestModel<T>> applyCallDefaults() {
        return upstream -> upstream.map(RequestModel::createSuccess)
                .startWith(RequestModel.createInFlight())
                .onErrorReturn(RequestModel::createFailure);
    }
}
