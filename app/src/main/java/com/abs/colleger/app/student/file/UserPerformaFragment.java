package com.abs.colleger.app.student.file;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abs.colleger.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserPerformaFragment extends Fragment {

    private final static String TAG = "PerformasFragment";
    View view;
    private RecyclerView getPerformas;
    private List<UserFilesData> list1;
    private UserFilesAdapter adapter;
    private DatabaseReference reference, dbRef;

    public UserPerformaFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_performas_user,container, false);


        getPerformas=view.findViewById(R.id.userFragmentPerformas);

        reference= FirebaseDatabase.getInstance().getReference().child("pdf");
        getPerformas();
        return view;
    }

    private void getPerformas() {
        dbRef=reference.child("Performa");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    getPerformas.setVisibility(View.GONE);
                }else{
                    getPerformas.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserFilesData data = snapshot.getValue(UserFilesData.class);
                        list1.add(data);
                    }
                    getPerformas.setHasFixedSize(true);
                    getPerformas.setLayoutManager((new LinearLayoutManager(getContext())));
                    adapter=new UserFilesAdapter(getContext(), list1 );
                    getPerformas.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
