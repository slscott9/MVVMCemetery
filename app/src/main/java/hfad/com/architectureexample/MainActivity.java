package hfad.com.architectureexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/*
    6. get the reference to our view model in MainActivity. We can now use the view model's database operation methods. The view model uses the repository interface to perform database operations.
 */
public class MainActivity extends AppCompatActivity {

    public static final int ADD_CEM_REQUEST = 1;
    public static final int EDIT_CEM_REQUEST = 2;



    private CemeteriesViewModel cemeteriesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnAddCemetery = findViewById(R.id.btn_add_cemetery);
        btnAddCemetery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditCemeteryActivity.class);
                startActivityForResult(intent, ADD_CEM_REQUEST); //we want to start AddCemeterActivity and get a result back. So we give this method a requestCode.
                                                                // If we started multiple activities for results we need different codes
            }
        });

        RecyclerView recyclerView = findViewById(R.id.CemeteryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final MyAdapter adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        //We do not use the new keyword because this would create a new instance everytime the activity started.
        cemeteriesViewModel = ViewModelProviders.of(this).get(CemeteriesViewModel.class);
        cemeteriesViewModel.getAllCemeteries().observe(this, new Observer<List<Cemeteries>>() {
            @Override
            public void onChanged(List<Cemeteries> cemeteries) { //called every time data in live object changes
                //update recycler view
                adapter.submitList(cemeteries); //every time data changes in main activity our adapter is updated using ListAdapter class's submitList.
            }
        });

        new ItemTouchHelper((new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) { //This method enables swipe functionality we need to specify swipe direction as params
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //We need a way to get the position that was swiped so we add another method to our cemetery adapter
                cemeteriesViewModel.delete(adapter.getCemeteryAt(viewHolder.getAdapterPosition())); //we use the adapter method which returns the position in our recycler view that was swiped
                Toast.makeText(MainActivity.this, "Cemetery deleted", Toast.LENGTH_SHORT).show();
            }
        })).attachToRecyclerView(recyclerView); //make sure to attach the swipe method to the recycler view


        adapter.setOnItemClickListener(new MyAdapter.onItemClickListener() { //When an item in the recycler view is clicked we want to start the add/edit activity passing in an intent the position in the recycler that was clicked
            @Override
            public void onItemClick(Cemeteries cemeteries) {
                Intent intent = new Intent(MainActivity.this, AddEditCemeteryActivity.class);
                intent.putExtra(AddEditCemeteryActivity.EXTRA_NAME,cemeteries.getName());
                intent.putExtra(AddEditCemeteryActivity.EXTRA_LOCATION, cemeteries.getLocation());
                //We also need to send the id in the intent so Room knows which field in the table to update
                intent.putExtra(AddEditCemeteryActivity.EXTRA_ID, cemeteries.getId());
                startActivityForResult(intent, EDIT_CEM_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_CEM_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditCemeteryActivity.EXTRA_NAME);
            String location = data.getStringExtra(AddEditCemeteryActivity.EXTRA_LOCATION); //if we were getting integers from the intent you neeed to provide a default value after the constant key name.

            Cemeteries cemeteries = new Cemeteries(name, location);
            cemeteriesViewModel.insert(cemeteries);

            Toast.makeText(this, "Cemetery saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_CEM_REQUEST && resultCode == RESULT_OK){ //
            int id = data.getIntExtra(AddEditCemeteryActivity.EXTRA_ID, -1);

            if(id == -1){
                Toast.makeText(this, "Cemetery cannot be updated", Toast.LENGTH_SHORT).show(); //need to check for invalid id
                return;
            }
            String name = data.getStringExtra(AddEditCemeteryActivity.EXTRA_NAME);
            String location = data.getStringExtra(AddEditCemeteryActivity.EXTRA_LOCATION); //if we were getting integers from the intent you neeed to provide a default value after the constant key name.

            Cemeteries cemeteries = new Cemeteries(name, location);
            cemeteries.setId(id); //if we forget this line the update will not happen because room cannot identify the cemetery position to be updated.
            cemeteriesViewModel.update(cemeteries);//update the view model

            Toast.makeText(this, "Cemetery updated", Toast.LENGTH_SHORT).show();

        } else{ //the onActivityResult is also called when the back button from AddCemeteryActivity is pressed. This time the result code will be RESULT_CANCELED
            Toast.makeText(this, "Cemetery not saved", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_cems:
                cemeteriesViewModel.deleteAllNotes();
                Toast.makeText(this, "All cemeteries deleted", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}