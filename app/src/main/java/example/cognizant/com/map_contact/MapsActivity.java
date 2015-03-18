package example.cognizant.com.map_contact;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;



public class MapsActivity extends FragmentActivity {
    String strUrl;

    ArrayList<String>  namelist;
    ArrayList<String>  Emaillist;
    ArrayList<String>  MobileList;
    ArrayList<String>  HomeLIst;

    ArrayList<String>  LatitudeLIst;
    ArrayList<String>  Longitude;



    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        strUrl = "http://www.cs.columbia.edu/~coms6998-8/assignments/homework2/contacts/contacts.txt";


        namelist=new ArrayList<String>();
        Emaillist=new ArrayList<String>();
        MobileList=new ArrayList<String>();
        HomeLIst=new ArrayList<String>();

        LatitudeLIst=new ArrayList<String>();
        Longitude =new ArrayList<String>();


        namelist.add("Dan");
        namelist.add("John");
        namelist.add("Daniel");
        namelist.add("Johnny");
        namelist.add("Makiyo");


        Emaillist.add("dan@columbia.edu");
        Emaillist.add("john@gmail.com");
        Emaillist.add("daniel@gmail.com");
        Emaillist.add("johnny@gmail.com");
        Emaillist.add("makiyo@gmail.com");

        MobileList.add("40010787");
        MobileList.add("23079732");
        MobileList.add("37985339");
        MobileList.add("40774042");
        MobileList.add("36155618");

        HomeLIst.add("116257324");
        HomeLIst.add("79145508");
        HomeLIst.add("23716735");
        HomeLIst.add("-73959961");
        HomeLIst.add("139746094");



        String x = MobileList.get(0).substring(0, 2) + "." + MobileList.get(0).substring(2, MobileList.get(0).length());

        for (int i=0;i<MobileList.size();i++)
        {
            LatitudeLIst.add(MobileList.get(i).substring(0, 2) + "." + MobileList.get(i).substring(2, MobileList.get(i).length()));
        }

        for (int i=0;i<HomeLIst.size();i++)
        {
            Longitude.add(HomeLIst.get(i).substring(0, 2) + "." + HomeLIst.get(i).substring(2, HomeLIst.get(i).length()));
        }

        Log.d("asd","lat sise is "+LatitudeLIst.size());
        Log.d("asd","long sise is "+Longitude.size());



        for (int i=0;i<HomeLIst.size();i++)
         {
             ArrayList<ContentProviderOperation> ops =  new ArrayList<ContentProviderOperation>();
             int rawContactID = ops.size();
             ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                     .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                     .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                     .build());

             ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                     .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                     .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                     .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, namelist.get(i))
                     .build());

             ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                     .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                     .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                     .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileList.get(i))
                     .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                     .build());
             ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                     .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactID)
                     .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                     .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeLIst.get(i))
                     .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                     .build());

             // Adding insert operation to operations list
             // to insert Home Email in the table ContactsContract.Data
             ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                     .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                     .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                     .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, Emaillist.get(i))
                     .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                     .build());
             try{
                 // Executing all the insert operations as a single database transaction
                 getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                 Log.d("asd","Ooption sizy "+ops.size() );

             }catch (RemoteException e) {
                 e.printStackTrace();
             }catch (OperationApplicationException e) {
                 e.printStackTrace();
             }

         }






        // Starting the download process
//        DownloadTask downloadTask = new DownloadTask();
//        downloadTask.execute(strUrl);

        setUpMapIfNeeded();




    }
    private class DownloadTask extends AsyncTask<String, Integer, String>{
        String data = null;
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);

                Log.d("Loaded Data is ",data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            // The parsing of the xml data is done in a non-ui thread

            Log.d("Background Task 123 ",result);




        }
    }





    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
        }

        return data;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

      for (int i=0;i<HomeLIst.size();i++)
      {
          mMap.addMarker(new MarkerOptions()
                  .position(new LatLng(Double.parseDouble(LatitudeLIst.get(i)),Double.parseDouble(Longitude.get(i))))
                          .title(namelist.get(i))
                          .snippet(Emaillist.get(i)));
      }

    }
}
