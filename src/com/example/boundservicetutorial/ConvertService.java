package com.example.boundservicetutorial;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class ConvertService extends Service
{
	private Messenger msg = new Messenger(new ConvertHanlder());;
	public static final int TO_UPPER_CASE = 0;
	public static final int TO_UPPER_CASE_RESPONSE = 1;

	@Override
	public IBinder onBind(Intent arg0)
	{
		return msg.getBinder();
	}

	class ConvertHanlder extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			// This is the action
			int msgType = msg.what;

			switch (msgType)
			{
				case TO_UPPER_CASE:
					{
						try
						{
							// Incoming data
							String data = msg.getData().getString("data");
							Message resp = Message.obtain(null,
									TO_UPPER_CASE_RESPONSE);
							Bundle bResp = new Bundle();
							bResp.putString("respData", data.toUpperCase());
							resp.setData(bResp);

							msg.replyTo.send(resp);
						}
						catch (RemoteException e)
						{

							e.printStackTrace();
						}
						break;
					}
				default:
					super.handleMessage(msg);
			}
		}
	}
}
