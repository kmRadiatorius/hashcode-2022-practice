package com.company;

import com.company.solver.base.SimulatedAnnealing;
import com.company.solver.pizzav2.BestPizzaSolutionV2;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        var algorithm = new SimulatedAnnealing<Set<String>>(1000, 5000, 0.99);

	    for (char i = 'd'; i <= 'e'; i++) {
            var clients = read(i + ".in.txt");
            var data = new BestPizzaSolutionV2(clients);
            var solution = algorithm.solve(data);

            System.out.println("SCORE " + i + ": " + solution.getScore());
            write(i + ".out.txt", solution.getSolution());
        }
    }

    public static List<String> getCount(List<String> items, int limit) {
        return items.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<Client> read(String fileName) {
        List<Client> clients = new ArrayList<>();
        var file = new File(fileName);
        try (var scanner = new Scanner(file)) {
            scanner.nextLine();
            while (scanner.hasNext()) {
                var likes = Arrays.stream(scanner.nextLine().split(" ")).skip(1).collect(Collectors.toList());
                var dislikes = Arrays.stream(scanner.nextLine().split(" ")).skip(1).collect(Collectors.toList());
                clients.add(new Client(likes, dislikes));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public static void write(String fileName, Set<String> results) {
        try (var writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.format("%d ", results.size()));
            writer.write(String.join(" ", results));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
