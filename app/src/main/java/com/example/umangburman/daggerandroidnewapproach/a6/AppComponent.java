package com.example.umangburman.daggerandroidnewapproach.a6;

import android.app.Application;

import com.example.umangburman.daggerandroidnewapproach.a2.AppModule;
import com.example.umangburman.daggerandroidnewapproach.a4.ActivityBuilder;
import com.example.umangburman.daggerandroidnewapproach.a5.MyApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = { AndroidInjectionModule.class, AppModule.class, ActivityBuilder.class })
public interface AppComponent extends AndroidInjector<MyApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

    void inject(MyApplication application);


}
