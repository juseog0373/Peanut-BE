package com.daelim.hackathon.crawler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawler")
public class CrawlerController {

    private final CrawlerService crawlerService;

    @GetMapping("/lunch")
    public ResponseEntity<LunchMenuResponse> getTodayLunchMenu() {
        return ResponseEntity.ok().body(crawlerService.getTodayLunchMenu());
    }
}
