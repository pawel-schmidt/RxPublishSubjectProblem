package com.simplemented.rxpublishsubject;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.functions.Func1;

public class RxUtils {

    @NonNull
    public static <T> Func1<? super Object, T> returnJust(@Nullable final T value) {
        return ignored -> value;
    }

    @NonNull
    public static Func1<? super Object, Boolean> isNotNull() {
        return o -> o != null;
    }
}
