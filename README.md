# Login Example using Efficient Dagger2 - Android Injector

This is a very simple **Login Example of Dagger2 - An Efficient Approach** in Android. It takes the input from the UI, uses Dagger2 and displays back on the UI efficiently.

This example is for those beginners who want to learn this efficient approach a.k.a **Android Injector**. One of the primary advantages of Dagger 2 over most other dependency injection frameworks is that its strictly generated implementation (no reflection) means that it can be used in Android applications. However, there are still some considerations to be made when using Dagger within Android applications.

So Let's Get Started:

1. What is Dependency Injection (Efficient)? 
2. What are ComponentInjector, AndroidInjector and other components?
3. How does it work?
4. Implementation Step-by-Step
5. Conclusion

## **What is Dependency Injection (Efficient)?**

**Answer:** Dependency Injection in build upon the concept of Inversion of Control. Which says that a class should get its dependencies from outside. In simple words, no class should instantiate another class but should get the instances from a configuration class.

If a java class creates an instance of another class via the new operator, then it cannot be used and tested independently from that class and is called a hard dependency.


## **What are ComponentInjector, AndroidInjector and other components?**

**Answer:** One of the central difficulties of writing an Android application using Dagger is that many Android framework classes are instantiated by the OS itself, like Activity and Fragment, but Dagger works best if it can create all the injected objects. Instead, you have to perform members injection in a lifecycle method.

This has a few problems:

- Copy-pasting code makes it hard to refactor later on. As more and more developers copy-paste that block, fewer will know what it actually does.

- More fundamentally, it requires the type requesting injection (FrombulationActivity) to know about its injector. Even if this is done through interfaces instead of concrete types, it breaks a core principle of dependency injection: a class shouldn’t know anything about how it is injected.

To overcome these, Dagger has introduced **dagger.android** from **version - 2.10** onwards. The classes in dagger.android offer one approach to simplify this pattern.

1. **AndroidInjectionModule:** Contains bindings to ensure the usability of dagger.android framework classes. This module should be installed in the component that is used to inject the Application class.

2. **ContributesAndroidInjector:** Generates an AndroidInjector for the return type of this method. The injector is implemented with a Subcomponent and will be a child of the Module's component. This annotation must be applied to an abstract method in a Module that returns a concrete Android framework type (e.g. FooActivity, BarFragment, MyService, etc). The method should have no parameters. 

3. **HasActivityInjector:** Provides an AndroidInjector of Activitys.

4. **DispatchingAndroidInjector<T>:** Performs members-injection on instances of core Android types (e.g. Activity, Fragment) that are constructed by the Android framework and not by Dagger. This class relies on an injected mapping from each concrete class to an AndroidInjector.Factory for an AndroidInjector of that class. Each concrete class must have its own entry in the map, even if it extends another class which is already present in the map. Calls Object.getClass() on the instance in order to find the appropriate AndroidInjector.Factory.
  
5. **AndroidInjection:** Injects core Android types.


## **How does it work?**

**Answer:** AndroidInjection.inject() gets a DispatchingAndroidInjector<Activity> from the Application and passes your activity to inject(Activity). The DispatchingAndroidInjector looks up the AndroidInjector.Factory for your activity’s class (which is YourActivitySubcomponent.Builder), creates the AndroidInjector (which is YourActivitySubcomponent), and passes your activity to inject(YourActivity).
  

## **Implementation Step-by-Step**

### **Step1:** Adding Implementations in your Gradle File:

```Java
def dagger_versions = "2.17"

// Dagger core dependencies
annotationProcessor "com.google.dagger:dagger-compiler:$dagger_versions"
implementation "com.google.dagger:dagger:$dagger_versions"

// Dagger Android dependencies
annotationProcessor "com.google.dagger:dagger-android-processor:$dagger_versions"
implementation "com.google.dagger:dagger-android:$dagger_versions"
implementation "com.google.dagger:dagger-android-support:$dagger_versions"
```

### **Step2:** Create a new class for View Model as shown below:

```Java
public class LoginViewModel {

    public String strUsername, strrPassword;

    public String getStrUsername() {
        return strUsername;
    }

    public void setStrUsername(String strUsername) {
        this.strUsername = strUsername;
    }

    public String getStrrPassword() {
        return strrPassword;
    }

    public void setStrrPassword(String strrPassword) {
        this.strrPassword = strrPassword;
    }
}
```

### **Step3:** Create a new class for AppModule:

```Java
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
```

### **Step4:** Create a new abstract class ContributesAndroidInjector:

```Java
import com.example.umangburman.daggerandroidnewapproach.a2.AppModule;
import com.example.umangburman.daggerandroidnewapproach.a7.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = AppModule.class)

    abstract MainActivity bindMainActivity();

}
```


### **Step5:** Create a new class for DispatchingAndroidInjector<Activity>: 

```Java
import android.app.Activity;
import android.app.Application;

import com.example.umangburman.daggerandroidnewapproach.a6.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class MyApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
```

### **Step6:** Create an interface for AndroidInjector<T>:
  
```Java
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
```

### **Step7:** Setup Main Activity:

```Java
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.umangburman.daggerandroidnewapproach.R;
import com.example.umangburman.daggerandroidnewapproach.a1.LoginViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    @Inject
    LoginViewModel loginViewModel;

    private EditText txtEmail, txtPassword;
    private TextView lblEmail, lblPassword;
    private Button btnLogin;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);

        setContentView(R.layout.activity_main);

        txtEmail = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPassword);

        lblEmail = findViewById(R.id.lblEmailAnswer);
        lblPassword = findViewById(R.id.lblPasswordAnswer);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = txtEmail.getText().toString().trim();
                String Password = txtPassword.getText().toString().trim();

                loginViewModel.setStrUsername(Email);
                loginViewModel.setStrrPassword(Password);

                lblEmail.setText(loginViewModel.getStrUsername());
                lblPassword.setText(loginViewModel.getStrrPassword());

            }
        });

    }
}
```

Finally, your **activity_main.xml**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".a7.MainActivity">

    <android.support.v4.widget.NestedScrollView
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:isScrollContainer="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.6"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.0">

        <android.support.constraint.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:context=".View.MainActivity">

            <TextView
                android:id="@+id/lblTitle"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="16dp"
                android:lineSpacingExtra="8sp"
                android:text="Login Example Using Dagger2"
                android:textAlignment="center"
                android:textSize="18sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <EditText
                android:id="@+id/txtEmailAddress"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="32dp"
                android:ems="10"
                android:hint="E-Mail Address"
                android:inputType="textEmailAddress"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/lblTitle" />

            <EditText
                android:id="@+id/txtPassword"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="16dp"
                android:ems="10"
                android:hint="Password"
                android:inputType="textPassword"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/txtEmailAddress" />

            <Button
                android:id="@+id/btnLogin"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginEnd="32dp"
                android:layout_marginStart="32dp"
                android:layout_marginTop="32dp"
                android:onClick="@{(v) -> ClickListener.onClick(v)}"
                android:text="Click to Login"
                android:textSize="18sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/txtPassword" />

            <TextView
                android:id="@+id/lblTagline"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="60dp"
                android:text="See the Results With Dagger2"
                android:textColor="@android:color/background_dark"
                android:textSize="20sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/btnLogin" />

            <TextView
                android:id="@+id/lblEmailAnswer"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="8dp"
                android:text="---"
                android:textColor="@android:color/holo_blue_light"
                android:textSize="20sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/lblTagline" />

            <TextView
                android:id="@+id/lblPasswordAnswer"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="32dp"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="8dp"
                android:text="---"
                android:textColor="@android:color/holo_blue_light"
                android:textSize="20sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/lblEmailAnswer" />

        </android.support.constraint.ConstraintLayout>

    </android.support.v4.widget.NestedScrollView>

</android.support.constraint.ConstraintLayout>
```

And, That is it.


## **Conclusion**

Hopefully this guide introduced you to a lesser known yet useful form of Android application dependency injection known as **Dagger2 - Efficient Approach**.

Feel free to reach me at any time on [LinkedIn](https://www.linkedin.com/in/umangburman/)
