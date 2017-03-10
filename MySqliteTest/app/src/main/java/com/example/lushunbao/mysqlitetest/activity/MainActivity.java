package com.example.lushunbao.mysqlitetest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lushunbao.mysqlitetest.R;
import com.example.lushunbao.mysqlitetest.model.Customer;
import com.example.lushunbao.mysqlitetest.service.CustomerService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btn_add;
    private Button btn_delete;
    private Button btn_update;
    private Button btn_query;
    private TextView db_info;

    private CustomerService customerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customerService = new CustomerService(this);
        init();
    }

    private void init(){
        btn_add = (Button)findViewById(R.id.btn_add);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_query = (Button)findViewById(R.id.btn_query);
        db_info = (TextView)findViewById(R.id.db_info);
    }

    public void onClick(View v){
        int id = v.getId();
        ArrayList<Customer> customers = customerService.getAllCustomer();
        int size = customers.size();
        switch (id){
            case R.id.btn_add:
                Customer customer = new Customer(1,"lusb1",27);
                customerService.addCustomer(customer);
                db_info.setText(customerService.getAllCustomerInfo());
                break;
            case R.id.btn_delete:
                if(size > 0){
                    Customer customerToDelete = customers.get(0);
                    customerService.deleteCustomer(customerToDelete.getId());
                    db_info.setText(customerService.getAllCustomerInfo());
                }
                break;
            case R.id.btn_update:
                if(size > 0){
                    Customer customerToUpdate = customers.get(customers.size()-1);
                    customerToUpdate.setName("updated Customer!");
                    customerService.updateCustomer(customerToUpdate);
                    db_info.setText(customerService.getAllCustomerInfo());
                }
                break;
            case R.id.btn_query:
                db_info.setText(customerService.getAllCustomerInfo());
                break;
        }
    }
}
