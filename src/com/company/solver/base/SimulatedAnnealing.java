package com.company.solver.base;

public class SimulatedAnnealing<T> implements Solver<T> {

    public int initialTemperature;
    public int maxIterations;
    public double coolingRate;

    public SimulatedAnnealing(int initialTemperature, int maxIterations, double coolingRate) {
        this.initialTemperature = initialTemperature;
        this.maxIterations = maxIterations;
        this.coolingRate = coolingRate;
    }

    @Override
    public SolutionSnapshot<T> solve(Solution<T> solution) {
        double t = initialTemperature;
        SolutionSnapshot<T> bestSolution = solution.getSolutionSnapshot();

        for (int i = 0; i < maxIterations; i++) {
            solution.doChange();
            SolutionSnapshot<T> currentSolution = solution.getSolutionSnapshot();

            if (currentSolution.getScore() > bestSolution.getScore()) {
                bestSolution = currentSolution;
            } else if (Math.exp(bestSolution.getScore() - currentSolution.getScore()) / t < Math.random()) {
                solution.revertChange();
            }

            t *= coolingRate;
        }

        return bestSolution;
    }
}
