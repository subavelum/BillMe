package com.map.suba.dontgiveup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.map.suba.mylibrary.adapter.SwipeToDismissTouchListener;
import com.map.suba.mylibrary.adapter.adapter.ListViewAdapter;

//import com.hudomju.swipe.SwipeToDismissTouchListener;
//import com.hudomju.swipe.adapter.ListViewAdapter;
//import com.hudomju.swipe.sample.mDatabase.DBAdapter;

public class MainActivity extends Activity implements DatePickerDialog.OnDateSetListener {

    private static final int TIME_TO_AUTOMATICALLY_DISMISS_ITEM = 1500;
    DBAdapter myDb;
    EditText et;
   // ArrayList<String> Str = new ArrayList<String>();
    long mId1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.editText);
        openDb();
        populateListViewFromDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDb();
    }
//When clicking user input is received and inserted into database
    public void onGoToListViewClicked(View view) {

        // EditText et=(EditText)findViewById(R.id.editText);
        String input = et.getText().toString();

        if (input.length() > 0) {

           // myDb.insertRow(input,"Due date to be set");
            myDb.insertRow(input, "Due date to be set");
            populateListViewFromDB();
            et.setText("");
        }
    }
    private void populateListViewFromDB() {


        myDb.sort();
        Cursor cursor = myDb.getAllRows();
        String[] fromFileNames = new String[]{DBAdapter.KEY_TASK,DBAdapter.KEY_DATE};
        int[] toViewIDs = new int[]{R.id.txt_data,R.id.txt_data1};




        final SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.list_item, cursor, fromFileNames, toViewIDs, 0);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(myCursorAdapter);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {

                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onPendingDismiss(ListViewAdapter recyclerView, int position) {

                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                // adapter.remove(position);
                                long mId = myCursorAdapter.getItemId(position);
                                myDb.deleteRow(mId);
                                populateListViewFromDB();
                            }
                        }
                );

        touchListener.setDismissDelay(TIME_TO_AUTOMATICALLY_DISMISS_ITEM);
        listView.setOnTouchListener(touchListener);


        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    mId1 = myCursorAdapter.getItemId(position);
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "Date Picker");
                    //Toast.makeText(MainActivity.this, "Position " + position, LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openDb() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDb() {
        myDb.close();
    }

//will be listening when setting date and returns selected date to following method
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        month=month+1;
        String currentInfo="";
        Cursor cursor=myDb.getRow(mId1);// finding selected row item to store the selected date
        if(cursor.moveToFirst()){
            do{
                currentInfo=cursor.getString(cursor.getColumnIndex(myDb.KEY_DATE));

            }while (cursor.moveToNext());
            cursor.close();
        }
        currentInfo =year+"-"+month+"-"+day;
        //Storing the date at selected row
        myDb.updateRow(mId1, currentInfo);

        populateListViewFromDB();
    }

}
