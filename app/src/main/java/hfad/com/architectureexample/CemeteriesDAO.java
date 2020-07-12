package hfad.com.architectureexample;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
/*
    3. We have created the Cemeteries Entity(database table) the next step is to create the corresponding dao.
    The Dao is where we define all the database operations we want to make on the cemetery entity (the database table)
    Create the DAO data access object (interface) must be interface or abstract class because we don’t provide method body we
    just create methods and annotate it and room will generate necessary code for us.
 */
@Dao
public interface CemeteriesDAO {

    @Insert //annotate with corresponding database operation using the @ tag room will automatically generate what needs to be made for this method
    void insert(Cemeteries cemetery); //passing a Cemetery table entity

    @Update
    void update(Cemeteries cemetery);

    @Delete
    void delete(Cemeteries cemetery);

    //When there is no annotation for the method we use @Query to define our own database operation
    //deletes all cemeteries using @Query annotation is used when there is not an annotation room can auto generate for us. This is like a custom method we made in openHelper database class
    //Notice DELETE FROM cemeteries means delete the whole table.
    @Query("DELETE FROM cemeteries")
    void deleteAllCemeteries();

    //Instead of getting a cursor back with our data Room can turn the result directly into our own Cemeteries java object (a list of Cemeteries objects)
    @Query("SELECT * FROM cemeteries ORDER BY name DESC") //Create our own Query select * means all columns then order by descending order.
    LiveData<List<Cemeteries>> getAllCemeteries(); //returns a List of Notes. LiveData means as soon as the Note table is updated/changed our activity is notified. Room takes care of all the things to update this
                                        //Live data object. We do not have to implement update methods when data changes we just use LiveData and room takes care of it.
}
