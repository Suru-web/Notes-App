package transitions;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.ViewGroup;

public class ZoomInAnimator extends Transition {

    private static final String PROPERTY_SCALE_X = "scaleX";
    private static final String PROPERTY_SCALE_Y = "scaleY";

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        // Capture the initial scale values of the view
        transitionValues.values.put(PROPERTY_SCALE_X, 1f);
        transitionValues.values.put(PROPERTY_SCALE_Y, 1f);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        // Capture the final scale values of the view
        transitionValues.values.put(PROPERTY_SCALE_X, 2f); // Zoom in to 2x size
        transitionValues.values.put(PROPERTY_SCALE_Y, 2f);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        // Create an ObjectAnimator to animate scaleX and scaleY
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(endValues.view, PROPERTY_SCALE_X, 1f, 2f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(endValues.view, PROPERTY_SCALE_Y, 1f, 2f);

        // Create an AnimatorSet to run both animations together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);

        return animatorSet;
    }
}

