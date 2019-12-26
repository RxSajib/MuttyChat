package com.muttychat.muttychat;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                UsersFragement usersFragement = new UsersFragement();
                return usersFragement;

            case 1:
                ChatFragement chatFragement = new ChatFragement();
                return chatFragement;

                default:
                    return null;
        }
       // return null;
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
                return "Users";
            case 1:
                return "Chat";
        }
        return super.getPageTitle(position);
    }
}
