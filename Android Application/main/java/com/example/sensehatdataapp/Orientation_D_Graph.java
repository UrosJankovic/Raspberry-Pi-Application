package com.example.sensehatdataapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Double.isNaN;

public class Orientation_D_Graph extends AppCompatActivity {

    private final double dataGraphMaxX = 10.0d;
    private final double dataGraphMinX =  0.0d;
    private final double dataGraphMaxY =  1.0d;
    private final double dataGraphMinY = -1.0d;
    private GraphView dataGraph;
    private GraphView dataGraph2;
    private GraphView dataGraph3;
    private LineGraphSeries<DataPoint> dataSeriesA;
    private LineGraphSeries<DataPoint> dataSeriesB;
    private LineGraphSeries<DataPoint> dataSeriesC;
    private final int dataGraphMaxDataPointsNumber = 1000;

    private RequestQueue queue;
    private Timer rqTimerA;
    private Timer rqTimerB;
    private Timer rqTimerC;
    private TimerTask rqTimertaskA;
    private TimerTask rqTimertaskB;
    private TimerTask rqTimertaskC;
    private final Handler handler=new Handler();
    private long rqTimerTimeStamp=0;
    private long requestTimerPreviousTime=-1;
    private boolean rqTimerFirstRequest = true;
    private boolean rqTimerFirstRequestAfterStop;

    public static String ipAddress ="192.168.0.23";
    public static int sampleTime = 500;
    public static int serverPort = 22;
    public static String PRESS_FILE="pressValues.json";
    public static String TEMP_FILE="tempValues.json";
    public static String HUMID_FILE="humidValues.json";



    TextView tv1, tv2, tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientationd__graph);

        Button SettingsBtn2= (Button) findViewById(R.id.SettingsBtn2);
        SettingsBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstIntent= new Intent(getApplicationContext(),Config.class);
                startActivity(firstIntent);
            }
        });

        /* BEGIN initialize GraphView */
        // https://github.com/jjoe64/GraphView/wiki
        dataGraph = (GraphView)findViewById(R.id.dataGraph);
        dataGraph2 = (GraphView)findViewById(R.id.dataGraph2);
        dataGraph3 = (GraphView)findViewById(R.id.dataGraph3);
        dataSeriesA = new LineGraphSeries<>(new DataPoint[]{});
        dataSeriesB = new LineGraphSeries<>(new DataPoint[]{});
        dataSeriesC = new LineGraphSeries<>(new DataPoint[]{});

        dataSeriesA.setTitle("Accelerometer");
        dataSeriesB.setTitle("Gyroscope");
        dataSeriesC.setTitle("Magnetometer");

        dataGraph2.addSeries(dataSeriesB);
        dataGraph.addSeries(dataSeriesA);
        dataGraph3.addSeries(dataSeriesC);

        dataGraph.getViewport().setXAxisBoundsManual(true);
        dataGraph.getViewport().setMinX(dataGraphMinX);
        dataGraph.getViewport().setMaxX(dataGraphMaxX);
        dataGraph.getViewport().setYAxisBoundsManual(true);
        dataGraph.getViewport().setMinY(dataGraphMinY);
        dataGraph.getViewport().setMaxY(dataGraphMaxY);
        dataGraph.getLegendRenderer().setVisible(true);
        dataGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraph.getLegendRenderer().setFixedPosition(650,-3);

        dataGraph2.getViewport().setXAxisBoundsManual(true);
        dataGraph2.getViewport().setMinX(dataGraphMinX);
        dataGraph2.getViewport().setMaxX(dataGraphMaxX);
        dataGraph2.getViewport().setYAxisBoundsManual(true);
        dataGraph2.getViewport().setMinY(dataGraphMinY);
        dataGraph2.getViewport().setMaxY(dataGraphMaxY);
        dataGraph2.getLegendRenderer().setVisible(true);
        //dataGraph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraph2.getLegendRenderer().setFixedPosition(650,82);


        dataGraph3.getViewport().setXAxisBoundsManual(true);
        dataGraph3.getViewport().setMinX(dataGraphMinX);
        dataGraph3.getViewport().setMaxX(dataGraphMaxX);
        dataGraph3.getViewport().setYAxisBoundsManual(true);
        dataGraph3.getViewport().setMinY(dataGraphMinY);
        dataGraph3.getViewport().setMaxY(dataGraphMaxY);
        dataGraph3.getLegendRenderer().setVisible(true);
        dataGraph3.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraph3.getLegendRenderer().setFixedPosition(650,165);


        /* END initialize GraphView */


        // Initialize Volley request queue
        queue = Volley.newRequestQueue(Orientation_D_Graph.this);



        tv1= (TextView) findViewById(R.id.textView9);
        tv2= (TextView) findViewById(R.id.textView10);
        tv3= (TextView) findViewById(R.id.textView11);



        tv1.setText("IP address: "+ipAddress);
        tv2.setText("Sample time: "+Integer.toString(sampleTime)+"ms");
        tv3.setText("Server Port: "+Integer.toString(serverPort));



    }


    private void startRequestTimer() {

        Bundle extras=getIntent().getExtras();
        int val1=extras.getInt("val1A");
        int val2=extras.getInt("val2A");
        int val3=extras.getInt("val3A");

        if(val1==1) {
            if (rqTimerA == null) {
                // set a new Timer
                rqTimerA = new Timer();

                // initialize the TimerTask's job
                initializeRequestTimerTaskA();

                rqTimerA.schedule(rqTimertaskA, 0, sampleTime);


                // clear error message
                //textViewError.setText("");
            }
        }else{
            dataGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        }

        if(val2==1) {
            if (rqTimerB == null) {
                // set a new Timer
                rqTimerB = new Timer();

                // initialize the TimerTask's job

                initializeRequestTimerTaskB();

                rqTimerB.schedule(rqTimertaskB, 0, sampleTime);


                // clear error message
                //textViewError.setText("");
            }
        }




        if(val3==1) {
            if (rqTimerC == null) {
                // set a new Timer
                rqTimerC = new Timer();

                // initialize the TimerTask's job

                initializeRequestTimerTaskC();

                rqTimerC.schedule(rqTimertaskC, 0, sampleTime);


                // clear error message
                //textViewError.setText("");
            }
        }
    }

    private void stopRequestTimer() {
        // stop the timer, if it's not already null
        if (rqTimerA != null) {
            rqTimerA.cancel();
            rqTimerA = null;
            rqTimerFirstRequestAfterStop = true;
        }
        if (rqTimerB != null) {
            rqTimerB.cancel();
            rqTimerA = null;
            rqTimerFirstRequestAfterStop = true;
        }
        if (rqTimerC != null) {
            rqTimerC.cancel();
            rqTimerC = null;
            rqTimerFirstRequestAfterStop = true;
        }
    }

    private void initializeRequestTimerTaskA() {
        rqTimertaskA = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() { sendGetRequestA(); }
                });
            }
        };
    }


    private void initializeRequestTimerTaskB() {
        rqTimertaskB = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() { sendGetRequestB(); }
                });
            }
        };
    }

    private void initializeRequestTimerTaskC() {
        rqTimertaskC = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() { sendGetRequestC(); }
                });
            }
        };
    }


    public void startclick(View v){
        //startRQTimer();

        Bundle extras=getIntent().getExtras();
        int val1=extras.getInt("val1A");
        int val2=extras.getInt("val2A");
        int val3=extras.getInt("val3A");

        if(val1==1 || val2==1 || val3==1) {
            startRequestTimer();
        }

           /* if (val1 == 1) {
                // tv1.setText("humidity");
              startRequestTimer();
            }


            if (val2 == 1) {
                //show graph
                startRequestTimerB();
            }


            if (val3 == 1) {
                //show graph
                startRequestTimerC();
            }*/

        if (val1 != 1 && val2 != 1 && val3 != 1) {
            Toast.makeText(Orientation_D_Graph.this, "No variables selected. \nSelect variables on main page to view.", Toast.LENGTH_LONG).show();
        }
    }


    public void stopclick(View v){

        //stopRQTimer();
        stopRequestTimer();
    }



    private String getURLA(String ip) {
        return ("http://" + ip + "/" + HUMID_FILE);
    }

    private String getURLB(String ip) {
        return ("http://" + ip + "/" + TEMP_FILE);
    }

    private String getURLC(String ip) {
        return ("http://" + ip + "/" + PRESS_FILE);
    }

    /**
     * @brief Initialize request timer period task with 'Handler' post method as 'sendGetRequest'.
     */

   /* private void errorHandling(int errorCode) {
        switch(errorCode) {
            case COMMON.ERROR_TIME_STAMP:
                textViewError.setText("ERR #1");
                Log.d("errorHandling", "Request time stamp error.");
                break;
            case COMMON.ERROR_NAN_DATA:
                textViewError.setText("ERR #2");
                Log.d("errorHandling", "Invalid JSON data.");
                break;
            case COMMON.ERROR_RESPONSE:
                textViewError.setText("ERR #3");
                Log.d("errorHandling", "GET request VolleyError.");
                break;
            default:
                textViewError.setText("ERR ??");
                Log.d("errorHandling", "Unknown error.");
                break;
        }
    }*/


    private double getRawDataFromResponseB(String response) {
        JSONObject jObject;
        double x = Double.NaN;

        // Create generic JSON object form string
        try {
            jObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return x;
        }

        // Read chart data form JSON object
        try {
            x = (double)jObject.get("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return x;
    }

    private double getRawDataFromResponseA(String response) {
        JSONObject jObject;
        double x = Double.NaN;

        // Create generic JSON object form string
        try {
            jObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return x;
        }

        // Read chart data form JSON object
        try {
            x = (double)jObject.get("data1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return x;
    }

    private double getRawDataFromResponseC(String response) {
        JSONObject jObject;
        double x = Double.NaN;

        // Create generic JSON object form string
        try {
            jObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return x;
        }

        // Read chart data form JSON object
        try {
            x = (double)jObject.get("data2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return x;
    }

    public void sendGetRequestA(){

        String url = getURLA(ipAddress);

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showGraphA(response);
                    }
                }, null);
               /* new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { errorHandling(COMMON.ERROR_RESPONSE); }
                }*/

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void sendGetRequestB(){
        String url = getURLB(ipAddress);

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showGraphB(response);
                    }
                }, null);
               /* new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { errorHandling(COMMON.ERROR_RESPONSE); }
                }*/

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void sendGetRequestC(){
        String url = getURLC(ipAddress);

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showGraphC(response);
                    }
                }, null);
               /* new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { errorHandling(COMMON.ERROR_RESPONSE); }
                }*/

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    public long timingIncrease(long currentTime){
        if(rqTimerFirstRequest)
        {
            requestTimerPreviousTime = currentTime;
            rqTimerFirstRequest = false;
            return 0;
        }

        // After each stop return value not greater than sample time
        // avoids holes in the plot
        if(rqTimerFirstRequestAfterStop)
        {
            if((currentTime - requestTimerPreviousTime) > sampleTime)
                requestTimerPreviousTime = currentTime - sampleTime;

            rqTimerFirstRequestAfterStop = false;
        }

        // If time difference is equal zero after start
        // return sample time
        if((currentTime - requestTimerPreviousTime) == 0)
            return sampleTime;

        // Return time difference between current and previous request
        return (currentTime - requestTimerPreviousTime);
    }


    public void showGraphA(String response){

        if(rqTimerA != null) {
            // get time stamp with SystemClock
            long requestTimerCurrentTime = SystemClock.uptimeMillis(); // current time
            rqTimerTimeStamp += timingIncrease(requestTimerCurrentTime);

            // get raw data from JSON response
            double rawData = getRawDataFromResponseA(response);

            // update chart
            if (isNaN(rawData)) {
                // errorHandling(COMMON.ERROR_NAN_DATA);

            } else {

                // update plot series
                double timeStamp = rqTimerTimeStamp / 1000.0; // [sec]
                boolean scrollGraph = (timeStamp > dataGraphMaxX);

                dataSeriesA.setColor(Color.GREEN);


                dataSeriesA.appendData(new DataPoint(timeStamp, rawData), scrollGraph, dataGraphMaxDataPointsNumber);


                // refresh chart
                dataGraph.onDataChanged(true, true);
            }

            // remember previous time stamp
            requestTimerPreviousTime = requestTimerCurrentTime;
        }
    }

    public void showGraphB(String response){

        if(rqTimerB != null) {
            // get time stamp with SystemClock
            long requestTimerCurrentTime = SystemClock.uptimeMillis(); // current time
            rqTimerTimeStamp += timingIncrease(requestTimerCurrentTime);

            // get raw data from JSON response
            double rawData = getRawDataFromResponseB(response);

            // update chart
            if (isNaN(rawData)) {
                // errorHandling(COMMON.ERROR_NAN_DATA);

            } else {

                // update plot series
                double timeStamp = rqTimerTimeStamp / 1000.0; // [sec]
                boolean scrollGraph = (timeStamp > dataGraphMaxX);


                dataSeriesB.setColor(Color.RED);




                dataSeriesB.appendData(new DataPoint(timeStamp, rawData), scrollGraph, dataGraphMaxDataPointsNumber);



                // refresh chart
                dataGraph2.onDataChanged(true, true);
            }

            // remember previous time stamp
            requestTimerPreviousTime = requestTimerCurrentTime;
        }else{
            dataGraph2.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        }
    }

    public void showGraphC(String response){

        if(rqTimerC != null) {
            // get time stamp with SystemClock
            long requestTimerCurrentTime = SystemClock.uptimeMillis(); // current time
            rqTimerTimeStamp += timingIncrease(requestTimerCurrentTime);

            // get raw data from JSON response
            double rawData = getRawDataFromResponseC(response);

            // update chart
            if (isNaN(rawData)) {
                // errorHandling(COMMON.ERROR_NAN_DATA);

            } else {

                // update plot series
                double timeStamp = rqTimerTimeStamp / 1000.0; // [sec]
                boolean scrollGraph = (timeStamp > dataGraphMaxX);


                dataSeriesC.setColor(Color.BLUE);


                dataSeriesC.appendData(new DataPoint(timeStamp, rawData), scrollGraph, dataGraphMaxDataPointsNumber);


                // refresh chart
                dataGraph3.onDataChanged(true, true);
            }

            // remember previous time stamp
            requestTimerPreviousTime = requestTimerCurrentTime;
        }
    }
}
