package ru.artemsh.touristRoutes.model;

public class ItemTask {
    private String tas;
    private ItemTask.StatusTask statusTask = StatusTask.WAITING;
    private int number;

    public enum  StatusTask {
        WAITING,SUCCESFULLY,TERRIBLY
    }

    public ItemTask() {
    }

    public ItemTask(String tas, StatusTask statusTask, int number) {
        this.tas = tas;
        this.statusTask = statusTask;
        this.number = number;
    }

    public String getTas() {
        return tas;
    }

    public void setTas(String tas) {
        this.tas = tas;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
