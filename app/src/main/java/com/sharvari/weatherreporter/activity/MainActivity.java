package com.sharvari.weatherreporter.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.sharvari.weatherreporter.R;
import com.sharvari.weatherreporter.adapter.MessagesListAdapter;
import com.sharvari.weatherreporter.model.Message;
import com.sharvari.weatherreporter.model.Response.CityReport.CityReportParameters;
import com.sharvari.weatherreporter.model.Response.WeatherReport.WeatherReportParameters;
import com.sharvari.weatherreporter.model.Response.WeatherResponse;
import com.sharvari.weatherreporter.network.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {


    private CityReportParameters city = null;
    private WeatherReportParameters weather = null;

    @BindView(R.id.btnSend)
    Button btnSend;

    @BindView(R.id.inputMsg)
    EditText inputMsg;

    @BindView(R.id.list_view_messages)
    ListView listViewMessages;

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    private ApiService apiService;

    private CompositeDisposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        disposable = new CompositeDisposable();
        apiService = ApiClient.getClient(this).create(ApiService.class);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                appendMessage(inputMsg.getText().toString(), "Me");
                sendMessageToServer(inputMsg.getText().toString());
                inputMsg.setText("");
            }
        });

        listMessages = new ArrayList<Message>();

        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

        appendMessage("Hi", "Reporter");
    }

    private void sendMessageToServer(String message) {

        if (message != null && !message.isEmpty()) {
            apiService.askQuery(message);
            disposable.add(
                    apiService.askQuery(message)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {
                        @Override
                        public void onSuccess(WeatherResponse weatherResponse) {
                            if(weatherResponse.getReturnCode().equals("0")){
                                city = weatherResponse.getCityReport();
                                weather = weatherResponse.getWeatherReport();
                                Log.i("sharvari", "onSuccess: "+city.getCountry());
                                String respMsg = city.getName()+" have "+weather.getWeather().get(0).getMain()+".\nWith "+weather.getWeather().get(0).getDescription();
                                appendMessage(respMsg, "Reporter");
                            }else if(weatherResponse.getReturnCode().equals("1") && weather != null){
                                String msg = "";
                                if(weatherResponse.getReturnMsg().equals("Temperature")){
                                    msg = "Temperature today is "+weather.getMain().getTemp()+".";
                                }else if(weatherResponse.getReturnMsg().equals("Maximum temperature")){
                                    msg = "Might be "+weather.getMain().getTemp_max()+".";
                                }else if(weatherResponse.getReturnMsg().equals("Minimum temperature")){
                                    msg = "Temperature might fall till "+weather.getMain().getTemp_min()+".";
                                }else if(weatherResponse.getReturnMsg().equals("Clouds")){
                                    msg = "Hmmm..... It is "+weather.getClouds().getAll()+"% cloudy today.";
                                }else if(weatherResponse.getReturnMsg().equals("Humidity")){
                                    msg = "Hmmm..... It is "+weather.getMain().getHumidity()+"% humid today.";
                                }else if(weatherResponse.getReturnMsg().equals("Wind")){
                                    msg = "Ummm.... Wind speed is "+weather.getWind().getSpeed()+" and moving towards "+weather.getWind().getDeg()+" degree.";
                                }else if(weatherResponse.getReturnMsg().equals("Pressure")){
                                    msg = "Pressure today is "+weather.getMain().getPressure()+".";
                                }else{
                                    msg = "We cannot recognize the city. Please tell us city name.";
                                }
                                appendMessage(msg, "Reporter");
                            }else if(weatherResponse.getReturnCode() == "1"){
                                appendMessage("We cannot recognize the city. Please tell us city name.", "Reporter");
                            }else{
                                appendMessage(weatherResponse.getReturnMsg(), "Reporter");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("sharvari", "onSuccess: "+e.getMessage());
                            showError(e);
                        }
                    })
            );
        }else{
            appendMessage("Say something", "Reporter");
        }
    }


    private void showError(Throwable e) {
        String message = "";
        try {
            if (e instanceof IOException) {
                message = "No internet connection!";
            } else if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                String errorBody = error.response().errorBody().string();
                JSONObject jObj = new JSONObject(errorBody);

                message = jObj.getString("error");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (TextUtils.isEmpty(message)) {
            message = "Unknown error occurred! Check LogCat.";
        }

        Snackbar snackbar = Snackbar
                .make(linearLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }


    private void appendMessage(String message, final String type){
        final Message m = new Message();
        m.setSelf(type == "Me" ? true : false);
        m.setFromName(type == "Me" ? "Me" : "Reporter");
        m.setMessage(message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(type == "Reporter") playBeep();
                listMessages.add(m);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void playBeep() {
        try{
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this, notification);
            r.play();
        }catch (Exception e){}
    }

    private interface ApiService{
        @GET("/sharvari?api=weather")
        Single<WeatherResponse> askQuery(@Query("query") String query);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
