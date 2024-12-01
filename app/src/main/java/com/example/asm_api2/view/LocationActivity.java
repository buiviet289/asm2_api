package com.example.asm_api2.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_api2.adapter.Adapter_Item_District_Select_GHN;
import com.example.asm_api2.adapter.Adapter_Item_Province_Select_GHN;
import com.example.asm_api2.adapter.Adapter_Item_Ward_Select_GHN;
import com.example.asm_api2.model.District;
import com.example.asm_api2.model.DistrictRequest;
import com.example.asm_api2.model.Fruit;
import com.example.asm_api2.model.GHNItem;
import com.example.asm_api2.model.GHNOrderRequest;
import com.example.asm_api2.model.GHNOrderRespone;
import com.example.asm_api2.model.Order;
//import com.example.asm_api2.model.Response;
import com.example.asm_api2.model.Province;
import com.example.asm_api2.model.ResponeGHN;
import com.example.asm_api2.model.Ward;
import com.example.asm_api2.R;
import com.example.asm_api2.services.GHNRequest;
import com.example.asm_api2.services.GHNServices;
import com.example.asm_api2.databinding.ActivityLocationBinding;
import com.example.asm_api2.services.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
LocationActivity extends AppCompatActivity {
    String TAG = "err";
    private ActivityLocationBinding binding;
    private GHNRequest request;
    private GHNServices ghnServices;
    private String productId, productTypeId, productName, description, WardCode;
    private double rate, price;
    private int image, DistrictID, ProvinceID;
    private Adapter_Item_Province_Select_GHN adapter_item_province_select_ghn;
    private Adapter_Item_District_Select_GHN adapter_item_district_select_ghn;
    private Adapter_Item_Ward_Select_GHN adapter_item_ward_select_ghn;
    HttpRequest httpRequest;
    Button btn_order;
    EditText edt_name,edt_phone,edt_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        httpRequest = new HttpRequest();
        request = new GHNRequest();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getString("productId");
            productTypeId = bundle.getString("productTypeId");
            productName = bundle.getString("productName");
            description = bundle.getString("description");
            rate = bundle.getDouble("rate");
            price = bundle.getDouble("price");
            image = bundle.getInt("image");
        }

        request.callAPI().getListProvince().enqueue(responseProvince);
        binding.spProvince.setOnItemSelectedListener(onItemSelectedListener);
        binding.spDistrict.setOnItemSelectedListener(onItemSelectedListener);
        binding.spWard.setOnItemSelectedListener(onItemSelectedListener);

        binding.spProvince.setSelection(1);
        binding.spDistrict.setSelection(1);
        binding.spWard.setSelection(1);
        btn_order = findViewById(R.id.btn_order);
         edt_name = findViewById(R.id.edt_name);
         edt_phone = findViewById(R.id.edt_phone);
         edt_location = findViewById(R.id.edt_location);
        btn_order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onFailure: 1");

                if (WardCode.equals("")) return;
                Log.d(TAG, "onFailure: 2");

                Fruit fruit = (Fruit) getIntent().getExtras().getSerializable("item");
//                if (fruit == null) {
//                    Toast.makeText(LocationActivity.this, "Sản phẩm không hợp lệ!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Log.d(TAG, "onFailure: 3"+fruit);

                GHNItem ghnItem = new GHNItem();
                Log.d(TAG, "onFailure: 4");

                ghnItem.setName("thuy");
                Log.d(TAG, "onFailure: 5");
                ghnItem.setPrice(12);
                Log.d(TAG, "onFailure: 5.1");
                ghnItem.setCode("1");
                Log.d(TAG, "onFailure: 5.2");
                ghnItem.setQuantity(1);
                Log.d(TAG, "onFailure: 5.3  ");
                ghnItem.setWeight(50);
                Log.d(TAG, "onFailure: 5");

                ArrayList<GHNItem> items = new ArrayList<>();
                Log.d(TAG, "onFailure: 6");

                items.add(ghnItem);
                Log.d(TAG, "onFailure: 7");

                GHNOrderRequest ghnOrderRequest = new GHNOrderRequest(

                        edt_name.getText().toString(),
                        edt_phone.getText().toString(),
                        edt_location.getText().toString(),
                        WardCode,
                        DistrictID,
                        items
                );
                Log.d(TAG, "onFailure: 8"+items);

                request.callAPI().GHNOrder(ghnOrderRequest).enqueue(responseOrder);
                Log.d(TAG, "onFailure: 9");

            }
        });

    }

    Callback<ResponeGHN<GHNOrderRespone>> responseOrder = new Callback<ResponeGHN<GHNOrderRespone>>() {
        @Override
        public void onResponse(Call<ResponeGHN<GHNOrderRespone>> call, retrofit2.Response<ResponeGHN<GHNOrderRespone>> response) {
            Log.d(TAG, "onFailure: ok");

            if (response.isSuccessful()) {
                if (response.body().getCode() == 200){
                    Log.d(TAG, "onFailure: okk");

                    Toast.makeText(LocationActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    Order order = new Order();
                    order.setOrder_code(response.body().getData().getOrder_code());
                    order.setId_user(getSharedPreferences("INFO", MODE_PRIVATE).getString("id", ""));
//                    httpRequest.callAPI().order(order).enqueue(responseOrderDatabase);
                    httpRequest.callAPI().order(order);
                    }
            }
        }

        @Override
        public void onFailure(Call<ResponeGHN<GHNOrderRespone>> call, Throwable t) {
            Log.d(TAG, "onFailure: loi");
            Toast.makeText(LocationActivity.this, "Loi", Toast.LENGTH_SHORT).show();
        }
    };
//    Callback< ResponeGHN <Order>> responseOrderDatabase = new Callback<ResponeGHN <Order>>() {
//        @Override
//        public void onResponse(Call<ResponeGHN<Order>> call, retrofit2.Response<ResponeGHN<Order>> response) {
//            if (response.isSuccessful()) {
//                if (response.body().getCode() ==200) {
//                    Toast.makeText(LocationActivity.this, "Cảm ơn đã mua hàng", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        }
//
//        @Override
//        public void onFailure(Call<ResponeGHN<Order>> call, Throwable t) {
//            Toast.makeText(LocationActivity.this, "Loi", Toast.LENGTH_SHORT).show();
//        }
//    };

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getId() == R.id.sp_province) {
                        ProvinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                        DistrictRequest districtRequest = new DistrictRequest(ProvinceID);
                        request.callAPI().getListDistrict(districtRequest).enqueue(responseDistrict);
                    } else if (parent.getId() == R.id.sp_district) {
                        DistrictID = ((District) parent.getAdapter().getItem(position)).getDistrictID();
                        request.callAPI().getListWard(DistrictID).enqueue(responseWard);
                    } else if (parent.getId() == R.id.sp_ward) {
                        WardCode = ((Ward) parent.getAdapter().getItem(position)).getWardCode();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    Callback<ResponeGHN<ArrayList<Province>>> responseProvince = new Callback<ResponeGHN<ArrayList<Province>>>() {
                @Override
                public void onResponse(Call<ResponeGHN<ArrayList<Province>>> call, Response<ResponeGHN<ArrayList<Province>>> response) {
                    if(response.isSuccessful()){
                        if(response.body().getCode() == 200){
                            ArrayList<Province> ds = new ArrayList<>(response.body().getData());
                            SetDataSpinProvince(ds);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponeGHN<ArrayList<Province>>> call, Throwable t) {
                    Toast.makeText(LocationActivity.this, "Lấy dữ liệu bị lỗi", Toast.LENGTH_SHORT).show();
                }
            };

    Callback<ResponeGHN<ArrayList<District>>> responseDistrict = new Callback<ResponeGHN<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<ResponeGHN<ArrayList<District>>> call, Response<ResponeGHN<ArrayList<District>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<District> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinDistrict(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeGHN<ArrayList<District>>> call, Throwable t) {

        }
    };

    Callback<ResponeGHN<ArrayList<Ward>>> responseWard = new Callback<ResponeGHN<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<ResponeGHN<ArrayList<Ward>>> call, Response<ResponeGHN<ArrayList<Ward>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){

                    if(response.body().getData() == null)
                        return;

                    ArrayList<Ward> ds = new ArrayList<>(response.body().getData());

                    ds.addAll(response.body().getData());
                    SetDataSpinWard(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeGHN<ArrayList<Ward>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    private void SetDataSpinProvince(ArrayList<Province> ds){
        adapter_item_province_select_ghn = new Adapter_Item_Province_Select_GHN(this, ds);
        binding.spProvince.setAdapter(adapter_item_province_select_ghn);
    }

    private void SetDataSpinDistrict(ArrayList<District> ds){
        adapter_item_district_select_ghn = new Adapter_Item_District_Select_GHN(this, ds);
        binding.spDistrict.setAdapter(adapter_item_district_select_ghn);
    }

    private void SetDataSpinWard(ArrayList<Ward> ds){
        adapter_item_ward_select_ghn = new Adapter_Item_Ward_Select_GHN(this, ds);
        binding.spWard.setAdapter(adapter_item_ward_select_ghn );
    }



}