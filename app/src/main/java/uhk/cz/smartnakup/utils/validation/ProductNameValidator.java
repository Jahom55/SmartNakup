package uhk.cz.smartnakup.utils.validation;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import uhk.cz.smartnakup.R;
import uhk.cz.smartnakup.db.ObjectProduct;
import uhk.cz.smartnakup.controllers.TableControllerProductDB;
import uhk.cz.smartnakup.utils.CustomAutoCompleteView;

/**
 * Created by Jaromir on 13.5.2016.
 */
public class ProductNameValidator implements TextWatcher {
    CustomAutoCompleteView product;
    Context context;

    public ProductNameValidator(CustomAutoCompleteView product, Context context) {
        this.product = product;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String productString = product.getText().toString();
        if (!isValidProduct(productString, context)) {
            product.setError("Invalid product name");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static boolean isValidProduct(String name, Context context) {
        ObjectProduct objectProduct = new TableControllerProductDB(context).readSingleRecordByName(name);
        if (objectProduct != null) {
            return true;
        }
        return false;
    }
}
