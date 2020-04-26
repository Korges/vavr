package com.korges.vavr.vavr_try;

import io.vavr.control.Try;


public class VavrTry {
    private HttpClient httpClient;

    public VavrTry(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Response getResponse() {
        try {
            return httpClient.call();
        } catch (Exception e) {
            return null;
        }
    }

    public Try<Response> getVavrResponse() {
        return Try.of(httpClient::call);
    }
}
