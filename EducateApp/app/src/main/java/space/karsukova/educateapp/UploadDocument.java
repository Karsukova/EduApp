package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class UploadDocument extends AppCompatActivity {

    private CardView addDocument;
    private final int REQ = 1;
    private EditText docTitle;
    private Button uploadButton;
    private TextView docTextView;
    private Uri docData;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl = "";
    private String docName, title;
    private ProgressDialog progressDialog;
    String[] mimeTypes =
            {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                    "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                    "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                    "text/plain",
                    "application/pdf",
                    "application/zip"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_doc);
        addDocument = findViewById(R.id.addDoc);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        docTitle = findViewById(R.id.docTitle);
        uploadButton = findViewById(R.id.uploadDocBtn);
        docTextView = findViewById(R.id.docTextView);
        addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = docTitle.getText().toString();
                if(title.isEmpty()){
                    docTitle.setError(getText(R.string.required_field));
                    docTitle.requestFocus();
                } else if (docData == null){
                    Toast.makeText(UploadDocument.this, R.string.pls_select_doc, Toast.LENGTH_SHORT).show();
                } else {
                    uploadDoc();
                }
            }
        });


    }

    private void uploadDoc() {
        progressDialog.setTitle(R.string.wait);
        progressDialog.setMessage(getText(R.string.uploading));
        progressDialog.show();
        StorageReference reference = null;
        String str = "";
        if (docName.contains(".doc") || docName.contains(".docx")) {
            str = "doc";
        } else if (docName.contains(".pdf")){
            str = "pdf";
        } else if (docName.contains(".ppt")|| docName.contains(".pptx")){
            str = "ppt";
        } else if (docName.contains(".txt")) {
            str = "txt";
        } else if (docName.contains(".xls") || docName.contains(".xlsx")){
            str = "xls";
        }
        reference = storageReference.child(str +"/" + docName + "-" + System.currentTimeMillis() + "." + str);
        reference.putFile(docData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UploadDocument.this, R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String downloadUrl) {
        String uniqueKey = databaseReference.child("doc").push().getKey();
        HashMap data = new HashMap();
        data.put("docTitle", title);
        data.put( "docUrl", downloadUrl);
        databaseReference.child("doc").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(UploadDocument.this, R.string.doc_success, Toast.LENGTH_SHORT).show();
                docTitle.setText("");
                startActivity(new Intent(getApplicationContext(), SuperAdminActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UploadDocument.this, R.string.doc_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, getText(R.string.select_doc)), REQ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ &&resultCode == RESULT_OK){
            docData = data.getData();
            if (docData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = UploadDocument.this.getContentResolver().query(docData, null, null,
                            null, null);
                    if (cursor != null && cursor.moveToFirst()){
                        docName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (docData.toString().startsWith("file://")){
                docName = new File(docData.toString()).getName();
            }
            docTextView.setText(docName);
        }
    }
}