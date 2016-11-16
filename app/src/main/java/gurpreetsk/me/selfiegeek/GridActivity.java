package gurpreetsk.me.selfiegeek;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

import gurpreetsk.me.selfiegeek.fragments.ImageGridFragment;
import gurpreetsk.me.selfiegeek.fragments.VideoGridFragment;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.CAMERA_STILL;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.MY_PERMISSIONS_REQUEST_ACCESS_CAMERA;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.MY_PERMISSIONS_REQUEST_ACCESS_STORAGE;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.SHARED_PREF_KEY;
import static gurpreetsk.me.selfiegeek.utils.permissions.askCameraPermission;
import static gurpreetsk.me.selfiegeek.utils.permissions.askStoragePermission;

public class GridActivity extends AppCompatActivity {

    private static final String TAG = "GridActivity";
    ViewPager pager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        int storagePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storagePermissionCheck != PackageManager.PERMISSION_GRANTED)
            askStoragePermission(this);

        pager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "Image", "Video"};

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ImageGridFragment();
                case 1:
                    return new VideoGridFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/"+getPackageName());
                    if(!dir.exists()){
                        dir.mkdir();
                    }

                } else {
                    Toast.makeText(this, getString(R.string.storage_permission_needed), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
