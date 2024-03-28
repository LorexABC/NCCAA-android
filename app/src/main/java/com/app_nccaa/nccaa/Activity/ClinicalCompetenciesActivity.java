package com.app_nccaa.nccaa.Activity;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app_nccaa.nccaa.Utils.FileDownloader;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Adapter.AdapterClinical;
import com.app_nccaa.nccaa.Model.CasesModel;
import com.app_nccaa.nccaa.Model.CategoriesModel;
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Student Clinical Competency Activity
 *
 * Modified by phantom
 * On 03/22/24
 * Category detail's modal is created.
 */
public class ClinicalCompetenciesActivity extends AppCompatActivity {

    private RecyclerView rv_case;
    private AdapterClinical adapterClinical;
    private EditText edt_search;
    private LinearLayout no_data_ll;

    private ArrayList<CasesModel> casesModelArrayList = new ArrayList<>();

    private UserSession session;

    private JSONArray jsonArray;
    private Button btn_completed;
    private Button btn_simulations;
    private Button btn_export;

    //for cases count
    private TextView tv_1, tv_2, tv_4, tv_5;

    private boolean completed = false, canCompleteClinicals = false;
    private RequestQueue requestQueue;
    private KProgressHUD progressDialogDwnld;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinical_competencies);

        session = new UserSession(ClinicalCompetenciesActivity.this);

        Log.e("token", session.getAPITOKEN());


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        requestQueue = Volley.newRequestQueue(ClinicalCompetenciesActivity.this);//Creating the RequestQueue


        edt_search = findViewById(R.id.edt_search);
        no_data_ll = findViewById(R.id.no_data_ll);
        btn_completed = findViewById(R.id.btn_completed);
        btn_simulations = findViewById(R.id.simulations);
        btn_export = findViewById(R.id.btn_export);

        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_4 = findViewById(R.id.tv_4);
        tv_5 = findViewById(R.id.tv_5);


        rv_case = findViewById(R.id.rv_case);
        rv_case.setLayoutManager(new LinearLayoutManager(this));
        adapterClinical = new AdapterClinical(this, casesModelArrayList, new AdapterClinical.OnItemClickListener() {
            @Override
            public void onItemEdit(int pos) {

                try {

                    if (!completed) {
                        startActivity(new Intent(ClinicalCompetenciesActivity.this, EditCompetenciesActivity.class)
                                .putExtra("id", casesModelArrayList.get(pos).getId())
                                .putExtra("title", casesModelArrayList.get(pos).getTitle())
                                .putExtra("date", casesModelArrayList.get(pos).getDate())
                                .putExtra("start_time", casesModelArrayList.get(pos).getStart_time())
                                .putExtra("end_time", casesModelArrayList.get(pos).getEnd_time())
                                .putExtra("duration", casesModelArrayList.get(pos).getDuration())
                                .putExtra("age", casesModelArrayList.get(pos).getAge())
                                .putExtra("asa", casesModelArrayList.get(pos).getAsa())
                                .putExtra("category", String.valueOf(jsonArray.getJSONObject(pos).getJSONArray("category")))
                                .putExtra("fromClinical", "EditCase"));
                    } else {
                        Toast.makeText(ClinicalCompetenciesActivity.this, "completed!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        rv_case.setAdapter(adapterClinical);


        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!completed) {
                    startActivity(new Intent(ClinicalCompetenciesActivity.this, CreateCompetenciesActivity.class)
                            .putExtra("fromClinical", "CreateCase"));
                } else {
                    Toast.makeText(ClinicalCompetenciesActivity.this, "completed!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.add_case).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!completed) {
                    startActivity(new Intent(ClinicalCompetenciesActivity.this, CreateCompetenciesActivity.class)
                            .putExtra("fromClinical", "CreateCase"));
                } else {
                    Toast.makeText(ClinicalCompetenciesActivity.this, "completed!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (canCompleteClinicals){
                    completeClinicals();
                } else {
                    Toast.makeText(ClinicalCompetenciesActivity.this, "Not eligible!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });


        btn_simulations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                selectImageAndDocument();
                startActivity(new Intent(ClinicalCompetenciesActivity.this, Simulation.class));
            }
        });

        findViewById(R.id.btn_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions()){

                    DownloadData();


                }else {
                    Toast.makeText(ClinicalCompetenciesActivity.this, "Go to Setting and Approve Storage Permission", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * Download Document for certificate feature
     */
    private class DownloadFileSecond extends AsyncTask<String, Void, Void> {

        File dir = null;

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            //    File folder = new File(extStorageDirectory, "Snagpay");

            if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
                dir = new File(Environment.getExternalStorageDirectory().getPath()
                        + "//"+getResources().getString(R.string.app_name));
            } else {
                dir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath()
                        + "//"+getResources().getString(R.string.app_name));
            }

            if (!dir.exists())
                dir.mkdir();

            File pdfFile = new File(dir, fileName);

            try{
                pdfFile.createNewFile();

            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            Toast.makeText(ClinicalCompetenciesActivity.this,"File Path : " + dir.getAbsolutePath().toString(),Toast.LENGTH_LONG).show();
            progressDialogDwnld.dismiss();
        }
    }

    private final int PICK_DOCUMENT = 3;

    private void setSimulation() {
        startActivity(new Intent(ClinicalCompetenciesActivity.this, Simulation.class));
    }
    private void selectImageAndDocument() {
        try {
            PackageManager pm = this.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(intent,PICK_DOCUMENT);
            } else {
                checkAndRequestPermissions();
                Toast.makeText(ClinicalCompetenciesActivity.this, "Permission error", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            checkAndRequestPermissions();
            Toast.makeText(ClinicalCompetenciesActivity.this, "Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;

    private boolean checkAndRequestPermissions() {

        int wtite = ContextCompat.checkSelfPermission(ClinicalCompetenciesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(ClinicalCompetenciesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ClinicalCompetenciesActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    /**
     * Lifecycle function
     * @param requestCode The request code passed in {@link #requestPermissions(
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("in fragment on request", "Permission callback called-------");

        switch (requestCode) {

            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted

                        final File destination;
                        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
                            destination = new File(Environment.getExternalStorageDirectory().getPath()
                                    + "//" + getResources().getString(R.string.app_name));
                        } else {
                            destination = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath()
                                    + "//" + getResources().getString(R.string.app_name));
                        }

                        if (!destination.exists())
                            destination.mkdir();

                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ClinicalCompetenciesActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(ClinicalCompetenciesActivity.this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(ClinicalCompetenciesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera and Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(ClinicalCompetenciesActivity.this, getResources().getString(R.string.go_to_enable), Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    /**
     * Get real path from URI
     * @param context
     * @param contentUri
     * @return String filepath
     */
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Get absolute file path
     * @param uri
     * @return String path
     */
    public String getPath(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    /**
     * Create path
     * @param context
     * @param uri
     * @return String createdPath
     */
    @Nullable
    public static String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator + "temp_file";
        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }
        return file.getAbsolutePath();
    }


    /**
     * Lifecycle function
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Uri imageUri = data.getData();
        String scheme = imageUri.getScheme();

        double dataSize = 0;
        if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                dataSize = inputStream.available();

                if ((dataSize / 1000000) < 10) {

                    if (requestCode == PICK_DOCUMENT) {

                        // Get the Uri of the selected file
                        Uri uri = data.getData();

                        Log.e("sdas", createCopyAndReturnRealPath(ClinicalCompetenciesActivity.this,uri));

                        String FileName = createCopyAndReturnRealPath(ClinicalCompetenciesActivity.this,uri);
                        if(getFileName(uri).contains(".xls")){
                            UploadData(uri,null,getFileName(uri));
                        } else {
                            Toast.makeText(this,  "Please select only .xlsx file only!...", Toast.LENGTH_SHORT).show();
                        }


                    }

                } else {
                    Toast.makeText(this,  R.string.file_size, Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');

            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private InputStream iStream = null;
    private byte[] inputData;

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    /**
     * Upload document
     * @param Document
     * @param bitmap
     * @param Name
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void UploadData(Uri Document,Bitmap bitmap,String Name)  {
        final KProgressHUD progressDialog = KProgressHUD.create(ClinicalCompetenciesActivity.this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        try {


            if(Document != null){
                iStream = getContentResolver().openInputStream(Document);
                inputData = getBytes(iStream);
            }



            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, session.BASEURL + "/cases/import",
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Log.e("Response", jsonObject.toString() + " --");
                                if (jsonObject.getString("ResponseCode").equals("200")) {

                                    Toast.makeText(ClinicalCompetenciesActivity.this, jsonObject.getString("ResponseMsg"), Toast.LENGTH_LONG).show();

                                } else if (jsonObject.getString("ResponseCode").equals("401")) {

                                    Toast.makeText(ClinicalCompetenciesActivity.this, jsonObject.getString("ResponseMsg"), Toast.LENGTH_LONG).show();

                                }  else if(jsonObject.getString("ResponseCode").equals("422")) {


                                    Toast.makeText(ClinicalCompetenciesActivity.this, jsonObject.getString("ResponseMsg"), Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(ClinicalCompetenciesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError){

                                if(error.networkResponse != null && error.networkResponse.data != null){
                                    switch(error.networkResponse.statusCode){
                                        case 500:
                                            String json = new String(error.networkResponse.data);
                                            json = session.trimMessage(json, "message");
                                            if(json != null) {
                                                Toast.makeText(ClinicalCompetenciesActivity.this, json, Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                    }
                                    //Additional cases
                                }
                            }
                            else if (error instanceof TimeoutError)
                                Toast.makeText(ClinicalCompetenciesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(ClinicalCompetenciesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();


                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json");
                    params.put("Authorization", "Bearer " + session.getAPITOKEN());
                    return params;
                }


                @Override
                protected Map<String, DataPart> getByteData() throws IOException {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("file", new DataPart(Name,inputData));
                    return params;
                }
            };
            //adding the request to volley

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(volleyMultipartRequest);

        }catch (Exception e){

        }

    }

    /**
     * Download document feature
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void DownloadData()  {
        final KProgressHUD progressDialog = KProgressHUD.create(ClinicalCompetenciesActivity.this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        try {




            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "/cases/export",
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Log.e("Response", jsonObject.toString() + " --");
                                if (jsonObject.getString("ResponseCode").equals("200")) {

                                    progressDialogDwnld = KProgressHUD.create(ClinicalCompetenciesActivity.this)
                                            .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                                            .setLabel("Please wait")
                                            .setCancellable(false)
                                            .setAnimationSpeed(2)
                                            .setDimAmount(0.5f)
                                            .show();
                                    Long tsLong = System.currentTimeMillis()/1000;
                                    String ts = tsLong.toString();

                                    new DownloadFileSecond().execute(jsonObject.getJSONObject("data").getString("url"), ts+".xlsx");
                                    Toast.makeText(ClinicalCompetenciesActivity.this, jsonObject.getString("ResponseMsg"), Toast.LENGTH_LONG).show();

                                } else if (jsonObject.getString("ResponseCode").equals("401")) {

                                    Toast.makeText(ClinicalCompetenciesActivity.this, jsonObject.getString("ResponseMsg"), Toast.LENGTH_LONG).show();

                                }  else if(jsonObject.getString("ResponseCode").equals("422")) {


                                    Toast.makeText(ClinicalCompetenciesActivity.this, jsonObject.getString("ResponseMsg"), Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(ClinicalCompetenciesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError){

                                if(error.networkResponse != null && error.networkResponse.data != null){
                                    switch(error.networkResponse.statusCode){
                                        case 500:
                                            String json = new String(error.networkResponse.data);
                                            json = session.trimMessage(json, "message");
                                            if(json != null) {
                                                Toast.makeText(ClinicalCompetenciesActivity.this, json, Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                    }
                                    //Additional cases
                                }
                            }
                            else if (error instanceof TimeoutError)
                                Toast.makeText(ClinicalCompetenciesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(ClinicalCompetenciesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();


                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json");
                    params.put("Authorization", "Bearer " + session.getAPITOKEN());
                    return params;
                }


                @Override
                protected Map<String, DataPart> getByteData() throws IOException {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                   // params.put("file", new DataPart(Name,inputData));
                    return params;
                }
            };
            //adding the request to volley

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(volleyMultipartRequest);

        }catch (Exception e){

        }

    }


    /**
     * Show Success dialog
     * @param message
     * @param okListener
     */
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ClinicalCompetenciesActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    /**
     * Get Cases
     * Calling API
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void getCases() {
        final KProgressHUD progressDialog = KProgressHUD.create(ClinicalCompetenciesActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "cases",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();
                        casesModelArrayList.clear();

                        try {
                            Log.e("casesList", new String(response.data) + "--");

                            JSONObject object = new JSONObject(new String(response.data));

                            jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                
                                CasesModel model = new CasesModel();
                                model.setId(jsonObject.getString("id"));
                                model.setTitle(jsonObject.getString("title"));
                                model.setDate(jsonObject.getString("date"));
                                model.setStart_time(jsonObject.getString("start_time"));
                                model.setEnd_time(jsonObject.getString("end_time"));
                                model.setDuration(jsonObject.getString("duration"));
                                model.setAge(jsonObject.getString("age"));
                                model.setAsa(jsonObject.getString("asa"));

                                ArrayList<CategoriesModel> categoriesArrayList = new ArrayList<>();

                                JSONArray array = jsonObject.getJSONArray("category");
                                for (int j = 0; j < array.length(); j++){
                                    JSONObject object1 = array.getJSONObject(j);
                                    CategoriesModel model1 = new CategoriesModel();
                                    model1.setId(object1.getString("id"));
                                    model1.setName(object1.getString("name"));
                                    model1.setTarget(object1.getString("target"));
                                    model1.setChecked(true);
                                    categoriesArrayList.add(model1);
                                }
                                model.setCategoriesArrayList(categoriesArrayList);

                                casesModelArrayList.add(model);
                            }

                            adapterClinical.notifyDataSetChanged();

                            if (casesModelArrayList.isEmpty()){
                                no_data_ll.setVisibility(View.VISIBLE);
                            } else {
                                no_data_ll.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {
                            Toast.makeText(ClinicalCompetenciesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if(json != null) {
                                            Toast.makeText(ClinicalCompetenciesActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(ClinicalCompetenciesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(ClinicalCompetenciesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
             /*   params.put("username", userNameET.getText().toString());
                params.put("password", passwordET.getText().toString());
                params.put("fullname", fullNameET.getText().toString());
                params.put("device_type", "android");
                params.put("device_token", android_id);*/

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //     params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + session.getAPITOKEN());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        volleyMultipartRequest.setShouldRetryServerErrors(true);

        Volley.newRequestQueue(ClinicalCompetenciesActivity.this).add(volleyMultipartRequest);
    }


    /**
     * Get Count data
     * Calling API
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void getCountData() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "clinical",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            Log.e("clinicalData", new String(response.data) + "--");


                            JSONObject object = new JSONObject(new String(response.data));

                            tv_1.setText(object.getString("hoursCompleted"));
                            tv_2.setText("/" + object.getString("hoursTarget"));
                            tv_4.setText(object.getString("casesCompleted"));
                            tv_5.setText("/" + object.getString("casesTarget"));

                            completed = object.getBoolean("completed");
                            canCompleteClinicals = object.getBoolean("canCompleteClinicals");

                            if (object.getString("completed").equals("true")){
                                btn_completed.setBackground(getResources().getDrawable(R.drawable.bg_blue_5dp));
                                btn_completed.setText("Completed");
                            } else {
                                btn_completed.setBackground(getResources().getDrawable(R.drawable.bg_light_gray_5dp));
                                btn_completed.setText("Not Completed");
                            }

                        } catch (Exception e) {
                            Toast.makeText(ClinicalCompetenciesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if(json != null) {
                                            Toast.makeText(ClinicalCompetenciesActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(ClinicalCompetenciesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(ClinicalCompetenciesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
             /*   params.put("username", userNameET.getText().toString());
                params.put("password", passwordET.getText().toString());
                params.put("fullname", fullNameET.getText().toString());
                params.put("device_type", "android");
                params.put("device_token", android_id);*/

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //     params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + session.getAPITOKEN());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        volleyMultipartRequest.setShouldRetryServerErrors(true);

        Volley.newRequestQueue(ClinicalCompetenciesActivity.this).add(volleyMultipartRequest);
    }


    /**
     * Complete the Clinical
     */
    private void completeClinicals() {
        final KProgressHUD progressDialog = KProgressHUD.create(ClinicalCompetenciesActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, session.BASEURL + "clinical/complete",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("completeResponse", new String(response.data) + "--");

                            JSONObject object = new JSONObject(new String(response.data));

                            if (object.getString("status").equals("success")){
                                getCountData();
                            }



                        } catch (Exception e) {
                            Toast.makeText(ClinicalCompetenciesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if(json != null) {
                                            Toast.makeText(ClinicalCompetenciesActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(ClinicalCompetenciesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(ClinicalCompetenciesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
             /*   params.put("username", userNameET.getText().toString());
                params.put("password", passwordET.getText().toString());
                params.put("fullname", fullNameET.getText().toString());
                params.put("device_type", "android");
                params.put("device_token", android_id);*/

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //     params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + session.getAPITOKEN());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        volleyMultipartRequest.setShouldRetryServerErrors(true);

        Volley.newRequestQueue(ClinicalCompetenciesActivity.this).add(volleyMultipartRequest);
    }


    /**
     * Filter by category name
     * @param text
     */
    public void filter(String text){

        ArrayList<CasesModel> temp = new ArrayList();

        for(CasesModel d: casesModelArrayList){
            for (int i = 0; i < d.getCategoriesArrayList().size(); i++) {
                if (d.getTitle().toLowerCase().contains(text.toLowerCase()) || d.getAge().toLowerCase().contains(text.toLowerCase())
                || d.getCategoriesArrayList().get(i).getName().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                    break;
                }
            }
        }
        adapterClinical.updateList(temp);
    }


    @Override
    protected void onResume() {
        super.onResume();

        getCases();
        getCountData();
    }

}