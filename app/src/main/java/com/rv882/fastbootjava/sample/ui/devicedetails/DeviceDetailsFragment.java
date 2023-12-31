package com.rv882.fastbootjava.sample.ui.devicedetails;

import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.rv882.fastbootjava.sample.R;
import com.rv882.fastbootjava.sample.data.FastbootDevice;

public class DeviceDetailsFragment extends Fragment {

    private DeviceDetailsViewModel viewModel;

    private TextView deviceTextView;
    
    public static DeviceDetailsFragment newInstance(String deviceId) {
        DeviceDetailsFragment fragment = new DeviceDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", deviceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(DeviceDetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_details, container, false);
        
        deviceTextView = view.findViewById(R.id.tv_device);
        
        viewModel.fastbootDevice.observe(getViewLifecycleOwner(), new Observer<FastbootDevice>() {
                @Override
                public void onChanged(FastbootDevice p1) {
                    if (p1 != null) {
                        String ddd = "DeviceId: " + p1.getDeviceId();
                        ddd += '\n';
                        ddd = "SerialNumber:: " + p1.getSerialNumber();
                        ddd += '\n';
                        ddd = "CurrentSlot: " + p1.getCurrentSlot();
                        deviceTextView.setText(ddd);
                    }
                }
            });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String deviceId = getArguments().getString("deviceId");
        viewModel.connectToDevice(deviceId);
    }
}
