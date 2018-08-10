package cpe.dna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReadCodes extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    private ListView list;
    private ListViewAdapter adapter;
    private SearchView editsearch;
    private String[] moviewList;
    public static ArrayList<codes> movieNamesArrayList = new ArrayList<codes>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_codes);

        // Generate sample data

        moviewList = new String[]{"DTC's " ,
                "Fuel system status " ,
                "Calculated Engine Load" ,
                "Engine Coolant temperature" ,
                "Short trim-bank 1" ,
                "Short trim-bank 2" ,
                "Intake Manifold absolute pressure" ,
                "Engine RPM" ,
                "Vehicle speed" ,
                "Timing advance" ,
                "Intake air temperature" ,
                "MAF air flow rate" ,
                "Throttle position" ,
                "Oxygen sensors present" ,
                "Oxygen sensor 1" ,
                "OBD standards" ,
                "Run time since engine starts" ,
                "PIDs supported [21 \u00AD 40]" ,
                "Distance traveled MIL on" ,
                "Commanded evaporative purge " ,
                "Warm-ups since codes cleared" ,
                "Distance traveled since codes cleard" ,
                "Absolute barometric pressure" ,
                "PIDs supported [41 \u00AD 60]" ,
                "Monitor status this drive cycle" ,
                "Control module voltage" ,
                "Absolute load voltage" ,
                "Fuel air commanded equivalence ratio" ,
                "Relative throttle position" ,
                "Ambient air temperature" ,
                "Absolute throttle positions B" ,
                "Accelator pedal position C" ,
                "Accelator pedal position D" ,
                "Commanded throttle actuator" ,
                "Time run with MIL on" ,
                "Time since trouble codes cleared" ,
                "Maximum value for Fuel–Air equivalence ratio, oxygen sensor voltage, oxygen sensor current, and intake manifold absolute pressure" ,
                "Fuel type" ,
                "Relative accelerator" ,
                "Fuel injection timing" ,
                "PIDs supported [61 \u00AD 80]",
                "Auxiliary input / output supported",
                "Commanded EGR and EGR Error ",
                "Commanded Diesel intake air flow control and relative intake air flow position",
                "Fuel pressure control system",
                "Boost pressure control",
                "Variable Geometry turbo (VGT) control",
                "Charge air cooler temperature (CACT)"};

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        movieNamesArrayList = new ArrayList<>();

        for (int i = 0; i < moviewList.length; i++) {
            codes movieNames = new codes(moviewList[i]);
            // Binds all strings into an array
            movieNamesArrayList.add(movieNames);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ReadCodes.this, movieNamesArrayList.get(position).getAnimalName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}
