package com.example.iot;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumid, txtLight, txtAI;
    LabeledSwitch btnLED, btnPUMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTemp = findViewById(R.id.txtTemperature);
        txtHumid = findViewById(R.id.txtHumidity);
        txtLight = findViewById(R.id.txtLight);
        txtAI = findViewById(R.id.txtAI);
        btnLED = findViewById(R.id.btnLED);
        btnPUMP = findViewById(R.id.btnPUMP);

        btnLED.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                sendDataMQTT("tienbuilam/feeds/nutnhan1", isOn ? "1" : "0");
            }
        });

        btnPUMP.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                sendDataMQTT("tienbuilam/feeds/nutnhan2", isOn ? "1" : "0");
            }
        });

        startMQTT();
    }

    public void sendDataMQTT(String topic, String value) {
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);
        msg.setPayload(value.getBytes(Charset.forName("UTF-8")));

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        } catch (MqttException e) {
            Log.e("MQTT", "Error Publishing: " + e.getMessage());
        }
    }

    public void startMQTT() {
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {}

            @Override
            public void connectionLost(Throwable cause) {}

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("MQTT", topic + " * " + message.toString());
                if (topic.contains("cambien1")) {
                    txtTemp.setText(message.toString() + "°C");
                } else if (topic.contains("cambien3")) {
                    txtHumid.setText(message.toString() + "%");
                } else if (topic.contains("cambien2")) {
                    txtLight.setText(message.toString() + " Lux");
                } else if (topic.contains("ai")) {
                    txtAI.setText(message.toString());
                } else if (topic.equals("tienbuilam/feeds/nutnhan1")) {
                    btnLED.setOn(message.toString().equals("1"));
                } else if (topic.equals("tienbuilam/feeds/nutnhan2")) {
                    btnPUMP.setOn(message.toString().equals("1"));
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
    }
}
