package com.example.umangburman.daggerandroidnewapproach.a4;

import com.example.umangburman.daggerandroidnewapproach.a2.AppModule;
import com.example.umangburman.daggerandroidnewapproach.a7.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = AppModule.class)

    abstract MainActivity bindMainActivity();

}
