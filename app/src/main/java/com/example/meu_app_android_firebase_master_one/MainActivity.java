package com.example.meu_app_android_firebase_master_one;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editTextId;
    EditText editTextRegistro;

    TextView textView;

    Button buttonCriar;
    Button buttonLer;
    Button buttonAtualizar;
    Button buttonDeletar;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        editTextId = findViewById(R.id.editTextId);
        editTextRegistro = findViewById(R.id.editTextRegistro);

        textView = findViewById(R.id.textView);

        buttonCriar = findViewById(R.id.buttonCriar);
        buttonLer = findViewById(R.id.buttonLer);
        buttonAtualizar = findViewById(R.id.buttonAtualizar);
        buttonDeletar = findViewById(R.id.buttonDeletar);

    }


    public void criar(View view) {

        Map<String, Object> colecao = new HashMap<>();
        colecao.put("registro", editTextRegistro.getText().toString());

        db.collection("minhacolecao")
                .add(colecao)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        editTextId.setText(documentReference.getId());
                        textView.setText("Cadastrado!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAGCadastro", "Erro ao cadastrar", e);
                    }
                });
    }



    public void ler(View view){
        DocumentReference docRef = db.collection("minhacolecao").document(editTextId.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        textView.setText(document.getString("registro"));
                    } else {
                        Log.d("TAG", "Documento não encontrado");
                    }
                } else {
                    Log.d("TAG", "Falhou em ", task.getException());
                }
            }
        });
    }



    public void atualizar(View view){
        db.collection("minhacolecao").document(editTextId.getText().toString()).update("registro", editTextRegistro.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                textView.setText("Atualizado!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Falhou ao atualizar");
            }
        });
    }



    public void deletar(View view){
        db.collection("minhacolecao").document(editTextId.getText().toString())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        editTextId.setText("");
                        textView.setText("Deletado!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Erro ao deletar!", e);
                    }
                });
    }
}
