package com.example.busrouteapp.database;

import java.util.*;
public class DBSetterGetter
{
    private String transID, transDate, transTime, route, client, startingLocation, endingLocation, travelSchedDate, travelSchedTime, bookingStatus, boardingStatus, seatReserved, reservationBalance;


    public DBSetterGetter(String transID, String transDate, String transTime, String route, String client, String startingLocation, String endingLocation, String travelSchedDate, String travelSchedTime, String bookingStatus, String boardingStatus, String seatReserved, String reservationBalance)
    {
        this.transID = transID;
        this.transDate = transDate;
        this.transTime = transTime;
        this.route = route;
        this.client = client;
        this.startingLocation = startingLocation;
        this.endingLocation = endingLocation;
        this.travelSchedDate = travelSchedDate;
        this.travelSchedTime = travelSchedTime;
        this.bookingStatus = bookingStatus;
        this.boardingStatus = boardingStatus;
        this.seatReserved = seatReserved;
        this.reservationBalance = reservationBalance;
    }

    @Override public String toString()
    {
        return Arrays.asList(transID, transDate, transTime, route, client, startingLocation, endingLocation, travelSchedDate, travelSchedTime, bookingStatus, boardingStatus, seatReserved, reservationBalance).toString();
    }

    //GETTER
    public String getTransID() {
        return transID;
    }
    public String getTransDate() {
        return transDate;
    }
    public String getTransTime() {
        return transTime;
    }
    public String getRoute() {
        return route;
    }
    public String getClient() {
        return client;
    }
    public String getStartingLocation() {
        return startingLocation;
    }
    public String getEndingLocation(){
        return endingLocation;
    }
    public String getTravelSchedDate(){
        return travelSchedDate;
    }
    public String getTravelSchedTime(){
        return travelSchedTime;
    }
    public String getBookingStatus(){
        return bookingStatus;
    }
    public String getBoardingStatus(){
        return boardingStatus;
    }
    public String getSeatReserved(){
        return seatReserved;
    }
    public String getReservationBalance(){
        return reservationBalance;
    }

    //SETTER
    public void setTransID(String transID) {
        this.transID = transID;
    }
    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }
    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
    public void setRoute(String route){
        this.route = route;
    }
    public void setClient(String client) {
        this.client = client;
    }
    public void setStartingLocation(String startingLocation) {
        this.startingLocation = startingLocation;
    }
    public void setEndingLocation(String endingLocation) {
        this.endingLocation = endingLocation;
    }
    public void setTravelSchedDate(String travelSchedDate){
        this.travelSchedDate = travelSchedDate;
    }
    public void setTravelSchedTime(String travelSchedTime){
        this.travelSchedTime = travelSchedTime;
    }
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
    public void setBoardingStatus(String boardingStatus) {
        this.boardingStatus = boardingStatus;
    }
    public void setSeatReserved(String seatReserved) {
        this.seatReserved = seatReserved;
    }
    public void setReservationBalance(String reservationBalance){
        this.reservationBalance = reservationBalance;
    }
}