package hfad.com.architectureexample;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
    1. This is the Entity which is the java class that represents the sqlite table (this is like our openHelper class) @Entity tag is how java creates all the boiler plate code normally written in openHelper class
    We create our sqlite table specifying columns and implement constructor, getter and setter methods for the class members
 */

@Entity(tableName = "cemeteries") // 1. create table name
public class Cemeteries {

    //2. This is the same as our sqlite query  execSql that creates the table
    @PrimaryKey(autoGenerate = true) //this means with each new row we add to the table an _id number is generated for it. row  _id 1, row 2, row 3
    private int id; //our _id column

    //Create the columns for table. Room automatically generates these for us since the @Entity tag is used.
    private String name;
    private String location;
    //private int priority; //each sql lite table needs a primary key, this way we can uniquely identify each field
    //@ColumnInfo(name = "priority_column") This is how we would change a column name in our table.


    //For room to persist these columns we need getter and setter methods so we can set and access them. generate
    //We do not need to set id because this will auto increment whenever we add data

    public Cemeteries(String name, String location) { //Room needs a Constructor so it can recreate these variables from data base
        //this.priority = priority;
        this.name = name;
        this.location = location;
    }

    public void setId(int id) { //need the setter for the id since it was not set in our constructor
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //public int getPriority() {
        //return priority;
    //}

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
