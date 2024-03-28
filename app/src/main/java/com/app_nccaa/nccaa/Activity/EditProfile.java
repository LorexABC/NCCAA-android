package com.app_nccaa.nccaa.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.TabSignInSignUpActivity;
import com.google.android.material.tabs.TabLayout;

/**
 * Edit profile Activity
 *
 * Modified by phantom
 * On 03/22/24
 * Capitalize system is modified in this section
 */
public class EditProfile extends AppCompatActivity {

    private TabLayout tabLayoutSignInUp;
    private ViewPager viewPagerSignInUp;

    public static boolean isChange = false;


    /**
     * Creator
     * @param savedInstanceState
     *
     * Modified by phantom
     * On 03/22/24
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        tabLayoutSignInUp=(TabLayout)findViewById(R.id.tabLayoutSignInUp);
        viewPagerSignInUp=(ViewPager)findViewById(R.id.viewPagerSignInUp);

        tabLayoutSignInUp.setSelectedTabIndicatorColor(getResources().getColor(R.color.blue));
        tabLayoutSignInUp.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        tabLayoutSignInUp.setTabTextColors(getResources().getColor(R.color.dark_gray_light), getResources().getColor(R.color.blue));

        tabLayoutSignInUp.addTab(tabLayoutSignInUp.newTab().setText("Personal Info"));
        tabLayoutSignInUp.addTab(tabLayoutSignInUp.newTab().setText("Employer Info"));

        tabLayoutSignInUp.setTabGravity(TabLayout.GRAVITY_FILL);

        final TabSignInSignUpActivity pagerTabActivity = new TabSignInSignUpActivity(this,getSupportFragmentManager(),
                tabLayoutSignInUp.getTabCount());
        viewPagerSignInUp.setAdapter(pagerTabActivity);

        viewPagerSignInUp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutSignInUp));

        tabLayoutSignInUp.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerSignInUp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }


    /**
     * Lifecycle function
     */
    @Override
    public void onBackPressed() {

        if (isChange) {

            new AlertDialog.Builder(EditProfile.this)
                    .setTitle("Warning!")
                    .setMessage("You have made changes to this page without saving.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Yes! Save all changes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        } else {
            finish();
        }

    }
}