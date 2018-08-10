package cpe.dna;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

public class Parameters extends AppCompatActivity {

    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button startButton, sendButton,clearButton,stopButton;
    TextView textView;
    EditText editText;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;

    RecyclerView recyclerView;
    ParamsAdapter paramsAdapter;
    List<Params> paramsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
        startButton = (Button) findViewById(R.id.bluetoothStartButton);
        startButton.setVisibility(View.VISIBLE);
        stopButton = (Button) findViewById(R.id.bluetoothStopButton);
        stopButton.setVisibility(View.GONE);
        stringBuilder = new StringBuilder();
        textView = (TextView) findViewById(R.id.bluetoothTextView);
    }

    @Override
    protected void onPause(){
        stopThread = true;
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);
        deviceConnected=false;
        textView.setText("Connection Closed!");
        super.onPause();
    }


    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getName().equals("HC-05"))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }

    public void onClickStart(View view) {
        if(BTinit())
        {
            if(BTconnect())
            {
                deviceConnected=true;
                beginListenForData();
                textView.setText("Connection Started!");
                startButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
            }
            else{
                Toast.makeText(this,"Can't find the device!",Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(this,"Can't find the device!",Toast.LENGTH_SHORT).show();
        }
    }

    StringBuilder stringBuilder;
    String arduinoData;
    int stringTokenizerSize;
    StringTokenizer stringTokenizerr;

    void beginListenForData()
    {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {

                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {
                                    stringBuilder.append(string);
                                    arduinoData = stringBuilder.toString();
                                    Log.d("BT_TAG",arduinoData);

                                    try {
                                        StringTokenizer stringTokenizer = new StringTokenizer(arduinoData, "|");
                                        String complete = stringTokenizer.nextToken();
                                        Log.d("BT_TAG","complete: " + complete);

                                        if (!complete.equals(null)){
                                            stringTokenizerr = new StringTokenizer(complete, ":");
                                            stringTokenizerSize = stringTokenizerr.countTokens();

                                            paramsItem();

                                            if(stringTokenizer.countTokens() >= 1){
                                                stringBuilder.setLength(1);
                                            }

                                        }
                                    }
                                    catch (Exception e){}

                                }
                            });
                        }

                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }

            }
        });

        thread.start();
    }

    public void paramsItem(){
        paramsList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.paramRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d("BT_TAG", String.valueOf(stringTokenizerSize));

        //String waste = stringTokenizerr.nextToken();


        for (int i = 0; i < (stringTokenizerSize/2); i++) {

            paramsList.add(new Params(stringTokenizerr.nextToken(), stringTokenizerr.nextToken()));

            Log.d("BT_TAG", String.valueOf(stringTokenizerSize));
            Log.d("BT_TAG", String.valueOf(i));

            paramsAdapter = new ParamsAdapter(this, paramsList);
            recyclerView.setAdapter(paramsAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void onClickSend(View view) {
        String string = editText.getText().toString();
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.append("\nSent Data:"+string+"\n");

    }

    public void onClickStop(View view) throws IOException {
        stopThread = true;
        outputStream.close();
        inputStream.close();
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);
        deviceConnected=false;
        textView.setText("Connection Closed!");
    }



}
