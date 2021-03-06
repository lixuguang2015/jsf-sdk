/**
 * Copyright 2004-2048 .
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ipd.jsf.gd.filter.limiter.bucket;


import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Title: <br>
 * <p>
 * Description: <br>
 * </p>
 *
 * @since 2016/04/27 10:46
 */
public abstract class AbstractTokenBucketLimiter extends RateLimiter {

    @Override
    protected void doSetRate(double tokenPerSecond) {
        syncAvailableToken(duration());
        this.maxTokens = tokenPerSecond;
        this.stableIntervalTokenMicros = SECONDS.toMicros(1L) / tokenPerSecond;
    }

    @Override
    public void syncAvailableToken(long nowMicros) {
        if (nowMicros > nextGenTokenMicros){
            double newTokens = (nowMicros - nextGenTokenMicros) / stableIntervalTokenMicros;
            availableTokens = Math.min(maxTokens, availableTokens + newTokens);
            nextGenTokenMicros = nowMicros;
        }
    }
}