package com.mossj.springsecurity;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    private static final Integer MAX_ATTAMPT = 3;
    private LoadingCache<String, Integer> attemptCache;

    public LoginAttemptService() {
        this.attemptCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.DAYS)
            .build(new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String key) throws Exception {
                    return 0;
                }
            });
    }

    public void loginSucceed(String ip) {
        attemptCache.invalidate(ip);
    }

    public void loginFailed(String ip) {
        Integer attempts = 0;
        try {
            attempts = attemptCache.get(ip);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        attempts ++;

        attemptCache.put(ip, attempts);
    }

    public boolean isBlocked(String ip) {
        Integer attempts = 0;
        try {
           attempts = attemptCache.get(ip);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return attempts > MAX_ATTAMPT;
    }
}
