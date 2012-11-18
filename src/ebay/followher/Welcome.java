package ebay.followher;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

public class Welcome extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
		        @Override
		        public void run() {
		        	Intent intent=new Intent(Welcome.this,Home.class);
                    startActivity(intent);
                    Welcome.this.finish();
		        }
		};
		timer.schedule(timerTask, 1000 * 2);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return false;
	}
}
