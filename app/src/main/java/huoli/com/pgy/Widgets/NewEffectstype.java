package huoli.com.pgy.Widgets;

import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FadeIn;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FlipH;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FlipV;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.NewsPaper;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SideFall;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideLeft;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideRight;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideTop;

/**
 * Created by YX on 2018/4/24.
 */

public enum NewEffectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(com.gitonway.lee.niftymodaldialogeffects.lib.effects.Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(com.gitonway.lee.niftymodaldialogeffects.lib.effects.RotateBottom.class),
    RotateLeft(com.gitonway.lee.niftymodaldialogeffects.lib.effects.RotateLeft.class),
    Slit(com.gitonway.lee.niftymodaldialogeffects.lib.effects.Slit.class),
    Shake(com.gitonway.lee.niftymodaldialogeffects.lib.effects.Shake.class),
    Sidefill(SideFall.class),
    NewFall(NewFall.class);
    private Class<? extends BaseEffects> effectsClazz;

    NewEffectstype(Class<? extends BaseEffects> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        BaseEffects bEffects = null;
        try {
            bEffects = effectsClazz.newInstance();
        } catch (ClassCastException e) {
            throw new Error("Can not init animatorClazz instance");
        } catch (InstantiationException e) {
            throw new Error("Can not init animatorClazz instance");
        } catch (IllegalAccessException e) {
            throw new Error("Can not init animatorClazz instance");
        }
        return bEffects;
    }
}

