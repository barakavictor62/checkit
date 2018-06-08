package com.example.baraka62.checkit;

import android.content.Context;
import android.view.Menu;

import java.lang.reflect.Constructor;

/**
 * Created by baraka62 on 12/4/2017.
 */

public class TheMenu {
    Context context;
    Menu menu = newMenuInstance(context);


    protected Menu newMenuInstance(Context context) {
        try {
            Class<?> menuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");

            Constructor<?> constructor = menuBuilderClass.getDeclaredConstructor(Context.class);

            return (Menu) constructor.newInstance(context);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
