package uhk.cz.smartnakup.utils;

import android.content.Context;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.widget.ArrayAdapter;

import uhk.cz.smartnakup.MainActivity;
import uhk.cz.smartnakup.tables.TableControllerProductDB;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{

    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        MainActivity mainActivity = ((MainActivity) context);
        // query the database based on the user input
        mainActivity.item = new TableControllerProductDB(mainActivity).getItemsFromDb(userInput.toString());

        // update the adapater
        mainActivity.myAdapter.notifyDataSetChanged();
        mainActivity.myAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_dropdown_item_1line, mainActivity.item);
        mainActivity.myAutoComplete.setAdapter(mainActivity.myAdapter);

    }

}
