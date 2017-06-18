/**
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fatec.rbr.serragensis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveApi.DriveIdResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;

import java.io.IOException;
import java.io.OutputStream;

public class EditContentsActivity extends MainActivity {

    private final String TAG = "EditContents";

    String contentsOfFile = "",EXISTING_FILE_ID = "";

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        Intent intent = getIntent();

        contentsOfFile = (intent.getStringExtra("contentsOfFile") != null) ? intent.getStringExtra("contentsOfFile") : "";
        EXISTING_FILE_ID = intent.getStringExtra("fileID");

        final ResultCallback<DriveIdResult> idCallback = new ResultCallback<DriveIdResult>() {
            @Override
            public void onResult(DriveIdResult result) {
                if (!result.getStatus().isSuccess()) {
                    Log.e(TAG,"Cannot find DriveId.");
                    showMessage("Problema ao abrir o arquivo. Você tem autorização para abrir esse arquivo?");
                    return;
                }
                DriveId driveId = result.getDriveId();
                DriveFile file = driveId.asDriveFile();
                new EditContentsAsyncTask(EditContentsActivity.this).execute(file);
            }
        };
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), EXISTING_FILE_ID)
              .setResultCallback(idCallback);
    }

    public class EditContentsAsyncTask extends ApiClientAsyncTask<DriveFile, Void, Boolean> {

        public EditContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected Boolean doInBackgroundConnected(DriveFile... args) {
            DriveFile file = args[0];
            try {
                DriveContentsResult driveContentsResult = file.open(
                        getGoogleApiClient(), DriveFile.MODE_WRITE_ONLY, null).await();
                if (!driveContentsResult.getStatus().isSuccess()) {
                    return false;
                }
                DriveContents driveContents = driveContentsResult.getDriveContents();
                OutputStream outputStream = driveContents.getOutputStream();

                outputStream.write(contentsOfFile.getBytes());
                Log.i(TAG,"Tried to write on the file");

                com.google.android.gms.common.api.Status status =
                        driveContents.commit(getGoogleApiClient(), null).await();
                return status.getStatus().isSuccess();
            } catch (IOException e) {
                Log.e(TAG,"IOException while appending to the output stream");
                showMessage("Erro ao escrever no arquivo");
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Log.e(TAG,"Error while editing contents");
                showMessage("Erro ao editar conteúdo do arquivo");
                return;
            }
            Log.i(TAG,"Successfully edited contents");
            showMessage("Arquivo salvo com sucesso");
            finish();
        }
    }
}
