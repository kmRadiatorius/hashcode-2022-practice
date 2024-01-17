package com.company.solver.base;

public interface Solver<T> {

    SolutionSnapshot<T> solve(Solution<T> solution);
}
