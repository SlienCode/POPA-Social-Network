package com.example.POPA;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScheduleMaker extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;

    Button btnUpDirectory,btnSDCard, loadButton;
    ImageButton backButton;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;

    ListView lvInternalStorage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_maker);
        lvInternalStorage = (ListView) findViewById(R.id.lvInternalStorage);
        btnUpDirectory = (Button) findViewById(R.id.btnUpDirectory);
        btnSDCard = (Button) findViewById(R.id.btnViewSDCard);
        loadButton = (Button) findViewById(R.id.loadButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(!Environment.isExternalStorageManager()) {
                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                this.startActivity(intent);
            }
        }

        //need to check the permissions
        checkFilePermissions();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if the user has already created a schedule or not
                DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ScheduleMaker");

                ValueEventListener scheduleValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                        for (DataSnapshot child : children) {

                            //we have made a schedule with this user!
                            if (child.getKey().equals(Key.key)) {

                                //go to ScheduleMaker_View
                                Intent intent = new Intent(ScheduleMaker.this, ScheduleMaker_View.class);
                                intent.putExtra("loaded", true);
                                startActivity(intent);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                mDatabaseReference.addListenerForSingleValueEvent(scheduleValueEventListener);
            }
        });

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);
                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                    Log.d(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);

                    //Execute method for reading the excel data.
                    Log.d("yolo", lastDirectory);
                    readExcelData(lastDirectory);

                }else
                {
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                    Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
                }
            }
        });

        //Goes up one directory level
        btnUpDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == 0){
                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                }else{
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();
                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));
                }
            }
        });

        //Opens the SDCard or phone memory
        btnSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
                Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
                checkInternalStorage();
            }
        });

    }

    private void readExcelData(String filePath) {
        Log.d(TAG, "readExcelData: Reading Excel File.");

        //decarle input file
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            //are we in the ΠΛΗΡΟΦΟΡΙΚΗ tab? Good, start writing.
            boolean found = false;

            //key: course, value: 5-value Array for every weekday
            HashMap<String, String[]> table = new HashMap<>();

            //5-value Array
            String hours[];

            //previous row course
            String lastCourse = "";

            //lectures or labs, which one is it
            String mode = "";

            //outer loop, loops through rows
            for (int r = 0; r < rowsCount; r++) {

                hours = new String[5];
                Row row = sheet.getRow(r);
                String value = getCellAsString(row, 0, formulaEvaluator);

                //test if a cell is just spaces
                String extraTest = value.replaceAll(" ", "");

                //just in case...
                value = value.replaceAll("  ", " ");

                if (found) {
                    if (value.equals("ΤΜΗΜΑ ΣΤΑΤΙΣΤΙΚΗΣ")) {

                        //game over, stop writing
                        Log.d(TAG, "Found first ΣΤΑΤΙΣΤΙΚΗ at row " + row.getRowNum());
                        break;
                    }
                    //we are at ΕΡΓΑΣΤΗΡΙΑ/ΦΡΟΝΤΙΣΤΗΡΙΑ because the course has already been inserted in
                    else if (table.containsKey(value)) {
                        for (int i = 0; i < 5; i++) {
                            if (!getCellAsString(row, i + 1, formulaEvaluator).equals("")) {
                                if (!table.get(value)[i].equals(""))
                                    hours[i] = table.get(value)[i] + " + " + mode + " " + getCellAsString(row, i + 1, formulaEvaluator) + " " + getCellAsString(sheet.getRow(r + 1), i + 1, formulaEvaluator);
                                else
                                    hours[i] = mode + " " + getCellAsString(row, i + 1, formulaEvaluator) + " " + getCellAsString(sheet.getRow(r + 1), i + 1, formulaEvaluator);
                            } else
                                hours[i] = table.get(value)[i];
                        }
                        table.put(value, hours);

                        //we got the classroom ID as well so we need to increment the row by 2 to get to the next course
                        r++;

                        lastCourse = value;
                    }
                    //blank row could mean this is the same course
                    else if (extraTest.equals("")) {
                        boolean flag = false;
                        for (int i = 0; i < 5; i++) {

                            //if at least one of them is not empty, it means this is an extension of a course
                            if (!getCellAsString(row, i + 1, formulaEvaluator).replaceAll(" ", "").equals(""))
                                flag = true;
                        }
                        if (flag) {
                            value = lastCourse;
                            if (mode.equals("")) {
                                for (int i = 0; i < 5; i++) {
                                    if (!getCellAsString(row, i + 1, formulaEvaluator).equals(""))
                                        hours[i] = table.get(value)[i] + " + " + getCellAsString(row, i + 1, formulaEvaluator) + " " + getCellAsString(sheet.getRow(r + 1), i + 1, formulaEvaluator);
                                    else
                                        hours[i] = table.get(value)[i];
                                }
                            }
                            else {
                                for (int i = 0; i < 5; i++) {
                                    if (!getCellAsString(row, i + 1, formulaEvaluator).equals("")) {
                                        if (!table.get(value)[i].equals(""))
                                            hours[i] = table.get(value)[i] + " + " + mode + " " + getCellAsString(row, i + 1, formulaEvaluator) + " " + getCellAsString(sheet.getRow(r + 1), i + 1, formulaEvaluator);
                                        else
                                            hours[i] = mode + " " + getCellAsString(row, i + 1, formulaEvaluator) + " " + getCellAsString(sheet.getRow(r + 1), i + 1, formulaEvaluator);
                                    }
                                    else
                                        hours[i] = table.get(value)[i];
                                }
                            }
                            table.put(value, hours);

                            //we got the classroom ID as well so we need to increment the row by 2 to get to the next course
                            r++;

                            lastCourse = value;
                        }
                    }
                    //modify the mode
                    else if (value.equals("ΤΜΗΜΑ ΠΛΗΡΟΦΟΡΙΚΗΣ"))
                        mode = "";
                    else if (value.equals("ΦΡΟΝΤΙΣΤΗΡΙΑ"))
                        mode = "Φροντ.";
                    else if (value.equals("ΕΡΓΑΣΤΗΡΙΑ"))
                        mode = "Εργαστ.";
                    else if (!(value.equals("ΜΑΘΗΜΑ") || value.contains("ΕΞΑΜΗΝΟ") || value.contains(".") || value.equals("ΥΠΟΧΡΕΩΤΙΚΑ") || value.contains("*") ||
                            value.equals("ΞΕΝΕΣ ΓΛΩΣΣΕΣ") || value.equals("ΕΠΙΛΟΓΗΣ") || value.equals("ΜΑΘΗΜΑΤΑ ΕΠΙΛΟΓΗΣ") || value.contains("ΑΓΩΓΗΣ"))) {
                        for (int i = 0; i < 5; i++) {
                            if (!getCellAsString(row, i + 1, formulaEvaluator).equals("")) {
                                if (mode.equals(""))
                                    hours[i] = getCellAsString(row, i + 1, formulaEvaluator) + " " + getCellAsString(sheet.getRow(r + 1), i + 1, formulaEvaluator);
                                else
                                    hours[i] = mode + " " + getCellAsString(row, i + 1, formulaEvaluator) + " " + getCellAsString(sheet.getRow(r + 1), i + 1, formulaEvaluator);
                            } else
                                hours[i] = "";
                        }
                        table.put(value, hours);

                        //we got the classroom ID as well so we need to increment the row by 2 to get to the next course
                        r++;

                        lastCourse = value;
                    }
                }
                else if (value.equals("ΤΜΗΜΑ ΠΛΗΡΟΦΟΡΙΚΗΣ")) {
                    Log.d(TAG, "Found first ΠΛΗΡΟΦΟΡΙΚΗ at row " + row.getRowNum());
                    found = true;
                }
            }

            //go to ScheduleMaker_New
            Intent intent = new Intent(ScheduleMaker.this, ScheduleMaker_New.class);
            intent.putExtra("map", table);
            startActivity(intent);

        }catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage() );
        }
    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            if (cellValue != null) {
                switch (cellValue.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = "" + cellValue.getBooleanValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        double numericValue = cellValue.getNumberValue();
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            double date = cellValue.getNumberValue();
                            SimpleDateFormat formatter =
                                    new SimpleDateFormat("MM/dd/yy");
                            value = formatter.format(HSSFDateUtil.getJavaDate(date));
                        } else {
                            value = "" + numericValue;
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        value = "" + cellValue.getStringValue();
                        break;
                    default:
                }
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );
        }
        return value;
    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: Started.");
        try{
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found.");
            }
            else{
                // Locate the image folder in your SD Car;d
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }

            listFile = file.listFiles();

            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }

            for (int i = 0; i < listFile.length; i++)
            {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);

        }catch(NullPointerException e){
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermissions() {
        int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
        permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
        if (permissionCheck != 0) {

            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    }