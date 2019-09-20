package ru.artemsh.touristRoutes.database;

import java.util.List;

import ru.artemsh.touristRoutes.model.Showplace;

public interface IDatabase {
    List<Showplace> getShowplaceAll();
    List<Showplace> getPlaceAll();
    List<Showplace> getAll();
    void delete(int id);
    void addShowplace(Showplace showplace);
    void addPlace(Showplace place);
}
