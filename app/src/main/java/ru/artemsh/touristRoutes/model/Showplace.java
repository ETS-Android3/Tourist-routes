package ru.artemsh.touristRoutes.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Entity(tableName = "showplace")
public class Showplace {
//    @PrimaryKey(autoGenerate = true)
    private Integer id;

//    @ColumnInfo(name = "lat")
    private double lat;
//    @ColumnInfo(name = "lng")
    private double lng;

//    @ColumnInfo(name = "title")
    private String title = "";
//    @ColumnInfo(name = "description")
    private String description = "";

//    @ColumnInfo(name = "monday")
    private WorkTime monday = new WorkTime();
//    @ColumnInfo(name = "tuesday")
    private WorkTime tuesday = new WorkTime();
//    @ColumnInfo(name = "wednesday")
    private WorkTime wednesday = new WorkTime();
//    @ColumnInfo(name = "thursday")
    private WorkTime thursday = new WorkTime();
//    @ColumnInfo(name = "friday")
    private WorkTime friday = new WorkTime();
//    @ColumnInfo(name = "saturday")
    private WorkTime saturday = new WorkTime();
//    @ColumnInfo(name = "sunday")
    private WorkTime sunday = new WorkTime();

//    @ColumnInfo(name = "namePhoto")
    private List<String> namePhoto = new ArrayList<String>();
//    @ColumnInfo(name = "itemTasks")
    private List<ItemTask> itemTasks = new ArrayList<ItemTask>();
//    @ColumnInfo(name = "place")
    private TypePlace place = TypePlace.SHOWPLACE;
//    @ColumnInfo(name = "numberOrder")
    private Integer numberOrder;
//    @ColumnInfo(name = "raiting")
    private Integer raiting;
//    @ColumnInfo(name = "was")
    private Date was;

    public Showplace(){

    }

    public Showplace(LatLng latLng) {
        lat = latLng.latitude;
        lng = latLng.longitude;
    }

    public Showplace(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Integer getRaiting() {
        return raiting;
    }

    public void setRaiting(Integer raiting) {
        this.raiting = raiting;
    }

    public TypePlace getPlace() {
        return place;
    }

    public void setPlace(TypePlace place) {
        this.place = place;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LatLng getLatLng(){
        return new LatLng(lat, lng);
    }

    public List<ItemTask> getItemTasks() {
        return itemTasks;
    }

    public void setItemTasks(List<ItemTask> itemTasks) {
        this.itemTasks = itemTasks;
    }

    public List<String> getNamePhoto() {
        return namePhoto;
    }

    public void setNamePhoto(List<String> namePhoto) {
        this.namePhoto = namePhoto;
    }

    public Integer getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(Integer numberOrder) {
        this.numberOrder = numberOrder;
    }

    public Date getWas() {
        return was;
    }

    public void setWas(Date was) {
        this.was = was;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkTime getMonday() {
        return monday;
    }

    public void setMonday(WorkTime monday) {
        this.monday = monday;
    }

    public WorkTime getTuesday() {
        return tuesday;
    }

    public void setTuesday(WorkTime tuesday) {
        this.tuesday = tuesday;
    }

    public WorkTime getWednesday() {
        return wednesday;
    }

    public void setWednesday(WorkTime wednesday) {
        this.wednesday = wednesday;
    }

    public WorkTime getThursday() {
        return thursday;
    }

    public void setThursday(WorkTime thursday) {
        this.thursday = thursday;
    }

    public WorkTime getFriday() {
        return friday;
    }

    public void setFriday(WorkTime friday) {
        this.friday = friday;
    }

    public WorkTime getSaturday() {
        return saturday;
    }

    public void setSaturday(WorkTime saturday) {
        this.saturday = saturday;
    }

    public WorkTime getSunday() {
        return sunday;
    }

    public void setSunday(WorkTime sunday) {
        this.sunday = sunday;
    }

    public List<String> getNamePhto() {
        return namePhoto;
    }

    public void setNamePhto(List<String> namePhoto) {
        this.namePhoto = namePhoto;
    }

    public enum TypePlace{
        SHOWPLACE,PLACE
    }

    public static class WorkTime{
        private String startWork = "";
        private String finishWork = "";

        public WorkTime() {
        }

        public WorkTime(String startWork, String finishWork) {
            this.startWork = startWork;
            this.finishWork = finishWork;
        }

        public String getStartWork() {
            return startWork;
        }

        public void setStartWork(String startWork) {
            this.startWork = startWork;
        }

        public String getFinishWork() {
            return finishWork;
        }

        public void setFinishWork(String finishWork) {
            this.finishWork = finishWork;
        }

        public String getButtonStr(){
            if(startWork.equals("")&&finishWork.equals("")){
                return "";
            }else if (startWork.equals("")){
                return finishWork;
            } else if (finishWork.equals("")){
                return startWork;
            }
            return startWork +  "\n" +finishWork;
        }
    }
}
