package com.weibo.library;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by weibo on 17-4-14.
 */

public class FileUtils {

  /**
   * 获取文件夹对象
   *
   * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
   */
  public static File getExternalCacheDir(String folderName) {
    File file = new File(App.INSTANCE.getCacheDir()
        + File.separator + folderName + File.separator);
    file.mkdirs();
    return file;
  }

  /**
   * 输入流转byte[]
   */
  public static byte[] input2byte(InputStream inStream) {
    if (inStream == null) {
      return null;
    }
    byte[] in2b = null;
    BufferedInputStream in = new BufferedInputStream(inStream);
    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
    int rc = 0;
    try {
      while ((rc = in.read()) != -1) {
        swapStream.write(rc);
      }
      in2b = swapStream.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      closeIO(inStream, in, swapStream);
    }
    return in2b;

  }

  /**
   * 关闭流
   */
  public static void closeIO(Closeable... closeables) {
    if (null == closeables || closeables.length <= 0) {
      return;
    }
    for (Closeable cb : closeables) {
      try {
        if (null == cb) {
          continue;
        }
        cb.close();
      } catch (IOException e) {
        throw new RuntimeException(
            FileUtils.class.getClass().getName(), e);
      }
    }
  }
}
