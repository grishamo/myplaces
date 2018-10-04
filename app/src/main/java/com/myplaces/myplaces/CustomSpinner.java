package com.myplaces.myplaces;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinner {

    List<String> categoriesList;
    int spinnerItemId;

   public CustomSpinner(){
       categoriesList = new ArrayList<>();
       categoriesList.add("Select...");
       categoriesList.add("Add New Category");

       for (MyPlace myPlace : AppManager.getInstance().getMyPlaces()) {
           if(!(categoriesList.contains(myPlace.getCategory()))) {
               categoriesList.add(myPlace.getCategory());
           }
       }

       categoriesList.add("California");
       categoriesList.add("Bars");
       categoriesList.add("Museums");

       spinnerItemId = R.layout.spinner_item;
   }

    public void populate(final Context context, Spinner spinnerId){
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, spinnerItemId, categoriesList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(spinnerItemId);
        spinnerId.setAdapter(spinnerArrayAdapter);

        spinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position).toString();

                // If user change the default selection
                // First item is disable and it is used for hint
                if(selectedItem.equals("Add New Category")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater li = LayoutInflater.from(context);

                    View dialogView = li.inflate(R.layout.new_category_dialog, null);

                    final EditText newCategoryEt = dialogView.findViewById(R.id.new_category_et);
                    builder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newCategoryStr = newCategoryEt.getText().toString();

                                if(!(categoriesList.contains(newCategoryStr))) {
                                    categoriesList.add(newCategoryStr);
                                }
                                parent.setSelection(categoriesList.size()-1);
                        }
                    }).show();

                //    builder.setView(dialogView).setNegativeButton("Cancel");
                }

                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (context, "Selected : " + selectedItem, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
