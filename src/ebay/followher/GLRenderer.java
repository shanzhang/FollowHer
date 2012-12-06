package ebay.followher;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Helpers.ClothesAdder;
import android.opengl.GLSurfaceView.Renderer;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

public class GLRenderer implements Renderer {

	private World world;
	private FrameBuffer fb;
	private Object3D whole = null;
	private Light sun;
	// private int an = 2;
	// private float ind = 0;
	private boolean stop = false;

	public void stop() {
		stop = true;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// doAnim();
		if (!stop) {
			if (Home.touchTurn != 0) {
				whole.rotateY(Home.touchTurn);
				Home.touchTurn = 0;
			}
			fb.clear(RGBColor.WHITE);
			world.renderScene(fb);
			world.draw(fb);
			fb.display();
		} else {
			if (fb != null) {
				fb.dispose();
				fb = null;
			}
		}

	}

	// private void doAnim() {
	// ind += 0.018f;
	// if (ind > 1f) {
	// ind -= 1f;
	// }
	// whole.animate(ind, an);
	// }

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		if (fb != null) {
			fb.dispose();
		}
		fb = new FrameBuffer(gl, width, height);

		world = new World();
		world.setAmbientLight(20, 20, 20);
		sun = new Light(world);
		sun.setIntensity(250, 250, 250);

		whole = mergeModel();
		whole.strip();
		whole.build();
		world.addObject(whole);

		Camera cam = world.getCamera();
		cam.moveCamera(Camera.CAMERA_MOVEOUT, 90);
		cam.moveCamera(Camera.CAMERA_MOVEUP, 50);

		SimpleVector worldCenter = whole.getTransformedCenter();
		SimpleVector sv = new SimpleVector();
		sv.set(worldCenter);
		sv.y -= 120;
		sv.z -= 120;
		sun.setPosition(sv);
		worldCenter.y = -60;
		worldCenter.z = 100;

		cam.lookAt(worldCenter);

	}

	private Object3D mergeModel() {

		if (Home.firstLoad) {
			if (!TextureManager.getInstance().containsTexture(
					ClothesAdder.body.textureName)) {
				TextureManager.getInstance().addTexture(
						ClothesAdder.body.textureName,
						new Texture(ClothesAdder.body.img));
			}
			ClothesAdder.body.obj.setTexture(ClothesAdder.body.textureName);
			return ClothesAdder.body.obj;
		} else {
			Object3D o3d = new Object3D(0);
			if (!TextureManager.getInstance().containsTexture(
					ClothesAdder.body.textureName)) {
				TextureManager.getInstance().addTexture(
						ClothesAdder.body.textureName,
						new Texture(ClothesAdder.body.img));
			}
			ClothesAdder.body.obj.setTexture(ClothesAdder.body.textureName);
			o3d = Object3D.mergeObjects(o3d, ClothesAdder.body.obj);
			if (ClothesAdder.upper.obj != null
					&& ClothesAdder.upper.img != null) {
				if (!TextureManager.getInstance().containsTexture(
						ClothesAdder.upper.textureName)) {
					TextureManager.getInstance().addTexture(
							ClothesAdder.upper.textureName,
							new Texture(ClothesAdder.upper.img));
				}
				else{
					TextureManager.getInstance().replaceTexture(
							ClothesAdder.upper.textureName,
							new Texture(ClothesAdder.upper.img));
				}
				ClothesAdder.upper.obj
						.setTexture(ClothesAdder.upper.textureName);
				o3d = Object3D.mergeObjects(o3d, ClothesAdder.upper.obj);
			}
			if (ClothesAdder.under.obj != null
					&& ClothesAdder.under.img != null) {
				if (!TextureManager.getInstance().containsTexture(
						ClothesAdder.under.textureName)) {
					TextureManager.getInstance().addTexture(
							ClothesAdder.under.textureName,
							new Texture(ClothesAdder.under.img));
				}
				TextureManager.getInstance().replaceTexture(
						ClothesAdder.under.textureName,
						new Texture(ClothesAdder.under.img));
				ClothesAdder.under.obj
						.setTexture(ClothesAdder.under.textureName);
				o3d = Object3D.mergeObjects(o3d, ClothesAdder.under.obj);
			}
			if (ClothesAdder.shoes.obj != null
					&& ClothesAdder.shoes.img != null) {
				if (!TextureManager.getInstance().containsTexture(
						ClothesAdder.shoes.textureName)) {
					TextureManager.getInstance().addTexture(
							ClothesAdder.shoes.textureName,
							new Texture(ClothesAdder.shoes.img));
				}
				TextureManager.getInstance().replaceTexture(
						ClothesAdder.shoes.textureName,
						new Texture(ClothesAdder.shoes.img));
				ClothesAdder.shoes.obj
						.setTexture(ClothesAdder.shoes.textureName);
				o3d = Object3D.mergeObjects(o3d, ClothesAdder.shoes.obj);
			}

			o3d = Object3D.mergeObjects(o3d, ClothesAdder.body.obj);
			o3d.compile();
			return o3d;
		}

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}
}
