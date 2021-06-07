package space.karsukova.educateapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import space.karsukova.educateapp.R;
import space.karsukova.educateapp.fragments.RequestsFragment;
import space.karsukova.educateapp.fragments.ViewGroupMaterialFragment;

public class GroupPagerAdapterAdmin extends FragmentPagerAdapter {

    public GroupPagerAdapterAdmin(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;
            case 1:
                ViewGroupMaterialFragment viewGroupMaterialFragment = new ViewGroupMaterialFragment();
                return viewGroupMaterialFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "ЗАПРОСЫ";
            case 1:
                return "МАТЕРИАЛЫ";
            default:
                return null;
        }
    }
}
