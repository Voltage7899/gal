package com.company.gal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class rec_user extends AppCompatActivity {

    //Все тоже самое,что и для Imagerecycler за исключением отсутсвия кода для удаления

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    private DatabaseReference database;
    private FirebaseStorage firebaseStorage;

    private List<ImageUpload> imageUploadList;
    private Button add_new;




    private ProgressBar progressBarCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_user);

        recyclerView=findViewById(R.id.recyclerView_User);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBarCircle=findViewById(R.id.progressBarCircle_User);
        add_new=findViewById(R.id.Add_User);

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(rec_user.this,new_image.class);
                startActivity(intent);
            }
        });

        database= FirebaseDatabase.getInstance().getReference("upload");
        firebaseStorage=FirebaseStorage.getInstance();

        imageUploadList=new ArrayList<>();
        imageAdapter=new ImageAdapter(rec_user.this,imageUploadList);
        recyclerView.setAdapter(imageAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                imageUploadList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ImageUpload imageUpload=dataSnapshot.getValue(ImageUpload.class);
                    imageUpload.setKey(dataSnapshot.getKey());
                    imageUploadList.add(imageUpload);
                }
                imageAdapter.notifyDataSetChanged();




                progressBarCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(rec_user.this, "Упс", Toast.LENGTH_SHORT).show();
                progressBarCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
}