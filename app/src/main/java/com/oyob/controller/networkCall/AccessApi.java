package com.oyob.controller.networkCall;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.oyob.controller.interfaces.TaskCompleted;
import com.oyob.controller.utils.Constant;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;




public class AccessApi extends AsyncTask<Void, Void, String> {

    public static final short GET_METHOD = 0;
    public static final short POST_METHOD = 1;
    public static final short PUT_METHOD = 2;
    public static final String ID = "Id";
    public static final short DELETE_METHOD = 3;


    private static DataOutputStream dataOutputStream;

    private String serviceUrl;
    Properties properties;
    short headerType;
    Context context;
    TaskCompleted completed;
    ProgressDialog progressBar;
    boolean hasToShowProgress = false;
    Properties header;
    int statusCode;
    private int resultTpye;


    public AccessApi(String serviceUrl, String cookie, Context context,
                     short headerType, Properties properties, Properties header,
                     final TaskCompleted taskCompleted, boolean hasToShowProgress,
                     final int resultType) {
        Log.d("serviceUrl", serviceUrl);
        this.serviceUrl = serviceUrl;

        this.resultTpye = resultType;
        this.context = context;
        completed = taskCompleted;
        this.headerType = headerType;
        this.properties = properties;
        this.header = header;
        this.hasToShowProgress = hasToShowProgress;


        if (hasToShowProgress) {
            progressBar = new ProgressDialog(context);

         /*   //progressBar.setBarColor(context.getResources().getColor(Constant.DEFAULT_PROGRESS_COLOR));
            progressBar.setCancelable(new ProgressDialog.OnCancel() {
                @Override
                public void onCancelDone() {
                    completed.onResult(Constant.RESULT_CANCEL, -1, resultType);
                    completed = null;
                    AccessApi.this.cancel(true);
                }
            });

            progressBar.setCancelable(false);
            progressBar.setTranslucent();
            Log.d("progress bar","PB Created");
*/
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (hasToShowProgress) {
            // progressBar.show();
            Log.d("progress bar","PreExecute PB Show");
            progressBar.show();
        }

    }

    @Override
    protected String doInBackground(Void... params) {
        String data = "";

        HttpURLConnection urlConnection = null;
        String inputdata = "";
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) urlToRequest
                    .openConnection();

            switch (headerType) {
                case GET_METHOD:
                    urlConnection.setRequestProperty("Accept",
                            "application/json;odata=verbose");
                    if (header != null) {
                        Set<String> propertyName = header.stringPropertyNames();
                        for (String p : propertyName) {
                            String val = header.getProperty(p);
                            Log.d(Constant.TAG, "Header:" + p + "=>" + val);
                            urlConnection.setRequestProperty(p, val);
                        }
                    }

                    break;
                case PUT_METHOD:
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    if (properties != null) {
                        inputdata = properties.getProperty(ID);
                    }

                    urlConnection.setRequestProperty("Accept", "application/json;odata=verbose");
                    urlConnection.setRequestProperty("Accept-Charset", "UTF-8");


                    urlConnection.setRequestProperty("Content-Length",
                            Integer.toString(inputdata.getBytes("UTF-8").length));

                    urlConnection.setRequestProperty("Content-Type", "application/json;odata=verbose");
                    if (header != null) {
                        Set<String> propertyName = header.stringPropertyNames();
                        for (String p : propertyName) {
                            String val = header.getProperty(p);
                            Log.d(Constant.TAG, "Header:" + p + "=>" + val);
                            urlConnection.setRequestProperty(p, val);
                        }
                    }


                    dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                    dataOutputStream.write(inputdata.getBytes("UTF-8"));
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    dataOutputStream = null;
                    break;
                case POST_METHOD:
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    if (properties != null) {
                        inputdata = properties.getProperty(ID);
                        Log.d(Constant.TAG, "API Request:"+inputdata);
                    }


                    urlConnection.setRequestProperty("Accept",
                            "application/json;odata=verbose");
                    urlConnection.setRequestProperty("Accept-Charset", "UTF-8");


                    urlConnection.setRequestProperty("Content-Length",
                            Integer.toString(inputdata.getBytes("UTF-8").length));

                    urlConnection.setRequestProperty("Content-Type", "application/json;odata=verbose");
                    if (header != null) {
                        Set<String> propertyName = header.stringPropertyNames();
                        for (String p : propertyName) {
                            String val = header.getProperty(p);
                            Log.d(Constant.TAG, "Header:" + p + "=>" + val);
                            urlConnection.setRequestProperty(p, val);
                        }
                    }


                    dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                    dataOutputStream.write(inputdata.getBytes("UTF-8"));
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    dataOutputStream = null;
                    break;
                case DELETE_METHOD:
                    urlConnection.setRequestMethod("DELETE");
                    urlConnection.setRequestProperty("Accept",
                            "application/json;odata=verbose");
                    if (header != null) {
                        Set<String> propertyName = header.stringPropertyNames();
                        for (String p : propertyName) {
                            String val = header.getProperty(p);
                            Log.d(Constant.TAG, "Header:" + p + "=>" + val);
                            urlConnection.setRequestProperty(p, val);
                        }
                    }

                    break;
                default:
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    if (properties != null) {
                        inputdata = properties.getProperty(ID);
                    }


                    urlConnection.setRequestProperty("Accept",
                            "application/json;odata=verbose");
                    urlConnection.setRequestProperty("Accept-Charset", "UTF-8");


                    urlConnection.setRequestProperty("Content-Length",
                            Integer.toString(inputdata.getBytes("UTF-8").length));

                    urlConnection.setRequestProperty("Content-Type", "application/json;odata=verbose");
                    if (header != null) {
                        Set<String> propertyName = header.stringPropertyNames();
                        for (String p : propertyName) {
                            String val = header.getProperty(p);
                            Log.d(Constant.TAG, "Header:" + p + "=>" + val);
                            urlConnection.setRequestProperty(p, val);
                        }
                    }


                    dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                    dataOutputStream.write(inputdata.getBytes("UTF-8"));
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    dataOutputStream = null;
                    break;

            }

            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(100000);
            // handle issues
            statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.d("URL Data ", "HTTP_UNAUTHORIZED");
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                Log.d("URL Data ", "HTTP_NOTOK " + statusCode);
            }

            // create JSON object from content
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                data = getResponseText(in);
            } else {
                InputStream in = new BufferedInputStream(
                        urlConnection.getErrorStream());
                data = getResponseText(in);
            }
            try {
//                FileOutputStream fos = context.openFileOutput("DayTwentyTwoFile", Context.MODE_PRIVATE);
//                fos.write(data.getBytes());
//                fos.close();
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        } catch (MalformedURLException e) {
            //data = "";
            Log.e("Error ", "MalformedURLException");
        } catch (SocketTimeoutException e) {
            //data = "";
            Log.e("Error ", "SocketTimeoutException");
        } catch (IOException e) {

            e.printStackTrace();
            //data = "";

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }


        }
        return data;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
         if (hasToShowProgress) {
             Log.d("progress bar","POst execute PB");
            if (progressBar != null) {
                progressBar.dismiss();
                Log.d("progress bar","Poist Execute PB Dismiss");
            }
        }
        if (completed != null) {
            Log.d("API Response", result);
            completed.onResult(result, statusCode, resultTpye);
        }
    }

    private static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

}
