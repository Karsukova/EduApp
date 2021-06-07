package space.karsukova.educateapp.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

import space.karsukova.educateapp.R;

public class HomeFragment extends Fragment {
    private SliderLayout sliderLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sliderLayout = view.findViewById(R.id.slider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(1);

        setSliderViews();
        return view;
    }

    private void setSliderViews() {
        for (int i = 0; i <5; i++){
            DefaultSliderView sliderView = new DefaultSliderView(getContext());
            switch (i){
                case  0:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/eduapp-468a6.appspot.com/o/gallery%2F156118_3.jpg?alt=media&token=27ec1328-a1b0-4233-8e3f-5c1967f1edd5");
                    break;
                case  1:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/eduapp-468a6.appspot.com/o/gallery%2F4.jpg?alt=media&token=a71367e1-2089-4f1a-b0a8-399735765bc1");
                    break;
                case  2:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/eduapp-468a6.appspot.com/o/gallery%2F2.jpg?alt=media&token=49a2ac40-f295-4541-92ee-76f3b37bb8d9");
                    break;
                case  3:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/eduapp-468a6.appspot.com/o/gallery%2F5.jpg?alt=media&token=cbe4cee1-e555-4247-882b-6ce152a63976");
                    break;
                case  4:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/eduapp-468a6.appspot.com/o/gallery%2F6.jpg?alt=media&token=32ec7b1a-e551-4642-94ed-47abc9355065");
                    break;
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderLayout.addSliderView(sliderView);
        }
    }
}