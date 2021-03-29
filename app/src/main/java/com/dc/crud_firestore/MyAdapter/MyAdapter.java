package com.dc.crud_firestore.MyAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dc.crud_firestore.MainActivity;
import com.dc.crud_firestore.Model.Buku;
import com.dc.crud_firestore.R;
import com.dc.crud_firestore.ShowActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

//extends RecyclerView.Adapter<>
//buat method dulu  public static class MyViewHodler extends RecyclerView.ViewHolder
//trs tambahin MyViewHolder di extends RecyclerView.Adapter<>
//alt + enter baris atas buat implement method pilih semua
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHodler> {
    private ShowActivity showActivity;
    //buat list
    private List<Buku> bList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    //constructor buat class ini
    public MyAdapter(ShowActivity showActivity, List<Buku> bList) {
        this.showActivity = showActivity;
        this.bList = bList;
    }

    //buat method update data
    //buiat show data yg ada di db ke main activity untuk di update
    public void updateData(int position){
        Buku item = bList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uId",item.getId());
        bundle.putString("uJudul",item.getJudul());
        bundle.putString("uDesc",item.getDesc());

        Intent intent = new Intent(showActivity, MainActivity.class);
        //parse data dri satu page ke page lain dgn cara pake intent
        //putExtras yg bundle
        intent.putExtras(bundle);
        showActivity.startActivity(intent);
    }

    public void deleteData(final int  position){
        Buku item = bList.get(position);
        db.collection("Buku").document(item.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    notifRemove(position);
                    Toast.makeText(showActivity, "Data Delete!!!!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(showActivity, "Error" +task.getException(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    //notify data udh di delete
    private void notifRemove(int position){
        bList.remove(position);
        notifyItemRemoved(position);
        showActivity.showData();
    }




    @NonNull
    @Override
    public MyViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout nya si item
        View v = LayoutInflater.from(showActivity).inflate(R.layout.item,parent,false);
        return new MyViewHodler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHodler holder, int position) {
        //set values dari item yg ada di inner method
        holder.judul.setText(bList.get(position).getJudul());
        holder.desc.setText(bList.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        //pass bukulist sesuai dengan size nya
        return bList.size();
    }

    //buat inner method ViewHolder nya
    public static class MyViewHodler extends RecyclerView.ViewHolder{
        //initialize item yg ada di layout nya si Item
        TextView judul,desc;



        public MyViewHodler(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.tv_judul);
            desc = itemView.findViewById(R.id.tv_desc);
        }
    }
}
