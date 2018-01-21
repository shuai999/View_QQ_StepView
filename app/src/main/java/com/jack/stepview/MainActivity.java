package com.jack.stepview;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;


/**
 * Email 2185134304@qq.com
 * Created by JackChen on 2018/1/21.
 * Version 1.0
 * Description: 让QQ运动步数动起来
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final QQStepView step_view = (QQStepView) findViewById(R.id.step_view);
        step_view.setMaxStep(4000); //设置最大步数为4000

        //属性动画 从0到3000 让运动步数动起来
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 3000);
        //这个属性动画1秒钟执行完
        valueAnimator.setDuration(1000) ;
        //设置插值器
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                step_view.setCurrentStep((int) animatedValue);
            }
        });
        //开启属性动画
        valueAnimator.start();

    }
}
