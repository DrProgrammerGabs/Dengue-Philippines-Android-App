package com.p000hl.android.core.utils;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.http.Headers;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;

/* renamed from: com.hl.android.core.utils.WebUtils */
public class WebUtils {
    public static StringBuffer errorMessage = new StringBuffer();

    /* renamed from: com.hl.android.core.utils.WebUtils$CallBack */
    public interface CallBack {
        void failAction(String str);

        void successAction(String str);
    }

    private WebUtils() {
    }

    public static boolean isConnectingToInternet(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService("connectivity");
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo state : info) {
                    if (state.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String downLoadResource(String urlPath, String localPath) {
        if (new File(localPath).exists()) {
            return localPath;
        }
        HttpURLConnection.setFollowRedirects(true);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlPath).openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod(HttpGet.METHOD_NAME);
            connection.setRequestProperty(HTTP.USER_AGENT, "Mozilla/4.0(compatible;MSIE7.0;windows NT 5)");
            connection.setRequestProperty(HTTP.CONTENT_TYPE, "text/html");
            Log.i("hl", "正在下载资源：" + urlPath);
            if (connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                File output = new File(localPath).getAbsoluteFile();
                if (!output.exists()) {
                    File parent = output.getParentFile();
                    if (parent != null) {
                        parent.mkdirs();
                    }
                    output.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(output, false);
                byte[] data = new byte[1024];
                while (true) {
                    int read = in.read(data);
                    if (read != -1) {
                        out.write(data, 0, read);
                    } else {
                        out.flush();
                        out.close();
                        in.close();
                        connection.disconnect();
                        StringBuffer sb = new StringBuffer("下载完毕");
                        sb.append("/n 下载的网址是");
                        sb.append(urlPath);
                        sb.append("/n 下载的文件路径是");
                        sb.append(localPath);
                        Log.i("hl", sb.toString());
                        return localPath;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer sb2 = new StringBuffer("下载失败");
        sb2.append("/n 下载的网址是");
        sb2.append(urlPath);
        sb2.append("/n 下载的文件路径是");
        sb2.append(localPath);
        Log.i("hl", sb2.toString());
        return "";
    }

    public static String getUrlContent(String urlPath, String encoding) {
        String strEncoding;
        HttpURLConnection.setFollowRedirects(true);
        while (true) {
            try {
                int index = urlPath.indexOf(" ");
                if (index < 0) {
                    break;
                }
                urlPath = urlPath.substring(0, index) + "%20" + urlPath.substring(index + 1);
            } catch (Exception e) {
            }
        }
        URL url = new URL(urlPath);
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty(HTTP.USER_AGENT, "Mozilla/4.0(compatible;MSIE7.0;windows NT 5)");
            con.setRequestProperty(HTTP.CONTENT_TYPE, "text/html");
            con.setConnectTimeout(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            con.connect();
            if (con.getResponseCode() != 200) {
                Log.i("hl", "获取网址页面内容失败时返回值为：" + con.getResponseCode() + ",网址为：" + urlPath);
                con.disconnect();
                URL url2 = url;
                return null;
            }
            String strContentType = con.getContentType();
            if (strContentType == null || strContentType.trim().length() == 0 || strContentType.indexOf("charset=") <= 0) {
                strEncoding = con.getContentEncoding();
            } else {
                strEncoding = strContentType.substring(strContentType.indexOf("charset=") + 8);
            }
            StringBuffer sbContent = new StringBuffer();
            if (strEncoding == null || strEncoding.trim().length() == 0) {
                strEncoding = encoding;
            }
            BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream(), strEncoding));
            for (String strContentLine = bf.readLine(); strContentLine != null; strContentLine = bf.readLine()) {
                sbContent.append(strContentLine);
            }
            String strResult = sbContent.toString();
            StringBuffer sb = new StringBuffer("下载");
            if (StringUtils.isEmpty(strResult)) {
                sb.append("成功");
            } else {
                sb.append("失败");
            }
            sb.append("/n 下载的网址是");
            sb.append(urlPath);
            Log.i("hl", sb.toString());
            return strResult;
        } catch (Exception e2) {
            URL url3 = url;
            Log.e("hl", "获取网页内容时发生错误,网页地址是:" + urlPath);
            return "";
        }
    }

    public static void getUrlContent(String strUrl, String encoding, CallBack c) {
        new GetUrlContentTask(strUrl, encoding, c).execute(new String[0]);
    }

    public static void downloadWebResource(String resUrl, String localPath, CallBack c) throws Exception {
        new DownAsyncTask(resUrl, localPath, c).execute(new String[0]);
    }

    public static String optDataToUrl(String postUrl, HashMap<String, String> formData) {
        String result = "";
        try {
            if (!postUrl.contains("?")) {
                postUrl = postUrl + "?";
            }
            new ArrayList();
            for (String strName : formData.keySet()) {
                postUrl = postUrl + "&" + strName + "=" + ((String) formData.get(strName));
            }
            HttpResponse response = new DefaultHttpClient(new BasicHttpParams()).execute(new HttpOptions(postUrl));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                StringBuffer contentBuffer = new StringBuffer();
                InputStream in = response.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(in, response.getEntity().getContentEncoding() == null ? "utf-8" : response.getEntity().getContentEncoding().getValue());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                while (true) {
                    String inputLine = reader.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    contentBuffer.append(inputLine);
                    contentBuffer.append("\r\n");
                }
                in.close();
                result = contentBuffer.toString();
            } else if (statusCode == 301 || statusCode == 302) {
                Header locationHeader = response.getFirstHeader(Headers.LOCATION);
                if (locationHeader != null) {
                    return getUrlContent(locationHeader.getValue(), "utf-8");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String deleteDataToUrl(String postUrl, HashMap<String, String> formData) {
        String result = "";
        try {
            if (!postUrl.contains("?")) {
                postUrl = postUrl + "?";
            }
            new ArrayList();
            for (String strName : formData.keySet()) {
                postUrl = postUrl + "&" + strName + "=" + ((String) formData.get(strName));
            }
            HttpResponse response = new DefaultHttpClient(new BasicHttpParams()).execute(new HttpDelete(postUrl));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                StringBuffer contentBuffer = new StringBuffer();
                InputStream in = response.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(in, response.getEntity().getContentEncoding() == null ? "utf-8" : response.getEntity().getContentEncoding().getValue());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                while (true) {
                    String inputLine = reader.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    contentBuffer.append(inputLine);
                    contentBuffer.append("\r\n");
                }
                in.close();
                result = contentBuffer.toString();
            } else if (statusCode == 301 || statusCode == 302) {
                Header locationHeader = response.getFirstHeader(Headers.LOCATION);
                if (locationHeader != null) {
                    return getUrlContent(locationHeader.getValue(), "utf-8");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String putDataToUrl(String postUrl, HashMap<String, String> formData) {
        String result = "";
        try {
            if (!postUrl.contains("?")) {
                postUrl = postUrl + "?";
            }
            new ArrayList();
            for (String strName : formData.keySet()) {
                postUrl = postUrl + "&" + strName + "=" + ((String) formData.get(strName));
            }
            HttpResponse response = new DefaultHttpClient(new BasicHttpParams()).execute(new HttpPut(postUrl));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                StringBuffer contentBuffer = new StringBuffer();
                InputStream in = response.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(in, response.getEntity().getContentEncoding() == null ? "utf-8" : response.getEntity().getContentEncoding().getValue());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                while (true) {
                    String inputLine = reader.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    contentBuffer.append(inputLine);
                    contentBuffer.append("\r\n");
                }
                in.close();
                result = contentBuffer.toString();
            } else if (statusCode == 301 || statusCode == 302) {
                Header locationHeader = response.getFirstHeader(Headers.LOCATION);
                if (locationHeader != null) {
                    return getUrlContent(locationHeader.getValue(), "utf-8");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String headDataToUrl(String postUrl, HashMap<String, String> formData) {
        String result = "";
        try {
            if (!postUrl.contains("?")) {
                postUrl = postUrl + "?";
            }
            new ArrayList();
            for (String strName : formData.keySet()) {
                postUrl = postUrl + "&" + strName + "=" + ((String) formData.get(strName));
            }
            HttpResponse response = new DefaultHttpClient(new BasicHttpParams()).execute(new HttpHead(postUrl));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                StringBuffer contentBuffer = new StringBuffer();
                InputStream in = response.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(in, response.getEntity().getContentEncoding() == null ? "utf-8" : response.getEntity().getContentEncoding().getValue());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                while (true) {
                    String inputLine = reader.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    contentBuffer.append(inputLine);
                    contentBuffer.append("\r\n");
                }
                in.close();
                result = contentBuffer.toString();
            } else if (statusCode == 301 || statusCode == 302) {
                Header locationHeader = response.getFirstHeader(Headers.LOCATION);
                if (locationHeader != null) {
                    return getUrlContent(locationHeader.getValue(), "utf-8");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDataToUrl(String postUrl, HashMap<String, String> formData) {
        String result = "";
        try {
            if (!postUrl.contains("?")) {
                postUrl = postUrl + "?";
            }
            new ArrayList();
            for (String strName : formData.keySet()) {
                postUrl = postUrl + "&" + strName + "=" + ((String) formData.get(strName));
            }
            HttpResponse response = new DefaultHttpClient(new BasicHttpParams()).execute(new HttpGet(postUrl));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                StringBuffer contentBuffer = new StringBuffer();
                InputStream in = response.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(in, response.getEntity().getContentEncoding() == null ? "utf-8" : response.getEntity().getContentEncoding().getValue());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                while (true) {
                    String inputLine = reader.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    contentBuffer.append(inputLine);
                    contentBuffer.append("\r\n");
                }
                in.close();
                result = contentBuffer.toString();
            } else if (statusCode == 301 || statusCode == 302) {
                Header locationHeader = response.getFirstHeader(Headers.LOCATION);
                if (locationHeader != null) {
                    return getUrlContent(locationHeader.getValue(), "utf-8");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String postDataToUrl(String postUrl, HashMap<String, String> formData) {
        String result = "";
        try {
            List<NameValuePair> list = new ArrayList<>();
            for (String strName : formData.keySet()) {
                list.add(new BasicNameValuePair(strName, (String) formData.get(strName)));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
            HttpPost post = new HttpPost(postUrl);
            post.setEntity(entity);
            HttpResponse response = new DefaultHttpClient(new BasicHttpParams()).execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                StringBuffer contentBuffer = new StringBuffer();
                InputStream in = response.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(in, response.getEntity().getContentEncoding() == null ? "utf-8" : response.getEntity().getContentEncoding().getValue());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while (true) {
                    String inputLine = bufferedReader.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    contentBuffer.append(inputLine);
                    contentBuffer.append("\r\n");
                }
                in.close();
                result = contentBuffer.toString();
            } else if (statusCode == 301 || statusCode == 302) {
                Header locationHeader = response.getFirstHeader(Headers.LOCATION);
                if (locationHeader != null) {
                    return getUrlContent(locationHeader.getValue(), "utf-8");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
