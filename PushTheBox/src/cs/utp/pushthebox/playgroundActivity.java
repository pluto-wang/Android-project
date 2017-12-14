package cs.utp.pushthebox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class playgroundActivity extends Activity {
	private ImageView img[][] = new ImageView[7][9];
	private int map[][] = new int[7][9];
	private int posx, posy, finx, finy;
	private ImageView control[] = new ImageView[4];
	private TextView mTextView, mTextView1;
	private int wall_c, wall_r, road, host_t, host_b, host_l, host_r, mouse,
			fish, door;
	private int flag, type, np = 0, score = 0;
	private int flag_m = 1, flag_v = 1, flag_t = 1;
	private MalibuCountDownTimer countDownTimer,countDownTime2;
	private SoundPool soundPool;
	private int winsound, eatsound;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground);
		Bundle bundle = this.getIntent().getExtras();
		type = bundle.getInt("stage");
		flag_m = bundle.getInt("Flag_m");
		flag_v = bundle.getInt("Flag_v");
		flag_t = bundle.getInt("Flag_t");
		if (type == 1) {
			flag = 1;
		}
		setRes();
		getImage();
		setKey();
		loadMap();
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(listener);
		mTextView = (TextView) findViewById(R.id.mTextView);
		mTextView1 = (TextView) findViewById(R.id.mTextView1);
		countDownTimer = new MalibuCountDownTimer(60000, 1000);
		if (flag_t == 1) {
			countDownTimer.start();
		}
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
		winsound = soundPool.load(this, R.raw.aplause, 1);
		eatsound = soundPool.load(this, R.raw.eat, 1);
	}

	public class MalibuCountDownTimer extends CountDownTimer {
		private int flag = 0;

		public MalibuCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		public void onFinish() {
			if (flag == 0) {
				mTextView.setText("00");
				AlertDialogLose();
			}
		}

		public void pause() {
			flag = 1;
		}

		public void resume() {
			flag = 0;
		}

		public void onTick(long millisUntilFinished) {
			if (flag == 0) {
				mTextView1.setText("00");
				if (millisUntilFinished < 10000) {
					mTextView.setText("0" + millisUntilFinished / 1000);
				} else {
					mTextView.setText("" + millisUntilFinished / 1000);
				}
			}
			// Log.e("test","test");
		}
	}

	private void setRes() {
		// 根據type，選擇遇到不同內容顯示的圖片
		if (type == 0) {
			flag = 0;
			wall_c = R.drawable.wall_col;
			wall_r = R.drawable.wall_row;
			road = R.drawable.flood_1;
			host_t = R.drawable.host_1_r;
			host_b = R.drawable.host_1_l;
			host_l = R.drawable.host_1_u;
			host_r = R.drawable.host_1_d;
			mouse = R.drawable.mouse_1;
			fish = R.drawable.fish_1;
			door = R.drawable.door_side;
		} else if (type == 1) {
			wall_c = R.drawable.wall_col;
			wall_r = R.drawable.wall_row;
			road = R.drawable.flood_2;
			host_t = R.drawable.host_2_r;
			host_b = R.drawable.host_2_l;
			host_l = R.drawable.host_2_u;
			host_r = R.drawable.host_2_d;
			mouse = R.drawable.mouse_2;
			fish = R.drawable.fish_2;
			door = R.drawable.door;
		} else {
			wall_c = R.drawable.wall_col;
			wall_r = R.drawable.wall_row;
			road = R.drawable.flood_3;
			host_t = R.drawable.host_3_r;
			host_b = R.drawable.host_3_l;
			host_l = R.drawable.host_3_u;
			host_r = R.drawable.host_3_d;
			mouse = R.drawable.mouse_3;
			fish = R.drawable.fish_3;
			door = R.drawable.door_side;
		}
	}

	// 移動,未製作箱子後有魚及吃魚
	private void reDraw() {
		// 根據字串切換圖
		// 這裡需要刻
		// 0=road,1=wall,2=player,3=box
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 9; j++) {
				if (map[i][j] == 0) {
					img[i][j].setImageResource(road);
				} else if (map[i][j] == 1) {
					img[i][j].setImageResource(wall_r);
				} else if (map[i][j] == 2) {
					img[i][j].setImageResource(wall_c);
				} else if (map[i][j] == 3) {
					img[i][j].setImageResource(mouse);
				} else if (map[i][j] == 4) {
					img[i][j].setImageResource(fish);
				} else if (map[i][j] == 5) {
					img[i][j].setImageResource(door);
				} else if (map[i][j] == 6) {
					img[i][j].setImageResource(host_t);
				} else if (map[i][j] == 7) {
					img[i][j].setImageResource(host_b);
				} else if (map[i][j] == 8) {
					img[i][j].setImageResource(host_l);
				} else if (map[i][j] == 9) {
					img[i][j].setImageResource(host_r);
				}
			}
		}
	}

	void setKey() {
		control[0] = (ImageView) findViewById(R.id.buttonTop);
		control[1] = (ImageView) findViewById(R.id.buttonBot);
		control[2] = (ImageView) findViewById(R.id.buttonLeft);
		control[3] = (ImageView) findViewById(R.id.buttonRight);
		control[0].setImageResource(R.drawable.buttontop);
		control[1].setImageResource(R.drawable.buttonbot);
		control[2].setImageResource(R.drawable.buttonleft);
		control[3].setImageResource(R.drawable.buttonright);
		control[0].setOnClickListener(move);
		control[1].setOnClickListener(move);
		control[2].setOnClickListener(move);
		control[3].setOnClickListener(move);
		control[0].setOnTouchListener(moveT);
		control[1].setOnTouchListener(moveT);
		control[2].setOnTouchListener(moveT);
		control[3].setOnTouchListener(moveT);
	}

	void loadMap() {
		// init
		// 讀檔
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 9; j++) {
				map[i][j] = 1;
			}
		}
		Resources resources = this.getResources();
		String read = null, setting;
		int mapid;
		if (type == 0) {
			mapid = R.raw.map;
		} else if (type == 1) {
			mapid = R.raw.map2;
		} else {
			mapid = R.raw.map3;
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(
				resources.openRawResource(mapid)));
		int i = 0;
		try {
			setting = in.readLine();
			while ((read = in.readLine()) != null) {
				String token[] = read.split(",");
				for (int j = 0, k = 0; k < token.length; j++, k++) {
					map[i][j] = Integer.parseInt(token[k]);
				}
				i++;
			}
			String token[] = setting.split(",");
			finx = Integer.parseInt(token[2]);
			finy = Integer.parseInt(token[3]);
			posx = Integer.parseInt(token[0]);
			posy = Integer.parseInt(token[1]);
			map[posx][posy] = 9;
			reDraw();
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		}

	}

	void getImage() {
		img[0][0] = (ImageView) findViewById(R.id.map00);
		img[0][1] = (ImageView) findViewById(R.id.map01);
		img[0][2] = (ImageView) findViewById(R.id.map02);
		img[0][3] = (ImageView) findViewById(R.id.map03);
		img[0][4] = (ImageView) findViewById(R.id.map04);
		img[0][5] = (ImageView) findViewById(R.id.map05);
		img[0][6] = (ImageView) findViewById(R.id.map06);
		img[0][7] = (ImageView) findViewById(R.id.map07);
		img[0][8] = (ImageView) findViewById(R.id.map08);
		img[1][0] = (ImageView) findViewById(R.id.map10);
		img[1][1] = (ImageView) findViewById(R.id.map11);
		img[1][2] = (ImageView) findViewById(R.id.map12);
		img[1][3] = (ImageView) findViewById(R.id.map13);
		img[1][4] = (ImageView) findViewById(R.id.map14);
		img[1][5] = (ImageView) findViewById(R.id.map15);
		img[1][6] = (ImageView) findViewById(R.id.map16);
		img[1][7] = (ImageView) findViewById(R.id.map17);
		img[1][8] = (ImageView) findViewById(R.id.map18);
		img[2][0] = (ImageView) findViewById(R.id.map20);
		img[2][1] = (ImageView) findViewById(R.id.map21);
		img[2][2] = (ImageView) findViewById(R.id.map22);
		img[2][3] = (ImageView) findViewById(R.id.map23);
		img[2][4] = (ImageView) findViewById(R.id.map24);
		img[2][5] = (ImageView) findViewById(R.id.map25);
		img[2][6] = (ImageView) findViewById(R.id.map26);
		img[2][7] = (ImageView) findViewById(R.id.map27);
		img[2][8] = (ImageView) findViewById(R.id.map28);
		img[3][0] = (ImageView) findViewById(R.id.map30);
		img[3][1] = (ImageView) findViewById(R.id.map31);
		img[3][2] = (ImageView) findViewById(R.id.map32);
		img[3][3] = (ImageView) findViewById(R.id.map33);
		img[3][4] = (ImageView) findViewById(R.id.map34);
		img[3][5] = (ImageView) findViewById(R.id.map35);
		img[3][6] = (ImageView) findViewById(R.id.map36);
		img[3][7] = (ImageView) findViewById(R.id.map37);
		img[3][8] = (ImageView) findViewById(R.id.map38);
		img[4][0] = (ImageView) findViewById(R.id.map40);
		img[4][1] = (ImageView) findViewById(R.id.map41);
		img[4][2] = (ImageView) findViewById(R.id.map42);
		img[4][3] = (ImageView) findViewById(R.id.map43);
		img[4][4] = (ImageView) findViewById(R.id.map44);
		img[4][5] = (ImageView) findViewById(R.id.map45);
		img[4][6] = (ImageView) findViewById(R.id.map46);
		img[4][7] = (ImageView) findViewById(R.id.map47);
		img[4][8] = (ImageView) findViewById(R.id.map48);
		img[5][0] = (ImageView) findViewById(R.id.map50);
		img[5][1] = (ImageView) findViewById(R.id.map51);
		img[5][2] = (ImageView) findViewById(R.id.map52);
		img[5][3] = (ImageView) findViewById(R.id.map53);
		img[5][4] = (ImageView) findViewById(R.id.map54);
		img[5][5] = (ImageView) findViewById(R.id.map55);
		img[5][6] = (ImageView) findViewById(R.id.map56);
		img[5][7] = (ImageView) findViewById(R.id.map57);
		img[5][8] = (ImageView) findViewById(R.id.map58);
		img[6][0] = (ImageView) findViewById(R.id.map60);
		img[6][1] = (ImageView) findViewById(R.id.map61);
		img[6][2] = (ImageView) findViewById(R.id.map62);
		img[6][3] = (ImageView) findViewById(R.id.map63);
		img[6][4] = (ImageView) findViewById(R.id.map64);
		img[6][5] = (ImageView) findViewById(R.id.map65);
		img[6][6] = (ImageView) findViewById(R.id.map66);
		img[6][7] = (ImageView) findViewById(R.id.map67);
		img[6][8] = (ImageView) findViewById(R.id.map68);
	}

	private OnClickListener move = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.buttonTop) {
				if (map[posx - 1][posy] == 1 || map[posx - 1][posy] == 2
						|| map[posx - 1][posy] == 5) {
				} else if (map[posx - 1][posy] == 3) {
					if (map[posx - 2][posy] == 1 || map[posx - 2][posy] == 2) {
					} else if (map[posx - 2][posy] == 3) {
					} else {
						map[posx - 2][posy] = 3;
						map[posx][posy] = 0;
						posx = posx - 1;
					}
				} else {
					if (map[posx - 1][posy] == 4) {
						if (flag_v == 1)
							soundPool.play(eatsound, 1.0F, 1.0F, 0, 0, 1.0F);
						score++;
					}
					map[posx][posy] = 0;
					posx = posx - 1;
				}
				map[posx][posy] = 6;
			} else if (v.getId() == R.id.buttonBot) {
				if (map[posx + 1][posy] == 1 || map[posx + 1][posy] == 2
						|| map[posx + 1][posy] == 5) {
				} else if (map[posx + 1][posy] == 3) {
					if (map[posx + 2][posy] == 1 || map[posx + 2][posy] == 2) {
					} else if (map[posx + 2][posy] == 3) {
					} else {
						map[posx + 2][posy] = 3;
						map[posx][posy] = 0;
						posx = posx + 1;
					}
				} else {
					if (map[posx + 1][posy] == 4) {
						if (flag_v == 1)
							soundPool.play(eatsound, 1.0F, 1.0F, 0, 0, 1.0F);
						score++;
					}
					map[posx][posy] = 0;
					posx = posx + 1;
				}
				map[posx][posy] = 7;
			} else if (v.getId() == R.id.buttonLeft) {
				if (map[posx][posy - 1] == 1 || map[posx][posy - 1] == 2
						|| map[posx][posy - 1] == 5) {
				} else if (map[posx][posy - 1] == 3) {
					if (map[posx][posy - 2] == 1 || map[posx][posy - 2] == 2) {
					} else if (map[posx][posy - 2] == 3) {
					} else {
						map[posx][posy - 2] = 3;
						map[posx][posy] = 0;
						posy = posy - 1;
					}
				} else {
					if (map[posx][posy - 1] == 4) {
						if (flag_v == 1)
							soundPool.play(eatsound, 1.0F, 1.0F, 0, 0, 1.0F);
						score++;
					}
					map[posx][posy] = 0;
					posy = posy - 1;
				}
				map[posx][posy] = 8;
			} else if (v.getId() == R.id.buttonRight) {
				if (map[posx][posy + 1] == 1 || map[posx][posy + 1] == 2
						|| map[posx][posy + 1] == 5) {
				} else if (map[posx][posy + 1] == 3) {
					if (map[posx][posy + 2] == 1 || map[posx][posy + 2] == 2) {
					} else if (map[posx][posy + 2] == 3) {
					} else {
						map[posx][posy + 2] = 3;
						map[posx][posy] = 0;
						posy = posy + 1;
					}
				} else {
					if (map[posx][posy + 1] == 4) {
						if (flag_v == 1)
							soundPool.play(eatsound, 1.0F, 1.0F, 0, 0, 1.0F);
						score++;
					}
					map[posx][posy] = 0;
					posy = posy + 1;
				}
				map[posx][posy] = 9;
			}

			reDraw();
			if (posx == finx && posy == finy) {
				if (flag_v == 1)
					soundPool.play(winsound, 1.0F, 1.0F, 0, 0, 1.0F);
				AlertDialog();
			}
		}
	};
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}

	};
	private OnTouchListener moveT = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v == control[0]) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					control[0].setImageResource(R.drawable.buttontop_p);
				} else {
					control[0].setImageResource(R.drawable.buttontop);
				}
			} else if (v == control[1]) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					control[1].setImageResource(R.drawable.buttonbot_p);
				} else {
					control[1].setImageResource(R.drawable.buttonbot);
				}
			} else if (v == control[2]) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					control[2].setImageResource(R.drawable.buttonleft_p);
				} else {
					control[2].setImageResource(R.drawable.buttonleft);
				}
			} else {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					control[3].setImageResource(R.drawable.buttonright_p);
				} else {
					control[3].setImageResource(R.drawable.buttonright);
				}
			}
			return false;
		}
	};

	@SuppressLint("NewApi")
	private void AlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.dialog);
		builder.setTitle("You win").setMessage(
				"Congratulation!!!\nYou Got " + score + " Points!!!");
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		};
		DialogInterface.OnClickListener reClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				loadMap();
				score = 0;
				countDownTimer.cancel();
				countDownTimer = new MalibuCountDownTimer(60000, 1000);
				countDownTimer.start();
				
			}
		};
		DialogInterface.OnClickListener neClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				countDownTimer.cancel();
				countDownTimer = new MalibuCountDownTimer(60000, 1000);
				countDownTimer.start();
				if (flag == 0 && type == 0 && np == 0) {// 1~2
					np = 1;
					type = 1;
					setRes();
					loadMap();
					score = 0;
				} else if (flag == 1 && type == 1 && np == 0) {// 2~3
					type = 2;
					setRes();
					loadMap();
					score = 0;
				} else if (flag == 0 && np == 1) {// 1~2~3
					type = 2;
					setRes();
					loadMap();
					score = 0;
				}
			}
		};
		// builder.setItems(R.drawable.buttonleft_p,OkClick);
		if (type == 2) {
			builder.setPositiveButton("Retry", reClick);
			builder.setNegativeButton("Exit", OkClick);
		} else {
			builder.setPositiveButton("Retry", reClick);
			builder.setNeutralButton("Next", neClick);
			builder.setNegativeButton("Exit", OkClick);
		}
		builder.show();
	}

	private void AlertDialogLose() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.dialog);
		builder.setTitle("You lose").setMessage("Sorry!!!\n");
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		};
		DialogInterface.OnClickListener reClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				loadMap();
				score = 0;
				countDownTimer = new MalibuCountDownTimer(60000, 1000);
				countDownTimer.start();
			}
		};
		builder.setPositiveButton("Retry", reClick);
		builder.setNegativeButton("Exit", OkClick);
		builder.show();
	}

	protected void onResume() {
		super.onResume();
		Intent i = new Intent(getApplicationContext(), MusicService.class);
		i.putExtra("operate", 0);
		if (flag_m == 1) {
			startService(i);
		}
		Settings.System.putInt(getContentResolver(),
				Settings.System.SOUND_EFFECTS_ENABLED, 0);
		if (flag_t == 1)
			countDownTimer.resume();
	}

	protected void onPause() {
		super.onPause();
		Intent i = new Intent(getApplicationContext(), MusicService.class);
		i.putExtra("operate", 1);
		startService(i);
		Settings.System.putInt(getContentResolver(),
				Settings.System.SOUND_EFFECTS_ENABLED, 1);
		if (flag_t == 1)
			countDownTimer.pause();
	}
}
