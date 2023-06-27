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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UserPreviousPapersFragment extends Fragment {

    private final static String TAG = "PreviousPapersFragment";
    View view;
    private RecyclerView getPreviousPapers;
    private List<UserFilesData> list1;
    private UserFilesAdapter adapter;
    private DatabaseReference reference, dbRef;


    public UserPreviousPapersFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_previous_papers_user,container, false);


        getPreviousPapers=view.findViewById(R.id.userFragmentPreviousPapers);

        reference= FirebaseDatabase.getInstance().getReference().child("pdf");
        getPreviousPapers();
        return view;
    }

    private void getPreviousPapers() {
        dbRef=reference.child("Previous Question Paper");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    getPreviousPapers.setVisibility(View.GONE);
                }else{
                    getPreviousPapers.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserFilesData data = snapshot.getValue(UserFilesData.class);
                        list1.add(data);
                    }
                    getPreviousPapers.setHasFixedSize(true);
                    getPreviousPapers.setLayoutManager((new LinearLayoutManager(getContext())));
                    adapter=new UserFilesAdapter(getContext(), list1 );
                    getPreviousPapers.setAdapter(adapter);
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
