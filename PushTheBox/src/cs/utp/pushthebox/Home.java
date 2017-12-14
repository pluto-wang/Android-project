package cs.utp.pushthebox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class Home extends Activity {
	private ImageView img;
	private ImageView hp;
	private ImageView start, mu, ti, vo;
	private int flag_m = 1, flag_v = 1, flag_t = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.home);
		img = (ImageView) findViewById(R.id.imageView2);
		hp = (ImageView) findViewById(R.id.imageButton1);
		start = (ImageView) findViewById(R.id.Enter);
		mu = (ImageView) findViewById(R.id.imageButton3);
		vo = (ImageView) findViewById(R.id.imageButton4);
		ti = (ImageView) findViewById(R.id.imageButton2);
		hp.setOnClickListener(show);
		start.setOnClickListener(startL);
		start.setOnTouchListener(startT);
		mu.setOnClickListener(music);
		vo.setOnClickListener(voice);
		ti.setOnClickListener(time);
		Animation am = new TranslateAnimation(0, 0, 0, 160);
		am.setDuration(5000);
		am.setRepeatCount(-1);
		img.setAnimation(am);
		am.startNow();
	}

	@SuppressLint("NewApi")
	private void AlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.dialog);
		builder.setTitle("Help").setMessage(
				"Just using the direction buttons to control "
						+ "the movement," + " eating fishes"
						+ " and Pushing mouses to reach the task of escaping"
						+ ",Good Luck >~<");
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		};
		builder.setNeutralButton("ok", OkClick);
		builder.show();
	}

	private OnTouchListener startT = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				start.setImageResource(R.drawable.paw_press);
			} else {
				start.setImageResource(R.drawable.paw_or);
			}
			return false;
		}

	};
	private OnClickListener startL = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(Home.this, stageSelecter.class);
			Bundle bundle = new Bundle();
			bundle.putInt("Flag_m", flag_m);
			bundle.putInt("Flag_v", flag_v);
			bundle.putInt("Flag_t", flag_t);
			i.putExtras(bundle);
			startActivityForResult(i, 1);

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			flag_m = 0;
			flag_v = 0;
			mu.setImageResource(R.drawable.nomu);
			vo.setImageResource(R.drawable.nov);
		} else if (resultCode == 1) {
			flag_m = 1;
			flag_v = 0;
			mu.setImageResource(R.drawable.muscic);
			vo.setImageResource(R.drawable.nov);
		} else if (resultCode == 2) {
			flag_v = 1;
			flag_m = 0;
			mu.setImageResource(R.drawable.nomu);
			vo.setImageResource(R.drawable.voice);
		} else if (resultCode == 3) {
			flag_v = 1;
			flag_m = 1;
			mu.setImageResource(R.drawable.muscic);
			vo.setImageResource(R.drawable.voice);
		} else if (resultCode == RESULT_OK) {
			finish();
		}
	}

	private OnClickListener music = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(), MusicService.class);
			switch (flag_m) {
			case 1:// on->off
				mu.setImageResource(R.drawable.nomu);
				i.putExtra("operate", 2);
				startService(i);
				flag_m = 0;
				break;
			case 0:// off->on
				mu.setImageResource(R.drawable.muscic);
				i.putExtra("operate", 3);
				startService(i);
				flag_m = 1;
				break;
			}
		}

	};
	private OnClickListener voice = new OnClickListener() {
		public void onClick(View v) {
			if (flag_v == 1) {// on->off
				vo.setImageResource(R.drawable.nov);
				flag_v = 0;
			} else {// off->on
				vo.setImageResource(R.drawable.voice);
				flag_v = 1;
			}
		}
	};
	private OnClickListener time = new OnClickListener() {
		public void onClick(View v) {
			if (flag_t == 1) {// on->off
				ti.setImageResource(R.drawable.notimer);
				flag_t = 0;
			} else {// off->on
				ti.setImageResource(R.drawable.timer);
				flag_t = 1;
			}
		}
	};
	private OnClickListener show = new OnClickListener() {
		public void onClick(View v) {
			AlertDialog();
		}
	};

	protected void onResume() {
		super.onResume();
		Intent i = new Intent(getApplicationContext(), MusicService.class);
		i.putExtra("operate", 0);
		if (flag_m == 1) {
			startService(i);
		}
		Settings.System.putInt(getContentResolver(),
				Settings.System.SOUND_EFFECTS_ENABLED, 0);
	}

	protected void onPause() {
		super.onPause();
		Intent i = new Intent(getApplicationContext(), MusicService.class);
		i.putExtra("operate", 1);
		startService(i);
		Settings.System.putInt(getContentResolver(),
				Settings.System.SOUND_EFFECTS_ENABLED, 1);
	}

	protected void onDestory() {
		super.onDestroy();
		Intent i = new Intent(getApplicationContext(), MusicService.class);
		stopService(i);
	}
}
