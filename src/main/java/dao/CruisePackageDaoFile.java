package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.CruisePackage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CruisePackageDaoFile implements GenericDao<CruisePackage> {
    private static final String FILE_PATH = "cruise_packages.json";
    private final Gson gson;
    private List<CruisePackage> packages;

    public CruisePackageDaoFile() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        packages = loadFromFile();
    }

    private List<CruisePackage> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type t = new TypeToken<List<CruisePackage>>(){}.getType();
            List<CruisePackage> res = gson.fromJson(reader, t);
            return res != null ? res : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(packages, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(CruisePackage entity) {
        if (read(entity.getName()) != null)
            throw new IllegalArgumentException("CruisePackage already exists: " + entity.getName());
        packages.add(entity);
        saveToFile();
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
        for (int i = 0; i < packages.size(); i++) {
            if (packages.get(i).getName().equalsIgnoreCase(entity.getName())) {
                packages.set(i, entity);
                saveToFile();
                return;
            }
        }
        throw new IllegalArgumentException("CruisePackage not found: " + entity.getName());
    }

    @Override
    public void delete(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for deleting CruisePackage.");
        String name = (String) keys[0];
        packages.removeIf(p -> p.getName().equalsIgnoreCase(name));
        saveToFile();
    }

    @Override
    public List<CruisePackage> readAll() {
        return new ArrayList<>(packages);
    }
}
