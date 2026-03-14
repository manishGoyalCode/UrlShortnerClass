package org.coding.hld.urlshortnerclass.service;

import org.coding.hld.urlshortnerclass.model.UrlMapping;
import org.coding.hld.urlshortnerclass.repository.UrlRepository;
import org.coding.hld.urlshortnerclass.util.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlService {

    private final UrlRepository repository;

    @Value("${feature.cache.enabled:false}")
    private boolean cacheEnabled;

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }

    public String createShortUrl(String originalUrl) {

        String code = ShortCodeGenerator.generate();

        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(code);
        mapping.setOriginalUrl(originalUrl);

        repository.save(mapping);

        if (cacheEnabled) {
            cache.put(code, originalUrl);
        }

        return code;
    }

    public Optional<String> getOriginalUrl(String code) {

        if (cacheEnabled && cache.containsKey(code)) {
            return Optional.of(cache.get(code));
        }

        Optional<UrlMapping> mapping = repository.findByShortCode(code);

        if (mapping.isPresent() && cacheEnabled) {
            cache.put(code, mapping.get().getOriginalUrl());
        }

        return mapping.map(UrlMapping::getOriginalUrl);
    }
}