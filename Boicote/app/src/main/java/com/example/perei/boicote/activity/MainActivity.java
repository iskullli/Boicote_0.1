package com.example.perei.boicote.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.perei.boicote.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter arrayAdapter;

    private ArrayList<String> usuarios;
    private ArrayList<String> userids;
    private ArrayList<String> seguindo;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();

    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    private String meuUid;
    private String meuNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seguindo = new ArrayList<>();
        usuarios = new ArrayList<>();
        userids = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, usuarios);
        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView) view;
                if (checkedTextView.isChecked()) {
                    seguindo.add(userids.get(position));
                } else {
                    seguindo.remove(seguindo.indexOf(userids.get(position)));
                }

                ref.child("users").child(meuUid).child("seguindo").setValue(seguindo);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
        } else {
            meuUid = user.getUid();
            ref.child("users").child(meuUid).child("nome").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    meuNome = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            userids.clear();
            usuarios.clear();
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(!dataSnapshot.child("uid").getValue(String.class).equals(meuUid)){
                        usuarios.add(dataSnapshot.child("nome").getValue(String.class));
                        userids.add(dataSnapshot.child("uid").getValue(String.class));
                        arrayAdapter.notifyDataSetChanged();
                        atualizarLista();
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            ref.child("users").addChildEventListener(childEventListener);

            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    seguindo.clear();
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        seguindo.add(data.getValue(String.class));
                    }
                    Log.d("meuLog", "seguindo: " + seguindo);
                    atualizarLista();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            ref.child("users").child(meuUid).child("seguindo").addValueEventListener(valueEventListener);
        }
    }

    public void atualizarLista(){
        for(String uid:userids){
            if(seguindo.contains(uid)){
                listView.setItemChecked(userids.indexOf(uid), true);
            } else {
                listView.setItemChecked(userids.indexOf(uid), false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_boicote_codigo_do_satanas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.feed) {



            Intent i = new Intent(getApplicationContext(), MeusFeeds.class);
            i.putStringArrayListExtra("seguindo", seguindo);
            startActivity(i);


        }else if (id == R.id.Boicotada) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Enviar um boicote");
            final EditText conteudoBoicote = new EditText(this);
            builder.setView(conteudoBoicote);

            builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Map<String, Object> boi = new HashMap<>();
                    boi.put("msg", conteudoBoicote.getText().toString());
                    boi.put("uid", meuUid);
                    boi.put("data", -1 * System.currentTimeMillis());
                    boi.put("nome", meuNome);
                    ref.child("boicotes").push().setValue(boi);

                    Toast.makeText(getApplicationContext(), "Seu boicote foi enviado", Toast.LENGTH_LONG).show();
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });

            builder.show();

            return true;


        } else if (id == R.id.sair) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
