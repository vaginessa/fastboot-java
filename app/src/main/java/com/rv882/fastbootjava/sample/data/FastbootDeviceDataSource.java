package com.rv882.fastbootjava.sample.data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import androidx.paging.PositionalDataSource;
import androidx.paging.PositionalDataSource.LoadInitialParams;
import androidx.paging.PositionalDataSource.LoadRangeParams;
import androidx.paging.PositionalDataSource.LoadInitialCallback;
import androidx.paging.PositionalDataSource.LoadRangeCallback;
import androidx.paging.DataSource;

import com.rv882.fastbootjava.FastbootDeviceManagerListener;
import com.rv882.fastbootjava.FastbootDeviceContext;
import com.rv882.fastbootjava.FastbootDeviceManager;

public class FastbootDeviceDataSource extends PositionalDataSource<FastbootDevice> {

    public static DataSource.Factory<Integer, FastbootDevice> FACTORY;
    private static FastbootDeviceDataSource ss = new FastbootDeviceDataSource();
    
    private static FastbootDeviceManagerListener fastbootDeviceManagerListener = new FastbootDeviceManagerListener() {
        @Override
        public void onFastbootDeviceAttached(String deviceId) {
            ss.invalidate();
        }

        @Override
        public void onFastbootDeviceDetached(String deviceId) {
            ss.invalidate();
        }

        @Override
        public void onFastbootDeviceConnected(String deviceId, FastbootDeviceContext deviceContext) {
            
        }
    };

    static {
        FACTORY = new DataSource.Factory<Integer, FastbootDevice>() {
            private FastbootDeviceDataSource lastSource = null;

            @Override
            public DataSource<Integer, FastbootDevice> create() {
                if (lastSource != null) {
                    FastbootDeviceManager.INSTANCE.removeFastbootDeviceManagerListener(fastbootDeviceManagerListener);
                }
                lastSource = new FastbootDeviceDataSource();
                return lastSource;
            }
        };
        
        FastbootDeviceManager.INSTANCE.addFastbootDeviceManagerListener(fastbootDeviceManagerListener);
    }

    @Override
    public void loadInitial(PositionalDataSource.LoadInitialParams p1, PositionalDataSource.LoadInitialCallback<FastbootDevice> p2) {
        List<String> allItems = FastbootDeviceManager.INSTANCE.getAttachedDeviceIds();
        List<FastbootDevice> items = allItems.stream()
            .map(new Function<String, FastbootDevice>() {
                @Override
                public FastbootDevice apply(String p1) {
                    return FastbootDevice.fromDeviceId(p1);
                }
            }).collect(Collectors.toList());
        if (p1.placeholdersEnabled)
            p2.onResult(items, p1.requestedStartPosition, allItems.size());
        else
            p2.onResult(items, p1.requestedStartPosition);
    }

    @Override
    public void loadRange(PositionalDataSource.LoadRangeParams p1, PositionalDataSource.LoadRangeCallback<FastbootDevice> p2) {
        List<FastbootDevice> items = FastbootDeviceManager.INSTANCE.getAttachedDeviceIds().stream()
            .map(new Function<String, FastbootDevice>() {
                @Override
                public FastbootDevice apply(String p1) {
                    return FastbootDevice.fromDeviceId(p1);
                }
            }).collect(Collectors.toList());
        p2.onResult(items);
    }
}
