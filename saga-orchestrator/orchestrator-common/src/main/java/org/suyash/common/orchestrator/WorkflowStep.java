package org.suyash.common.orchestrator;


import org.suyash.common.messages.Response;

public interface WorkflowStep<T extends Response> extends
                                                        RequestSender,
                                                        RequestCompensator,
                                                        ResponseProcessor<T>,
                                                        WorkflowChain {


}
