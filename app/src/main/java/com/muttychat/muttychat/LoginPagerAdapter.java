package com.muttychat.muttychat;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginPagerAdapter extends FragmentPagerAdapter {
    public LoginPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LoginFragement loginFragement = new LoginFragement();
                return loginFragement;


            case 1:
                RegistationFragement registationFragement = new RegistationFragement();
                return registationFragement;

            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Login";

            case 1:
                return "Registration";

        }

        return super.getPageTitle(position);
    }
}
