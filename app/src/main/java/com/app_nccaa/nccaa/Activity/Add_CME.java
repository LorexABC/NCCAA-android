package com.app_nccaa.nccaa.Activity;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app_nccaa.nccaa.Adapter.SelectCitySpinner;
import com.app_nccaa.nccaa.Model.CityModel;
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Add CME Module
 *
 * Modified by phantom
 * On 02/22/24
 * Validation, loading system features are modified and so on.
 */
public class Add_CME extends AppCompatActivity {

    private TextView cmeTextTV;

    private Spinner cme_provider_Spinner;

    private UserSession session;
    private final ArrayList<CityModel> cme_provider_ArrayList = new ArrayList<>();

    private RequestQueue requestQueue;
    private EditText mDocument_name;
    private EditText mHours;
    private String mUploadID = "";
    private String mCME_Provider_ID = "";
    private String mType = "anesthesia";
    private CheckBox mChk_bx_1,mChk_bx_2;
    private String mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cme);

        session = new UserSession(Add_CME.this);
        requestQueue = Volley.newRequestQueue(Add_CME.this); //Creating the RequestQueue

        // Init global component
        mDocument_name = findViewById(R.id.mDocument_name);
        mHours = findViewById(R.id.mHours);
        mChk_bx_1 = findViewById(R.id.mChk_bx_1);
        mChk_bx_2 = findViewById(R.id.mChk_bx_2);
        cmeTextTV = findViewById(R.id.cmeTextTV);
        cme_provider_Spinner = findViewById(R.id.cme_provider_Spinner);


        if (savedInstanceState == null) { // Get mYear value from extra content if it exists.
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mYear = null;

            } else {
                mYear = extras.getString("mYear");

            }
        } else {
            mYear = (String) savedInstanceState.getSerializable("mYear");

        }

        Log.e("mYear",mYear);

        // Back button handler
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish this Activity
            }
        });

        TextView uploadCertification = (TextView) findViewById(R.id.add_CME_Btn);

        uploadCertification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadCertification.getText() == "Uploaded") {
                    Log.e("Upload Certification", "Already Exist");
                    try {
                        CharSequence[] options = { "Confirm", "Cancel" };
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Add_CME.this);
                        builder.setTitle("Certification file exists. \nWould you like to replace it?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if(options[id].equals("Confirm")) {
                                    dialog.dismiss();
                                    selectImageAndDocument();
                                } else if(options[id].equals("Cancel")) {
                                    dialog.dismiss();
                                }
                            }
                        });

                        builder.show();

                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                    selectImageAndDocument();
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.radio);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioMale:
                        // do operations specific to this selection
                        mType = "anesthesia";

                        break;
                    case R.id.radioFemale:
                        // do operations specific to this selection
                        mType = "other";
                        break;

                }
            }
        });

        cmeTextTV.setText(noTrailingwhiteLines(Html.fromHtml("<p>CME Submission is now easier than ever and NCCAA has gone completely digital (no more paper!). Upload the CME certificate earned, which displays the hours granted, attendance date, name of accreditor issuing the CME, and title of meeting or CME as proof you earned the CME credit. You may enter credits in increments of 1/4 hour. NCCAA accepts CME credits provided by the AMA, AAPA, ACCME, and FAACT. <span style=\"text-decoration: underline; color: #4D85F0;\"><u>see more</u></span></p>")));

        cmeTextTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Add_CME.this, More_Info_Cert_Exam.class)
                        .putExtra("from", "add_cme"));
            }
        });


        // CME Provider SPINNER
        CityModel suff1 = new CityModel();
        suff1.setId("ama");
        suff1.setName("AMA");
        cme_provider_ArrayList.add(suff1);
        CityModel suff2 = new CityModel();
        suff2.setId("accme");
        suff2.setName("ACCME");
        cme_provider_ArrayList.add(suff2);
        CityModel suff3 = new CityModel();
        suff3.setId("aapa");
        suff3.setName("AAPA");
        cme_provider_ArrayList.add(suff3);
        CityModel suff4 = new CityModel();
        suff4.setId("aha");
        suff4.setName("AHA");
        cme_provider_ArrayList.add(suff4);
        CityModel suff5 = new CityModel();
        suff5.setId("faact");
        suff5.setName("FAACT");
        cme_provider_ArrayList.add(suff5);
        CityModel cityModel1 = new CityModel();
        cityModel1.setId("");
        cityModel1.setName("Select the CME Accreditor");
        cme_provider_ArrayList.add(cityModel1);

        SelectCitySpinner adapter = new SelectCitySpinner(Add_CME.this, android.R.layout.simple_spinner_item, cme_provider_ArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        cme_provider_Spinner.setAdapter(adapter);
        cme_provider_Spinner.setSelection(adapter.getCount());

        // Select component listener (cme_provider_spinner)
        cme_provider_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (cme_provider_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                }
                mCME_Provider_ID = cme_provider_ArrayList.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Submit handler
        findViewById(R.id.mAdd_CME).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUploadID.equals("")){
                    Toast.makeText(Add_CME.this,"Please select File / Take Photo",Toast.LENGTH_LONG).show();
                } else if(mDocument_name.getText().toString().equals("")){
                    Toast.makeText(Add_CME.this,"Please Enter Document Name",Toast.LENGTH_LONG).show();
                }else if(mHours.getText().toString().equals("")){
                    Toast.makeText(Add_CME.this,"Please Enter Hours",Toast.LENGTH_LONG).show();
                }else if(mCME_Provider_ID.equals("")){
                    Toast.makeText(Add_CME.this,"Please Enter CME Provider",Toast.LENGTH_LONG).show();
                }else if(!mChk_bx_1.isChecked() || !mChk_bx_2.isChecked()){
                    Toast.makeText(Add_CME.this,"Please Check Both Box...",Toast.LENGTH_LONG).show();
                }else{
                    add_CME(mDocument_name.getText().toString(),mHours.getText().toString(),mCME_Provider_ID,mUploadID,mType);
                }

            }
        });
    }

    private final int PICK_DOCUMENT = 3;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    /**
     * System Event Modal
     * @description: If this function, system modal will be shown
     */
    private void selectImageAndDocument() {
        try {
            PackageManager pm = this.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, this.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.gallary), getString(R.string.document), getString(R.string.cancel)};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Add_CME.this);
                builder.setTitle(R.string.select_option);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals(getResources().getString(R.string.take_photo))) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals(getResources().getString(R.string.gallary))) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals(getResources().getString(R.string.document))) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/*");
                            startActivityForResult(intent, PICK_DOCUMENT);

                        } else if (options[item].equals(getResources().getString(R.string.cancel))) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else {
                checkAndRequestPermissions();
            }
        } catch (Exception e) {

            checkAndRequestPermissions();
            Toast.makeText(Add_CME.this, "Permission error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    /**
     * System Event Handler
     * @param requestCode
     * @param resultCode
     * @param data
     *
     * Modified by phantom
     * On 03/22/24
     */
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Pick up image from camera
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                UploadData(null, bitmap, "Untitled image");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) { // Pick up image from gallery

            try {

                Uri selectedImageUri = data.getData();

                if(validationFileTypeAndFileSize(selectedImageUri, "")) {
                    return;
                }

                String selectedImagePath = getRealPathFromURI2(selectedImageUri);
                Log.e("selectedImagePath", selectedImagePath);

                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Create image from data of exist image of gallery
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                String path = getPath(selectedImageUri);
                Matrix matrix = new Matrix();
                ExifInterface exif = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        exif = new ExifInterface(path);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                matrix.postRotate(180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                matrix.postRotate(270);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Bitmap bitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

                UploadData(null, bitmap, getFileName(selectedImageUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_DOCUMENT) { // Pick up image from document.

            // Get the Uri of the selected file
            Uri uri = data.getData();

            if(validationFileTypeAndFileSize(uri, "pdf|doc|docx|txt")) {
                return;
            }

            assert uri != null;
            UploadData(uri, null, getFileName(uri));
        }

        TextView addCMEBtn = (TextView) findViewById(R.id.add_CME_Btn);
        addCMEBtn.setText("Uploaded");
    }

    /**
     * Get File Name from URI
     * @param uri
     * @return String filename
     *
     * Created by phantom
     * On 03/22/24
     */
    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = getContentResolver().
                    query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();

            assert result != null;

            int cut = result.lastIndexOf('/');

            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * Get Real Path from URI
     * @param uri
     * @return String path
     *
     * Created by phantom
     * On 03/22/24
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
     * Get File Size from URI
     * @param uri
     * @return long size (byte unit)
     *
     * Created by phantom
     * On 03/22/24
     */
    public long getFileSize(Uri uri) {
        if (uri != null) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    // Note: COLUMN_SIZE is available starting from API Level 1
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                    // fileSize contains the size of the file in bytes
                    return cursor.getLong(sizeIndex);
                }
            } catch (Exception e) {
                Log.e("File Size", "Failed to get file size", e);
            }
        }
        return 0;
    }

    /**
     * Get file type from URI
     * @param uri
     * @return String type
     *
     * Created by phantom
     * On 03/22/24
     */
    public String getFileType(Uri uri) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if(extension != null)
            return extension;
        return "";
    }

    /**
     * Validation from file size and file type
     * Description: Limit file size -> 25M, allowed only document and image file type.
     * @param uri
     * @param allowedTypes
     * @return Bool result
     *
     * Created by phantom
     * On 03/22/24
     */
    public boolean validationFileTypeAndFileSize(Uri uri, String allowedTypes) {

        if(getFileSize(uri) > 1024 * 1024 * 25) { // file size validation
            Toast.makeText(Add_CME.this, "The maximum file size must be 25M or less.",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        if(Objects.equals(allowedTypes, "")) {
            return false;
        }

        String extension = getFileType(uri);

        // Allowed file type array.
        String[] allowedExtensions = allowedTypes.split("\\|");

        // Check if the file extension is in the list of allowed extensions
        for (String allowedExtension : allowedExtensions) {
            if (extension.equals(allowedExtension)) {
                return false;
            }
        }

        Toast.makeText(Add_CME.this, "Not matched file type. (You must input only " +
                        allowedTypes + " file.)",
                Toast.LENGTH_LONG).show();

        return true;
    }

    /**
     * Get Byte Array of file from Stream
     * @param inputStream
     * @return bytep[] result
     * @throws IOException
     */
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


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Files.FileColumns.DOCUMENT_ID};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Same here above method.
     * @param contentUri
     * @return String path
     */
    public String getRealPathFromURI2(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Permissions Definition
     * @return boolean result
     */
    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(Add_CME.this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(Add_CME.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(Add_CME.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(Add_CME.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    /**
     * Permissions handler.
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
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Add_CME.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(Add_CME.this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(Add_CME.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
                            Toast.makeText(Add_CME.this, getResources().getString(R.string.go_to_enable), Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }


    /**
     * Common dialog
     * @param message
     * @param okListener
     */
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Add_CME.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    /**
     * Create image handler
     * @param bitmap1
     * @return byte[] imageData
     */
    public byte[] getFileDataFromDrawable(Bitmap bitmap1) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private InputStream iStream = null;
    private byte[] inputData;

    /**
     * Upload image and document(certificate)
     * @param Document
     * @param bitmap
     * @param Name
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void UploadData(Uri Document, Bitmap bitmap, String Name) {
        final KProgressHUD progressDialog = KProgressHUD.create(Add_CME.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        try {


            if (Document != null) {
                iStream = getContentResolver().openInputStream(Document);
                inputData = getBytes(iStream);
            }

            // Create api what need to upload image and document
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, session.BASEURL + "users/me/uploads",
                    new Response.Listener<NetworkResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(NetworkResponse response) {

                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Log.e("Response", jsonObject.toString() + " --");
                                mUploadID = jsonObject.getString("uploadId");

                                Toast.makeText(Add_CME.this, "Uploaded", Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                Toast.makeText(Add_CME.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError) {
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    switch (error.networkResponse.statusCode) {
                                        case 500:
                                            String json = new String(error.networkResponse.data);
                                            json = session.trimMessage(json, "message");
                                            if (json != null) {
                                                Toast.makeText(Add_CME.this, json, Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                    }
                                    //Additional cases
                                }
                            } else if (error instanceof TimeoutError)
                                Toast.makeText(Add_CME.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(Add_CME.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

                    if (Document == null) {
                        params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                    } else {
                        params.put("file", new DataPart(Name, inputData));
                    }

                    return params;
                }
            };
            //adding the request to volley

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(volleyMultipartRequest);

        } catch (Exception e) {

        }

    }

    /**
     * Remove Trailing white line
     * @param text
     * @return Charsequence removed string
     */
    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

    /**
     * Add CME Handler
     * @param name
     * @param hours
     * @param cmeProvider
     * @param uploadId
     * @param type
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void add_CME(String name, String hours, String cmeProvider, String uploadId, String type) {
        final KProgressHUD progressDialog = KProgressHUD.create(Add_CME.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Map<String, Object> postParam3= new HashMap<String, Object>();
        postParam3.put("name", name);
        postParam3.put("hours", Float.parseFloat(hours));
        postParam3.put("cmeProvider", cmeProvider);
        postParam3.put("uploadId", Integer.parseInt(uploadId));
        postParam3.put("type", type);

        JSONObject object = new JSONObject(postParam3);

        Log.e("getJson", object.toString() + "--");

        // Create add cme api
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, session.BASEURL + "users/me/cmeCycles/" + mYear + "/entries", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Toast.makeText(Add_CME.this, "Added Successfully...", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Add_CME.this, CME_Submissions.class);
                        intent.putExtra("strRecipient", getIntent().getStringExtra("strRecipient"));
                        intent.putExtra("strIsCurrent", getIntent().getStringExtra("strIsCurrent"));
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();

                    String json = new String(error.networkResponse.data);
                    json = session.trimMessage(json, "error");
                    Toast.makeText(Add_CME.this, json, Toast.LENGTH_LONG).show();

                    if (error instanceof ServerError) {

                    }
                    else if (error instanceof TimeoutError) {
                        Toast.makeText(Add_CME.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                    }
                    else if (error instanceof NetworkError) {
                        Toast.makeText(Add_CME.this, "Bad Network Connection", Toast.LENGTH_LONG).show();
                    }

                }
            }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + session.getAPITOKEN());
                return headers;
            }
        };

        jsonObjReq.setTag("TAG");
        // Adding request to request queue
        requestQueue.add(jsonObjReq);
    }

    /**
     * Create loading system when this activity is updated
     */
    private void UpdateData() {
        final KProgressHUD progressDialog = KProgressHUD.create(Add_CME.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        };

        long delay = 1000L;
        timer.schedule(task, delay);
    }

    @Override
    protected void onResume() {
        super.onResume();

        UpdateData();
    }
}
