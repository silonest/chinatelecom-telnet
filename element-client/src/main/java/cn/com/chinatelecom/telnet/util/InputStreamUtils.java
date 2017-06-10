package cn.com.chinatelecom.telnet.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class InputStreamUtils {
  private static final String DEFAULT_TERMINATOR = "\r\n";
  private static final Charset DEFAULT_INPUT_CHARSET = Charset.forName("GBK");
  private static final int DEFAULT_BUFFER_SIZE = 1024;

  public static String read(InputStream input, Charset charset, String pattern) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
    CharBuffer charBuffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
    StringBuilder sb = new StringBuilder();
    while (reader.read(charBuffer) != -1) {
      charBuffer.flip();
      String read = charBuffer.toString().trim();
      System.out.println(read);
      sb.append(read);
      if (sb.toString().endsWith(pattern)) {
        break;
      }
    }
    return sb.toString();
  }

  // public static String read(InputStream input) throws IOException {
  // String result = read(input, DEFAULT_INPUT_CHARSET);
  // return result;
  // }
  //
  // public static String read(InputStream input, String charsetName) throws IOException {
  // String result = read(input, Charset.forName(charsetName));
  // return result;
  // }

  public static String readStr(InputStream input, String terminator) {
    InputStreamReader inputStream = new InputStreamReader(input, DEFAULT_INPUT_CHARSET);
    StringBuilder sb = new StringBuilder();
    return null;
  }
}
