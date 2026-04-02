package drinkshop.service;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;

import java.util.List;
import java.util.Map;

public class StocService {

    private final Repository<Integer, Stoc> stocRepo;

    public StocService(Repository<Integer, Stoc> stocRepo) {
        this.stocRepo = stocRepo;
    }

    public List<Stoc> getAll() {
        return stocRepo.findAll();
    }

    public void add(Stoc s) {
        stocRepo.save(s);
    }

    public void update(Stoc s) {
        stocRepo.update(s);
    }

    public void delete(int id) {
        stocRepo.delete(id);
    }

    // verifică stoc pentru un "coș" agregat: ingredient -> cantitate necesară
    public boolean areSuficiente(Map<String, Double> necesarPerIngredient) {
        for (Map.Entry<String, Double> entry : necesarPerIngredient.entrySet()) {
            String ingredient = entry.getKey();
            double necesar = entry.getValue();

            double disponibil = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().equalsIgnoreCase(ingredient))
                    .mapToDouble(Stoc::getCantitate)
                    .sum();

            if (disponibil < necesar) return false;
        }
        return true;
    }

    public void consuma(Map<String, Double> necesarPerIngredient) {
        if (!areSuficiente(necesarPerIngredient)) {
            throw new IllegalStateException("Stoc insuficient pentru comanda.");
        }

        for (Map.Entry<String, Double> entry : necesarPerIngredient.entrySet()) {
            String ingredient = entry.getKey();
            double ramas = entry.getValue();

            List<Stoc> ingredienteStoc = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().equalsIgnoreCase(ingredient))
                    .toList();

            for (Stoc s : ingredienteStoc) {
                if (ramas <= 0) break;

                double deScazut = Math.min(s.getCantitate(), ramas);

                // IMPORTANT: fără (int) => nu mai pierzi cantitate prin trunchiere
                s.setCantitate(s.getCantitate() - deScazut);

                ramas -= deScazut;
                stocRepo.update(s);
            }
        }
    }

    // păstrăm API-ul vechi (pentru compatibilitate)
    public boolean areSuficient(Reteta reteta) {
        if (reteta == null) return false;

        Map<String, Double> necesar = reteta.getIngrediente().stream()
                .collect(java.util.stream.Collectors.toMap(
                        IngredientReteta::getDenumire,
                        IngredientReteta::getCantitate,
                        Double::sum
                ));

        return areSuficiente(necesar);
    }

    public void consuma(Reteta reteta) {
        if (reteta == null) throw new IllegalStateException("Reteta lipsa.");

        Map<String, Double> necesar = reteta.getIngrediente().stream()
                .collect(java.util.stream.Collectors.toMap(
                        IngredientReteta::getDenumire,
                        IngredientReteta::getCantitate,
                        Double::sum
                ));

        consuma(necesar);
    }
}