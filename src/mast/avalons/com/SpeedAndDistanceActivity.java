package mast.avalons.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SpeedAndDistanceActivity extends Activity  implements SensorEventListener{
	SQLiteDatabase mDatabase;
	Cursor mCursor;	
	SensorManager sm = null;
	final String tag = "log";
    TextView DataText;
    String TimeNowtmp;
    int N, sec, stroka=0;
    XYZTdata[] mass = new XYZTdata[10000];
    float x, y, z, t = 0;
   int onoff=0;    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);             
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        Button write = (Button) findViewById(R.id.write);
        DataText = (TextView) findViewById(R.id.data);
          
        
        start.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) 
            		{
                    	startwrite();
                    }
                });
       
        stop.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) 
            		{
            			stopwrite();
                    }
            	});
        
        write.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) 
            		{
                    	masstosql();
                    	DataText.setText("Writed!!!");
                    }
            	});        
    }
    

    public void startwrite()
    {
    	onoff=1;
    	return;
    }
    public void stopwrite()
    {
    	onoff=0;
    	DataText.setText(String.valueOf(stroka));
    	return;
    }
    public void masstosql()
    {
    	long StartTime = mass[0].t;
    	for(int i=0; i<stroka; i++)
    	{
    		 ContentValues values = new ContentValues(2);
             values.put(DbHelper.X, Float.toString(mass[i].x));
             values.put(DbHelper.Y, Float.toString(mass[i].y));
             values.put(DbHelper.Z, Float.toString(mass[i].z));
             values.put(DbHelper.T, Float.toString(mass[i].t-StartTime));
             getContentResolver().insert(Provider.CONTENT_URI, values);            
             
        	
    	}
    	copyDb2SD();
    	return;
    }
      
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			if (onoff==1) {
				x = event.values[0];
				y = event.values[1];
				z = event.values[2];
				t = System.currentTimeMillis();
				Log.d(tag, "x="+x+"y="+y+"z="+z+"t="+t);
		     XYZTdata tmp;
    	     tmp = new XYZTdata(x, y,z, System.currentTimeMillis());
    	     mass[stroka] = tmp;
    	     stroka++;
		     }
		}	
		
	}    
    
	protected void onResume() {
        super.onResume();
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    @Override
    protected void onStop() {
        sm.unregisterListener(this);
        super.onStop();
    }  
    
    protected void copyDb2SD(){
    	File file1 = new File(Environment.getDataDirectory().getPath()+"/data/mast.avalons.com/databases/db.db");
    	File folder = new File(Environment.getExternalStorageDirectory () + "/DB");
    	folder.mkdir();
    	File file2 = new File(Environment.getExternalStorageDirectory () + "/DB/db"+System.currentTimeMillis()+".db");
    	try {
    		
			copyFile(file1,file2);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    };
    
    private static void copyFile(File sourceFile, File destFile)
            throws IOException {
    if (!sourceFile.exists()) {
    	 return;
    }
    if (!destFile.exists()) {
            destFile.createNewFile();
    }
    FileChannel source = null;
    FileChannel destination = null;
    source = new FileInputStream(sourceFile).getChannel();
    destination = new FileOutputStream(destFile).getChannel();
    if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
    }
    if (source != null) {
            source.close();
    }
    if (destination != null) {
            destination.close();
    }

}
    
}