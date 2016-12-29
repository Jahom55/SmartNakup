package uhk.cz.smartnakup.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import uhk.cz.smartnakup.db.ObjectProduct;

/**
 * Created by Jaromir on 6.5.2016.
 */
public class InternetUtilsForFirebase {

    public static boolean haveInternet(Context ctx) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        return true;
    }

    public static void deleteProduct(Firebase mRef, final String id){
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ObjectProduct post = postSnapshot.getValue(ObjectProduct.class);
                    if (post.getId() == Integer.parseInt(id)) {
                        Firebase deleteUrl = new Firebase(postSnapshot.getRef().toString());
                        deleteUrl.removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
