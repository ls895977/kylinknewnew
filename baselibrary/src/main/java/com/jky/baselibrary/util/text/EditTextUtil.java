package com.jky.baselibrary.util.text;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

public class EditTextUtil {

    public static void showPassword(EditText editText, boolean show) {
        if (editText == null)
            return;
        if (show) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
