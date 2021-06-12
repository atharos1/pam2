package com.tutv.android.di;

import android.content.Context;

public class ContainerLocator {
    private static Container container;

    private ContainerLocator() {}

    public static Container locateComponent(final Context context) {
        if(container == null)
            setComponent(new AppContainer(context));

        return container;
    }

    public static void setComponent(final Container container) {
        ContainerLocator.container = container;
    }
}
