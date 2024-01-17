package com.company.solver.pizza;

import com.company.solver.base.SimulatedAnnealing;
import com.company.solver.base.Solution;
import com.company.solver.base.SolutionSnapshot;
import com.company.solver.base.Solver;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SolutionUtil {

    private SolutionUtil(){}

    public static SolutionSnapshot<Set<String>> trySolutions(SimulatedAnnealing<Set<String>> solver, BestPizzaSolution solution, int times) {
        var baseSolution = solution.getSolutionSnapshot();
        var executor = Executors.newFixedThreadPool(times);
        var solutionFutures = new ArrayList<Future<SolutionSnapshot<Set<String>>>>();
        for (int i = 0; i < times; i++) {
            solutionFutures.add(executor.submit(() -> solver.solve(solution.copy())));
        }

        return solutionFutures.stream()
                .map(a -> {
                    try {
                        return a.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return baseSolution;
                })
                .reduce(baseSolution, (acc, r) -> acc = r.getScore() > acc.getScore() ? r : acc);
    }

}
