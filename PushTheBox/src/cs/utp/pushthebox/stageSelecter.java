package cs.utp.pushthebox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class stageSelecter extends Activity {
	private ImageView mu, vo, back, exit;
	private int flag_m = 1, flag_v = 1, flag_t = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stageseleter);
		Bundle bundle = this.getIntent().getExtras();
		flag_m = bundle.getInt("Flag_m");
		flag_v = bundle.getInt("Flag_v");
		flag_t = bundle.getInt("Flag_t");
		Gallery gallery = (Gallery) findViewById(R.id.selecter);
		ImageAdapter imageAdapter = new ImageAdapter(this);
		Integer[] mImageIds = { R.drawable.stage1, R.drawable.stage2,
				R.drawable.stage3 };
		imageAdapter.setmImageIds(mImageIds);
		imageAdapter.setHeight(200);
		imageAdapter.setWidth(360);
		gallery.setAdapter(imageAdapter);
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {
				Intent i = new Intent(stageSelecter.this,
						playgroundActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("stage", position);
				bundle.putInt("Flag_m", flag_m);
				bundle.putInt("Flag_v", flag_v);
				bundle.putInt("Flag_t", flag_t);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
		mu = (ImageView) findViewById(R.id.music);
		vo = (ImageView) findViewById(R.id.voice);
		back = (ImageView) findViewById(R.id.back);
		exit = (ImageView) findViewById(R.id.exit);
		mu.setOnClickListener(listener);
		vo.setOnClickListener(listener);
		back.setOnClickListener(listener);
		exit.setOnClickListener(listener);
		if (flag_m == 0) {
			mu.setImageResource(R.drawable.nomu);
		} else if (flag_m == 1) {
			mu.setImageResource(R.drawable.muscic);
		}
		if (flag_v == 0) {
			vo.setImageResource(R.drawable.nov);
		} else if (flag_v == 1) {
			vo.setImageResource(R.drawable.voice);
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mu) {
				Intent i = new Intent(getApplicationContext(),
						MusicService.class);
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

			} else if (v == vo) {
				if (flag_v == 1) {
					vo.setImageResource(R.drawable.nov);
					flag_v = 0;
				} else {
					vo.setImageResource(R.drawable.voice);
					flag_v = 1;
				}
			} else if (v == back) {
				Intent intent = getIntent();
				if (flag_m == 0 && flag_v == 0) {
					setResult(0, intent);
				} else if (flag_m == 1 && flag_v == 0) {
					setResult(1, intent);
				} else if (flag_m == 0 && flag_v == 1) {
					setResult(2, intent);
				} else {
					setResult(3, intent);
				}
				finish();
			} else if (v == exit) {
				Intent intent = getIntent();
				setResult(RESULT_OK, intent);
				finish();
			}
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

class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private Integer width;
	private Integer height;
	private Integer[] mImageIds;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(mImageIds[position]);
		imageView.setLayoutParams(new Gallery.LayoutParams(width, height));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		return imageView;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer[] getmImageIds() {
		return mImageIds;
	}

	public void setmImageIds(Integer[] mImageIds) {
		this.mImageIds = mImageIds;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

}
