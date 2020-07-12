package hfad.com.architectureexample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/*
    5. We have created the entity(database table), DAO, repository, now we create the view model. It is a part of the android architecture components library. View model stores and procesess data for the user interface
    and communicate with the repository. It requests data from the repository so the activity or fragment can draw it onto the screen and passes action from the ui back to the repository.
    The view model keeps its data so when the activity is destroyed the view model still contains data. The activity just retreives the view model instance and all its data is restored to previous state.

    Remember the View model doesn't have to worry about how the repository interface works it just calls the methods to do operations on the database. The repository interface does the rest.
 */

public class CemeteriesViewModel extends AndroidViewModel {

    private CemeteriesRepository repository;
    private LiveData<List<Cemeteries>> allCemeteries;

    public CemeteriesViewModel(@NonNull Application application) {
        super(application);
        repository = new CemeteriesRepository(application); //instantiate our objects
        allCemeteries = repository.getAllCemeteries(); //populate the liveData list with data from our Cemetery repository class(the abstraction layer)
    }

    //Activity later only has a reference to the  ViewModel not to the repository so we create wrapper methods for our database operation methods from our repository

    public void insert(Cemeteries cemeteries){
        repository.insert(cemeteries);
    }

    public void update(Cemeteries cemeteries){
        repository.update(cemeteries);
    }

    public void delete(Cemeteries cemeteries){
        repository.delete(cemeteries);
    }

    public void deleteAllNotes(){
        repository.deleteAllNotes();
    }

    public LiveData<List<Cemeteries>> getAllCemeteries(){
        return allCemeteries;
    }
}
