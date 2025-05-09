package com.example.marketix

import android.content.Context
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

open class BaseActivity : DaggerAppCompatActivity() {

    /*
       Uncomment if you disable PrivateFactory injection. See ViewPumpConfig#setPrivateFactoryInjectionEnabled(boolean)
    */
    //    @Override
    //    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    //    public View onCreateView(View parent, String name, @NonNull Context context, @NonNull AttributeSet attrs) {
    //        return ViewPumpContextWrapper.onActivityCreateView(this, parent, super.onCreateView(parent, name, context, attrs), name, context, attrs);
    //    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
}