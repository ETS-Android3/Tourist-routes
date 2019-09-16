package ru.artemsh.touristRoutes.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Showplace {

    private double lat;
    private double lng;

    private String title;
    private String description;

    private WorkTime monday;
    private WorkTime tuesday;
    private WorkTime wednesday;
    private WorkTime thursday;
    private WorkTime friday;
    private WorkTime saturday;
    private WorkTime sunday;

    private List<String> namePhoto = new ArrayList<String>();
    private List<ItemTask> itemTasks = new ArrayList<ItemTask>();
    private int numberOrder;
    private Date was;

    public Showplace() {
    }

    public Showplace(String title, String description) {
        this.title = title;
        this.description = description;
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

    public int getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(int numberOrder) {
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

    public static class WorkTime{
        private String startWork;
        private String finishWork;

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
            return startWork +  "\n" +finishWork;
        }
    }
}
