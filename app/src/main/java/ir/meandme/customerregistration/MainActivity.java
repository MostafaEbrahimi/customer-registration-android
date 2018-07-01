package ir.meandme.customerregistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.GoalRow;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bloco.faker.Faker;
import io.objectbox.Box;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.customer_recycler_view) RecyclerView customer_recyclerview;
    @BindView(R.id.progress_container) LinearLayout progress_container;
    @BindView(R.id.no_customer_exist) LinearLayout no_customer_exist;
    @BindView(R.id.no_customer_img) ImageView no_customer_exist_img;
    @BindView(R.id.add_new_customer_no_customer_mode) TextView add_new_customer_no_customer_mode;

    private Box<Customer> customerBox ;
    private ArrayList<Customer> customers = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private int page = 0;
    private int pageSize = 20;
    private CustomerAdapter customerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        customerBox = ((MainApplication) getApplication()).getBoxStore().boxFor(Customer.class);
        Hawk.init(this).build();
//        insertFakeDataFirstTime();
        customers.clear();
        List<Customer> customerTemp = customerBox.query().orderDesc(Customer_.id).build().find(pageSize*page,pageSize);
        if (customerTemp.size()>0)
            page++;
        customers.addAll(customerTemp);
        if (customers.size()<=0 && customer_recyclerview.getVisibility()==View.VISIBLE){
            customer_recyclerview.setVisibility(View.GONE);
            no_customer_exist.setVisibility(View.VISIBLE);
        }
        customerAdapter = new CustomerAdapter(this,customers);
        customer_recyclerview.setLayoutManager(linearLayoutManager);
        customer_recyclerview.setAdapter(customerAdapter);

        Glide.with(this).load(R.drawable.man).into(no_customer_exist_img);
        handlePaging();
    }

    private void insertFakeDataFirstTime() {
        if (!Hawk.contains("bool")) {
            Hawk.put("bool", true);
            generateFakeCustomers();
        }

    }

    private void generateFakeCustomers() {
        Faker faker = new Faker();
        for (int i=0;i<100;i++){
            Customer customer = new Customer();
            customer.setFirstname(String.valueOf(faker.name.firstName()));
            customer.setLastname(String.valueOf(faker.name.lastName()));
            customer.setAddress(String.valueOf(faker.address.streetAddress()));
            customer.setPhonenumber(String.valueOf(faker.phoneNumber.phoneNumber()));
            customer.setPhone(String.valueOf(faker.phoneNumber.cellPhone()));
            customer.setEmail(String.valueOf(faker.internet.email(faker.name.firstName())));
            customerBox.put(customer);
        }
    }

    @OnClick(R.id.add_new_customer) void addNewCustomer(){
        startActivityForResult(new Intent(this,CreateNewCustomer.class),100);
    }

    @OnClick(R.id.add_new_customer_no_customer_mode) void addNewCustomerNoCustomerMode(){
        addNewCustomer();
    }

    private void handlePaging() {
        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    progress_container.setVisibility(View.VISIBLE);
                    retrieveMoreResults();
                }
            }
        };
        customer_recyclerview.setOnScrollListener(mScrollListener);
    }

    private void retrieveMoreResults() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Customer> customerTemp = customerBox.query().orderDesc(Customer_.id).build().find(pageSize*page,pageSize);
                if (customerTemp.size()>0) {
                    page++;
                    customers.addAll(customerTemp);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress_container.setVisibility(View.GONE);
                        customerAdapter.notifyDataSetChanged();
                        if (customers.size()>0 && customer_recyclerview.getVisibility()==View.GONE){
                            customer_recyclerview.setVisibility(View.VISIBLE);
                            no_customer_exist.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            customers.clear();
            progress_container.setVisibility(View.VISIBLE);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    page = 0;
                    final List<Customer> customerTemp = customerBox.query().orderDesc(Customer_.id).build().find(pageSize*page,pageSize);
                    if (customerTemp.size()>0)
                        page++;
                    customers.addAll(customerTemp);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_container.setVisibility(View.GONE);
                            customerAdapter.notifyDataSetChanged();
                            if (customers.size()>0 && customer_recyclerview.getVisibility()== View.GONE){
                                customer_recyclerview.setVisibility(View.VISIBLE);
                                no_customer_exist.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
            thread.start();
        }
    }
}
