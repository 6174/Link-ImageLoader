package cn.link.imageloader;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    LKImageView mImageView;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ConfigOptions options = new ConfigOptions.Builder(this).build();

        ImageLoaderEngine.init(options);


        mListView = (ListView) findViewById(R.id.listview);

        mListView.setAdapter(new ImageAdapter());
    }

    class ImageAdapter extends BaseAdapter {

        DisplayOptions mDisplayOptions;

        ImageAdapter() {
            mDisplayOptions = new DisplayOptions.Builder()
                    .setResetImage(getResources().getColor(R.color.zfz))
                    .build();
        }

        @Override
        public int getCount() {
            return sUrls.length;  //ToDo
        }

        @Override
        public Object getItem(int position) {
            return null;  //ToDo
        }

        @Override
        public long getItemId(int position) {
            return 0;  //ToDo
        }

        class Holder {
            LKImageView mImageView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.cell, null);
                Holder holder = new Holder();
                holder.mImageView = (LKImageView) view.findViewById(R.id.img);
                view.setTag(holder);
            } else {
                view = convertView;
            }

            Holder holder = (Holder) view.getTag();
            mDisplayOptions.mDisplayUrl = sUrls[position];
            holder.mImageView.setDisplayOptions(mDisplayOptions);
            holder.mImageView.display();

            return view;  //ToDo
        }
    }

    public static final String[] sUrls = new String[]{
            "http://photo.yupoo.com/strongwillow/C46Z9FOY/medish.jpg",
            "http://photo.yupoo.com/strongwillow/C46Z46HF/medish.jpg",
            "http://photo.yupoo.com/strongwillow/C46Z1hWu/medish.jpg",
            "http://photo.yupoo.com/strongwillow/C46YXVV8/medish.jpg",
            "http://photo.yupoo.com/strongwillow/C46YWkas/medish.jpg",
            "http://photo.yupoo.com/strongwillow/C46YU8pb/medish.jpg",
            "http://photo.yupoo.com/strongwillow/C46YT9VY/medish.jpg",
            "http://photo.yupoo.com/strongwillow/C3cWsaFs/medish.jpg",
            "http://photo.yupoo.com/strongwillow/AOe7qBNK/medish.jpg",
            "http://photo.yupoo.com/strongwillow/AOe7iTpC/medish.jpg",
            "http://photo.yupoo.com/strongwillow/ACnyczFE/medish.jpg",
            "http://photo.yupoo.com/strongwillow/ACny6ra2/medish.jpg",
            "http://photo.yupoo.com/strongwillow/ACnvjb4M/medish.jpg",
            "http://photo.yupoo.com/strongwillow/ACnuW8aC/medish.jpg",
            "http://photo.yupoo.com/strongwillow/ACntUr7e/medish.jpg",
            "http://photo.yupoo.com/strongwillow/ACnsojXG/medish.jpg",
            "http://photo.yupoo.com/strongwillow/ACnqMbqC/medish.jpg",
            "http://photo.yupoo.com/strongwillow/AyGPWlEq/medish.jpg",
            "http://photo.yupoo.com/strongwillow/AyGPWcaZ/medish.jpg",
            "http://photo.yupoo.com/strongwillow/AyGPVBa4/medish.jpg"
    };
}
