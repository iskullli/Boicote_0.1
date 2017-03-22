package com.example.perei.boicote.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

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
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoicoteFragment extends Fragment {

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

    public BoicoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boicote, container, false);
        setHasOptionsMenu(true);
        seguindo = new ArrayList<>();
        usuarios = new ArrayList<>();
        userids = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_checked, usuarios);
        listView = (ListView) view.findViewById(R.id.listView);
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
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            getActivity().finish();
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu,  MenuInflater inflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
//        Inflater.inflate(R.menu.menu_settings, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//    }




}


