package com.cypher.sffilmfinder.presentation;


import java.io.IOException;

public interface BasePresenter<T> {
    static boolean isIoException(Throwable throwable) {
        return throwable instanceof IOException;
    }

    void bind(T view);

    void unbind();
}
