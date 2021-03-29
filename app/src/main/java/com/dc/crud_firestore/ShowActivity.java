package com.dc.crud_firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.dc.crud_firestore.Model.Buku;
import com.dc.crud_firestore.MyAdapter.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore db;

    private MyAdapter myAdapter;
    private List<Buku> bukuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        recyclerView = findViewById(R.id.recyclerView);
        //set size dari recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        //initialize list dan adapternya
        bukuList = new ArrayList<>();
        myAdapter = new MyAdapter(this,bukuList);

        //set adapter ke recyclerview
        recyclerView.setAdapter(myAdapter);

        //set swipe kanan kiri dengan fungsi yg diinginkan
        ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelper(myAdapter));
        touchHelper.attachToRecyclerView(recyclerView);


        //panggil method nya
        showData();
    }

    public void showData() {

        //manggil data dari db nya menggunakan firestore
        db.collection("Buku").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //clear dulu list nya
                        bukuList.clear();
                        //loop
                        for(DocumentSnapshot documentSnapshot :task.getResult()){

                            Buku buku = new Buku(documentSnapshot.getString("id"),documentSnapshot.getString("judul"),documentSnapshot.getString("deskripsi"));
                            //add model to list
                            bukuList.add(buku);
                        }
                        //kasih tau adapter nya kalau ada yg berubah
                        myAdapter.notifyDataSetChanged();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowActivity.this, "Oppsss.... something wrong ", Toast.LENGTH_SHORT).show();
            }
        });



    }
}










