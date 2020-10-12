package com.hfad.nationalparksguide.data.model;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler {

    static FirebaseDatabase db;

    public static DatabaseReference getDbReference(){

        if(db==null){
            db = FirebaseDatabase.getInstance();
            db.setPersistenceEnabled(true);
        }

        DatabaseReference databaseReference = db.getReference();
        databaseReference.keepSynced(true);
        return databaseReference;
    }

    public static DatabaseReference getDbReference(String path){

        if(db==null){
            db = FirebaseDatabase.getInstance();
            db.setPersistenceEnabled(true);
        }

        DatabaseReference databaseReference = db.getReference(path);
        databaseReference.keepSynced(true);
        return databaseReference;
    }
}
