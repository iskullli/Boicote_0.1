package com.example.perei.boicote.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perei.boicote.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity {
    private TextView cadastre;
    private EditText editLoginUsuario;
    private EditText editLoginSenha;
    private Button botaoLogar;

    private FirebaseAuth firebaseAuth;

    public void goToMain(){

        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editLoginUsuario = (EditText) findViewById(R.id.edit_login_usuario);
        editLoginSenha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.button_logar);
        cadastre = (TextView) findViewById(R.id.cadastree);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();




        if (firebaseAuth.getCurrentUser() != null){
            Log.i("verificaUsurio", "Usuário está logado!");
            goToMain();
        }else{
            Log.i("verificaUsurio", "Usuário não está logado!");

        }



        firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();





        //Login
        botaoLogar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String login = editLoginUsuario.getText().toString().trim();
                String senha = editLoginSenha.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(login, senha)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.d("meuLog", "Falha na autenticação");
                                }else{
                                            Log.i("LoginUser", "Usuário logado com sucesso");
        Toast.makeText(Login.this, "Usuário logado com sucesso", Toast.LENGTH_SHORT).show();
        goToMain();
                                }
                            }
                        });

            }
        });





        cadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });



    }
}
