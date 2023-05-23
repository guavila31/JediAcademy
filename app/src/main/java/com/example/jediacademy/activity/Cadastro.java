package com.example.jediacademy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jediacademy.R;
import com.example.jediacademy.config.ConfiguracaoFirebase;
import com.example.jediacademy.helper.Base64Custom;
import com.example.jediacademy.model.Professor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cadastro extends AppCompatActivity {
    private Button btnAcessarConta, btnCriar;
    private TextView txtEmail, txtSenha, txtConfirmarSenha, txtNome;
    private Professor professor;
    private FirebaseAuth autenticacao;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        txtConfirmarSenha = findViewById(R.id.txtConfirmarSenha);
        txtNome = findViewById(R.id.txtNome);
        btnCriar = findViewById(R.id.btnCriar);
        btnAcessarConta = findViewById(R.id.btnAcessarConta);

        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoNome = txtNome.getText().toString();
                String textoEmail = txtEmail.getText().toString();
                String textoSenha = txtSenha.getText().toString();
                String textoConfirmarSenha = txtConfirmarSenha.getText().toString();

                if (textoNome.isEmpty() & textoEmail.isEmpty() & textoSenha.isEmpty() & textoConfirmarSenha.isEmpty()) {
                    Toast.makeText(Cadastro.this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
                } else if (textoNome.isEmpty()) {
                    Toast.makeText(Cadastro.this, "Preencha o Nome!", Toast.LENGTH_SHORT).show();
                } else if (textoEmail.isEmpty()) {
                    Toast.makeText(Cadastro.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
                } else if (textoSenha.isEmpty()) {
                    Toast.makeText(Cadastro.this, "Preencha a Senha!", Toast.LENGTH_SHORT).show();
                } else if (!textoConfirmarSenha.equals(textoSenha)) {
                    Toast.makeText(Cadastro.this, "As senhas devem ser iguais!", Toast.LENGTH_SHORT).show();
                } else {
                    professor = new Professor();
                    professor.setNome(textoNome);
                    professor.setEmail(textoEmail);
                    professor.setSenha(textoSenha);
                    cadastrarUsuario();
                }
            }
        });

        btnAcessarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ir = new Intent(Cadastro.this, Login.class);
                startActivity(ir);
            }
        });
    }

    public void fecharTeclado(View view) {
        InputMethodManager fecharteclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fecharteclado.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(professor.getEmail(), professor.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Cadastro.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent ir = new Intent(Cadastro.this, AgendaActivity.class);
                            startActivity(ir);

                            String idUsuario = Base64Custom.codificarBase64(professor.getEmail());
                            professor.setIdUsuario(idUsuario);
                            professor.salvar();

                            finish();
                        } else {
                            String erroCadastro = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroCadastro = "Digite uma senha mais forte, mínimo 6 digitos!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroCadastro = "Email inválido!";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroCadastro = "Email já cadastrado!";
                            } catch (Exception e) {
                                erroCadastro = "Erro ao cadastrar [2]: " + e.getMessage();
                            }

                            Toast.makeText(Cadastro.this, erroCadastro, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
