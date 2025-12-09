package com.example.cosmocats.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ProblemDetailBuilder {
    private HttpStatus status;
    private URI type;
    private String title;
    private String detail;
    private Map<String, Object> properties = new HashMap<>();

    public static ProblemDetailBuilder builder() {
        return new ProblemDetailBuilder();
    }

    public ProblemDetailBuilder status(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ProblemDetailBuilder type(URI type) {
        this.type = type;
        return this;
    }

    public ProblemDetailBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ProblemDetailBuilder detail(String detail) {
        this.detail = detail;
        return this;
    }

    public ProblemDetailBuilder property(String name, Object value) {
        this.properties.put(name, value);
        return this;
    }

    public ProblemDetail build() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(type);
        problemDetail.setTitle(title);
        properties.forEach(problemDetail::setProperty);
        return problemDetail;
    }
}
