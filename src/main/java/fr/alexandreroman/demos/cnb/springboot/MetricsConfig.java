/*
 * Copyright (c) 2020 VMware, Inc.
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
class MetricsConfig {
    @Bean
    Counter appInfo(MeterRegistry reg) {
        return Counter.builder("app.info").description("Get application info")
                .tag("java.version", System.getProperty("java.version"))
                .tag("openssl.version", getOpenSslVersion())
                .register(reg);
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
                        final StringBuilder buf = new StringBuilder(lines.get(0));
                        if (lines.size() > 1 && !lines.get(1).contains("not available")) {
                            buf.append(" ").append(lines.get(1));
                        }
                        return buf.toString();
                    }
                }
            }
        } catch (IOException e) {
            log.warn("Failed to get OpenSSL version", e);
        } catch (InterruptedException ignore) {
        }
        log.warn("Failed to get OpenSSL version: no output");
        return null;
    }
}
