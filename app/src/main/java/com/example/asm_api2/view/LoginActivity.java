package com.example.asm_api2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_api2.Fragment.Admin.Admin_Bottom_navigation;
import com.example.asm_api2.MainActivity;
import com.example.asm_api2.databinding.ActivityLoginBinding;
import com.example.asm_api2.model.Response;
import com.example.asm_api2.model.User;
import com.example.asm_api2.services.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        httpRequest = new HttpRequest();

        userListener();


    }


    private void userListener() {
        binding.logRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                String _username = binding.edUsername.getText().toString().trim();
                String _password = binding.edPassword.getText().toString().trim();
                user.setUsername(_username);
                user.setPassword(_password);
                httpRequest.callAPI().login(user).enqueue(responseUser);

            }
        });
    }

    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() ==200) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("INFO",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("refreshToken", response.body().getRefreshToken());
                    editor.putString("id", response.body().getData().get_id());
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, Admin_Bottom_navigation.class));
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            t.getMessage();
        }
    };

}