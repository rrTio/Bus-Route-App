package com.example.busrouteapp.database;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class ssoSetterGetter {

    private String clientID, tenantID;

    public ssoSetterGetter(String clientID, String tenantID){this.clientID = clientID; this.tenantID = tenantID;}

    @NotNull
    @Override public String toString(){return Arrays.asList(clientID, tenantID).toString();}

    public String getClientID(){return clientID;}
    public String getTenantID(){return tenantID;}

    public void setClientID(String clientID) {this.clientID = clientID;}
    public void setTenantID(String tenantID) {this.tenantID = tenantID;}
}
