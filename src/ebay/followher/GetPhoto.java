package ebay.followher;

import java.io.FileNotFoundException;

import opencv.todo.GetStatsFromPhoto;

import ebay.followher.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

public class GetPhoto extends Activity {
	//opencv class to figure the photo
	public GetStatsFromPhoto pstats;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getphoto);

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
		startActivityForResult(intent, 1);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(GetPhoto.this, Home.class);
			startActivity(i);
			this.finish();
		}
		return true;
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == RESULT_OK) {  
            Uri uri = data.getData();  
            Log.e("uri", uri.toString()); 
            Log.e("uri", uri.getPath());  
            ContentResolver cr = this.getContentResolver(); 
 
            //获得照片，并显示
            try {  
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
                ImageView imageView = (ImageView) findViewById(R.id.photo_selected);  
                /* 将Bitmap设定到ImageView */  
                imageView.setImageBitmap(bitmap); 
                pstats = new GetStatsFromPhoto(bitmap);
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }
            
            //获得照片路径
            //get the picture path
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            System.out.println("image path ===>" + cursor.getString(column_index));
            
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }  

}
