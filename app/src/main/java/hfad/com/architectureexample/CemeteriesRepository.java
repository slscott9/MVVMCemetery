package hfad.com.architectureexample;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/*
    4. Repository class is a simple java class and is not from the special architecture components libraries. It is best practice because it provides another abstraction layer
    between the different data sources and the app. In this example we only have one data source the sqlite database. In the real world you would have another data source from the internet and cache it locally
    in the sqlite database. This means you need a way to mediate between these different data sources and decide when you fetch data from them.

    The repository is another way to abstract your data source operations and provide a clean api to the rest of the app. So the view model does not have to care where the data comes from
    or how it is fetched it only has to call methods on the repository directory
 */
public class CemeteriesRepository {

    private CemeteriesDAO cemeteriesDAO;
    private LiveData<List<Cemeteries>> allCemeteries;

    public CemeteriesRepository(Application application){ //This constructor is called when we want to create a database instance in the activity. We can pass it an Application object because activities are subclass
                                                    //of the Context class. We then use this parameter to create an instance of our database.
        CemeteriesDatabase database = CemeteriesDatabase.getInstance(application);
        cemeteriesDAO = database.cemeteriesDAO();
        allCemeteries = cemeteriesDAO.getAllCemeteries();
    }

    public void insert(Cemeteries cemetery){
        new InsertCemeteryAsyncTask(cemeteriesDAO).execute(cemetery); //These methods are the app interface that the repository exposes to the outside. so our view model only has to call these methods
    }                                                                   // and doesn't have to care what is happening and all the data is fetched with our a sync tasks. This repository is the abstraction layer

    public void update(Cemeteries cemetery){
        new UpdateCemeteryAsyncTask(cemeteriesDAO).execute(cemetery);
    }

    public void delete(Cemeteries cemetery){
        new DeleteCemeteryAsyncTask(cemeteriesDAO).execute(cemetery);
    }

    public void deleteAllNotes(){
        new DeleteAllCemeteriesAsyncTask(cemeteriesDAO).execute();
    }

    public LiveData<List<Cemeteries>> getAllCemeteries() {
        return allCemeteries;
    }

    //Room does not allow you to run tasks on the main thread so we have to create multiple AsyncTasks for each data base operation
    private static class InsertCemeteryAsyncTask extends AsyncTask<Cemeteries, Void, Void>{

        private CemeteriesDAO cemeteriesDAO;

        private InsertCemeteryAsyncTask(CemeteriesDAO cemeteriesDAO){ //cannot access dao directly so we must pass it through a constructor
            this.cemeteriesDAO = cemeteriesDAO;
        }

        @Override
        protected Void doInBackground(Cemeteries... cemetery) {
            cemeteriesDAO.insert(cemetery[0]);
            return null;
        }
    }

    private static class UpdateCemeteryAsyncTask extends AsyncTask<Cemeteries, Void, Void>{

        private CemeteriesDAO cemeteriesDAO;

        private UpdateCemeteryAsyncTask(CemeteriesDAO cemeteriesDAO){
            this.cemeteriesDAO = cemeteriesDAO;
        }

        @Override
        protected Void doInBackground(Cemeteries... cemeteries) {
            cemeteriesDAO.update(cemeteries[0]);
            return null;
        }
    }

    private static class DeleteCemeteryAsyncTask extends AsyncTask<Cemeteries, Void, Void>{

        private CemeteriesDAO cemeteriesDAO;

        private DeleteCemeteryAsyncTask(CemeteriesDAO cemeteriesDAO){
            this.cemeteriesDAO = cemeteriesDAO;
        }

        @Override
        protected Void doInBackground(Cemeteries... cemeteries) {
            cemeteriesDAO.delete(cemeteries[0]);
            return null;
        }
    }

    private static class DeleteAllCemeteriesAsyncTask extends AsyncTask<Void, Void, Void>{

        private CemeteriesDAO cemeteriesDAO;

        private DeleteAllCemeteriesAsyncTask(CemeteriesDAO cemeteriesDAO){
            this.cemeteriesDAO = cemeteriesDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cemeteriesDAO.deleteAllCemeteries();
            return null;
        }
    }
}
