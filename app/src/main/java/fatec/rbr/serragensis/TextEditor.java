package fatec.rbr.serragensis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource.MetadataResult;
import com.google.android.gms.drive.Metadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextEditor extends MainActivity {

    private final String TAG = "TextEditor";

    String fileID = "";

    public void openViewEdit(View v){
        TextView contentsOfFile = (TextView)findViewById(R.id.eT_content);

        Intent myIntent = new Intent(getBaseContext(), EditContentsActivity.class);

        myIntent.putExtra("contentsOfFile", contentsOfFile.getText().toString());
        myIntent.putExtra("fileID", fileID);
        startActivity(myIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);

        Intent intent = getIntent();
        fileID = intent.getStringExtra("fileID");

    }
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), fileID)
                .setResultCallback(idCallback);
    }

    final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {

        @Override
        public void onResult(DriveApi.DriveIdResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.e(TAG,"Cannot find DriveId.");
                showMessage("Problema ao abrir o arquivo. Você tem autorização para abrir esse arquivo?");
                return;
            }

            DriveId driveId = result.getDriveId();
            DriveFile file = driveId.asDriveFile();
            file.getMetadata(getGoogleApiClient())
                    .setResultCallback(metadataCallback);

            new RetrieveDriveFileContentsAsyncTask(TextEditor.this).execute(result.getDriveId());
        }
    };

    final private ResultCallback<MetadataResult> metadataCallback = new
            ResultCallback<MetadataResult>() {
                @Override
                public void onResult(MetadataResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.e(TAG,"Failed to get the name of the file");
                        showMessage("Problema em pegar o nome");
                        return;
                    }
                    Metadata metadata = result.getMetadata();
                    TextView nome = (TextView)findViewById(R.id.tv_Nome);
                    nome.setText(metadata.getTitle());
                }
            };

    final private class RetrieveDriveFileContentsAsyncTask
            extends ApiClientAsyncTask<DriveId, Boolean, String> {

        public RetrieveDriveFileContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackgroundConnected(DriveId... params) {
            String contents = null;
            DriveFile file = params[0].asDriveFile();
            DriveApi.DriveContentsResult driveContentsResult =
                    file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return null;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(driveContents.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                contents = builder.toString();
            } catch (IOException e) {
                Log.e(TAG,"IOException while reading from the stream");
                showMessage("Problema na Leitura do arquivo");
            }

            driveContents.discard(getGoogleApiClient());
            return contents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Log.e(TAG,"Error while reading from the file");
                showMessage("Erro ao ler do arquivo");
                return;
            }
            TextView eT_content = (TextView)findViewById(R.id.eT_content);
            eT_content.setText(result);
            eT_content.setEnabled(true);


        }
    }

}
