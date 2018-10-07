package com.myplaces.myplaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

public class SpinnerData {
    private List<String> categoriesList;
    private int spinnerItemId;
    private String selectedItemStr;
    private Spinner spinner;
    private AppManager appManager;

    public SpinnerData() {
        categoriesList = new ArrayList<>();
        appManager = AppManager.getInstance();

        categoriesList.add(App.getContext().getResources().getString(R.string.selected_category));
        categoriesList.add(App.getContext().getResources().getString(R.string.new_category));

        List<String> existingCategories = appManager.getCategoriesList();
        categoriesList.addAll(existingCategories);

        spinnerItemId = R.layout.spinner_item;
    }

    public void populate(final Context context, Spinner spinnerId) {
        spinner = spinnerId;
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
                String selectedItem = parent.getItemAtPosition(position).toString();

                // If user change the default selection
                // First item is disable and it is used for hint
                if (selectedItem.equals("Add New Category")) {
                    ShowAddCategoryDialog(context, parent);
                }

                if (position > 0) {
                    selectedItemStr = selectedItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void ShowAddCategoryDialog(Context context, final AdapterView<?> parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.new_category_dialog, null);

        final EditText newCategoryEt = dialogView.findViewById(R.id.new_category_et);
        builder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCategoryStr = newCategoryEt.getText().toString();

                if (!(categoriesList.contains(newCategoryStr))) {
                    categoriesList.add(newCategoryStr);
                    if(!appManager.getCategoriesList().contains(newCategoryStr))
                    {
                        appManager.getCategoriesList().add(newCategoryStr);
                    }
                }
                parent.setSelection(categoriesList.size() - 1);
                selectedItemStr = parent.getSelectedItem().toString();
            }
        }).show();

        //    builder.setView(dialogView).setNegativeButton("Cancel");
    }

    public String getSelectedItemStr() {
        return selectedItemStr;
    }

    public void selectItem(String category) {
        if (category != null) {
            int position = categoriesList.indexOf(category);
            if (position != -1) {
                spinner.setSelection(position);
            }
        }
    }
}
