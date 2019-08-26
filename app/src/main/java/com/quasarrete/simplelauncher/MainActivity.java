 package com.quasarrete.simplelauncher;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

 public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeDrawer();
    }
     List<AppObject> installedApps = new ArrayList<>();

     private void initializeDrawer() {
         View mBottomSheet = findViewById(R.id.bottomSheet);
         final GridView mGridView = findViewById(R.id.drawerGrid);
         final BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
         mBottomSheetBehavior.setHideable(false);
         mBottomSheetBehavior.setPeekHeight(300);

         installedApps = getInstalledApps();
         mGridView.setAdapter(new AppAdapter(getApplicationContext(), installedApps));

         mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
             @Override
             public void onStateChanged(@NonNull View view, int newState) {
                 if((newState == BottomSheetBehavior.STATE_HIDDEN) && mGridView.getChildAt(0).getY()!=0){
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                 }
                 if((newState == BottomSheetBehavior.STATE_DRAGGING) && mGridView.getChildAt(0).getY()!=0){
                     mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                 }
             }

             @Override
             public void onSlide(@NonNull View view, float v) {

             }
         });

     }

    private List<AppObject> getInstalledApps(){
        List<AppObject> list = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> untreatedAppList = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);

        for(ResolveInfo untreatedApp: untreatedAppList){
            String appName = untreatedApp.activityInfo.loadLabel(getPackageManager()).toString();
            String packageName = untreatedApp.activityInfo.packageName;
            Drawable image = untreatedApp.activityInfo.loadIcon(getPackageManager());
            AppObject app = new AppObject(appName, packageName, image);
            if(!list.contains(app)){
                list.add(app);
            }
        }

        return list;

    }

}
