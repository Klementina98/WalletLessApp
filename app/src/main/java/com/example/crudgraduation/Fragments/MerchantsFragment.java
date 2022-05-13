package com.example.crudgraduation.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.crudgraduation.Activity.AddMerchantActivity;
import com.example.crudgraduation.Adapter.MainAdapter;
import com.example.crudgraduation.Model.Card;
import com.example.crudgraduation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MerchantsFragment extends Fragment {

    public final static String TAG;

    static {
        TAG = AboutAppFragment.class.getCanonicalName();
    }

    Button addMerchant;
    TextView list_status;
    MainAdapter mainAdapter;
    RecyclerView allCards;
    EditText search;
    ArrayList<Card> list;
    DatabaseReference ref;

    public MerchantsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setUp(){
        addMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open new activity
                Intent intent = new Intent(getActivity(), AddMerchantActivity.class);
                startActivity(intent);
            }
        });
        getData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_merchants, container, false);
        findViews(rootView);
        setUp();
        return rootView;
    }

    private void findViews(View rootView) {
        addMerchant = rootView.findViewById(R.id.add_merchant);
        search = rootView.findViewById(R.id.searchText);
        ref = FirebaseDatabase.getInstance().getReference().child("AllCards");
        allCards = rootView.findViewById(R.id.recyclerListCard);
        allCards.setLayoutManager(new LinearLayoutManager(getContext()));
        list_status = rootView.findViewById(R.id.list_status);
        list_status.setVisibility(View.GONE);
    }

    private void getData() {
        if(ref!=null){
            list = new ArrayList<>();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        list = new ArrayList<>();
                        for (DataSnapshot ds:snapshot.getChildren()){
                            list.add(ds.getValue(Card.class));

                        }
                        mainAdapter = new MainAdapter(list);
                        allCards.setAdapter(mainAdapter);

                    }
                    for (Card c:list){
                        System.out.println(c.getCardName());
                    }
                    if (list.size()>0){
                        list_status.setVisibility(View.GONE);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
            (new Handler()).postDelayed(this::checkListSize, 2000);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    search(editable.toString());
                }
            });
        }
    }

    private void checkListSize() {
        if (list.size()==0){
            //you don't have internet connection
            list_status.setVisibility(View.VISIBLE);
        }
    }

    private void search(String str) {
        if (list.size() > 0) {
        ArrayList<Card> filteredList = new ArrayList<>();
        if (str.equals("")) {
            filteredList = list;
        } else {
            for (Card obj : list) {
                if (obj.getCardName().toLowerCase().contains(str.toLowerCase())) {
                    filteredList.add(obj);
                }
            }
        }

        mainAdapter.FilteredList(filteredList); }
    }


}