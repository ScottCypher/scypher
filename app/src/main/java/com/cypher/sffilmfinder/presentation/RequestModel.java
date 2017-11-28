package com.cypher.sffilmfinder.presentation;

import timber.log.Timber;

public final class RequestModel<T> {
    public final T value;
    public final Status status;
    public final Throwable error;

    private RequestModel(T value, Status status, Throwable error) {
        this.value = value;
        this.status = status;
        this.error = error;
    }

    public static <T> RequestModel<T> createInFlight() {
        return new RequestModel<>(null, Status.IN_FLIGHT, null);
    }

    public static <T> RequestModel<T> createSuccess(T value) {
        return new RequestModel<>(value, Status.SUCCESS, null);
    }

    public static <T> RequestModel<T> createFailure(Throwable throwable) {
        Timber.e(throwable);
        return new RequestModel<>(null, Status.FAILURE, throwable);
    }

    public enum Status {
        IN_FLIGHT,
        SUCCESS,
        FAILURE
    }
}
