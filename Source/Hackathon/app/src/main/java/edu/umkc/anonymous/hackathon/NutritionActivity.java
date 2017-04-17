package edu.umkc.anonymous.hackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NutritionActivity extends AppCompatActivity {
    String sourceText;
    TextView inputTextView;
    TextView nutritionTextView;
    TextView nameTextView;
    TextView caloriesTextView;
    TextView servingTextView;
    TextView fatTextView;
    TextView saturatedTextView;
    TextView cholesterolTextView;
    TextView sodiumTextView;
    TextView fiberTextView;
    TextView vitaminATextView;
    TextView vitaminCTextView;
    TextView calciumTextView;
    TextView descriptionTextView;
    TextView healthStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        inputTextView = (TextView) findViewById(R.id.txt_input);
        nutritionTextView = (TextView) findViewById(R.id.txt_nutrition);
        nameTextView = (TextView) findViewById(R.id.txt_name);
        servingTextView = (TextView) findViewById(R.id.txt_serving);
        caloriesTextView = (TextView) findViewById(R.id.txt_calories);
        fatTextView = (TextView) findViewById(R.id.txt_fat);
        saturatedTextView = (TextView) findViewById(R.id.txt_saturated);
        cholesterolTextView = (TextView) findViewById(R.id.txt_cholesterol);
        sodiumTextView = (TextView) findViewById(R.id.txt_sodium);
        fiberTextView = (TextView) findViewById(R.id.txt_fiber);
        vitaminATextView = (TextView) findViewById(R.id.txt_vitaminA);
        vitaminCTextView = (TextView) findViewById(R.id.txt_vitaminC);
        calciumTextView = (TextView) findViewById(R.id.txt_calcium);
        descriptionTextView = (TextView) findViewById(R.id.txt_description);
        healthStatusTextView = (TextView) findViewById(R.id.txt_health_status);
     }

    public void getNutrition(View v) {
        //TextView sourceTextView = (TextView) findViewById(R.id.txt_Food);
        //sourceText = sourceTextView.getText().toString();
        sourceText = "doughnut";
        inputTextView.setText("Input value = " + sourceText);

        JSONObject request = new JSONObject();
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        String url = "https://api.nutritionix.com/v1_1/search";
        String requestURL = "https://api.nutritionix.com/v1_1/search/" + sourceText + "?results=0:20&fields=item_name,brand_name,item_id,nf_calories,nf_total_fat,nf_saturated_fat,nf_cholesterol,nf_sodium,nf_dietary_fiber," +
                "nf_vitamin_a_dv,nf_vitamin_c_dv,nf_calcium_dv,nf_serving_size_qty,nf_serving_size_unit&appId=747fc1eb&appKey=a94618e5725ffed4019654db41d7e030";
        String[] fields = {"item_name", "nf_calories", "nf_total_fat", "nf_saturated_fat", "nf_trans_fatty_acid",
                "nf_cholesterol", "nf_sodium", "nf_dietary_fiber", "nf_calcium_dv", "nf_vitamin_a_dv", "nf_vitamin_c_dv",
                "nf_serving_size_qty", "nf_serving_size_unit"};

        JSONArray jsonFields = null;
        JSONObject filters = new JSONObject();

        try {
            jsonFields = new JSONArray(fields);
            filters.put("cal_min", "0");
            filters.put("cal_max", 50000);
            request.put("appId", "747fc1eb");
            request.put("appKey", "a94618e5725ffed4019654db41d7e030");
            request.put("query", sourceText);
            request.put("fields", jsonFields);
            request.put("filters", filters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String response = "";
        OkHttpClient client = new OkHttpClient();
        String requestString = request.toString();
        RequestBody body = RequestBody.create(JSON, requestString);
        try {
//            Request req = new Request.Builder()
//                    .url(url).post(body).build();
            Request req = new Request.Builder().url(requestURL).build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.print(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final JSONObject jsonResult;
                    final String result = response.body().string();
                    try {
                        jsonResult = new JSONObject(result);
                        JSONArray responseArray = jsonResult.getJSONArray("hits");
                        final String responseText = responseArray.get(0).toString();
                        final JSONObject nutrition = (JSONObject) responseArray.get(0);
                        final JSONObject fields = nutrition.getJSONObject("fields");
                        final Map<String, String> nutritionMap = new HashMap();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nutritionTextView.setText("Nutrition Facts");
                                try {
                                    String name = fields.getString("item_name");
                                    nutritionMap.put("name", fields.getString("item_name"));
                                    String serving = fields.getString("nf_serving_size_qty") + " " + fields.getString("nf_serving_size_unit");
                                    nutritionMap.put("serving", fields.getString("nf_serving_size_qty") + " " + fields.getString("nf_serving_size_unit"));
                                    String calories = fields.getString("nf_calories");
                                    nutritionMap.put("calories", fields.getString("nf_calories"));
                                    String fat = fields.getString("nf_total_fat");
                                    nutritionMap.put("fat", fields.getString("nf_total_fat"));
                                    String saturated = fields.getString("nf_saturated_fat");
                                    nutritionMap.put("saturated", fields.getString("nf_saturated_fat"));
                                    String cholesterol = fields.getString("nf_cholesterol");
                                    nutritionMap.put("cholesterol", fields.getString("nf_cholesterol"));
                                    String sodium = fields.getString("nf_sodium");
                                    nutritionMap.put("sodium", fields.getString("nf_sodium"));
                                    String fiber = fields.getString("nf_dietary_fiber");
                                    nutritionMap.put("fiber", fields.getString("nf_dietary_fiber"));
                                    String vitaminA = fields.getString("nf_vitamin_a_dv");
                                    nutritionMap.put("vitaminA", fields.getString("nf_vitamin_a_dv"));
                                    String vitaminC = fields.getString("nf_vitamin_c_dv");
                                    nutritionMap.put("vitaminC", fields.getString("nf_vitamin_c_dv"));
                                    String calcium = fields.getString("nf_calcium_dv");
                                    nutritionMap.put("calcium", fields.getString("nf_calcium_dv"));

//                                    nameTextView.setText("Name: " + name);
//                                    servingTextView.setText("Serving Size: " + serving);
//                                    caloriesTextView.setText("Calories: " + calories);
//                                    fatTextView.setText("Total Fat: " + fat + " g");
//                                    saturatedTextView.setText("Saturated Fat: " + saturated + " g");
//                                    cholesterolTextView.setText("Cholesterol: " + cholesterol + " mg");
//                                    sodiumTextView.setText("Sodium: " + sodium + " mg");
//                                    fiberTextView.setText("Fiber: "  + fiber + " g");
//                                    vitaminATextView.setText("Vitamin A: " + vitaminA + "%");
//                                    vitaminCTextView.setText("Vitamin C: " + vitaminC + "%");
//                                    calciumTextView.setText("Calcium: " + calcium + "%");

                                    nameTextView.setText("Name: " + nutritionMap.get("name"));
                                    servingTextView.setText("Serving Size: " + nutritionMap.get("serving"));
                                    caloriesTextView.setText("Calories: " + nutritionMap.get("calories"));
                                    fatTextView.setText("Total Fat: " + nutritionMap.get("fat") + " g");
                                    saturatedTextView.setText("Saturated Fat: " + nutritionMap.get("saturated") + " g");
                                    cholesterolTextView.setText("Cholesterol: " + nutritionMap.get("cholesterol") + " mg");
                                    sodiumTextView.setText("Sodium: " + nutritionMap.get("sodium") + " mg");
                                    fiberTextView.setText("Fiber: "  + nutritionMap.get("fiber") + " g");
                                    vitaminATextView.setText("Vitamin A: " + nutritionMap.get("vitaminA") + "%");
                                    vitaminCTextView.setText("Vitamin C: " + nutritionMap.get("vitaminC") + "%");
                                    calciumTextView.setText("Calcium: " + nutritionMap.get("calcium") + "%");

                                    determineHealth(nutritionMap);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void determineHealth(Map<String, String> nutritionMap) {
        List<String> bad = new ArrayList<>();
        List<String> good = new ArrayList<>();

        double fat = Double.parseDouble(nutritionMap.get("fat"));
        if (fat >= 13.0) {
            bad.add("high in total fat");
        } else if (fat < 3.25) {
            good.add("low in total fat");
        }

        double saturated = Double.parseDouble(nutritionMap.get("saturated"));
        if (saturated >= 4.0) {
            bad.add("high in saturated fat");
        } else if (saturated < 1.0) {
            good.add("low in saturated fat");
        }

        double cholesterol = Double.parseDouble(nutritionMap.get("cholesterol"));
        if (cholesterol >= 60.0) {
            bad.add("high in cholesterol");
        } else if (cholesterol < 15.0) {
            good.add("low in cholesterol");
        }

        double sodium = Double.parseDouble(nutritionMap.get("sodium"));
        if (sodium >= 480.0) {
            bad.add("high in sodium");
        } else if (sodium < 120.0) {
            good.add("low in sodium");
        }

        double fiber = Double.parseDouble(nutritionMap.get("fiber"));
        if (fiber >= 5.0) {
            good.add("high in fiber");
        } else if (fiber < 1.25) {
            bad.add("low in fiber");
        }

        double vitaminA = Double.parseDouble(nutritionMap.get("vitaminA"));
        if (vitaminA < 5.0) {
            bad.add("low in vitamin A");
        } else if (vitaminA >= 20.0) {
            good.add("high in vitamin A");
        }

        double vitaminC = Double.parseDouble(nutritionMap.get("vitaminC"));
        if (vitaminC < 5.0) {
            bad.add("low in vitamin C");
        } else if (vitaminC >= 20.0) {
            good.add("high in vitamin C");
        }

        double calcium = Double.parseDouble(nutritionMap.get("calcium"));
        if (calcium < 5.0) {
            bad.add("low in calcium");
        } else if (calcium >= 20.0) {
            good.add("high in calcium");
        }

        String description = nutritionMap.get("name") + " is ";
        String healthStatus = "";
        for (int i = 0; i < good.size(); i++) {
            description += good.get(i) + ", ";
        }

        for (int j = 0; j < bad.size(); j++) {
            if (j == (bad.size() - 1)) {
                description += bad.get(j);
            } else {
                description += bad.get(j) + ", ";
            }
        }

        descriptionTextView.setText(description);

        if (good.size() >= 2 && good.size() > bad.size()) {
            healthStatus = "HEALTHY FOOD";
        } else if (bad.size() >= 2 && bad.size() > good.size()) {
            healthStatus = "JUNK FOOD";
        } else {
            healthStatus = "NEUTRAL FOOD";
        }

        healthStatusTextView.setText(healthStatus);

    }

}

