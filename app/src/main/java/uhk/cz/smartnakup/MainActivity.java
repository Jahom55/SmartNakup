package uhk.cz.smartnakup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

import uhk.cz.smartnakup.db.ObjectCart;
import uhk.cz.smartnakup.db.ObjectProduct;
import uhk.cz.smartnakup.controllers.TableControllerProductCart;
import uhk.cz.smartnakup.controllers.TableControllerProductDB;
import uhk.cz.smartnakup.utils.CustomAutoCompleteTextChangedListener;
import uhk.cz.smartnakup.utils.CustomAutoCompleteView;
import uhk.cz.smartnakup.utils.InternetUtilsForFirebase;
import uhk.cz.smartnakup.utils.validation.ProductNameValidator;
import uhk.cz.smartnakup.utils.validation.QuantityValidator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public CustomAutoCompleteView myAutoComplete;
    public ArrayAdapter<String> myAdapter;
    public String[] item = new String[]{""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionBtnAddtoCart(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        countRecords();
        readRecords();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            synchronizateProductsTables(MainActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.db) {
            Intent intent = new Intent(this, ProductDBActivity.class);
            startActivity(intent);
        } else if (id == R.id.db_plan) {
            Intent intent = new Intent(this, ProductDBPlanActivity.class);
            startActivity(intent);
        } else if (id == R.id.mapButton) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.testKompasButton) {
            Intent intent = new Intent(this, TestKompas.class);
            startActivity(intent);
        } else if (id == R.id.kontakt) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"homolkajaromir@seznam.cz"});
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.commentAndroidApp);
            try {
                startActivity(Intent.createChooser(i, "Send message to developer"));
            } catch (android.content.ActivityNotFoundException ex) {
            }

        } else if (id == R.id.kontaktEmail) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.shoppingList);
            i.putExtra(Intent.EXTRA_TEXT, getStringFromTableCart());
            try {
                startActivity(Intent.createChooser(i, "Send email to friend"));
            } catch (android.content.ActivityNotFoundException ex) {
            }
        } else if (id == R.id.kontaktFb) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent
                    .putExtra(Intent.EXTRA_TEXT, getStringFromTableCart());
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.facebook.orca");
            try {
                startActivity(sendIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, R.string.haventMessenger, Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void actionBtnToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void actionClearCartList(View view) {
        final Context context = view.getContext();
        new AlertDialog.Builder(context)
                .setTitle(R.string.titleClearCart_mainactivity)
                .setMessage(R.string.messageClearCart_mainactivity)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean deleteSuccess = new TableControllerProductCart(context).deleteTable();
                                if (deleteSuccess) {
                                    Toast.makeText(context, R.string.cartClearCartSuccesful_mainactivity, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, R.string.cartClearCartUnsuccesful_mainactivity, Toast.LENGTH_SHORT).show();
                                }
                                readRecords();
                                countRecords();
                                dialog.cancel();
                            }

                        })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                }).show();

    }

    public void actionBtnAddtoCart(View view) {
        final Context context = view.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.productcart_input_form, null, false);
        myAutoComplete = (CustomAutoCompleteView) formElementsView.findViewById(R.id.autocomplete);
        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(context));
        myAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, item);
        myAutoComplete.setAdapter(myAdapter);
        final EditText mnozstvi = (EditText) formElementsView.findViewById(R.id.editTextQuantity);

        mnozstvi.addTextChangedListener(new QuantityValidator(mnozstvi));
        myAutoComplete.addTextChangedListener(new ProductNameValidator(myAutoComplete, context));

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle(R.string.titleFormAddtoCart_mainactiviy)
                .setPositiveButton(R.string.create,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (!QuantityValidator.isValidQuantity(mnozstvi.getText().toString()) || !ProductNameValidator.isValidProduct(myAutoComplete.getText().toString(), context)) {
                                    Toast.makeText(MainActivity.this, R.string.pleaseFillCorrectValues, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String productName = myAutoComplete.getText().toString();
                                int quantity = Integer.parseInt(mnozstvi.getText().toString());


                                ObjectProduct objectProduct1 = new TableControllerProductDB(context).readSingleRecordByName(productName);

                                ObjectCart objectCart = new ObjectCart();
                                objectCart.setProduct(objectProduct1.getId());
                                objectCart.setQuantity(quantity);
                                objectCart.setBought(0);

                                boolean createSuccessful = new TableControllerProductCart(context).create(objectCart);

                                if (createSuccessful) {
                                    Toast.makeText(context, R.string.cartAddSuccesful_mainactivity, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, R.string.cartAddUnsuccesful_mainactivity, Toast.LENGTH_SHORT).show();
                                }
                                readRecords();
                                countRecords();
                                dialog.cancel();
                            }

                        }).show();

    }

    private void synchronizateProductsTables(Context context1) {
        final Context context = context1;
        new AlertDialog.Builder(context)
                .setTitle(R.string.titleSynchronizate_mainactivity)
                .setMessage(R.string.messageSynchronizate_mainactivity)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (InternetUtilsForFirebase.haveInternet(context)) {
                                    final TableControllerProductDB controller = new TableControllerProductDB(context);
                                    controller.deleteTable();
                                    new SynchronizateProductsFromFB().execute(context);
                                    dialog.cancel();
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.noInternetNoSynchnization, Toast.LENGTH_SHORT).show();

                                }
                            }

                        })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    private String getStringFromTableCart() {
        TableControllerProductCart controller = new TableControllerProductCart(this);
        TableControllerProductDB controllerPro = new TableControllerProductDB(this);
        List<ObjectCart> products = controller.read();
        StringBuilder stringB = new StringBuilder();
        if (products.isEmpty()) {
            return "Nic v kosiku.";
        } else {
            stringB.append("Ahoj!\n" +
                    "Kup prosím tyto věci: \n");
            for (ObjectCart cart : products) {
                stringB.append(controllerPro.readSingleRecord(cart.getProduct()).getName());
                stringB.append(" ");
                stringB.append(cart.getQuantity());
                stringB.append("x");
                stringB.append("\n");
            }
            stringB.append("Díky!");
        }
        return stringB.toString();


    }

    public void countRecords() {
        int recordCount = new TableControllerProductCart(this).count();
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        textViewRecordCount.setText(recordCount + " položek v nákupním seznamu");
    }

    public void readRecords() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();

        List<ObjectCart> products = new TableControllerProductCart(this).read();
        TableControllerProductDB dbController = new TableControllerProductDB(this);

        if (products.size() > 0) {
            for (ObjectCart obj : products) {

                int id = obj.getId();
                String productName = dbController.readSingleRecord(obj.getProduct()).getName();
                int quantity = obj.getQuantity();

                LinearLayout ll = new LinearLayout(getApplicationContext());
                ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                String nameOfCartItem = productName;
                TextView textViewProductNameItem = new TextView(this);
                textViewProductNameItem.setPadding(0, 10, 0, 10);
                textViewProductNameItem.setText(nameOfCartItem);
                textViewProductNameItem.setTag(Integer.toString(id));
                textViewProductNameItem.setMinimumWidth(250);

                TextView textViewProductQuantity = new TextView(this);
                textViewProductQuantity.setPadding(0, 10, 0, 10);
                textViewProductQuantity.setText(quantity + " kusů");
                textViewProductQuantity.setTag(Integer.toString(id));
                textViewProductQuantity.setMinimumWidth(150);

                CheckBox chYes = new CheckBox(this);
                chYes.setTag(Integer.toString(id));
                chYes.setChecked(obj.getBought() == 1);
                chYes.setOnCheckedChangeListener(OnCheckedChangeListener());

                textViewProductNameItem.setOnLongClickListener(OnlongClickProductCartListener());

                ll.addView(textViewProductNameItem);
                ll.addView(textViewProductQuantity);
                ll.addView(chYes);
                linearLayoutRecords.addView(ll);
            }
        } else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText(R.string.noCartItems_mainactivity);
            linearLayoutRecords.addView(locationItem);
        }

    }

    private CompoundButton.OnCheckedChangeListener OnCheckedChangeListener() {

        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String id = buttonView.getTag().toString();
                ObjectCart objectCart = new TableControllerProductCart(buttonView.getContext()).readSingleRecord(Integer.parseInt(id));
                if (buttonView.isChecked()) objectCart.setBought(1);
                if (!buttonView.isChecked()) objectCart.setBought(0);
                new TableControllerProductCart(buttonView.getContext()).update(objectCart);

            }
        };
        return changeListener;
    }

    private View.OnLongClickListener OnlongClickProductCartListener() {
        View.OnLongClickListener clickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final Context context = v.getContext();
                final String id = v.getTag().toString();
                final CharSequence[] items = {"Editovat", "Smazat"};

                new AlertDialog.Builder(context).setTitle(R.string.titleFormEditCart_mainactivity)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    editRecord(Integer.parseInt(id), context);
                                } else if (item == 1) {
                                    boolean deleteSuccessful = new TableControllerProductCart(context).delete(Integer.parseInt(id));
                                    if (deleteSuccessful) {
                                        Toast.makeText(context, R.string.cartDeleteSuccesful_mainactivity, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, R.string.cartDeleteUnsuccesful_mainactivity, Toast.LENGTH_SHORT).show();
                                    }

                                    countRecords();
                                    readRecords();
                                }
                                dialog.dismiss();

                            }
                        }).show();


                return false;
            }

        };

        return clickListener;
    }


    public void editRecord(final int cartId, final Context context) {
        final TableControllerProductCart tableControllerProductCart = new TableControllerProductCart(context);
        final ObjectCart objectCart = tableControllerProductCart.readSingleRecord(cartId);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.productcart_input_form, null, false);
        myAutoComplete = (CustomAutoCompleteView) formElementsView.findViewById(R.id.autocomplete);
        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(context));
        myAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, item);
        myAutoComplete.setAdapter(myAdapter);
        final EditText quantity = (EditText) formElementsView.findViewById(R.id.editTextQuantity);
        ObjectProduct objectProduct = new TableControllerProductDB(context).readSingleRecord(objectCart.getProduct());
        myAutoComplete.setText(objectProduct.getName());
        quantity.setText(String.valueOf(objectCart.getQuantity()));
        myAutoComplete.addTextChangedListener(new ProductNameValidator(myAutoComplete, context));
        quantity.addTextChangedListener(new QuantityValidator(quantity));
        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle(R.string.titleEditFormCart_mainactivity)
                .setPositiveButton(R.string.save,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (!QuantityValidator.isValidQuantity(quantity.getText().toString()) || !ProductNameValidator.isValidProduct(myAutoComplete.getText().toString(), context)) {
                                    Toast.makeText(MainActivity.this, R.string.pleaseFillCorrectValues, Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                ObjectCart objectCart1 = new ObjectCart();
                                objectCart1.setId(cartId);
                                ObjectProduct objectProduct1 = new TableControllerProductDB(context).readSingleRecordByName(myAutoComplete.getText().toString());
                                objectCart1.setProduct(objectProduct1.getId());
                                objectCart1.setQuantity(Integer.parseInt(quantity.getText().toString()));
                                objectCart1.setBought(objectCart.getBought());

                                dialog.cancel();

                                boolean updateSuccessful = tableControllerProductCart.update(objectCart1);

                                if (updateSuccessful) {
                                    Toast.makeText(context, R.string.cartEditFormSuccesful_mainactivity, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, R.string.cartEditFormUnsuccesful_mainactivity, Toast.LENGTH_SHORT).show();
                                }
                                countRecords();
                                readRecords();
                            }

                        }).show();

    }

    private class SynchronizateProductsFromFB extends AsyncTask<Context, Void, String> {

        @Override
        protected String doInBackground(final Context... params) {
            Firebase.setAndroidContext(params[0]);
            Firebase mRef = new Firebase("https://brilliant-torch-5232.firebaseio.com/products");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot productsSnap : dataSnapshot.getChildren()) {
                        ObjectProduct prod = productsSnap.getValue(ObjectProduct.class);
                        prod.setId(prod.getId());
                        new TableControllerProductDB(params[0]).createWithId(prod);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this, R.string.toastSynchronizate_mainactivityAsync, Toast.LENGTH_SHORT).show();
        }
    }
}





