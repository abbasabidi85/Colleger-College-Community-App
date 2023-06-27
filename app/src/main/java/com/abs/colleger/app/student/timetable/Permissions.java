package com.abs.colleger.app.student.timetable;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions extends AppCompatActivity {
    String[] permissions = new String[]{
            "android.permission.POST_NOTIFICATIONS"
    };
    boolean isPostNotificationsEnabled=false;

    String TAG="Permissions";

    public void requestPermissions(){
        if (!isPostNotificationsEnabled){
            requestPermissionPostNotifications();
        }
    }
    public boolean permissionResultCheck(){
        return isPostNotificationsEnabled;
    }

    public void requestPermissionPostNotifications(){
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            isPostNotificationsEnabled=true;
        }else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)){

            }else{

            } requestPermissionPostNotification.launch(permissions[0]);
        }
    }

    private ActivityResultLauncher<String> requestPermissionPostNotification =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted->{
               if (isGranted){
                   isPostNotificationsEnabled=true;
               }
               else{
                   isPostNotificationsEnabled=false;
               }
            });
}
