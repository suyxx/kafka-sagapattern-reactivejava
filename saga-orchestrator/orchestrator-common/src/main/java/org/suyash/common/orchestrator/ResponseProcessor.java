package org.suyash.common.orchestrator;


import org.reactivestreams.Publisher;
import org.suyash.common.messages.Request;
import org.suyash.common.messages.Response;

public interface ResponseProcessor<T extends Response> {

    Publisher<Request> process(T response);

}
