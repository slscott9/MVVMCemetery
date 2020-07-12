package hfad.com.architectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditCemeteryActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "hfad.com.architectureexample.EXTRA_NAME";
    public static final String EXTRA_LOCATION = "hfad.com.architectureexample.EXTRA_LOCATION";
    public static final String EXTRA_ID = "hfad.com.architectureexample.EXTRA_ID";

    private EditText etName;
    private EditText etLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cemetery);

        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){ //check if the intent has the position in the recycler view that was selected. If so we want to set the title to update. If no id then set title to add
            setTitle("Edit cemetery");
            //if we have an id in the intent that was passed we need to get the cemetery at the position in the recycler view's edit text data so the user can see what to update.
            etName.setText(intent.getStringExtra(EXTRA_NAME));
            etLocation.setText(intent.getStringExtra(EXTRA_LOCATION));

        }else{
            setTitle("Add Cemetery");

        }
    }

    private void saveCemetery(){
        String name = etName.getText().toString(); //extract data from the views
        String location = etLocation.getText().toString();

        if(name.trim().isEmpty() || location.trim().isEmpty()){ //Check if they are empty
            Toast.makeText(this, "Please insert a name and location", Toast.LENGTH_SHORT).show();
            return;
        }
        //startActivityForResult method lets us start this activity and send data back with it to main activity
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_LOCATION, location);

        int id = getIntent().getIntExtra(EXTRA_ID, -1); // we need to get the id of the cemetery that was selected in main activity. -1 would mean it was an invalid id. we need to check for this
        if(id != -1){
            data.putExtra(EXTRA_ID, id);  //We need to pass the id of the cemetery that was updated to main activity knows which cemetery to update
                                        //if we have valid id put the id in the intent to send back to main activity to update
        }


        setResult(RESULT_OK, data); //if the user entered information into the edit text fields we send an intent with the information back to main activity to display.
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_cemetery_menu, menu);
        return true; //if we return false it will not show the menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_cemetery:
                saveCemetery();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}