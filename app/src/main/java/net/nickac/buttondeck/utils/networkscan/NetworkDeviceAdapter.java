package net.nickac.buttondeck.utils.networkscan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.nickac.buttondeck.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NickAc on 30/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class NetworkDeviceAdapter extends ArrayAdapter<NetworkDevice> {
    private List<NetworkDevice> devices;

    public NetworkDeviceAdapter(@NonNull Context context, List<NetworkDevice> networkDevices) {
        super(context, 0);
        devices = networkDevices != null ? networkDevices : new ArrayList<>();
        notifyDataSetChanged();
    }


    @Nullable
    @Override
    public NetworkDevice getItem(int position) {
        return devices.get(position);
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    public List<NetworkDevice> getDevices() {
        return devices;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        NetworkDevice device = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_networkdevice, parent, false);
        }
        if (device != null) {
            TextView deviceName = convertView.findViewById(R.id.textView5);
            deviceName.setText(getContext().getString(R.string.device_name) + " " + device.getDeviceName());

            TextView deviceIp = convertView.findViewById(R.id.textView4);
            deviceIp.setText(getContext().getString(R.string.device_ip) + " " + device.getIp());
        }
        return convertView;
    }

    public void setAddresses(List<NetworkDevice> addresses) {
        this.devices = addresses;
    }
}
