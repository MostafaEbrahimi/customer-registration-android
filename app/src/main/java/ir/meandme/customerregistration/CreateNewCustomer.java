package ir.meandme.customerregistration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import ir.meandme.persianviews.ConfigAndURLs;

public class CreateNewCustomer extends AppCompatActivity {

    @BindView(R.id.firstname) EditText firstname;
    @BindView(R.id.lastname) EditText lastname;
    @BindView(R.id.phonenumber) EditText phonenumber;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.address) EditText address;
    @BindView(R.id.phone) EditText phone;
    @BindView(R.id.save_btn) Button save_btn;

    private Box<Customer> customerBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_customer_activity);
        ButterKnife.bind(this);
//        firstname.requestFocus();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        customerBox = ((MainApplication) getApplication()).getBoxStore().boxFor(Customer.class);
    }

    @OnClick(R.id.back) void backPress(){
        onBackPressed();
    }


    @OnClick(R.id.save_btn)
    void saveNewCustomer() {
        if (isValidCustomerData()) {
            Customer customer = new Customer();
            customer.setFirstname(firstname.getText().toString());
            customer.setLastname(lastname.getText().toString());
            customer.setPhonenumber(phonenumber.getText().toString());
            customer.setPhone(phone.getText().toString());
            customer.setEmail(email.getText().toString());
            customer.setAddress(address.getText().toString());
            customer.setDate(new Date());
            customerBox.put(customer);
            setResult(RESULT_OK);
            this.finish();
        }
    }

    public boolean isValidCustomerData() {
        if (firstname.length()<3){
            ConfigAndURLs.showToast(this,getString(R.string.first_name_must_be_more_than_3));
            return false;
        }
        if (lastname.length()<3){
            ConfigAndURLs.showToast(this,getString(R.string.last_name_must_be_more_than_3));
            return false;
        }
        if (phonenumber.length()!=11){
            ConfigAndURLs.showToast(this,getString(R.string.phone_number_must_be_11));
            return false;
        }
        return true;
    }
}
