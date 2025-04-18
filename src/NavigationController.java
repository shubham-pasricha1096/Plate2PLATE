package controllers;

import javax.swing.*;

public class NavigationController {
    private static JFrame currentWindow;

    public static void navigateTo(Class<? extends JFrame> screenClass, Object... params) {
        if (currentWindow != null) {
            currentWindow.dispose();
        }

        try {
            currentWindow = screenClass.getDeclaredConstructor(getParameterTypes(params))
                    .newInstance(params);
            currentWindow.setVisible(true);
        } catch (Exception e) {
            showError("Navigation failed: " + e.getMessage());
        }
    }

    private static Class<?>[] getParameterTypes(Object... params) {
        Class<?>[] types = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }
        return types;
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Navigation Error", JOptionPane.ERROR_MESSAGE);
    }
}