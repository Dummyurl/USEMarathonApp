//package ru.use.marathon.activities;
//
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.support.v4.app.NavUtils;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import ru.alexanderklimov.cathouse.data.HotelContract;
//import ru.alexanderklimov.cathouse.data.HotelContract.GuestEntry;
//import ru.alexanderklimov.cathouse.data.HotelDbHelper;
//import ru.use.marathon.R;
//
//public class EditorActivity extends AppCompatActivity {
//
//    private EditText mNameEditText;
//    private EditText mCityEditText;
//    private EditText mAgeEditText;
//
//    private Spinner mGenderSpinner;
//
//    /**
//     * Пол для гостя. Возможные варианты:
//     * 0 для кошки, 1 для кота, 2 - не определен.
//     */
//    private int mGender = 2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_editor);
//
//        mNameEditText = (EditText) findViewById(R.id.edit_guest_name);
//        mCityEditText = (EditText) findViewById(R.id.edit_guest_city);
//        mAgeEditText = (EditText) findViewById(R.id.edit_guest_age);
//        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
//
//        setupSpinner();
//    }
//
//    /**
//     * Настраиваем spinner для выбора пола у гостя.
//     */
//    private void setupSpinner() {
//
//        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
//                R.array.array_gender_options, android.R.layout.simple_spinner_item);
//
//        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//
//        mGenderSpinner.setAdapter(genderSpinnerAdapter);
//        mGenderSpinner.setSelection(2);
//
//        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selection = (String) parent.getItemAtPosition(position);
//                if (!TextUtils.isEmpty(selection)) {
//                    if (selection.equals(getString(R.string.gender_female))) {
////                        mGender = 0; // Кошка
//                        mGender = HotelContract.GuestEntry.GENDER_FEMALE; // Кошка
//                    } else if (selection.equals(getString(R.string.gender_male))) {
////                        mGender = 1; // Кот
//                        mGender = HotelContract.GuestEntry.GENDER_MALE; // Кот
//                    } else {
////                        mGender = 2; // Не определен
//                        mGender = HotelContract.GuestEntry.GENDER_UNKNOWN; // Не определен
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                mGender = 2; // Unknown
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu options from the res/menu/menu_editor.xml file.
//        // This adds menu items to the app bar.
//        getMenuInflater().inflate(R.menu.menu_editor, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // User clicked on a menu option in the app bar overflow menu
//        switch (item.getItemId()) {
//            // Respond to a click on the "Save" menu option
//            case R.id.action_save:
//                insertGuest();
//                // Закрываем активность
//                finish();
//                return true;
//            // Respond to a click on the "Delete" menu option
//            case R.id.action_delete:
//                // Do nothing for now
//                return true;
//            // Respond to a click on the "Up" arrow button in the app bar
//            case android.R.id.home:
//                // Navigate back to parent activity (CatalogActivity)
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    /**
//     * Сохраняем введенные данные в базе данных.
//     */
//    private void insertGuest() {
//        // Считываем данные из текстовых полей
//        String name = mNameEditText.getText().toString().trim();
//        String city = mCityEditText.getText().toString().trim();
//        String ageString = mAgeEditText.getText().toString().trim();
//        int age = Integer.parseInt(ageString);
//
//        HotelDbHelper mDbHelper = new HotelDbHelper(this);
//
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(GuestEntry.COLUMN_NAME, name);
//        values.put(GuestEntry.COLUMN_CITY, city);
//        values.put(GuestEntry.COLUMN_GENDER, mGender);
//        values.put(GuestEntry.COLUMN_AGE, age);
//
//        // Вставляем новый ряд в базу данных и запоминаем его идентификатор
//        long newRowId = db.insert(GuestEntry.TABLE_NAME, null, values);
//
//        // Выводим сообщение в успешном случае или при ошибке
//        if (newRowId == -1) {
//            // Если ID  -1, значит произошла ошибка
//            Toast.makeText(this, "Ошибка при заведении гостя", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Гость заведён под номером: " + newRowId, Toast.LENGTH_SHORT).show();
//        }
//    }
//}
