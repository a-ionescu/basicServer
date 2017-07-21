package com.spirent.pttb.basicserver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final int portNumber = 6000;
    private static final int accessP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //int permissionCheck = ContextCompat.checkSelfPermission(this,
        //       Manifest.permission.ACCESS_FINE_LOCATION);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    accessP);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    accessP);

        }


        ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });

        LocationManager LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LM.addNmeaListener(new GpsStatus.NmeaListener() {
            public void onNmeaReceived(long timestamp, String  nmea) {
                Log.d("NMEA", ":" + nmea);
            }
        });




        ServerSocket serverSocket;
        try {
            Toast.makeText(this,"createServer",Toast.LENGTH_LONG).show();
            serverSocket = new ServerSocket(portNumber);
            Socket socket = serverSocket.accept();
            Toast.makeText(this,"Accept",Toast.LENGTH_LONG).show();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bw.write("Message from the server");
            Toast.makeText(this,"Message from the server",Toast.LENGTH_LONG).show();
            //Insert new line \n
            bw.newLine();
            //Send the message
            bw.flush();

            //Receive a message from client
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //System.out.println("Message from the client: " + br.readLine());
            //System.out.println("Server has ended");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
