package com.example.noteify;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    ListView listView;
    Button button;
    public String veriText,veriZaman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ListView listView=(ListView)findViewById(R.id.listView);
        Button button = (Button)findViewById(R.id.button_time);
        Button button_ok=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(), "time picker");



                //Veritabani veritabani = new Veritabani(MainActivity.this);





            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = (EditText)findViewById(R.id.editText);
                if(editText.getText().toString()!=""){

                    Veritabani veritabani = new Veritabani(MainActivity.this);
                    veritabani.VeriEkle(editText.getText().toString(), veriZaman);

                    List<String> vVeriler = veritabani.VeriListele();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, vVeriler);
                    listView.setAdapter(adapter);

                    //EditText editText =(EditText)findViewById(R.id.editText);
                    editText.setText("");
                }

            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                if (view.getId() == R.id.listView) {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                    contextMenu.setHeaderTitle(listView.getItemAtPosition(info.position).toString());
                    contextMenu.add(0, 0, 0, "Sil");
                    contextMenu.add(0, 1, 0, "Düzenle");
                }
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        /*EditText editText = (EditText)findViewById(R.id.editText);
        String notifyText=" (" + hourOfDay + ":" + minute + ")";

        String message =editText.getText().toString();
        notifyText+=message;
        editText.setText(notifyText);*/




        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);

    }

    private void updateTimeText(Calendar c ){
        String timeText = "Alarm set for: ";
        timeText+= DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        veriZaman=DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        //textview.settext(timeText);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1 , intent , 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }




    public boolean onContextItemSelected(MenuItem item){
        boolean donus;
        final ListView listView=(ListView)findViewById(R.id.listView);
        switch (item.getItemId()){
            case 0:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final String secili = listView.getItemAtPosition(info.position).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Veri Silme");
                builder.setMessage("\"" + secili + "\" adlı veriyi silmek istediğinize emin misiniz ?");
                builder.setNegativeButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] dizi = secili.split("-");
                        long id = Long.parseLong(dizi[0].trim());
                        Veritabani veritabani = new Veritabani(MainActivity.this);
                        veritabani.VeriSil(id);
                        veritabani.VeriListele();
                    }
                });

                builder.setPositiveButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                donus=true;
                break;
            default:
                donus=false;
                break;
        }
        return donus;

    }
}


















