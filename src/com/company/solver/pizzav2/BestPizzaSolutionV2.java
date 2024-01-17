package com.company.solver.pizzav2;

import com.company.Client;
import com.company.solver.base.Solution;
import com.company.solver.pizza.BestPizza;
import com.company.solver.pizza.BestPizzaSolution;

import java.util.*;
import java.util.stream.Collectors;

public class BestPizzaSolutionV2 implements Solution<Set<String>> {

    private static final Random rand = new Random();

    private final List<Client> clients;
    private Client lastChangedClient;

    public BestPizzaSolutionV2(List<Client> clients) {
        matchOpposingClients(clients);
        //randomizeSelection();
        this.clients = clients.stream().sorted((a, b) -> b.dislikedClients.size() - a.dislikedClients.size()).skip((int)(clients.size() * 0.2)).collect(Collectors.toList());
    }

    @Override
    public void doChange() {
        var selectedIngredients = getSelectedIngredients();
        List<Client> unsatisfiedClients = clients.stream()
                .filter(a -> !a.likesPizza(selectedIngredients))
                .collect(Collectors.toList());

        lastChangedClient = unsatisfiedClients.get(Math.abs(rand.nextInt()) % unsatisfiedClients.size());
        lastChangedClient.select();
    }

    public void revertChange() {
        if (lastChangedClient != null) {
            lastChangedClient.restorePreviousMove();
        }
    }

    @Override
    public BestPizza getSolutionSnapshot() {
        var selected = getSelectedIngredients();
        return new BestPizza(selected, getScore(selected));
    }

    public BestPizzaSolution copy() {
        return new BestPizzaSolution(clients);
    }

    private Set<String> getSelectedIngredients() {
        var selected = new HashSet<String>();
        for (Client client : clients) {
            if (client.isSelected) {
                selected.addAll(client.likes);
            }
        }
        return selected;
    }

    private double getScore(Set<String> solution) {
        return clients.stream()
                .filter(client -> client.likesPizza(solution))
                .count();
    }

    private void matchOpposingClients(List<Client> clients) {
        for (int i = 0; i < clients.size() - 1; i++) {
            for (int j = i + 1; j < clients.size(); j++) {
                clients.get(i).matchOpposingClient(clients.get(j));
            }
        }
    }

    private void randomizeSelection() {
        for (Client client : clients) {
            if (rand.nextBoolean()) {
                client.select();
            }
        }
    }
}
