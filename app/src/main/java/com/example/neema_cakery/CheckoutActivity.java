package com.example.neema_cakery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvTotal;
    private RadioGroup rgPayment;
    private LinearLayout llMpesa, llCard;
    private TextInputEditText etMpesaPhone, etCardNumber, etExpiry, etCvv;
    private Button btnPay;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        dbHelper = new DatabaseHelper(this);

        tvTotal = findViewById(R.id.tvCheckoutTotal);
        rgPayment = findViewById(R.id.rgPayment);
        llMpesa = findViewById(R.id.llMpesaInput);
        llCard = findViewById(R.id.llCardInput);
        etMpesaPhone = findViewById(R.id.etMpesaPhone);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiry = findViewById(R.id.etExpiry);
        etCvv = findViewById(R.id.etCvv);
        btnPay = findViewById(R.id.btnPayNow);

        double totalAmount = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);
        tvTotal.setText(String.format(Locale.getDefault(), "KSH %.0f", totalAmount));

        rgPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbMpesa) {
                llMpesa.setVisibility(View.VISIBLE);
                llCard.setVisibility(View.GONE);
            } else if (checkedId == R.id.rbCard) {
                llMpesa.setVisibility(View.GONE);
                llCard.setVisibility(View.VISIBLE);
            }
        });

        btnPay.setOnClickListener(v -> {
            if (rgPayment.getCheckedRadioButtonId() == R.id.rbMpesa) {
                String phone = etMpesaPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "Please enter your M-Pesa phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "STK Push sent to " + phone + ". Prompting for PIN...", Toast.LENGTH_LONG).show();
            } else {
                String card = etCardNumber.getText().toString().trim();
                String exp = etExpiry.getText().toString().trim();
                String cvv = etCvv.getText().toString().trim();
                if (TextUtils.isEmpty(card) || TextUtils.isEmpty(exp) || TextUtils.isEmpty(cvv)) {
                    Toast.makeText(this, "Please fill in all card details", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "Processing card payment...", Toast.LENGTH_LONG).show();
            }

            dbHelper.clearCart();
            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
