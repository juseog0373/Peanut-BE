package com.daelim.hackathon.word;

import com.daelim.hackathon.word.dto.GameResponse;
import com.daelim.hackathon.word.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/word")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private String lastWord = ""; // 끝말잇기 게임을 위한 마지막 단어 저장

    @GetMapping("/jsonapi")
    public SuccessResponse callApiWithJson(@RequestParam String query) {
        return dictionaryService.callApiWithJson(query);
    }

    @GetMapping("/game")
    public GameResponse wordGame(@RequestParam(name="word") String word) {
        if (!lastWord.isEmpty() && !word.startsWith(String.valueOf(lastWord.charAt(lastWord.length() - 1)))) {
            lastWord="";
            return new GameResponse(false, "The word does not start with the last character of the previous word.", lastWord);
        }

        SuccessResponse response = dictionaryService.callApiWithJson(word);
        if (response.isSuccess()) {
            lastWord = word;
            return new GameResponse(true, "Word accepted.", lastWord);
        } else {
            lastWord = ""; // false가 반환될 때 lastWord 초기화
            return new GameResponse(false, "Word not found in dictionary.", lastWord);
        }
    }
}
