package cs.utp.pushthebox;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

public class MusicService extends Service {
	private MediaPlayer myplayer;

	public void onCreate() {
		myplayer = MediaPlayer.create(this, R.raw.bm);
		try {
			myplayer.setOnCompletionListener(comL);
		} catch (Exception e) {
		}
	}

	private OnCompletionListener comL = new OnCompletionListener() {
		public void onCompletion(MediaPlayer nouse) {
			try {
				myplayer.stop();
				myplayer.prepare();
				myplayer.start();// µL­­¦¸¼½©ñ
			} catch (Exception e) {
			}
		}
	};

	@Override
	public void onDestroy() {
		try {
			myplayer.stop();
			myplayer.release();
		} catch (Exception e) {
		}
	}

	public void onStart(Intent intent, int startId) {
		myplayer.start();
		int operate = intent.getIntExtra("operate", 4);
		int flag_m;
		if (myplayer.isPlaying()) {
			flag_m = 1;
		} else {
			flag_m = 0;
		}
		switch (operate) {
		case 0:
			if (!myplayer.isPlaying()) {
				myplayer.start();
			}
			break;
		case 1:
			if (myplayer.isPlaying()) {
				myplayer.pause();
			}
			break;
		case 2:
			flag_m = 0;
			if (myplayer.isPlaying()) {
				myplayer.pause();
			}
			break;
		case 3:
			flag_m = 1;
			if (!myplayer.isPlaying() && flag_m == 1) {
				myplayer.start();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
