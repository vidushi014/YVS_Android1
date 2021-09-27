package com.example.voice_notes;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore gFireStore;
//    public static List<CategoryModel> gCatList = new ArrayList<>();

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


//    public static void loadCategory(MyCompleteListener completeListener){
//
//        gCatList.clear();
//        gFireStore.collection("CATEGORIES").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
//
//                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
//                        {
//                            docList.put(doc.getId(),doc);
//                        }
//
//                        QueryDocumentSnapshot CatListDoc = docList.get("categories");
//
//
//                        long catCount= CatListDoc.getLong("Count");
//
//                        for (int i=1 ; i<=catCount;i++){
//
//                            String catID = CatListDoc.getString("Cat" + String.valueOf(i) +"_ID");
//
////                            Log.i("hemloo",catID);
//
//                            QueryDocumentSnapshot catDoc = docList.get(catID);
//
////
////                            if(CatListDoc==null){
////                                Log.i("nahi hua","kya karein");
////                            }
////
//
//
//                            int noOfTest= catDoc.getLong("recordings").intValue();
//
//
//                            String catName = catDoc.getString("NAME");
//
//                            gCatList.add(new CategoryModel(catID,catName,noOfTest));
//                        }
//
//                        completeListener.onSuccess();
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        completeListener.onFailure();
//
//                    }
//                });
//
//
//    }

}
