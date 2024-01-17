package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Client {

    public final Set<String> likes;
    public final Set<String> dislikes;
    public final List<Client> dislikedClients = new ArrayList<>();
    public boolean isSelected = false;
    public boolean lastSelection = false;

    public Client(List<String> likes, List<String> dislikes) {
        this.likes = likes.stream().collect(Collectors.toSet());
        this.dislikes = dislikes.stream().collect(Collectors.toSet());
    }

    public boolean likesPizza(Set<String> ingredients) {
        return likes.stream().allMatch(ingredients::contains) && dislikes.stream().noneMatch(ingredients::contains);
    }

    public Set<String> getBadIngredients(Set<String> ingredients) {
        var badIngredients = likes.stream().filter(a -> !ingredients.contains(a)).collect(Collectors.toSet());
        badIngredients.addAll(dislikes.stream().filter(a -> ingredients.contains(a)).collect(Collectors.toSet()));
        return badIngredients;
    }

    public void matchOpposingClient(Client other) {
        if (likes.stream().anyMatch(a -> other.dislikes.contains(a))) {
            dislikedClients.add(other);
        }
    }

    public void select() {
        if (!isSelected) {
            lastSelection = false;
            isSelected = true;
            dislikedClients.forEach(a -> a.deselect());
        }
    }

    public void deselect() {
        lastSelection = isSelected;
        isSelected = false;
    }

    public void restorePreviousMove() {
        isSelected = lastSelection;
        dislikedClients.forEach(a -> a.isSelected = a.lastSelection);
    }

}
