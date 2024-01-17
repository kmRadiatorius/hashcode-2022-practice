package com.company.solver.pizza;

import com.company.solver.base.SolutionSnapshot;

import java.util.Set;

public class BestPizza implements SolutionSnapshot<Set<String>> {

    private Set<String> solution;
    private double score;

    public BestPizza(Set<String> solution, double score) {
        this.solution = solution;
        this.score = score;
    }

    @Override
    public Set<String> getSolution() {
        return solution;
    }

    @Override
    public double getScore() {
        return score;
    }
}
