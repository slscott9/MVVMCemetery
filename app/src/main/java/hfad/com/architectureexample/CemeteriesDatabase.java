package hfad.com.architectureexample;

import android.content.Context;
import android.os.AsyncTask;
import android.text.NoCopySpan;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/*
    4. We have our Entity(database table) and our DAO. We now create another class for the Room database itself
    This class is for the actual Room database contains an instance of it
 */
@Database(entities = {Cemeteries.class}, version = 2) //this is like the sqlite paramters in the execSql() method. Tell it the entities and the version number
public abstract class CemeteriesDatabase extends RoomDatabase {

    /*
        We create this instance because we have to turn this class into a singleton. Singleton means we can not create multiple instances of this database.
     */
    private static CemeteriesDatabase instance;

    public abstract CemeteriesDAO cemeteriesDAO(); //we will use this method to access our DAO. Even though it is abstract we can still use it immediately because Room takes care of this code for us.

    /*
        Now we create our database which is a singleton
        synchronized means only one thread at a time can access this method. This is so you don't accidentally create two instances of this database.
        .fallbackToDestructiveMigration() - if we change update version number of database then this function will just create a new database.
        .build() returns an instance of this database
     */
    public static synchronized CemeteriesDatabase getInstance(Context context){ //returns a CemeteriesDatabase instance so we can access its methods
        if(instance == null){ //We only want to instantiate this database if we don't already have an instance
            instance = Room.databaseBuilder(context.getApplicationContext(), CemeteriesDatabase.class, "cemeteries").fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return instance;
    }


    //This is how we populate the database right when we create it. It is the same as the openHelper class's onCreate where we added data on spot

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CemeteriesDAO cemeteriesDAO;

        private PopulateDbAsyncTask(CemeteriesDatabase db){ //we don't have an instance of our cemetaryDao so we pass our database instance, and get a cemeteriesDao instance from it.
            cemeteriesDAO = db.cemeteriesDAO();
        }



        @Override
        protected Void doInBackground(Void... voids) {
            cemeteriesDAO.insert(new Cemeteries("Cemetery 1", "Jemison"));
            cemeteriesDAO.insert(new Cemeteries("Cemetery 2", "Maplesville"));
            cemeteriesDAO.insert(new Cemeteries("Cemetery 3", "Isabella"));
            cemeteriesDAO.insert(new Cemeteries("Cemetery 4", "Thorsby"));

            return null;
        }
    }
}
