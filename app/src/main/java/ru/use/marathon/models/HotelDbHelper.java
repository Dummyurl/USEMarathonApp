package ru.use.marathon.models;
/**
 * Created by Marat on 24-July-18.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.use.marathon.models.data.HotelContract;
import ru.use.marathon.models.data.HotelContract.GuestEntry;


public class HotelDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = HotelDbHelper.class.getSimpleName();

    /**
     * Имя файла базы данных
     */
    private static final String DATABASE_NAME = "hote.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 49;
// а вот как текст добавить скачанный
    // ну тут как обычных базах sql
    // берешь id новости из базы вот этой:..
    //не не именно откуда достать если хочу добавить в базу активити какой
    // аа, ну я могу тебе вместе с id закидывать и текст внутри, но он будет как ссылка, имей в виду//кей спасибо
    // тебе надо скидывать текст внтури новости вместе с новостным элементом? ну когда тыкаешь именно на новость
    // ну смотри, тебе надо сохранять именно те новости ( их содержимое), в которые юзер заходил до этого, понимаешь? Тебе надо сохранять целую ссылку в базу, потом грузить ее локально
    // у меня так делается там есть fab на него тыкаешь и сохраняется /. это пока так, это даже классно, надо назвать эту кнопку Сохранить и реально сохранять новостб таким образом!
    /**
     * Конструктор {@link HotelDbHelper}.
     *
     * @param context Контекст приложения
     */
    public HotelDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + GuestEntry.TABLE_NAME + " ("

                + GuestEntry.COLUMN_TITLE + " TEXT , "
                + GuestEntry.COLUMN_CONTENT + " TEXT , "
                + GuestEntry.COLUMN_IMAGE + " TEXT , "
                + GuestEntry.COLUMN_TEXTCONTENT + " TEXT , "
                + GuestEntry.COLUMN_CREATED_AT + " TEXT );";
        //+ GuestEntry.COLUMN_TEXT + " TEXT NOT NULL, "
        // + GuestEntry.COLUMN_ID + " INTEGER NOT NULL, "


        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    /**
     * Вызывается при обновлении схемы базы даннных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
