package ru.artemsh.touristRoutes.database;

import java.util.ArrayList;
import java.util.List;

import ru.artemsh.touristRoutes.model.Showplace;

public class LocalDatabase implements IDatabase {

    private static List<Showplace> showplaces = new ArrayList<Showplace>();
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
    public void delete(int id) {
        for(int i=0;i<showplaces.size();i++){
            if (showplaces.get(i).getId()==id){
                showplaces.remove(i);
            }
        }

        for(int i=0;i<places.size();i++){
            if (places.get(i).getId()==id){
                places.remove(i);
            }
        }
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
