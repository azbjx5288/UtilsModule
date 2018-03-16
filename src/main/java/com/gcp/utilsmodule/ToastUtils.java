package com.gcp.utilsmodule;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	private Context context;
	private Toast toast = null;

	public ToastUtils(Context context) {
		this.context = context;
	}

	public void show(String text) {
		show(text, Toast.LENGTH_SHORT);
	}

	public void show(String text, int showLength) {
		if (toast == null) {
			toast = Toast.makeText(context, text, showLength);
		} else {
			toast.setText(text);
			toast.setDuration(showLength);
		}
		toast.show();
	}
}
