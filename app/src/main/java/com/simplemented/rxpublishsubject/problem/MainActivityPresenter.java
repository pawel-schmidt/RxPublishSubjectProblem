package com.simplemented.rxpublishsubject.problem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.simplemented.rxpublishsubject.shared.CalculationResult;
import com.simplemented.rxpublishsubject.shared.RxUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

public class MainActivityPresenter {

    @NonNull
    private final Scheduler computationScheduler;
    @NonNull
    private final Scheduler uiScheduler;

    @NonNull
    private final PublishSubject<CharSequence> textChangesSubject = PublishSubject.create();

    @NonNull
    private final Observable<Double> doublesObservable;

    public MainActivityPresenter(@NonNull final Scheduler computationScheduler,
                                 @NonNull final Scheduler uiScheduler) {
        this.computationScheduler = computationScheduler;
        this.uiScheduler = uiScheduler;

        doublesObservable = textChangesSubject
                .map(new Func1<CharSequence, Double>() {
                    @Override
                    public Double call(final CharSequence charSequence) {
                        return tryParse(charSequence);
                    }
                })
                .filter(RxUtils.isNotNull())
                .share();
    }

    @NonNull
    public Observer<CharSequence> textChangesObserver() {
        return textChangesSubject;
    }

    @NonNull
    public Observable<CalculationResult> calculationResultObservable() {
        return doublesObservable
                .debounce(1500L, TimeUnit.MILLISECONDS)
                .map(new Func1<Double, CalculationResult>() {
                    @Override
                    public CalculationResult call(final Double d) {
                        return new CalculationResult(d, d * d, d * d * d);
                    }
                })
                .subscribeOn(computationScheduler)
                .observeOn(uiScheduler);
    }

    @NonNull
    public Observable<Boolean> progressBarVisibilityObservable() {
        return Observable
                .merge(
                        doublesObservable.map(RxUtils.returnJust(true)),
                        calculationResultObservable().map(RxUtils.returnJust(false))
                )
                .observeOn(uiScheduler);
    }

    @Nullable
    private Double tryParse(final CharSequence charSequence) {
        try {
            return Double.parseDouble(charSequence.toString());
        } catch (final NumberFormatException e) {
            return null;
        }
    }
}
