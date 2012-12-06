package ebay.followher;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;


import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class Home extends Activity {
	/** Called when the activity is first created. */

	GLSurfaceView glView;

	GLRenderer renderer;
	
	public static Home master;
	
	public static float xpos = -1;
	public static float touchTurn = 0;
	
	private static final int MENU_CHOOSE_PHOTO = 1;
	private static final int MENU_GET_ITEM = 2;
	private static final int MENU_EXIT = 3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		if (master != null) {
            copy(master);
        }
		
		super.onCreate(savedInstanceState);

		//	加载图片
		LoadImg.loadi(getResources());
		
		//载入Assets文件夹下的文件
		new LoadAssets(getResources());
		
		glView = new GLSurfaceView(this);

		renderer = new GLRenderer();

		glView.setRenderer(renderer);

		setContentView(glView);
	}
	
    private void copy(Object src) {
        try {
            // 返回一个数组，其中包含目前这个类的的所有字段的Filed对象
            Field[] fs = src.getClass().getDeclaredFields();
            // 遍历fs数组
            for (Field f : fs) {
                // 尝试设置无障碍标志的值。标志设置为false将使访问检查，设置为true，将其禁用。
                f.setAccessible(true);
                // 将取到的值全部装入当前类中
                f.set(this, f.get(src));
            }
        } catch (Exception e) {
            // 抛出运行时异常
            throw new RuntimeException(e);
        }
    }

	// 重写onPause()
    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }

    // 重写onResume()
    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

    // 重写onStop()
    @Override
    protected void onStop() {
        super.onStop();
    }
    
    public boolean onTouchEvent(MotionEvent me) {

        // 按键开始
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            // 保存按下的初始x,y位置于xpos,ypos中
            xpos = me.getX();
            return true;
        }
        // 按键结束
        if (me.getAction() == MotionEvent.ACTION_UP) {
            // 设置x,y及旋转角度为初始值
            xpos = -1;
            touchTurn = 0;
            return true;
        }

        if (me.getAction() == MotionEvent.ACTION_MOVE) {
            // 计算x,y偏移位置及x,y轴上的旋转角度
            float xd = me.getX() - xpos;
            xpos = me.getX();
            // 以x轴为例，鼠标从左向右拉为正，从右向左拉为负
            touchTurn = xd / -100f;
            return true;
        }

        // 每Move一下休眠毫秒
        try {
            Thread.sleep(15);
        } catch (Exception e) {
            // No need for this...
        }

        return super.onTouchEvent(me);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_GET_ITEM, 0, "Get Clothes");
	    menu.add(1, MENU_CHOOSE_PHOTO, 1, "Idol Yourself");
	    menu.add(2, MENU_EXIT, 2, "EXIT");
//	    SubMenu animMenu = menu.addSubMenu("Get Clothes");
//		int menuItem = 101;
//		for (SkinClip clip : masterNinja.getSkinClipSequence()) {
//			animMenu.add(0, menuItem++, 1, "Anim: " + clip.getName());
//		}
	    return true;
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case MENU_EXIT:
		        this.finish();
		    case MENU_GET_ITEM:
		    	Intent BrowseItem = new Intent(Home.this, BrowseItem.class);
		    	startActivity(BrowseItem);
		        return true;
		    case MENU_CHOOSE_PHOTO:
		    	Intent getPhoto = new Intent(Home.this, GetPhoto.class);
		    	startActivity(getPhoto);
		    	return true;
	    }
//	    if (item.getItemId() > 100) {
//	        animation = item.getItemId() - 100;
//	        return true;
//	    }
	    return false;
	}
	
}

// 加载图片类
class LoadImg {
	public static Bitmap bmp;

	public static void loadi(Resources res) {
		bmp = BitmapFactory.decodeResource(res, R.drawable.soilder);
	}
}

// 加载assets类
class LoadAssets {
	public static Resources res;

	public LoadAssets(Resources resources) {
		res = resources;
	}

	public static InputStream loadf(String fileName) {
		AssetManager am = res.getAssets();
		try {
			return am.open(fileName, AssetManager.ACCESS_UNKNOWN);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
