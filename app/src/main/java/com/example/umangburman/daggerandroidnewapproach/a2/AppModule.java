package com.example.umangburman.daggerandroidnewapproach.a2;

import android.app.Application;
import android.content.Context;

import com.example.umangburman.daggerandroidnewapproach.a1.LoginViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Singleton
    @Provides
    Context provideContext(Application application) {
        return application;
    }


    @Provides
    LoginViewModel provideLoginViewModel() {
        return new LoginViewModel();
    }

    // ........ Other Object Creation Below ......... //

}
