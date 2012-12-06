package ebay.followher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import Helpers.ClothesAdder;
import Helpers.Constants;
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
import android.view.SubMenu;

import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class Home extends Activity {

	GLSurfaceView glView;
	GLRenderer renderer;

	public static float xpos = -1;
	public static float touchTurn = 0;

	public static float herSize = Constants.lSize;
	public static float clothesSize = Constants.lSize;

	public static boolean firstLoad = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Resources res = getResources();

		firstLoad = getIntent().getStringExtra("texture") == null ? true
				: false;

		initializeModelTexture(res);

		glView = new GLSurfaceView(this);

		if (renderer == null)
			renderer = new GLRenderer();

		glView.setRenderer(renderer);

		setContentView(glView);
	}

	private Object3D load3DS(Resources res, String file) {
		String obj3ds = firstLoad ? format3DS("bikini") : format3DS(file);
//		String obj3ds = format3DS("bikini");

		AssetManager am = res.getAssets();
		InputStream inputStream = null;
		try {
			inputStream = am.open(obj3ds, AssetManager.ACCESS_UNKNOWN);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object3D[] model = Loader.load3DS(inputStream, herSize);
		Object3D o3d = new Object3D(0);
		Object3D temp = null;
		for (int i = 0; i < model.length; i++) {
			temp = model[i];
			temp.setCenter(SimpleVector.ORIGIN);
			temp.rotateX((float) (-.5 * Math.PI));
			temp.rotateMesh();
			temp.setRotationMatrix(new Matrix());
			o3d = Object3D.mergeObjects(o3d, temp);
			o3d.compile();
		}
		return o3d;
	}

	public Bitmap loadImg(Resources res, String file) {
		Bitmap temp = null;
		try {
			AssetManager mngr = res.getAssets();
			InputStream img = mngr.open(formatJPG(file));
			temp = BitmapFactory.decodeStream(img);
			return temp;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void initializeModelTexture(Resources res) {

		ClothesAdder.body.textureName = "body";
		ClothesAdder.body.img = loadImg(res, "body");
		ClothesAdder.body.obj = load3DS(res, "body");

		if (!firstLoad) {
			if (ClothesAdder.upper.texturePath != null
					&& ClothesAdder.upper.objPath != null) {
				ClothesAdder.upper.img = BitmapFactory
						.decodeFile(ClothesAdder.upper.texturePath);
				ClothesAdder.upper.obj = loadLocal3DS(ClothesAdder.upper.objPath);
			}
			if (ClothesAdder.under.texturePath != null
					&& ClothesAdder.under.objPath != null) {
				ClothesAdder.under.img = BitmapFactory
						.decodeFile(ClothesAdder.under.texturePath);
				ClothesAdder.under.obj = loadLocal3DS(ClothesAdder.under.objPath);
			}
			if (ClothesAdder.shoes.texturePath != null
					&& ClothesAdder.shoes.objPath != null) {
				ClothesAdder.shoes.img = BitmapFactory
						.decodeFile(ClothesAdder.shoes.texturePath);
				ClothesAdder.shoes.obj = loadLocal3DS(ClothesAdder.shoes.objPath);
			}

		}
	}

	public Object3D loadLocal3DS(String objPath) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(objPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Object3D[] model = Loader.load3DS(inputStream, clothesSize);
		Object3D o3d = new Object3D(0);
		Object3D temp = null;
		for (int i = 0; i < model.length; i++) {
			temp = model[i];
			temp.setCenter(SimpleVector.ORIGIN);
			temp.rotateX((float) (-.5 * Math.PI));
			temp.rotateMesh();
			temp.setRotationMatrix(new Matrix());
			o3d = Object3D.mergeObjects(o3d, temp);
			o3d.compile();
		}
		return o3d;
	}

	@Override
	protected void onPause() {
		super.onPause();
		glView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		glView.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public String formatJPG(String input) {
		return input + ".jpg";
	}

	private String format3DS(String file) {
		return file + ".3DS";
	}

	public boolean onTouchEvent(MotionEvent me) {

		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			xpos = me.getX();
			return true;
		}
		if (me.getAction() == MotionEvent.ACTION_UP) {
			xpos = -1;
			touchTurn = 0;
			return true;
		}

		if (me.getAction() == MotionEvent.ACTION_MOVE) {
			float xd = me.getX() - xpos;
			xpos = me.getX();
			touchTurn = xd / -100f;
			return true;
		}

		try {
			Thread.sleep(15);
		} catch (Exception e) {
		}

		return super.onTouchEvent(me);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu dress = menu.addSubMenu(0, 100, 0, "Dress Her");
		dress.addSubMenu(0, 110, 0, "Upper Clothes");
		dress.addSubMenu(0, 120, 1, "Under Clothes");
		dress.addSubMenu(0, 130, 2, "Shoes or Boots");

		SubMenu style = menu.addSubMenu(2, 300, 1, "Dress Style");
		style.addSubMenu(0, 310, 0, "Upper Clothes");
		style.addSubMenu(0, 320, 1, "Under Clothes");
		style.addSubMenu(0, 330, 2, "Shoes or Boots");

		menu.addSubMenu(1, 200, 2, "Build Her");
		menu.addSubMenu(3, 400, 3, "Exit eBay");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 400:
			android.os.Process.killProcess(android.os.Process.myPid());
		case 100:
			// Intent BrowseItem = new Intent(Home.this, DressSelector.class);
			// startActivity(BrowseItem);
			// this.finish();
			return true;
		case 110:
			Intent up = new Intent(Home.this, DressSelector.class);
			up.putExtra("clothes", "upper");
			startActivity(up);
			this.finish();
			return true;
		case 120:
			Intent un = new Intent(Home.this, DressSelector.class);
			un.putExtra("clothes", "under");
			startActivity(un);
			this.finish();
			return true;
		case 130:
			Intent sh = new Intent(Home.this, DressSelector.class);
			sh.putExtra("clothes", "shoes");
			startActivity(sh);
			this.finish();
			return true;
		case 200:
			Intent getPhoto = new Intent(Home.this, GetPhoto.class);
			startActivity(getPhoto);
			this.finish();
			return true;
		case 310:
			Intent upp = new Intent(Home.this, TextureSelector.class);
			upp.putExtra("clothes", "upper");
			startActivity(upp);
			this.finish();
			return true;
		case 320:
			Intent und = new Intent(Home.this, TextureSelector.class);
			und.putExtra("clothes", "under");
			startActivity(und);
			this.finish();
			return true;
		case 330:
			Intent sho = new Intent(Home.this, TextureSelector.class);
			sho.putExtra("clothes", "shoes");
			startActivity(sho);
			this.finish();
			return true;
		}
		return false;
	}

}
