package uhk.cz.smartnakup.utils.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Jaromir on 13.5.2016.
 */
public class QuantityValidator implements TextWatcher {
    EditText quantity;

    public QuantityValidator(EditText quantity) {
        this.quantity = quantity;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String quantityString = quantity.getText().toString();
        if (!isValidQuantity(quantityString)) {
            quantity.setError("Invalid Quantity");
        }

    }

    public static boolean isValidQuantity(String quantity) {
        if (quantity != null && quantity.length() > 0) {
            return true;
        }
        return false;
    }



}
