package drinkshop.repository.file;

import drinkshop.domain.Stoc;

public class FileStocRepository extends FileAbstractRepository<Integer, Stoc> {

    public FileStocRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Stoc entity) {
        return entity.getId();
    }

    @Override
    protected Stoc extractEntity(String line) {
        String[] elems = line.split(";");

        int id = Integer.parseInt(elems[0].trim());
        String ingredient = elems[1].trim();

        double cantitate = Double.parseDouble(elems[2].trim());
        double stocMinim = Double.parseDouble(elems[3].trim());

        // Constructorul Stoc primește int, deci păstrăm “unități întregi” aici
        return new Stoc(id, ingredient, (int) cantitate, (int) stocMinim);
    }

    @Override
    protected String createEntityAsString(Stoc entity) {
        return entity.getId() + ";" +
                entity.getIngredient() + ";" +
                format(entity.getCantitate()) + ";" +
                format(entity.getStocMinim());
    }

    private static String format(double value) {
        // scrie 1000 în loc de 1000.0
        if (value == Math.rint(value)) {
            return Integer.toString((int) value);
        }
        return Double.toString(value);
    }
}