package ir.meandme.customerregistration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ir.meandme.persianviews.OnOneOffClickListener;

//import client.models.Facility;

/**
 * Created by Mostafa on 8/26/2016.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

    private final List<Customer> customers;
    private Context mContext;


    private CustomerAdapter THIS = this;
//    private CalendarTool calendarTool = new CalendarTool();



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView customer_name,customer_shap_name;
        public LinearLayout customer_item;



        public MyViewHolder(View convertView) {
            super(convertView);
            customer_name = convertView.findViewById(R.id.customer_name);
            customer_shap_name = convertView.findViewById(R.id.shape_name);
            customer_item = convertView.findViewById(R.id.customer_item);
            
        }

        @Override
        public void onClick(View v) {
            //put data on hawk and get in another activity
        }


    }


    public CustomerAdapter(Context mContext, List<Customer> services) {

        this.mContext = mContext;
        this.customers = services;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder,final int position) {
        final Customer customer= customers.get(position);
        String shapename1 = "";
        if (customer.getFirstname()!=null && customer.getFirstname().length()>0){
            shapename1 = String.valueOf( customer.getFirstname().charAt(0));
        }
        if (customer.getLastname()!=null && customer.getLastname().length()>0){
            if (!shapename1.equals(""))
                shapename1 += String.valueOf( customer.getFirstname().charAt(0));
            else {
                shapename1 = String.valueOf( customer.getFirstname().charAt(0)) + String.valueOf( customer.getFirstname().charAt(0));
            }
        }
        viewHolder.customer_name.setText(String.valueOf(customer.getFirstname() +" "+ customer.getLastname()));
        viewHolder.customer_shap_name.setText(shapename1);
        viewHolder.customer_item.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(mContext,SingleCustomer.class);
                intent.putExtra("CUSTOMER_NUMBER",customer.getId());
                ((MainActivity)mContext).startActivityForResult(intent,101);
            }
        });
    }



    @Override
    public int getItemCount() {
        return customers.size();
    }


    private void addOnClickListenerToViews(final MyViewHolder viewHolder, final int position, final Customer customer){
    }



}