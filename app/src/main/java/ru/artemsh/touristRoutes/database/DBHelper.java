package ru.artemsh.touristRoutes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import ru.artemsh.touristRoutes.model.Showplace;

public class DBHelper extends SQLiteOpenHelper implements IDatabase{

    private SQLiteDatabase db;
    private String NAMEDATABASE = "place_table";

    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "places", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с полями
        db.execSQL("create table "+NAMEDATABASE+" ("
                + "id integer primary key autoincrement,"
                + "place text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public List<Showplace> getShowplaceAll() {
        List<Showplace> all = getAll();
        List<Showplace> places = new ArrayList<>();
        for(int i=0;i<all.size();i++){
            if (all.get(i).getPlace() == Showplace.TypePlace.SHOWPLACE){
                places.add(all.get(i));
            }
        }
        return places;
    }

    @Override
    public List<Showplace> getPlaceAll() {
        List<Showplace> all = getAll();
        List<Showplace> places = new ArrayList<>();
        for(int i=0;i<all.size();i++){
            if (all.get(i).getPlace() == Showplace.TypePlace.PLACE){
                places.add(all.get(i));
            }
        }
        return places;
    }

    @Override
    public List<Showplace> getAll() {
        db = this.getWritableDatabase();
        Cursor c = db.query(NAMEDATABASE, null, null, null, null, null, null);
        List<Showplace> showplaces = new ArrayList<>();
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("place");
            Showplace showplace;
            do {
                showplace = gson.fromJson(c.getString(nameColIndex), Showplace.class);
                showplace.setId(c.getInt(idColIndex));
                showplaces.add(showplace);
            } while (c.moveToNext());
        }
        this.close();
        return showplaces;
    }

    @Override
    public void delete(int id) {
        db = this.getWritableDatabase();
        db.delete(NAMEDATABASE, "id = " + id, null);
        this.close();
    }

    @Override
    public void addShowplace(Showplace place) {
        add(place);
    }

    private void add(Showplace showplace){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("place", gson.toJson(showplace));
        if (showplace.getId() == null){
            db.insert(NAMEDATABASE, null, cv);
        }else{
            db.update(NAMEDATABASE, cv, "id = ?", new String[] { showplace.getId().toString() });
        }
        this.close();
    }

    @Override
    public void addPlace(Showplace place) {
        add(place);
    }
}
