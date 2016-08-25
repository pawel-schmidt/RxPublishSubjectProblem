package com.simplemented.rxpublishsubject;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.functions.Func1;

public class RxUtils {

    @NonNull
    public static <T> Func1<? super Object, ? extends T> returnJust(@Nullable final T value) {
        return new Func1<Object, T>() {
            @Override
            public T call(final Object ignored) {
                return value;
            }
        };
    }

    @NonNull
    public static Func1<? super Object, Boolean> isNotNull() {
        return new Func1<Object, Boolean>() {
            @Override
            public Boolean call(final Object o) {
                return o != null;
            }
        };
    }
}
