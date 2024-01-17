package com.company.solver.pizza;

import com.company.Client;
import com.company.solver.base.Solution;

import java.util.*;
import java.util.stream.Collectors;

public class BestPizzaSolution implements Solution<Set<String>> {

    private static final Random rand = new Random();

    private List<String> ingredients;
    private List<Client> clients;
    private List<Boolean> selection;
    private List<Boolean> previousSelection;

    public BestPizzaSolution(List<Client> clients) {
        this.clients = clients;
        ingredients = extractIngredients(clients);
        selection = randomizeSelection(ingredients.size());
    }

    @Override
    public void doChange() {
        var selectedIngredients = getSelectedIngredients();
        List<Client> unsatisfiedClients = clients.stream()
                .filter(a -> a.likesPizza(selectedIngredients))
                .collect(Collectors.toList());

        var badIngredients = unsatisfiedClients.get(Math.abs(rand.nextInt()) % unsatisfiedClients.size()).getBadIngredients(selectedIngredients);
        int a = nextIndex();
        previousSelection = new ArrayList<>(selection);
        for (int i = 0; i < ingredients.size(); i++) {
            if (badIngredients.contains(ingredients.get(i))) {
                selection.set(i, !selection.get(i));
            }
        }

    }

    public void revertChange() {
        selection = previousSelection;
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
        for (int i = 0; i < ingredients.size(); i++) {
            if (selection.get(i)){
                selected.add(ingredients.get(i));
            }
        }
        return selected;
    }

    private double getScore(Set<String> solution) {
        return clients.stream()
                .filter(client -> client.likesPizza(solution))
                .count();
    }

    private int nextIndex() {
        return Math.abs(rand.nextInt()) % ingredients.size();
    }

    private List<String> extractIngredients(List<Client> clients) {
        var allIngredients = clients.stream()
                .flatMap(a -> a.likes.stream())
                .collect(Collectors.toList());

        allIngredients.addAll(clients.stream()
                .flatMap(a -> a.dislikes.stream())
                .collect(Collectors.toList()));

        return allIngredients;
    }

    private List<Boolean> randomizeSelection(int count) {
        var selection = new ArrayList<Boolean>();
        for (int i = 0; i < count; i++) {
            selection.add(rand.nextBoolean());
        }

        return selection;
    }
}
