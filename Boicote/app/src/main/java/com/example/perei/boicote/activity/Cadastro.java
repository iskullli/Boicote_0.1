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
import com.example.perei.boicote.util.ParseErros;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Cadastro extends AppCompatActivity {

    private EditText textoUsuario;
    private EditText textoEmail;
    private EditText textoSenha;
    private TextView facaLogin;
    private Button botaoCadastrar;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        textoUsuario = (EditText) findViewById(R.id.textoUsuario);
        textoEmail = (EditText) findViewById(R.id.textoEmail);
        textoSenha = (EditText) findViewById(R.id.textoSenha);
        facaLogin = (TextView) findViewById(R.id.facaLogin);
        botaoCadastrar = (Button) findViewById(R.id.botaoCadastrar);




        firebaseAuth = FirebaseAuth.getInstance();

        facaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cadastro.this, Login.class);
            }
        });

        //Cadastro
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = textoEmail.getText().toString();
                String senha = textoSenha.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email,senha)
                        .addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String senha = textoSenha.getText().toString();
                                if(senha.length() <= 6){
                                if(task.isSuccessful()){ //Cadastrado com sucesso

                                    Log.i("CreateUser", "Cadastrado com sucesso");
                                    Toast.makeText(Cadastro.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                    backToLogin();
                                }else{
                                    Log.i("CreateUser", "Não Cadastrado");
                                    Toast.makeText(Cadastro.this, "Erro no Cadastro, sua senha precisa ter 6 digitos ou mais", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        });
            }
        });
    }

    public void backToLogin(){

        Intent intent = new Intent(this,Login.class);
        startActivity(intent);

    }


}
