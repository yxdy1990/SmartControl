package com.example.evan.smartcontrol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.evan.smartcontrol.ui.AboutMeFragment;
import com.example.evan.smartcontrol.ui.HomePageFragment;
import com.example.evan.smartcontrol.ui.IntelligentFragment;
import com.example.evan.smartcontrol.ui.MessageFragment;
import com.orhanobut.logger.Logger;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.jpeng.jptabbar.*;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @BindView(R.id.tab_bar)
    JPTabBar mTabBar;

    private HomePageFragment homePageFragment;
    private IntelligentFragment intelligentFragment;
    private MessageFragment messageFragment;
    private AboutMeFragment aboutMeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initUserInterfaceView();
    }

    public void initUserInterfaceView() {
        final FragmentManager fragmentManager = getSupportFragmentManager();

        mTabBar.setTitles(R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4)
                .setNormalIcons(R.mipmap.tab1_normal, R.mipmap.tab2_normal, R.mipmap.tab3_normal, R.mipmap.tab4_normal)
                .setSelectedIcons(R.mipmap.tab1_selected, R.mipmap.tab2_selected, R.mipmap.tab3_selected, R.mipmap.tab4_selected)
                .generate();
        mTabBar.setNormalColor(mTabBar.getResources().getColor(R.color.colorTabNor));
        mTabBar.setSelectedColor(mTabBar.getResources().getColor(R.color.colorTabSel));
        mTabBar.setTabListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int index) {
                Logger.d("Selected Index: " + index);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (index) {
                    case 0: {
                        hideFragment(intelligentFragment, transaction);
                        hideFragment(messageFragment, transaction);
                        hideFragment(aboutMeFragment, transaction);
                        if (homePageFragment == null) {
                            homePageFragment = new HomePageFragment();
                            transaction.add(R.id.main_frame_layout, intelligentFragment);
                        } else {
                            transaction.show(homePageFragment);
                        }
                        break;
                    }
                    case 1: {
                        hideFragment(homePageFragment, transaction);
                        hideFragment(messageFragment, transaction);
                        hideFragment(aboutMeFragment, transaction);
                        if (intelligentFragment == null) {
                            intelligentFragment = IntelligentFragment.newInstance("Intelligent 1", "Intelligent 2");
                            transaction.add(R.id.main_frame_layout, intelligentFragment);
                        } else {
                            transaction.show(intelligentFragment);
                        }
                        break;
                    }
                    case 2: {
                        hideFragment(homePageFragment, transaction);
                        hideFragment(intelligentFragment, transaction);
                        hideFragment(aboutMeFragment, transaction);
                        if (messageFragment == null) {
                            messageFragment = MessageFragment.newInstance(1);
                            transaction.add(R.id.main_frame_layout, messageFragment);
                        } else {
                            transaction.show(messageFragment);
                        }
                        break;
                    }
                    case 3:
                        hideFragment(homePageFragment, transaction);
                        hideFragment(intelligentFragment, transaction);
                        hideFragment(messageFragment, transaction);
                        if (aboutMeFragment == null) {
                            aboutMeFragment = AboutMeFragment.newInstance(10);
                            transaction.add(R.id.main_frame_layout, aboutMeFragment);
                        } else {
                            transaction.show(aboutMeFragment);
                        }
                        break;
                    default:
                        break;
                }
                transaction.commit();
            }
        });
        // Display HomePageFragment default
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        homePageFragment = new HomePageFragment();
        fragmentTransaction.replace(R.id.main_frame_layout, homePageFragment);
        fragmentTransaction.commit();
    }

    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();
}
