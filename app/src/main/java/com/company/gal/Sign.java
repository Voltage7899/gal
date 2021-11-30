package com.company.gal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class Sign extends AppCompatActivity {


    private Button loginButton;
    private EditText phone, pass;
    private TextView adminlink, clientlink;

    private String parentDataBaseName = "User";
    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        database= FirebaseDatabase.getInstance().getReference();

        init();
        sing();
    }

    private void sing() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_field=phone.getText().toString();
                String pass_field = pass.getText().toString();

                if(TextUtils.isEmpty(phone_field)&&TextUtils.isEmpty(pass_field)){
                    Toast.makeText(Sign.this, "Введите все данные", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(parentDataBaseName=="User"){

                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("User").child(phone_field).exists()){

                                    User userCurrentData=snapshot.child("User").child(phone_field).getValue(User.class);

                                    Log.d(TAG,"Переменная имени  "+userCurrentData.name);
                                    Log.d(TAG,"Переменная телефона "+userCurrentData.phone);
                                    Log.d(TAG,"Переменная пароля "+userCurrentData.pass);

                                    if(userCurrentData.phone.equals(phone_field) && userCurrentData.pass.equals(pass_field)){
                                        //если все окей,то переносит на страницу для юзеров со списком товаров
                                        Toast.makeText(Sign.this, "Вы вошли как Юзер", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(Sign.this,rec_user.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(Sign.this, "Wrong Data", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    if (parentDataBaseName=="Admin"){

                        if(TextUtils.isEmpty(phone_field)&&TextUtils.isEmpty(pass_field)){
                            Toast.makeText(Sign.this, "Введите все данные", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            database.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.child("Admin").child(phone_field).exists()){

                                        User userCurrentData=snapshot.child("Admin").child(phone_field).getValue(User.class);


                                        Log.d(TAG,"Переменная имени  "+userCurrentData.name);
                                        Log.d(TAG,"Переменная телефона "+userCurrentData.phone);
                                        Log.d(TAG,"Переменная пароля "+userCurrentData.pass);

                                        if(userCurrentData.phone.equals(phone_field) && userCurrentData.pass.equals(pass_field)){
                                            Toast.makeText(Sign.this, "Вы вошли как админ", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(Sign.this,rec_admin.class);
                                            startActivity(intent);

                                        }
                                        else {
                                            Toast.makeText(Sign.this, "Wrong Data", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                }
            }
        });
    }

    public void init() {
        //Привязка элементов
        loginButton = findViewById(R.id.button);
        phone = findViewById(R.id.Phone);
        pass = findViewById(R.id.Pass);

        adminlink = findViewById(R.id.adminlink);
        clientlink = findViewById(R.id.userlink);
//Установка слушателей
        clientlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminlink.setVisibility(View.VISIBLE);
                clientlink.setVisibility(View.INVISIBLE);
                loginButton.setText("Вход");
                parentDataBaseName = "User";
            }
        });
//Установка слушателя
        adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminlink.setVisibility(View.INVISIBLE);
                clientlink.setVisibility(View.VISIBLE);
                loginButton.setText("Вход для админа");
                parentDataBaseName = "Admin";
            }
        });
    }
}