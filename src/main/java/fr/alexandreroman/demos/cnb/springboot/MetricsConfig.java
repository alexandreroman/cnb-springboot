/*
 * Copyright (c) 2021 VMware, Inc. or its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.alexandreroman.demos.cnb.springboot;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
class MetricsConfig {
    private Counter appInfoCounter;

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(
            @Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    @Bean
    Counter appInfo(MeterRegistry reg) {
        Tags tags = Tags.of("java.version", System.getProperty("java.version"));
        final String openSslVersion = getOpenSslVersion();
        if (openSslVersion != null) {
            tags = tags.and("openssl.version", openSslVersion);
        }
        appInfoCounter = Counter.builder("app.info").description("Get application info")
                .tags(tags)
                .register(reg);
        return appInfoCounter;
    }

    @EventListener
    void onApplicationReady(ApplicationReadyEvent e) {
        appInfoCounter.increment();
    }

    private String getOpenSslVersion() {
        try {
            final Process proc = new ProcessBuilder("openssl", "version", "-a").start();
            if (proc.waitFor() != 0) {
                log.warn("Unable to get OpenSSL version");
            } else {
                try (final BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                    final List<String> lines = in.lines().collect(Collectors.toUnmodifiableList());
                    if (!lines.isEmpty()) {
                        final StringBuilder buf = new StringBuilder(lines.get(0).trim());
                        if (lines.size() > 1 && !lines.get(1).contains("not available")) {
                            buf.append(" ").append(lines.get(1).trim());
                        }
                        return buf.toString();
                    }
                }
            }
        } catch (IOException e) {
            log.warn("Failed to get OpenSSL version", e);
            return null;
        } catch (InterruptedException ignore) {
        }
        log.warn("Failed to get OpenSSL version: no output");
        return null;
    }
}
