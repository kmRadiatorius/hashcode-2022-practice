package com.company.solver.base;

public interface SolutionSnapshot<T> {

    T getSolution();
    double getScore();
}
