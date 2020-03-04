//package ru.artemsh.touristRoutes.database;
//
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import java.util.List;
//
//import ru.artemsh.touristRoutes.model.Showplace;
//
//@Dao
//interface RoomDatabasePlace extends IDatabase {
//    @Query("SELECT * FROM showplace")
//    @Override
//    List<Showplace> getShowplaceAll();
//
//    @Query("SELECT * FROM showplace WHERE place=0")
//    @Override
//    List<Showplace> getPlaceAll();
//
//    @Query("SELECT * FROM showplace WHERE place=1")
//    @Override
//    List<Showplace> getAll();
//
//    @Override
//    @Insert
//    void addShowplace(Showplace showplace);
//
//    @Override
//    @Insert
//    void addPlace(Showplace place);
//
//    @Override
//    @Update
//    void update(Showplace showplace);
//
//    @Override
//    @Delete
//    void delete(Showplace showplace);
//}
