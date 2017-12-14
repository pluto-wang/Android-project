package cs.utp.pushthebox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.start);

		new Thread() {
			public void run() {

				try {
					Intent i = new Intent(MainActivity.this, Home.class);
					Thread.sleep(5000);
					startActivity(i);
					finish();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

}
