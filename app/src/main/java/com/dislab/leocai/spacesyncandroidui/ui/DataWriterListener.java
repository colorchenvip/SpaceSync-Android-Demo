package com.dislab.leocai.spacesyncandroidui.ui;

import android.os.Environment;

import com.dislab.leocai.spacesync.core.MultiClientDataBuffer;
import com.dislab.leocai.spacesync.core.SyncBufferListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by leocai on 17-1-11.
 */
public class DataWriterListener implements  SyncBufferListener {

    FileWriter fileWriter;
    private int writeCount;

    public DataWriterListener() {

    }

    public void close() {
        if (fileWriter != null) {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dealWithSyncBuffer(MultiClientDataBuffer buffer, boolean isSyncTime) {
        if (isSyncTime) {
            try {
                fileWriter = new FileWriter(new File(Environment.getExternalStorageDirectory(), "multi_data_" + (writeCount++ + ".csv")));
                double[][][] data_multi = buffer.get();
                int clientNum = data_multi.length;
                int row = data_multi[0].length;
                int column = data_multi[0][0].length;
                int newColumn = column * clientNum;
                double[][] newdata = new double[row][newColumn];
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < column * clientNum; j++) {
                        int clientId = j / column;
                        int columnId = j % column;
                        newdata[i][j] = data_multi[clientId][i][columnId];
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < newdata[0].length; j++) {
                        sb.append(newdata[i][j]);
                        if(j!=newdata[0].length-1) sb.append(",");
                    }
                    sb.append("\n");
                }
                fileWriter.write(sb.toString());
                try {
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
