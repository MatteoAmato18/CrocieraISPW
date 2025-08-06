package dao;

import entity.CruisePackage;
import java.util.ArrayList;
import java.util.List;

public class CruisePackageDaoMemory implements GenericDao<CruisePackage> {
    private final List<CruisePackage> packages = new ArrayList<>();

    public CruisePackageDaoMemory() {
        packages.add(new CruisePackage("All Inclusive", 350.00));
        packages.add(new CruisePackage("Only Beverage", 120.00));
    }

    @Override
    public void create(CruisePackage entity) {
        if (read(entity.getName()) == null) packages.add(entity);
    }

    @Override
    public CruisePackage read(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for reading CruisePackage.");
        String name = (String) keys[0];
        return packages.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(CruisePackage entity) {
        CruisePackage existing = read(entity.getName());
        if (existing != null) {
            packages.remove(existing);
            packages.add(entity);
        }
    }

    @Override
    public void delete(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for deleting CruisePackage.");
        String name = (String) keys[0];
        packages.removeIf(p -> p.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<CruisePackage> readAll() {
        return new ArrayList<>(packages);
    }
}
