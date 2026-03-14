package org.coding.hld.urlshortnerclass.controller;

import org.coding.hld.urlshortnerclass.service.UrlService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/url")
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public Map<String, String> shorten(@RequestBody Map<String, String> request) {

        String originalUrl = request.get("url");

        String code = service.createShortUrl(originalUrl);

        return Map.of(
                "shortCode", code,
                "shortUrl", "http://localhost:8080/url/" + code
        );
    }

    @GetMapping("/{code}")
    public Map<String, String> getOriginal(@PathVariable String code) {

        return service.getOriginalUrl(code)
                .map(url -> Map.of(
                        "shortCode", code,
                        "originalUrl", url
                ))
                .orElse(Map.of("error", "Not found"));
    }
}