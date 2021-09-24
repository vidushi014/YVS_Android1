package com.example.voice_notes;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore gFireStore;

    public static void createUserdata(String email,MyCompleteListener completeListener){

        Map<String, Object> userData = new ArrayMap<>();
        userData.put("Email_ID",email);

        DocumentReference userDoc = gFireStore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = gFireStore.batch();

        batch.set(userDoc,userData);

        DocumentReference countDoc = gFireStore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc,"Count" , FieldValue.increment(1));

        batch.commit()
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {

                         completeListener.onSuccess();

                     }
                 })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });
    }

}
