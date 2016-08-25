package com.simplemented.rxpublishsubject.shared;

public class CalculationResult {

    private final double value;
    private final double square;
    private final double cube;

    public CalculationResult(final double value, final double square, final double cube) {
        this.value = value;
        this.square = square;
        this.cube = cube;
    }

    public double value() {
        return value;
    }

    public double square() {
        return square;
    }

    public double cube() {
        return cube;
    }
}
