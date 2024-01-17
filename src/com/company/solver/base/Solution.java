package com.company.solver.base;

public interface Solution<T> {

    void doChange();
    void revertChange();
    SolutionSnapshot<T> getSolutionSnapshot();

}
