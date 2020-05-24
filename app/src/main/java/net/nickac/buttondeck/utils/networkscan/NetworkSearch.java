package net.nickac.buttondeck.utils.networkscan;

import android.os.AsyncTask;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by NickAc on 30/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 */
public class NetworkSearch {

    public static String intToIp(int i) {

        return ((i >> 24) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                (i & 0xFF);
    }

    public static class AsyncScan extends AsyncTask<NetworkDeviceAdapter, Void, List<NetworkDevice>> {

        private Runnable afterCompletion;
        private NetworkDeviceAdapter adapter;

        public AsyncScan(NetworkDeviceAdapter adapter) {
            this.adapter = adapter;
        }

        public static boolean isValidIp4Address(final String hostName) {
            try {
                return Inet4Address.getByName(hostName) != null;
            } catch (UnknownHostException ex) {
                return false;
            }
        }

        public static String getLocalIpv4Address() {
            try {
                String ipv4;
                List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
                if (nilist.size() > 0) {
                    for (NetworkInterface ni : nilist) {
                        List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                        if (ialist.size() > 0) {
                            for (InetAddress address : ialist) {
                                if (!address.isLoopbackAddress() && isValidIp4Address(ipv4 = address.getHostAddress())) {
                                    return ipv4;
                                }
                            }
                        }

                    }
                }

            } catch (SocketException ex) {

            }
            return "";
        }

        public static String getIpAddress() {
            try {
                for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = (NetworkInterface) en.nextElement();
                    for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            String ipAddress = inetAddress.getHostAddress();
                            Log.e("IP address", "" + ipAddress);
                           return ipAddress;
                            //return "127.0.0.1";
                        }
                    }
                }
            } catch (SocketException ex) {
            }
            return null;
        }

        public void setAfterCompletion(Runnable afterCompletion) {
            this.afterCompletion = afterCompletion;
        }

        @Override
        protected List<NetworkDevice> doInBackground(NetworkDeviceAdapter... voids) {
            String ipString = getIpAddress();
            if (ipString == null) {
                return new ArrayList<>(1);
            }

            int lastDot = ipString.lastIndexOf(".");
            ipString = ipString.substring(0, lastDot);

            List<NetworkDevice> addresses = Pinger.getDevicesOnNetwork(ipString);
            adapter = voids[0];
            return addresses;
        }

        @Override
        protected void onPostExecute(List<NetworkDevice> inetAddresses) {
            super.onPostExecute(inetAddresses);
            adapter.setAddresses(inetAddresses);
            adapter.notifyDataSetChanged();
            if (afterCompletion != null) {
                afterCompletion.run();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

    }

}
