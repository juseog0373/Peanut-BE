package com.daelim.hackathon.word;

import com.daelim.hackathon.word.dto.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class DictionaryService {

    @Value("${dictionary.api.key}")
    private String apiKey;

    private Set<String> usedWords = new HashSet<>();

    public SuccessResponse callApiWithJson(String query) {
        // 단어 길이가 1 이하인 경우 false 반환
        if (query.length() <= 1) {
            return new SuccessResponse(false, "Word length must be greater than 1.");
        }

        // 이미 사용된 단어인 경우 false 반환
        if (usedWords.contains(query)) {
            usedWords.clear(); // 사용된 단어 목록 초기화
            return new SuccessResponse(false, "Word already used. Game restarted.");
        }

        StringBuilder result = new StringBuilder();
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());
            String apiUrl = "https://krdict.korean.go.kr/api/search?q=" + encodedQuery + "&key=" + apiKey;

            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() != 200) {
                throw new IOException("Failed : HTTP error code : " + urlConnection.getResponseCode());
            }

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"))) {
                String returnLine;
                while ((returnLine = bufferedReader.readLine()) != null) {
                    result.append(returnLine);
                }
            }

            String responseString = result.toString();
            log.debug("API 응답: {}", responseString);

            // JSON인지 XML인지 판별
            if (responseString.trim().startsWith("{")) {
                // JSON 응답
                JSONObject jsonObject = new JSONObject(responseString);
                return checkResponse(jsonObject, query);
            } else if (responseString.trim().startsWith("<")) {
                // XML 응답을 JSON으로 변환
                JSONObject jsonObject = XML.toJSONObject(responseString);
                return checkResponse(jsonObject, query);
            } else {
                throw new IOException("Unexpected response format");
            }
        } catch (Exception e) {
            log.error("API 호출 중 에러 발생", e);
            return new SuccessResponse(false, "Error: " + e.getMessage());
        }
    }

    private SuccessResponse checkResponse(JSONObject jsonObject, String query) {
        if (jsonObject.has("channel")) {
            JSONObject channel = jsonObject.getJSONObject("channel");
            if (channel.has("item")) {
                Object item = channel.get("item");
                if (item instanceof JSONObject) {
                    usedWords.add(query); // 사용된 단어로 추가
                    return new SuccessResponse(true, "Word found and defined.");
                } else if (item instanceof org.json.JSONArray) {
                    org.json.JSONArray items = (org.json.JSONArray) item;
                    if (items.length() > 0) {
                        usedWords.add(query); // 사용된 단어로 추가
                        return new SuccessResponse(true, "Word found and defined.");
                    }
                }
            }
        }
        return new SuccessResponse(false, "No items found.");
    }
}
