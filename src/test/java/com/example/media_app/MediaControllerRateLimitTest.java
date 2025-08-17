package com.example.media_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MediaControllerRateLimitTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowWithinRateLimit() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/media/1/view"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void shouldBlockAfterExceedingRateLimit() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/media/1/view"))
                    .andExpect(status().isOk());
        }
        // 6th call should be blocked
        mockMvc.perform(post("/media/1/view"))
                .andExpect(status().isTooManyRequests());
    }
}

