/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 *
 */
package org.elasticsearch.xpack.core.indexlifecycle.action;

import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.test.AbstractStreamableXContentTestCase;
import org.elasticsearch.xpack.core.indexlifecycle.Step.StepKey;
import org.elasticsearch.xpack.core.indexlifecycle.StepKeyTests;
import org.elasticsearch.xpack.core.indexlifecycle.action.MoveToStepAction.Request;
import org.junit.Before;

public class MoveToStepRequestTests extends AbstractStreamableXContentTestCase<Request> {

    private String index;
    private static final StepKeyTests stepKeyTests = new StepKeyTests();

    @Before
    public void setup() {
        index = randomAlphaOfLength(5);
    }

    @Override
    protected Request createTestInstance() {
        return new Request(index, stepKeyTests.createTestInstance(), stepKeyTests.createTestInstance());
    }

    @Override
    protected Request createBlankInstance() {
        return new Request();
    }

    @Override
    protected Request doParseInstance(XContentParser parser) {
        return Request.parseRequest(index, parser);
    }

    @Override
    protected boolean supportsUnknownFields() {
        return false;
    }

    @Override
    protected Request mutateInstance(Request request) {
        String index = request.getIndex();
        StepKey currentStepKey = request.getCurrentStepKey();
        StepKey nextStepKey = request.getNextStepKey();

        switch (between(0, 2)) {
            case 0:
                index += randomAlphaOfLength(5);
                break;
            case 1:
                currentStepKey = stepKeyTests.mutateInstance(currentStepKey);
                break;
            case 2:
                nextStepKey = stepKeyTests.mutateInstance(nextStepKey);
                break;
            default:
                throw new AssertionError("Illegal randomisation branch");
        }

        return new Request(index, currentStepKey, nextStepKey);
    }
}
