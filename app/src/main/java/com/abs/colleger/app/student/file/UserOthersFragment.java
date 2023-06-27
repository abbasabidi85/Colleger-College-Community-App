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

public class UserOthersFragment extends Fragment {

    private final static String TAG = "OthersFragment";
    View view;
    private RecyclerView getOthers;
    private List<UserFilesData> list1;
    private UserFilesAdapter adapter;
    private DatabaseReference reference, dbRef;

    public UserOthersFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_others_user,container, false);


        getOthers=view.findViewById(R.id.userFragmentOthers);

        reference= FirebaseDatabase.getInstance().getReference().child("pdf");
        getOthers();
        return view;
    }

    private void getOthers() {
        dbRef=reference.child("Other");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    getOthers.setVisibility(View.GONE);
                }else{
                    getOthers.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserFilesData data = snapshot.getValue(UserFilesData.class);
                        list1.add(data);
                    }
                    getOthers.setHasFixedSize(true);
                    getOthers.setLayoutManager((new LinearLayoutManager(getContext())));
                    adapter=new UserFilesAdapter(getContext(), list1 );
                    getOthers.setAdapter(adapter);
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
