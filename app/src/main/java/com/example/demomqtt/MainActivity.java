package com.example.demomqtt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class MainActivity extends AppCompatActivity {

    TextView recibir;
    MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recibir = findViewById(R.id.txtRecibir);

        String clientId= MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(),"tcp://broker.emqx.io:1883",clientId);
        //client = new MqttAndroidClient(this.getApplicationContext(),"tcp://test.mosquitto.org:1883",clientId);
        try{
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast t = Toast.makeText(getApplicationContext(),"CONECTADO",Toast.LENGTH_SHORT);
                    t.show();
                    String topic = "android/demo";
                    int qos = 1;
                    if(client.isConnected()){
                        try{
                            IMqttToken subToken = client.subscribe(topic,qos);
                            subToken.setActionCallback(new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {

                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                                }
                            });
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast t = Toast.makeText(getApplicationContext(),"FALLO",Toast.LENGTH_SHORT);
                    t.show();
                }
            });
        }catch (MqttException e){
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                    recibir.setText(new String(message.getPayload()));
                String TAG = "";
                Log.d(TAG,message.getPayload().toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }
}