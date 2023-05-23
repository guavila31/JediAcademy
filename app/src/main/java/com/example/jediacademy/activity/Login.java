package com.example.jediacademy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jediacademy.R;
import com.example.jediacademy.config.ConfiguracaoFirebase;
import com.example.jediacademy.model.Professor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Login extends AppCompatActivity {
    Button btnEntrar, btnCriarConta,btnAcessarConta;
    TextView txtEmail,txtSenha;
    private FirebaseAuth autenticacao;
    private Professor professor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificarUsuarioLogado();
        btnAcessarConta=findViewById(R.id.btnAcessarConta);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha=findViewById(R.id.txtSenha);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Acessou ao botao");
                String textoSenha = txtSenha.getText().toString();
                String textoEmail = txtEmail.getText().toString();

                if (textoEmail.isEmpty() & textoSenha.isEmpty()) {
                    Toast.makeText(Login.this, "Email e senha são obrigatório!", Toast.LENGTH_SHORT).show();
                }else if  (textoEmail.isEmpty() ) {
                    Toast.makeText(Login.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
                }else if  (textoSenha.toString().isEmpty() ) {
                    Toast.makeText(Login.this, "Preencha a Senha!", Toast.LENGTH_SHORT).show();

                } else {
                    System.out.println("loginnnnnnnnnnnnnnnnnnnnnnnnnn");
                    professor = new Professor();
                    professor.setEmail(textoEmail);
                    professor.setSenha(textoSenha);
                    validarLogin();
                }
            }
        });
        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ir = new Intent(Login.this, Cadastro.class);
                startActivity(ir);
            }
        });
    }
    public void validarLogin(){
        System.out.println("Entrou no at: senha>"+professor.getSenha()+"Email: "+professor.getEmail());
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(professor.getEmail(), professor.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Seja Bem Vindo!", Toast.LENGTH_SHORT).show();
                            abrirTelaAgenda();

                        } else {
                            String erroCadastro = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroCadastro = "Email ou senha inválidos!";
                            } catch (FirebaseAuthInvalidUserException e) {
                                erroCadastro = "Usuário não cadastrado!";
                            } catch (Exception e) {
                                erroCadastro = "Erro ao cadastrar [1]" + e.getMessage();
                            }

                            Toast.makeText(Login.this,erroCadastro, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null){
           abrirTelaAgenda();
            finish();
        }
    }
    public void abrirTelaAgenda(){
        startActivity(new Intent(this, AgendaActivity.class));
    }
    public void fecharTeclado(View view){
        InputMethodManager fecharteclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fecharteclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
