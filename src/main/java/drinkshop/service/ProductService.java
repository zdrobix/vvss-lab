package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final Repository<Integer, Product> productRepo;

    // Constrângeri pentru parametrii testați
    private static final double MIN_PRICE = 0.01;
    private static final double MAX_PRICE = 10000.00;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 50;

    public ProductService(Repository<Integer, Product> productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * Adaugă un produs nou cu validare strictă
     * @param p produsul de adăugat
     * @throws ValidationException dacă produsul e invalid
     */
    public void addProduct(Product p) {
        validateProductAddition(p);
        productRepo.save(p);
    }

    /**
     * Actualizează un produs existent cu validare strictă
     * @throws ValidationException dacă datele sunt invalide
     */
    public void updateProduct(int id, String name, double price, CategorieBautura categorie, TipBautura tip) {
        validateProductData(name, price);
        Product updated = new Product(id, name, price, categorie, tip);
        productRepo.update(updated);
    }

    /**
     * Validează produsul pentru adăugare/actualizare
     * @throws ValidationException cu detalii despre erori
     */
    private void validateProductAddition(Product product) {
        if (product == null) {
            throw new ValidationException("Produsul nu poate fi null!");
        }
        validateProductData(product.getNume(), product.getPret());
    }

    /**
     * Validează name și price conform constrângerilor
     * @throws ValidationException dacă name sau price sunt invalide
     */
    private void validateProductData(String name, double price) {
        String errors = "";

        // Validare price
        if (price <= 0 || price > MAX_PRICE) {
            errors += "Prețul trebuie să fie > 0 și <= " + MAX_PRICE + "! (Valoare: " + price + ")\n";
        }

        // Validare name
        if (name == null || name.isBlank()) {
            errors += "Numele nu poate fi gol sau null!\n";
        } else if (name.length() < MIN_NAME_LENGTH) {
            errors += "Numele trebuie să aibă minim " + MIN_NAME_LENGTH + " caractere! (Lungime: " + name.length() + ")\n";
        } else if (name.length() > MAX_NAME_LENGTH) {
            errors += "Numele trebuie să aibă maxim " + MAX_NAME_LENGTH + " caractere! (Lungime: " + name.length() + ")\n";
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void deleteProduct(int id) {
        productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }

    public List<Product> filterByCategorie(CategorieBautura categorie) {
        if (categorie == CategorieBautura.ALL) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getCategorie() == categorie)
                .collect(Collectors.toList());
    }

    public List<Product> filterByTip(TipBautura tip) {
        if (tip == TipBautura.ALL) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getTip() == tip)
                .collect(Collectors.toList());
    }
}