package com.yyq.cat.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: TiAmo
 * Date: 2020/8/24 12:43
 * Description:
 */
public class FileUtils {
    public static String getOutputPath(Context context){
        return context.getFilesDir() + "/internal/files/";
    }

    public static String writeFileInAssetsToInternalDir(Context context, String fileNameInAssets) {
        String outPutPath = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            File outputDir = new File(getOutputPath(context));
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File outputFile = new File(outputDir, fileNameInAssets);
            in = context.getAssets().open(fileNameInAssets);
            out = new FileOutputStream(outputFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            outPutPath = outputFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != in) {
                    in.close();
                }

                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outPutPath;
    }
}