package ir.meandme.customerregistration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import ir.meandme.persianviews.ConfigAndURLs;

public class SingleCustomer extends AppCompatActivity {

    @BindView(R.id.firstname) EditText firstname;
    @BindView(R.id.lastname) EditText lastname;
    @BindView(R.id.phonenumber) EditText phonenumber;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.address) EditText address;
    @BindView(R.id.phone) EditText phone;
    @BindView(R.id.save_btn) Button save_btn;
    @BindView(R.id.data_display) TextView date_display;

    private Box<Customer> customerBox;
    private Long customer_id ;
    private Customer customer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_customer_activity);
        ButterKnife.bind(this);
        customerBox = ((MainApplication) getApplication()).getBoxStore().boxFor(Customer.class);
        Long  id = getIntent().getExtras().getLong("CUSTOMER_NUMBER");
        if (id != null) {
            customer_id = id;
            fillData(id);
        }
        else finish();

    }

    @OnClick(R.id.back) void backPress(){
        onBackPressed();
    }

    private void fillData(Long id) {
        customer = customerBox.query().equal(Customer_.id,id).build().findFirst();
        firstname.setText(String.valueOf(customer.getFirstname()));
        lastname.setText(String.valueOf(customer.getLastname()));
        email.setText(String.valueOf(customer.getEmail()));
        phonenumber.setText(String.valueOf(customer.getPhonenumber()));
        phone.setText(String.valueOf(customer.getPhone()));
        address.setText(String.valueOf(customer.getAddress()));

        Calendar cal = Calendar.getInstance();
        cal.setTime(customer.getDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        CalendarTool calendarTool = new CalendarTool(year,month,day);
//        calendarTool.setGregorianDate();
        date_display.setText(calendarTool.getIranianYear() + "/" + calendarTool.getIranianMonth() + "/" +calendarTool.getIranianDay() );
    }



    @OnClick(R.id.save_btn) void updateCustomer() {
        if (isValidCustomerData()) {
            Customer customer = new Customer();
            customer.setId(customer_id);
            customer.setFirstname(firstname.getText().toString());
            customer.setLastname(lastname.getText().toString());
            customer.setPhonenumber(phonenumber.getText().toString());
            customer.setPhone(phone.getText().toString());
            customer.setEmail(email.getText().toString());
            customer.setAddress(address.getText().toString());
            customer.setDate(customer.getDate());
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
