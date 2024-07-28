package com.craxiom.networksurvey.fragments.model;

import com.craxiom.mqttlibrary.connection.BrokerConnectionInfo;
import com.craxiom.networksurvey.mqtt.MqttConnectionInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Holds the MQTT Broker connection settings scanned from a QR Code
 *
 * @since 1.7.0
 */
public record MqttConnectionSettings(
        @SerializedName("mqtt_host") String host,
        @SerializedName("mqtt_port") int port,
        @SerializedName("mqtt_tls") Boolean tlsEnabled,
        @SerializedName("mqtt_client") String deviceName,
        @SerializedName("mqtt_username") String mqttUsername,
        @SerializedName("mqtt_password") String mqttPassword,
        @SerializedName("mqtt_topic_prefix") String mqttTopicPrefix,
        @SerializedName("cellular_stream_enabled") Boolean cellularStreamEnabled,
        @SerializedName("wifi_stream_enabled") Boolean wifiStreamEnabled,
        @SerializedName("bluetooth_stream_enabled") Boolean bluetoothStreamEnabled,
        @SerializedName("gnss_stream_enabled") Boolean gnssStreamEnabled,
        @SerializedName("device_status_stream_enabled") Boolean deviceStatusStreamEnabled // TODO Handle these fields in the QR Code scanner
) implements Serializable
{

    public static class Builder
    {
        private String host;
        private int port;
        private Boolean tlsEnabled;
        private String deviceName;
        private String mqttUsername;
        private String mqttPassword;
        private String mqttTopicPrefix;
        private Boolean cellularStreamEnabled;
        private Boolean wifiStreamEnabled;
        private Boolean bluetoothStreamEnabled;
        private Boolean gnssStreamEnabled;
        private Boolean deviceStatusStreamEnabled;

        public Builder host(String host)
        {
            this.host = host;
            return this;
        }

        public Builder port(int port)
        {
            this.port = port;
            return this;
        }

        public Builder tlsEnabled(Boolean tlsEnabled)
        {
            this.tlsEnabled = tlsEnabled;
            return this;
        }

        public Builder deviceName(String deviceName)
        {
            this.deviceName = deviceName;
            return this;
        }

        public Builder mqttUsername(String mqttUsername)
        {
            this.mqttUsername = mqttUsername;
            return this;
        }

        public Builder mqttPassword(String mqttPassword)
        {
            this.mqttPassword = mqttPassword;
            return this;
        }

        public Builder mqttTopicPrefix(String mqttTopicPrefix)
        {
            this.mqttTopicPrefix = mqttTopicPrefix;
            return this;
        }

        public Builder cellularStreamEnabled(Boolean cellularStreamEnabled)
        {
            this.cellularStreamEnabled = cellularStreamEnabled;
            return this;
        }

        public Builder wifiStreamEnabled(Boolean wifiStreamEnabled)
        {
            this.wifiStreamEnabled = wifiStreamEnabled;
            return this;
        }

        public Builder bluetoothStreamEnabled(Boolean bluetoothStreamEnabled)
        {
            this.bluetoothStreamEnabled = bluetoothStreamEnabled;
            return this;
        }

        public Builder gnssStreamEnabled(Boolean gnssStreamEnabled)
        {
            this.gnssStreamEnabled = gnssStreamEnabled;
            return this;
        }

        public Builder deviceStatusStreamEnabled(Boolean deviceStatusStreamEnabled)
        {
            this.deviceStatusStreamEnabled = deviceStatusStreamEnabled;
            return this;
        }

        public MqttConnectionSettings build()
        {
            return new MqttConnectionSettings(host, port, tlsEnabled, deviceName, mqttUsername, mqttPassword, mqttTopicPrefix, cellularStreamEnabled, wifiStreamEnabled, bluetoothStreamEnabled, gnssStreamEnabled, deviceStatusStreamEnabled);
        }
    }

    public BrokerConnectionInfo toMqttConnectionInfo()
    {
        return new MqttConnectionInfo(host, port, tlsEnabled, deviceName, mqttUsername, mqttPassword, cellularStreamEnabled, wifiStreamEnabled, bluetoothStreamEnabled, gnssStreamEnabled, deviceStatusStreamEnabled, mqttTopicPrefix);
    }
}
