package com.example.Wifi;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.R;

public class WiFiDemo extends Activity implements OnClickListener {
	private static final String TAG = "WiFi";
	public WifiManager wifi;
	private BroadcastReceiver receiver;
	private ArrayAdapter adapter;
	private ListView listview;
	private Button buttonScan;
	String[] wifis;
	String[] networkSSID;

	/** Called when the activity is first created. */
	@Override
 	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Setup UI

		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.listView);

		adapter = new ArrayAdapter
				(this, android.R.layout.simple_list_item_1);

		setWiFiStatus();
		listview.setAdapter(adapter);
	}

	public void onToggleClicked(View view) {

		adapter.clear();

		ToggleButton toggleButton = (ToggleButton) view;

		if (wifi == null) {
			// Device does not support Wi-Fi
			Toast.makeText(getApplicationContext(), " Wi-fi indisponible",
					Toast.LENGTH_SHORT).show();
			toggleButton.setChecked(false);

		} else {
			if (toggleButton.isChecked()) { // To turn on Wi-Fi
				if (!wifi.isWifiEnabled()) {

					Toast.makeText(getApplicationContext(), "Wi-fi activé" +
									"\n" + "Recherche des points d'accès...",
							Toast.LENGTH_SHORT).show();

					wifi.setWifiEnabled(true);

				} else {
					Toast.makeText(getApplicationContext(), "Wi-Fi déjà activé" +
									"\n" + "Recherche des points d'accès...",
							Toast.LENGTH_SHORT).show();
				}

			} else { // To turn off Wi-Fi
				Toast.makeText(getApplicationContext(), "Wi-Fi désactivé",
						Toast.LENGTH_SHORT).show();
				wifi.setWifiEnabled(false);
			}
		}
	}
	private void setWiFiStatus() {
		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		if (receiver == null)
			receiver = new WiFiScanReceiver(this);

		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

	}
	public class WiFiScanReceiver extends BroadcastReceiver {
		WiFiDemo wifiDemo;

		public WiFiScanReceiver(WiFiDemo wifiDemo) {
			super();
			this.wifiDemo = wifiDemo;
		}

		@Override
		public void onReceive(Context c, Intent intent) {
			List<ScanResult> wifiScanList = wifiDemo.wifi.getScanResults();
			wifis = new String[wifiScanList.size()];
			networkSSID= new String[wifiScanList.size()];
			for (int i = 0; i < wifiScanList.size(); i++) {
				ScanResult accessPoint = wifiScanList.get(i);
				wifis[i] = accessPoint.SSID+", "+accessPoint.BSSID;
				networkSSID[i]=accessPoint.SSID;
			}

			listview.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis));
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					//FragmentManager fm = getFragmentManager();
					//FragmentTransaction ft = fm.beginTransaction();
					//ft.replace(R.id.fragment_wifi , new WifiFragment(), "fragment_screen");
					//ft.commit();
					connectWifi(networkSSID[position]);
					Toast.makeText(getApplicationContext(),
							"" + position, Toast.LENGTH_SHORT).show();

				}
			});
			}
		}


	@Override
	public void onStop() {
		super.onStop();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}

	public void onClick(View view) {
		Toast.makeText(this, "Attente point d'accès...",
				Toast.LENGTH_LONG).show();

		if (view.getId() == R.id.buttonScan) {
			Log.d(TAG, "onClick() wifi.startScan()");
			wifi.startScan();
		}

	}
	public void connectWifi( String networkSSID) {
		String networkPass = "txrobotic";

		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\"" + networkSSID + "\"";
		conf.preSharedKey = "\""+ networkPass +"\"";
		WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
		wifiManager.addNetwork(conf);
		List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
		for( WifiConfiguration i : list ) {
			if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
				wifiManager.disconnect();
				wifiManager.enableNetwork(i.networkId, true);
				wifiManager.reconnect();

				break;
			}
			Toast.makeText(this, "Connexion à "+networkSSID,
					Toast.LENGTH_LONG).show();
		}
	}

}