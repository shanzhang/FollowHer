package ebay.followher;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

public class GLRenderer implements Renderer {
	private World world;
	private FrameBuffer fb;
	private Object3D soilder;
	private String[] texturesName = { "snork" };
	private Light sun;
	private float scale = 2f;
	// 行走动画 相关参数
	private int an = 2;
	private float ind = 0;
	private boolean stop = false;
	// 停止
	public void stop() {
		stop = true;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		doAnim();
		if (!stop) {
			// 如果touchTurn不为0,向Y轴旋转touchTure角度
			if (Home.touchTurn != 0) {
				// 旋转物体的旋转绕Y由给定矩阵W轴角（弧度顺时针方向为正值）,应用到对象下一次渲染时。
				soilder.rotateY(Home.touchTurn);
				// 将touchTurn置0
				Home.touchTurn = 0;
			}
			// 用颜色清除FrameBuffer
			fb.clear(RGBColor.WHITE);
			// 变换和灯光所有多边形
			world.renderScene(fb);
			// 绘制
			world.draw(fb);
			// 显示
			fb.display();
		}else {
            if (fb != null) {
                fb.dispose();
                fb = null;
            }
		}

	}

	/**
	 * 实现动画的代码
	 * */
	private void doAnim() {
		// TODO Auto-generated method stub
		// 每一帧加0.018f
		ind += 0.018f;
		if (ind > 1f) {
			ind -= 1f;
		}
		// 关于此处的两个变量，ind的值为0-1(jpct-ae规定),0表示第一帧，1为最后一帧；
		// 至于an这个变量，它的意思是sub-sequence如果在keyframe(3ds中),因为在一个
		// 完整的动画包含了seq和sub-sequence，所以设置为2表示执行sub-sequence的动画，
		// 但这里设置为2我就不太明白了，不过如果不填，效果会不自然，所以我就先暂时把它
		// 设置为2
		soilder.animate(ind, an);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		// 如果FrameBuffer不为NULL,释放fb所占资源
		if (fb != null) {
			fb.dispose();
		}
		fb = new FrameBuffer(gl, width, height);

		if (Home.master == null) {
			world = new World();
			world.setAmbientLight(20, 20, 20);
			sun = new Light(world);
			sun.setIntensity(250, 250, 250);
			// TextureManager.getInstance()取得一个Texturemanager对象
			// addTexture(textureName,texture)添加一个纹理，这边只是和我们的texturesName绑定一个纹理
			TextureManager.getInstance().addTexture(texturesName[0],
					new Texture(LoadImg.bmp));
			// 从assets文件夹中读取soilder.md2文件来实例化Object3D snork
			soilder = Loader.loadMD2(LoadAssets.loadf("soilder.md2"), scale);
			// 旋转soilder对象到"适当位置"
			soilder.translate(-90, 0, 0);
			// 这才是将纹理添加进去
			soilder.setTexture(texturesName[0]);
			// 释放部分资源
			soilder.strip();
			// 编译
			soilder.build();
			// 将snork添加到World对象中
			world.addObject(soilder);

			Camera cam = world.getCamera();
			cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);

			SimpleVector sv = new SimpleVector();
			// 将当前SimpleVector的x,y,z值设为给定的SimpleVector(cube.getTransformedCenter())的值
			sv.set(soilder.getTransformedCenter());
			// Y方向上减去100
			sv.y -= 120;
			// Z方向上减去100
			sv.z -= 120;
			// 设置光源位置
			sun.setPosition(sv);

			cam.lookAt(soilder.getTransformedCenter());

			// if(Home.master == null)
			// Home.master = new Home();
		}

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}
	
//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder)
//		
//	}
	
}
