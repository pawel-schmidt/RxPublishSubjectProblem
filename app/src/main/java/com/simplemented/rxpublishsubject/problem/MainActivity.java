package com.simplemented.rxpublishsubject.problem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.simplemented.rxpublishsubject.R;
import com.simplemented.rxpublishsubject.shared.CalculationResult;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

public class MainActivity extends AppCompatActivity {

    private final SerialSubscription subscriptions = new SerialSubscription();

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.activity_main_edit_text);
        final TextView textView = (TextView) findViewById(R.id.activity_main_text_view);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);

        final MainActivityPresenter presenter =
                new MainActivityPresenter(Schedulers.computation(), AndroidSchedulers.mainThread());

        subscriptions.set(new CompositeSubscription(
                RxTextView.textChanges(editText)
                        .subscribe(presenter.textChangesObserver()),
                presenter.progressBarVisibilityObservable()
                        .subscribe(RxView.visibility(progressBar)),
                presenter.calculationResultObservable()
                        .subscribe(new Action1<CalculationResult>() {
                            @Override
                            public void call(final CalculationResult result) {
                                textView.setText(getString(R.string.activity_main_result_fmt,
                                        result.value(), result.square(), result.cube()));
                            }
                        })
        ));

        presenter.textChangesObserver().onNext("0");
    }

    @Override
    protected void onDestroy() {
        subscriptions.set(Subscriptions.empty());
        super.onDestroy();
    }
}
