package com.abs.colleger.app.admin.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abs.colleger.app.R;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewAdapter>{
    Context mContext;
    List<Lecture> mData;

    public LectureAdapter(Context mcontext, List<Lecture>mData) {
        this.mContext=mcontext;
        this.mData=mData;
    }

    @NonNull
    @Override
    public LectureViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(mContext).inflate(R.layout.lecture_item,parent, false);
        return new LectureViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureViewAdapter holder, int position) {

        Lecture item = mData.get(position);
        holder.startTime.setText(item.getStartTime());
        holder.subject.setText(item.getSubject());
        holder.teacher.setText(item.getTeacher());
        holder.roomNumber.setText(item.getRoomNumber());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class LectureViewAdapter extends RecyclerView.ViewHolder {

        private TextView startTime, subject, teacher, roomNumber;

        public LectureViewAdapter(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.adminStartTimeTv);
            subject = itemView.findViewById(R.id.adminSubjectNameTv);
            teacher = itemView.findViewById(R.id.adminTeacherNameTv);
            roomNumber = itemView.findViewById(R.id.adminRoomNumberTv);
        }
    }
}
