package com.example.perei.boicote.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perei.boicote.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends AppCompatActivity {

    private EditText editLoginUsuario;
    private EditText editLoginSenha;
    private Button botaoLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editLoginUsuario = (EditText) findViewById(R.id.edit_login_usuario);
        editLoginSenha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.button_logar);



        ParseUser.logOut();


        //Verificar se o usuário está logado
        verificarUsuarioLogado();

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = editLoginUsuario.getText().toString();
                String senha = editLoginSenha.getText().toString();

                verificarLogin(usuario,senha);
            }
        });


    }


    private void verificarLogin(String usuario, String senha){

        ParseUser.logInInBackground(usuario, senha, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {


                if(e == null){
                    Toast.makeText(Login.this,"Login realizado com sucesso", Toast.LENGTH_LONG).show();
                    abrirAreaPrincipal();
                }else{
                    Toast.makeText(Login.this,"Erro ao fazer login" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(Login.this, Cadastro.class);
        startActivity( intent );
    }

    private void verificarUsuarioLogado(){

        if( ParseUser.getCurrentUser() != null ){
            //Enviar usuário para tela principal do App
            abrirAreaPrincipal();
        }

    }

    private void abrirAreaPrincipal(){
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity( intent );
        finish();
    }

}
