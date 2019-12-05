package com.tofa.circular.sqldatabase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.tofa.circular.customclass.Utils;

import java.util.Timer;
import java.util.TimerTask;
import androidx.annotation.Nullable;

import static com.tofa.circular.customclass.Utils.insertDataArrayList;


public class InsertDataService extends Service {
    private DatabaseHelper db;
   private Timer timer = new Timer();
    private TimerTask hourlyTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        db = new DatabaseHelper(this);
        hourlyTask = new TimerTask() {
            @Override
            public void run() {
                try {
//                    insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_CALORIES, "5-Dec-2019",Utils.getCurrentTime(),1.5+""));
//                    insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_CALORIES, "5-Dec-2019",Utils.getCurrentTime(),1.8+""));
//                    insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_CALORIES, "5-Dec-2019",Utils.getCurrentTime(),1.4+""));

                    if (insertDataArrayList !=null && insertDataArrayList.size() > 0){
                        for (int i = 0; i< insertDataArrayList.size(); i++){
                            if (insertDataArrayList.get(0) != null && insertDataArrayList.size()>0) {
                                DatabaseHelperTable model = insertDataArrayList.get(0);
                                db.insertStepsTakenData(model.dataType,model.date,model.time,model.value);
                                insertDataArrayList.remove(0);
                                i = -1;
                            }else {
                                return;
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(hourlyTask, 0l, 5000);
    }

    @Override
    public void onDestroy() {
        try {
            timer.cancel();
            hourlyTask.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
