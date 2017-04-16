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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NutritionActivity extends AppCompatActivity {
    String sourceText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
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
     }

    public void getNutrition(View v) {
        //TextView sourceTextView = (TextView) findViewById(R.id.txt_Food);
        //sourceText = sourceTextView.getText().toString();
        sourceText = "apple";

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nutritionTextView.setText("Nutrition Facts");
                                try {
                                    String name = fields.getString("item_name");
                                    String serving = fields.getString("nf_serving_size_qty") + " " + fields.getString("nf_serving_size_unit");
                                    String calories = fields.getString("nf_calories");
                                    String fat = fields.getString("nf_total_fat");
                                    String saturated = fields.getString("nf_saturated_fat");
                                    String cholesterol = fields.getString("nf_cholesterol");
                                    String sodium = fields.getString("nf_sodium");
                                    String fiber = fields.getString("nf_dietary_fiber");
                                    String vitaminA = fields.getString("nf_vitamin_a_dv");
                                    String vitaminC = fields.getString("nf_vitamin_c_dv");
                                    String calcium = fields.getString("nf_calcium_dv");

                                    nameTextView.setText("Name: " + name);
                                    servingTextView.setText("Serving Size: " + serving);
                                    caloriesTextView.setText("Calories: " + calories);
                                    fatTextView.setText("Total Fat: " + fat + " g");
                                    saturatedTextView.setText("Saturated Fat: " + saturated + " g");
                                    cholesterolTextView.setText("Cholesterol: " + cholesterol + " mg");
                                    sodiumTextView.setText("Sodium: " + sodium + " mg");
                                    fiberTextView.setText("Fiber: "  + fiber + " g");
                                    vitaminATextView.setText("Vitamin A: " + vitaminA + "%");
                                    vitaminCTextView.setText("Vitamin C: " + vitaminC + "%");
                                    calciumTextView.setText("Calcium: " + calcium + "%");


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
}

