package com.vgs.aluno.soumeudono;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class EditorTXT extends MainActivity {

    String IDArquivo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_txt);
        Intent intent = getIntent();
        IDArquivo = intent.getStringExtra("IDArquivo");

        final Button button = (Button) findViewById(R.id.btn_Salvar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showMessage("Finge que salva");  //IMplementar função do EditContent
            }
        });

    }
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), IDArquivo)
                .setResultCallback(idCallback);
    }

    final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {

        @Override
        public void onResult(DriveApi.DriveIdResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Cannot find DriveId. Are you authorized to view this file?");
                return;
            }

            DriveId driveId = result.getDriveId();
            DriveFile file = driveId.asDriveFile();
            file.getMetadata(getGoogleApiClient())
                    .setResultCallback(metadataCallback);

            new RetrieveDriveFileContentsAsyncTask(EditorTXT.this).execute(result.getDriveId());
        }
    };

    final private ResultCallback<MetadataResult> metadataCallback = new
            ResultCallback<MetadataResult>() {
                @Override
                public void onResult(MetadataResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Problema em pegar o nome");
                        return;
                    }
                    Metadata metadata = result.getMetadata();
                    TextView t = (TextView)findViewById(R.id.tv_Nome);
                    t.setText(metadata.getTitle());
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
                showMessage("IOException while reading from the stream");
            }

            driveContents.discard(getGoogleApiClient());
            return contents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                showMessage("Error while reading from the file");
                return;
            }
            TextView t = (TextView)findViewById(R.id.eT_conteudo);
            t.setText(result);


        }
    }

}
