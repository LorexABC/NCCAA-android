package com.app_nccaa.nccaa.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NameTextWatcher implements TextWatcher {

    private final EditText editText;

    public NameTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int before, int count) {
        // Not used in this feature.
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String input = s.toString();

        if(input.endsWith(" ") || input.endsWith("-")) {
            editText.removeTextChangedListener(this);
            editText.setText(input);
            editText.setSelection(editText.getText().length());
            editText.addTextChangedListener(this);
            return;
        }

        String[] names = input.split(" ");

        StringBuilder formattedName = new StringBuilder();
        for(String name: names) {
            if(!name.isEmpty()) {
                String formattedPart = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

                // Handle hyphenated middle names
                if (formattedPart.contains("-")) {
                    String[] hyphenatedNames = formattedPart.split("-");
                    StringBuilder hyphenatedFormattedName = new StringBuilder();
                    for (String hyphenatedName : hyphenatedNames) {
                        hyphenatedFormattedName.append(hyphenatedName.substring(0, 1).toUpperCase())
                                .append(hyphenatedName.substring(1).toLowerCase())
                                .append("-");
                    }
                    formattedPart = hyphenatedFormattedName.substring(0, hyphenatedFormattedName.length() - 1); // Remove the last "-"
                }

                formattedName.append(formattedPart).append(" ");
            }
            editText.removeTextChangedListener(this);
            editText.setText(formattedName.toString().trim());
            editText.setSelection(editText.getText().length());
            editText.addTextChangedListener(this);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Not used in this feature.
    }
}
