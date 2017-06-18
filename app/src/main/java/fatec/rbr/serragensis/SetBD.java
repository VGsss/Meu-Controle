package fatec.rbr.serragensis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource.MetadataResult;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SetBD extends MainActivity {

    private final String TAG = "TextEditor";

    String fileID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            new RetrieveDriveFileContentsAsyncTask(SetBD.this).execute(result.getDriveId());
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
                }
            };

    final private class RetrieveDriveFileContentsAsyncTask
            extends ApiClientAsyncTask<DriveId, Boolean, String> {

        public RetrieveDriveFileContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackgroundConnected(DriveId... params) {
            DriveFile file = params[0].asDriveFile();
            DriveApi.DriveContentsResult driveContentsResult =
                    file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return null;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();
            try {
                importDatabase(driveContents.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
                return "1";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Log.e(TAG,"Error while reading from the file");
                showMessage("Erro ao ler do arquivo");
                return;
            }
            else{
                showMessage("DB carregada com sucesso");
                finish();
            }

        }
        private void importDatabase(InputStream input) throws IOException
        {
            InputStream mInput = input;
            String outFileName = getApplicationContext().getDatabasePath("orcamentosDB").toString();
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = mInput.read(mBuffer))>0)
            {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
        }
    }

}