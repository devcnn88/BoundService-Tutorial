package com.example.boundservicetutorial;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private ServiceConnection sConn;
	private Messenger messenger;
	private TextView txtResult;
	private EditText edt;
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtResult = (TextView) findViewById(R.id.txtResult);
		edt = (EditText) findViewById(R.id.et);
		sConn = new ServiceConnection()
		{

			@Override
			public void onServiceDisconnected(ComponentName name)
			{
				messenger = null;
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service)
			{
				// We are conntected to the service
				messenger = new Messenger(service);

			}
		};

		// We bind to the service
		bindService(new Intent(this, ConvertService.class), sConn,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v)
	{

		String val = edt.getText().toString();
		Message msg = Message.obtain(null, ConvertService.TO_UPPER_CASE);

		msg.replyTo = new Messenger(new ResponseHandler());
		// We pass the value
		Bundle b = new Bundle();
		b.putString("data", val);

		msg.setData(b);

		try
		{
			messenger.send(msg);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}

	}

	class ResponseHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			int respCode = msg.what;

			switch (respCode)
			{
				case ConvertService.TO_UPPER_CASE_RESPONSE:
					{
						result = msg.getData().getString("respData");
						txtResult.setText(result);
					}
			}
		}

	}
}
