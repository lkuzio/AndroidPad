package xyz.javista.androidpad;

import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class DeviceListItemClickHandler {
    private FullscreenActivity fullscreenActivity;

    public DeviceListItemClickHandler(FullscreenActivity fullscreenActivity) {
        this.fullscreenActivity = fullscreenActivity;
    }

    public AdapterView.OnItemClickListener invoke() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selected = (String) fullscreenActivity.listView.getItemAtPosition(position);
                BluetoothDevice device = fullscreenActivity.getDevices().get(selected);
                fullscreenActivity.getDevicesLayout().setVisibility(View.INVISIBLE);
                fullscreenActivity.getControlPanel().setVisibility(View.VISIBLE);
                TextView deviceLabel = (TextView) fullscreenActivity.findViewById(R.id.device_name);
                deviceLabel.setText(selected);
                Thread connectThread = new Thread(new ConnectThread(device, fullscreenActivity.getmBluetoothAdapter()));
                connectThread.start();
            }
        };
    }
}
