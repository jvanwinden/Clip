package com.jvw.clip;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;


/**
 * The configuration screen for the {@link SendWidget SendWidget} AppWidget.
 */
public class SendWidgetConfigureActivity extends Activity implements View.OnClickListener {

	private static ServerDataBase data;
	private ArrayAdapter<Server> adapter;
	private Spinner spinner;
	private int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_widget_configure);
		setResult(RESULT_CANCELED);
		spinner = (Spinner) findViewById(R.id.widget_select_spinner);
		Button add = (Button) findViewById(R.id.widget_add_button);
		data = new ServerDataBase(this);
		adapter = new ArrayAdapter<Server>(this, R.layout.spinner_item, data.getAll().toArray(new Server[data.getAll().size()]));

		spinner.setAdapter(adapter);

		add.setOnClickListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			Log.d("CLIP", "finish early");
			finish();
		}

	}

	@Override
	public void onClick(View v) {
		Log.d("CLIP", "onClick called");
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.send_widget);


		Intent intent = new Intent(this, Main.class);
		PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);

		views.setOnClickPendingIntent(R.id.widget_send_button, pending);
		views.setTextViewText(R.id.widget_send_button, "Send to " + adapter.getItem(spinner.getSelectedItemPosition()).getName());

		SendWidget.updateWidget(this, manager, widgetId);

		Intent result = new Intent();
		result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_OK, result);
		finish();
	}
}


