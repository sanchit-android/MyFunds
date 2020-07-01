package com.sanchit.myfunds.activity.mf;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.myfunds.R;
import com.sanchit.myfunds.model.mf.Fund;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MFTradeAddActivity extends AppCompatActivity {

    private static Map<String, Fund> funds;

    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mf_trade_add);

        funds = (Map<String, Fund>) getIntent().getSerializableExtra("funds");

        setupFundSelector();
        setupTradeDatePicker(this);
        setupEditTexBoxes(this);
    }

    private void setupEditTexBoxes(MFTradeAddActivity mfTradeAddActivity) {
        final EditText et_Units = (EditText) findViewById(R.id.editTextMFUnits);
        final EditText et_Price = (EditText) findViewById(R.id.editTextMFPrice);
        final EditText et_Inv = (EditText) findViewById(R.id.editTextMFInvestment);

        MyEditTextListener listener = new MyEditTextListener(et_Units, et_Price, et_Inv);

        et_Price.addTextChangedListener(listener);
        et_Units.addTextChangedListener(listener);
    }

    private void setupTradeDatePicker(final MFTradeAddActivity context) {
        final EditText eText = (EditText) findViewById(R.id.editTextTradeDate);
        eText.setInputType(InputType.TYPE_NULL);
    }

    private void setupFundSelector() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getFundsName());
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextViewFundName);
        textView.setAdapter(adapter);
    }

    private String[] getFundsName() {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Fund> entry : funds.entrySet()) {
            result.add(entry.getValue().fundName);
        }
        String[] array = new String[result.size()];
        return result.toArray(array);
    }

    public void onClickEditTextTradeDate(View view) {
        final EditText editText = (EditText) view;

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(MFTradeAddActivity.this, R.style.MyTimePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }

    private static final class MyEditTextListener implements TextWatcher {


        private final EditText et_units;
        private final EditText et_price;
        private final EditText et_Inv;

        public MyEditTextListener(EditText et_units, EditText et_price, EditText et_Inv) {
            this.et_units = et_units;
            this.et_price = et_price;
            this.et_Inv = et_Inv;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                BigDecimal units = new BigDecimal(et_units.getText().toString());
                BigDecimal price = new BigDecimal(et_price.getText().toString());
                et_Inv.setText(units.multiply(price).toPlainString());
            } catch(Exception e) {
                et_Inv.setText("0");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
