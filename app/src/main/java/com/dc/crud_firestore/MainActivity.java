package com.dc.crud_firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText edt_judul;
    private EditText edt_deskripsi;
    private Button btn_save;
    private Button btn_show;
    private FirebaseFirestore db;

    //buat String item update yg dri adapter nya untuk update
    private String uId,uJudul,uDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_judul = findViewById(R.id.editTitle);
        edt_deskripsi = findViewById(R.id.editDesc);
        btn_save = findViewById(R.id.btnSave);
        btn_show = findViewById(R.id.btnShow);

        db = FirebaseFirestore.getInstance();


        //buat get instance dgn bundle
        Bundle bundle = getIntent().getExtras();
        //kondisi
        if(bundle!=null){
            btn_save.setText("Update");
            uId = bundle.getString("uId");
            uJudul = bundle.getString("uJudul");
            uDesc = bundle.getString("uDesc");

            //mengubah edt yg ada di main activty dgn dri db nya by intetn
            edt_judul.setText(uJudul);
            edt_deskripsi.setText(uDesc);

        }else {
            btn_save.setText("Save");
        }




        //insert data
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get value inputan
                String judul = edt_judul.getText().toString();
                String desc = edt_deskripsi.getText().toString();

                //save data biasa gk perlu bundle
//                //random id
//                String id = UUID.randomUUID().toString();
//                //metod dengan parse data id,judul,desc
//                saveToFireStore(id,judul,desc);

                //kalau untuk case dri btn save ke btn update
                //pake cara bundle
                Bundle bundle1 = getIntent().getExtras();
                //kondisi jika user mau update
                if(bundle1 != null){
                    String id = uId;
                    updateToFireStore(id,judul,desc);

                }else{
                    //random id
                    String id = UUID.randomUUID().toString();
                    //metod dengan parse data id,judul,desc
                    saveToFireStore(id,judul,desc);

                }


            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ShowActivity.class));
            }
        });


    }

    private void updateToFireStore(String id, String judul, String desc) {

        db.collection("Buku").document(id).update("judul",judul,"deskripsi",desc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Sukses Update", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveToFireStore(String id, String judul, String desc) {
        if(!judul.isEmpty() && !desc.isEmpty()){
            //buat HashMap dengan parse String dan Object
            HashMap<String,Object> map = new HashMap<>();
            //"nama kolom",parse value nya yg ada di method
            map.put("id",id);
            map.put("judul",judul);
            map.put("deskripsi",desc);

            //create documemt dengan nama table
            //collection ->nama doc collection/db nya ->collection("User)
            //push berdasarkan id nya  ->document(id)
            //set insert sesuai dengan map nya -> set(map)
            db.collection("Buku").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Sukses ", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Gagal ", Toast.LENGTH_SHORT).show();
                }
            });



        }else{
            Toast.makeText(this, "Gak boleh kosong ", Toast.LENGTH_SHORT).show();
        }
    }
}
