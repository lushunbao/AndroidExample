package com.example.lushunbao.mysqlitetest.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lushunbao.mysqlitetest.model.Customer;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lushunbao on 2017/3/8.
 */

public class CustomerService {
    private MySqliteHelper mySqliteHelper;
    private SQLiteDatabase db;

    private Context context;

    public CustomerService(Context context){
        this.context = context;
        mySqliteHelper = new MySqliteHelper(context);
        db = mySqliteHelper.getWritableDatabase();
    }

    public long addCustomer(Customer customer){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Customer.NAME,customer.getName());
        contentValues.put(Customer.AGE,customer.getAge());
        long rowId = db.insert(Customer.TB_NAME,null,contentValues);
        return rowId;
    }

    public void deleteCustomer(int id){
        db.delete(Customer.TB_NAME,Customer.ID+"=?",new String[]{id+""});
    }

    public void updateCustomer(Customer customer){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Customer.ID,customer.getId());
        contentValues.put(Customer.NAME,customer.getName());
        contentValues.put(Customer.AGE,customer.getAge());
        db.update(Customer.TB_NAME,contentValues,Customer.ID+"=?",new String[]{customer.getId()+""});
    }

    public ArrayList<Customer> getAllCustomer(){
        ArrayList<Customer> customers = new ArrayList<>();
        Cursor cursor = db.query(Customer.TB_NAME,new String[]{Customer.ID,Customer.NAME,Customer.AGE},null,null,null,null,null);
        if(cursor != null){
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex(Customer.ID));
                String name = cursor.getString(cursor.getColumnIndex(Customer.NAME));
                int age = cursor.getInt(cursor.getColumnIndex(Customer.AGE));
                Customer customer = new Customer(id,name,age);
                customers.add(customer);
            }
        }
        return customers;
    }

    public String getAllCustomerInfo(){
        ArrayList<Customer> customers = getAllCustomer();
        StringBuffer sb = new StringBuffer("");
        for(int i=0;i<customers.size();i++){
            Customer c = customers.get(i);
            if(i < customers.size()){
                sb.append("ID="+c.getId()+"\t"+"NAME="+c.getName()+"\t"+"AGE="+c.getAge()+"\n");
            }
            else{
                sb.append("ID="+c.getId()+"\t"+"NAME="+c.getName()+"\t"+"AGE="+c.getAge());
            }
        }
        return sb.toString();
    }

}
