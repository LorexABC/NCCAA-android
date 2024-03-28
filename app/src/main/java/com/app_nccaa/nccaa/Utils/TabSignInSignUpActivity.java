package com.app_nccaa.nccaa.Utils;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app_nccaa.nccaa.Fragment.Employer_Info_Frag;
import com.app_nccaa.nccaa.Fragment.Personal_Info_Frag;

public class TabSignInSignUpActivity extends FragmentPagerAdapter {

    private Context myContext;
    private int totalTabs;

    public TabSignInSignUpActivity(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Personal_Info_Frag personal_info_frag = new Personal_Info_Frag();
                return personal_info_frag;
            case 1:
                Employer_Info_Frag employeer_info_frag = new Employer_Info_Frag();
                return employeer_info_frag;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
