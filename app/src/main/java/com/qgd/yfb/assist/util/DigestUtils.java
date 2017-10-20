package com.qgd.yfb.assist.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by yangke on 2017/5/24.
 */

public class DigestUtils {
    public static String md5sum(File file) {
        InputStream fis = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 1024];
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            return StringUtils.bytesToHexString(md5.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Throwable e) {
                    //ignore
                }
            }
        }
    }
}
