package com.example.healthmate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.healthmate.databinding.FragmentHomeBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final String apiKey = "YOUR_API_KEY_HERE";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private AnimatedVectorDrawable thinkingAnimation; // Variable for the animation

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        thinkingAnimation = (AnimatedVectorDrawable) binding.thinkingAnimationView.getDrawable();

        setupLaunchers();
        setupClickListeners();
        return binding.getRoot();
    }

    private void setupLaunchers() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) launchCamera();
            else showToast(getString(R.string.toast_permission_required));
        });

        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                Bundle extras = result.getData().getExtras();
                if (extras != null && extras.get("data") != null) {
                    Bitmap capturedImage = (Bitmap) extras.get("data");
                    binding.previewImageView.setImageBitmap(capturedImage);
                    binding.previewImageView.setVisibility(View.VISIBLE);
                    binding.symptomInput.setText("");
                    setLoading(true);
                    String base64Image = bitmapToBase64(capturedImage);
                    if (!base64Image.isEmpty()) callVisionApi(base64Image);
                    else showError(getString(R.string.toast_error_image));
                }
            }
        });
    }

    private void setupClickListeners() {
        binding.sendButton.setOnClickListener(v -> {
            String userInput = binding.symptomInput.getText().toString().trim();
            if (userInput.isEmpty()) return;
            binding.previewImageView.setVisibility(View.GONE);
            setLoading(true);
            callTextApi(userInput);
        });

        binding.cameraButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });
    }


    private void setLoading(boolean isLoading) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (isLoading) {
                    // Start thinking animation
                    binding.responseTextView.setVisibility(View.GONE);
                    binding.thinkingAnimationView.setVisibility(View.VISIBLE);
                    thinkingAnimation.start();
                } else {
                    // Stop thinking animation
                    thinkingAnimation.stop();
                    binding.thinkingAnimationView.setVisibility(View.GONE);
                    binding.responseTextView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(takePictureIntent);
    }

    private void callTextApi(String text) {
        String lang = getCurrentLanguage();
        String systemInstruction = (lang.equals("si"))
                ? "You are an expert AI Health Information Assistant for people in Sri Lanka. Your goal is to provide helpful, safe, and general health information, NOT a medical diagnosis. You must ALWAYS respond in Sinhala language only. When a user gives you symptoms, explain possible related common conditions, provide general wellness tips (like rest, hydration), and mention simple, safe, over-the-counter remedies if appropriate. MOST IMPORTANTLY: YOU MUST ALWAYS end every single response with the following disclaimer, exactly as written, in bold: '**මෙය වෛද්‍ය උපදෙසක් නොවේ. නිවැරදි රෝග විනිශ්චයක් සහ ප්‍රතිකාර සඳහා කරුණාකර සුදුසුකම් ලත් වෛද්‍යවරයෙකු හමුවන්න.**'"
                : "You are an expert AI Health Information  Assistant. Your goal is to provide helpful, safe, and general health information, NOT a medical diagnosis. You must ALWAYS respond in English. When a user gives you symptoms, explain possible related common conditions, provide general wellness tips (like rest, hydration), and mention simple, safe, over-the-counter remedies if appropriate. MOST IMPORTANTLY: YOU MUST ALWAYS end every single response with the following disclaimer, exactly as written, in bold: '**This is not medical advice. For an accurate diagnosis and treatment, please consult a qualified doctor.**'";

        JSONObject jsonBody = new JSONObject();
        try {
            JSONArray contents = new JSONArray();
            JSONObject part = new JSONObject().put("text", systemInstruction + "\n\nUser's Symptoms: " + text);
            contents.put(new JSONObject().put("parts", new JSONArray().put(part)));
            jsonBody.put("contents", contents);
        } catch(JSONException e) {
            showError(getString(R.string.toast_error_request));
            return;
        }
        makeApiCall(jsonBody);
    }

    private void callVisionApi(String base64Image) {
        String lang = getCurrentLanguage();
        String systemInstruction = (lang.equals("si"))
                ? "In Sinhala, describe the object in this image. If it is a medicine, identify it. Then, add the following text at the end, exactly as written, in bold: '**මෙම තොරතුරු පොදු දැනගැනීම සඳහා පමණි. මෙම ඖෂධය ලබා ගැනීමට පෙර සෑමවිටම වෛද්‍යවරයෙකුගෙන් හෝ ඖෂධවේදියෙකුගෙන් උපදෙස් ලබාගන්න.**'"
                : "In English, describe the object in this image. If it is a medicine, identify it. Then, add the following text at the end, exactly as written, in bold: '**This information is for general knowledge only. Always consult a doctor or pharmacist before taking this medication.**'";

        JSONObject jsonBody = new JSONObject();
        try {
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            JSONObject textPart = new JSONObject().put("text", systemInstruction);
            JSONObject inlineData = new JSONObject().put("mimeType", "image/jpeg").put("data", base64Image);
            JSONObject imagePart = new JSONObject().put("inlineData", inlineData);
            content.put("parts", new JSONArray().put(textPart).put(imagePart));
            contents.put(content);
            jsonBody.put("contents", contents);
        } catch(JSONException e) {
            showError(getString(R.string.toast_error_request));
            return;
        }
        makeApiCall(jsonBody);
    }

    private void makeApiCall(JSONObject jsonBody) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder().url(apiUrl).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                showError(getString(R.string.toast_error_network));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try (Response responseToClose = response) {
                    String responseBody = responseToClose.body().string();
                    if (!responseToClose.isSuccessful()) {
                        showError(String.format(getString(R.string.toast_error_api), responseToClose.code()));
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(responseBody);
                    if (jsonObject.has("promptFeedback")) {
                        showError(getString(R.string.toast_error_blocked));
                        return;
                    }
                    String aiResponse = jsonObject.getJSONArray("candidates").getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");
                    Spanned formattedText = formatResponse(aiResponse);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            binding.responseTextView.setText(formattedText);
                            setLoading(false);
                        });
                    }
                } catch (Exception e) {
                    showError(getString(R.string.toast_error_response));
                }
            }
        });
    }

    private Spanned formatResponse(String rawText) {
        String htmlText = rawText.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>");
        htmlText = htmlText.replaceAll("^\\* (.*)", "&#8226; $1").replaceAll("\n\\* (.*)", "<br>&#8226; $1");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(htmlText);
        }
    }

    private void showError(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                setLoading(false);
                binding.responseTextView.setText(message);
                showToast(message);
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) return "";
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
        } catch (IOException e) {
            return "";
        }
    }

    private String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
