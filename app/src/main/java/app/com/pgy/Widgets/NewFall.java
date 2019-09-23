package app.com.pgy.Widgets;

import android.animation.ObjectAnimator;
import android.view.View;

import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;

/**
 * Created by YX on 2018/4/24.
 */

public class NewFall extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 0,2,5,7,5,3,2, 1.5f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY", 0,2,5,7,5,3,2, 1.5f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration * 3 / 2)
//                ObjectAnimator.ofFloat(view, "translationY", -1000, 0).setDuration(mDuration),
//                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration * 3 / 2)
//                ObjectAnimator.ofFloat(view, "rotationY", 180, 0).setDuration(mDuration),
//                ObjectAnimator.ofFloat(view, "translationX", -500, 0).setDuration(mDuration),
//                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration * 3 / 2)

        );
    }
}
