package com.korges.vavr;

import com.korges.vavr.vavr_try.Response;
import com.korges.vavr.vavr_try.HttpClient;
import com.korges.vavr.vavr_try.VavrTry;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Try in Vavr
 * https://www.baeldung.com/vavr-try
 *
 * Vavr library gives us a special container that represents a computation
 * that may either result in an exception or complete successfully.
 * Enclosing operation within Try object gave us a result that is either Success or a Failure.
 * Then we can execute further operations accordingly to that type.
 *
 * The important thing to notice is a return type Try<Response>. When a method returns such result type,
 * we need to handle that properly and keep in mind, that result type can be Success or Failure,
 * so we need to handle that explicitly at a compile time.
 */
public class TryBaeldung {

    /**
     * Handling Success
     */
    @Test
    public void givenHttpClient_whenMakeACall_shouldReturnSuccess() {
        // given
        Integer defaultChainedResult = 1;
        String id = "a";
        HttpClient httpClient = () -> new Response(id);

        // when
        Try<Response> response = new VavrTry(httpClient).getVavrResponse();
        Integer chainedResult = response
                .map(this::actionThatTakesResponse)
                .getOrElse(defaultChainedResult);
        Stream<String> stream = response.toStream().map(it -> it.id);

        // then
        assertTrue(!stream.isEmpty());
        assertTrue(response.isSuccess());
        response.onSuccess(r -> assertEquals(id, r.id));
        response.andThen(r -> assertEquals(id, r.id));

        assertNotEquals(defaultChainedResult, chainedResult);

    }

    /**
     * Handling Failure
     */
    @Test
    public void givenHttpClientFailure_whenMakeACall_shouldReturnFailure() {
        // given
        Integer defaultChainedResult = 1;
        HttpClient httpClient = () -> {
            throw new Exception("problem");
        };

        // when
        Try<Response> response = new VavrTry(httpClient).getVavrResponse();
        Integer chainedResult = response
                .map(this::actionThatTakesResponse)
                .getOrElse(defaultChainedResult);
        Option<Response> optionalResponse = response.toOption();

        // then
        assertTrue(optionalResponse.isEmpty());
        assertTrue(response.isFailure());
        response.onFailure(ex -> assertTrue(ex instanceof Exception));
        assertEquals(defaultChainedResult, chainedResult);
    }

    private int actionThatTakesResponse(Response response) {
        return response.id.hashCode();
    }

    /**
     * Utilizing Pattern Matching
     *
     * When our httpClient returns an Exception, we could do a pattern matching on a type of that Exception.
     * Then according to a type of that Exception in recover() a method we can decide if we want to recover from
     * that exception and turn our Failure into Success or if we want to leave our computation result as a Failure:
     */
    @Test
    public void givenHttpClientThatFailure_whenMakeACall_shouldReturnFailureAndNotRecover() {
        // given
        Response defaultResponse = new Response("b");
        HttpClient httpClient = () -> {
            throw new IllegalArgumentException("Critical problem");
        };

        // when
        Try<Response> recovered = new VavrTry(httpClient).getVavrResponse()
                .recover(r -> Match(r).of(
                        Case($(instanceOf(IllegalArgumentException.class)), defaultResponse)
                ));

        // then
        assertTrue(recovered.isFailure());
    }
}
