package cpe.dna;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by anthony on 2/17/18.
 */

public class FragmentMap extends Fragment {
    private static final String TAG = "TAB2";

    GridView androidGridView;

    String[] gridViewString = {
            "NAVIGATION", "GAS STATIONS"

    } ;

    int[] gridViewImageId = {
            R.drawable.ic_place_black_24dp, R.drawable.ic_local_gas_station_black_24dp

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview, container, false);


        CustomGridView adapterViewAndroid = new CustomGridView(getContext(), gridViewString, gridViewImageId);
        androidGridView = (GridView) view.findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                //Toast.makeText(getContext(), "GridView Item: " + gridViewString[+i], Toast.LENGTH_SHORT).show();

                if (gridViewString[+i].equals("NAVIGATION")){
                    Intent intentParameters = new Intent(getContext(),NavigationActivity.class);
                    startActivity(intentParameters);
                }
                else{
                    Toast.makeText(getContext(),"Sorry!The page you're trying to access is under construction.",Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
