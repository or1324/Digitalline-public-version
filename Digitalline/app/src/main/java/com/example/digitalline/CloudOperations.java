package com.example.digitalline;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CloudOperations {
    public static void getAllLabsAsync(Activity activity, LabsDownloadListener listener) {
        HashMap<String, LinkedList<LabDetails>> labs = new HashMap<>();
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("isManager", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for (DocumentSnapshot document : documents) {
                        List<HashMap<String, Object>> genericLabs = (List<HashMap<String, Object>>) document.get("labs");
                        LinkedList<LabDetails> currentUserLabs = new LinkedList<>();
                        for (HashMap<String, Object> map : genericLabs) {
                            HashMap<String, Double> pair = ((HashMap<String, Double>) map.get("coordinates"));
                            currentUserLabs.add(new LabDetails(map.get("labName").toString(), new Pair<>(pair.get("left"), pair.get("right"))));
                        }
                        labs.put(document.getId().replaceAll(" ", "."), currentUserLabs);
                    }
                    listener.onLabsDownloaded(labs);
                } else
                    UserMessages.showToastMessage("There was an error. Please try again", activity);
            }
        });
    }
}
