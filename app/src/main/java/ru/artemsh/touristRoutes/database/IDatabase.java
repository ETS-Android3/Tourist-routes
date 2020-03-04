package ru.artemsh.touristRoutes.database;

import java.util.List;

import ru.artemsh.touristRoutes.model.Showplace;

public interface IDatabase {
    List<Showplace> getShowplaceAll();
    List<Showplace> getPlaceAll();
    List<Showplace> getAll();
    void update(Showplace showplace);
    void delete(Showplace showplace);
    void addShowplace(Showplace showplace);
    void addPlace(Showplace place);
}
