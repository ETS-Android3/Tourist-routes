package ru.artemsh.touristRoutes.database;

import java.util.ArrayList;
import java.util.List;

import ru.artemsh.touristRoutes.model.Showplace;

public class LocalDatabase implements IDatabase {

    private static List<Showplace> showplaces = new ArrayList<Showplace>(){{
       add(new Showplace("title", "descr"));
    }};
    private static List<Showplace> places = new ArrayList<>();

    @Override
    public List<Showplace> getShowplaceAll() {
        return showplaces;
    }

    @Override
    public List<Showplace> getPlaceAll() {
        return places;
    }

    @Override
    public List<Showplace> getAll() {
        List<Showplace> list = new ArrayList<Showplace>();
        list.addAll(showplaces);
        list.addAll(places);

        return list;
    }

    @Override
    public void addShowplace(Showplace showplace) {
        showplaces.add(showplace);
    }

    @Override
    public void addPlace(Showplace place) {
        places.add(place);
    }
}
