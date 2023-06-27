package com.abs.colleger.app.student.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abs.colleger.app.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class UserLectureAdapter extends RecyclerView.Adapter<UserLectureAdapter.UserLectureViewAdapter>{
    Context mContext;
    List<UserLecture> mData;

    public UserLectureAdapter(Context mcontext, List<UserLecture>mData) {
        this.mContext=mcontext;
        this.mData=mData;
    }

    @NonNull
    @Override
    public UserLectureViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(mContext).inflate(R.layout.user_lecture_item,parent, false);
        return new UserLectureViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserLectureViewAdapter holder, int position) {

        UserLecture item = mData.get(position);
        holder.startTime.setText(item.getStartTime());
        holder.subject.setText(item.getSubject());
        holder.teacher.setText(item.getTeacher());
        holder.roomNumber.setText(item.getRoomNumber());
        
        holder.notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "This feature is coming soon... ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class UserLectureViewAdapter extends RecyclerView.ViewHolder {

        private TextView startTime, subject, teacher, roomNumber;
        MaterialCardView notifyButton;

        public UserLectureViewAdapter(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.startTimeTv);
            subject = itemView.findViewById(R.id.subjectNameTv);
            teacher = itemView.findViewById(R.id.teacherNameTv);
            roomNumber = itemView.findViewById(R.id.roomNumberTv);
            notifyButton = itemView.findViewById(R.id.notifyButton);
        }
    }
}
