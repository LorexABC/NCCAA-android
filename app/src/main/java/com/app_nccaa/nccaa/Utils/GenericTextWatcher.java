package com.app_nccaa.nccaa.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.app_nccaa.nccaa.R;

public class GenericTextWatcher implements TextWatcher {
    private final EditText[] editText;
    private View view;
    public GenericTextWatcher(View view, EditText editText[])
    {
        this.editText = editText;
        this.view = view;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        switch (view.getId()) {

            case R.id.et1:
                if (text.length() == 1)
                    editText[1].requestFocus();
                break;
            case R.id.et2:

                if (text.length() == 1)
                    editText[2].requestFocus();
                else if (text.length() == 0)
                    editText[0].requestFocus();
                break;
            case R.id.et3:
                if (text.length() == 1)
                    editText[3].requestFocus();
                else if (text.length() == 0)
                    editText[1].requestFocus();
                break;
            case R.id.et4:
                if(text.length() == 1)
                    editText[4].requestFocus();
                else if (text.length() == 0)
                    editText[2].requestFocus();
                break;
            case R.id.et5:
                if(text.length() == 1)
                    editText[5].requestFocus();
                else if (text.length() == 0)
                    editText[3].requestFocus();
                break;
            case R.id.et6:
              if (text.length() == 1){

              }
             else if (text.length() == 0)
                    editText[4].requestFocus();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }
}