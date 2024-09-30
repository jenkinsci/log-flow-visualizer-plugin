/*
 * The MIT License
 *
 * Copyright 2023
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.jenkins.plugins.LogFlowVisualizer.input;

import hudson.Extension;
import hudson.util.FormValidation;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.verb.POST;
import org.springframework.lang.NonNull;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class LogFlowInputAdvanced extends LogFlowInput {

    private static final int DEFAULT_CONTENT_LENGTH = 30;

    private final String startMark;

    // to find specified mark in content of mark private final String namedMark;
    private final String endMark;

    private final Boolean deleteMark;
    private int maxContentLength = DEFAULT_CONTENT_LENGTH;

    private int numberOfLineToDisplay = 0;

    @DataBoundConstructor
    public LogFlowInputAdvanced(String startMark, String endMark, Boolean deleteMark, int maxContentLength, int numberOfLineToDisplay) {
        this.startMark = startMark;
        this.endMark = endMark;
        this.deleteMark = deleteMark;
        this.maxContentLength = maxContentLength;
        this.numberOfLineToDisplay = numberOfLineToDisplay;
    }

    public String getStartMark() {
        return startMark;
    }

    public String getEndMark() {
        return endMark;
    }

    public Boolean getDeleteMark() {
        return deleteMark;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public int getNumberOfLineToDisplay() {
        return numberOfLineToDisplay;
    }


    @Extension @Symbol("advancedInput")
    public static final class DescriptorImpl extends LogFlowInputDescriptor {
        @SuppressWarnings("lgtm[jenkins/no-permission-check]")
        @POST
        public FormValidation doCheckStartMark(@QueryParameter String startMark) {
            if (startMark.isEmpty()) {
                return FormValidation.error("Regex is empty");
            }
            try {
                Pattern.compile(startMark);
            } catch (PatternSyntaxException exception) {
                return FormValidation.error("Regex is invalid");
            }
            return FormValidation.ok();
        }

        @SuppressWarnings("lgtm[jenkins/no-permission-check]")
        @POST
        public FormValidation doCheckEndMark(@QueryParameter String endMark) {
            if (endMark.isEmpty()) {
                return FormValidation.error("Regex is empty");
            }
            try {
                Pattern.compile(endMark);
            } catch (PatternSyntaxException exception) {
                return FormValidation.error("Regex is invalid");
            }
            return FormValidation.ok();
        }

        @SuppressWarnings("lgtm[jenkins/no-permission-check]")
        @POST
        public FormValidation doCheckMaxContentLength(@QueryParameter String maxContentLength) {
            try {
                int inputNumber = Integer.parseInt(maxContentLength);
                if (inputNumber < 1) {
                    return FormValidation.error("Max content length should be greater than 0");
                }
                return FormValidation.ok();

            } catch (NumberFormatException e) {
                return FormValidation.error("Couldn't parse number input");
            } catch (Exception e) {
                return FormValidation.error("Something went wrong");
            }

        }

        @SuppressWarnings("lgtm[jenkins/no-permission-check]")
        @POST
        public FormValidation doCheckNumberOfLineToDisplay(@QueryParameter String numberOfLineToDisplay) {
            try {
                int inputNumber = Integer.parseInt(numberOfLineToDisplay);
                if (inputNumber < 0) {
                    return FormValidation.error("Number of line to display should be 0 or greater");
                }
                return FormValidation.ok();
            } catch (NumberFormatException e) {
                return FormValidation.error("Couldn't parse number input");
            } catch (Exception e) {
                return FormValidation.error("Something went wrong");
            }
        }

        @Override
        @NonNull
        public String getDisplayName() {
            return "Advanced Regex Format";
        }

    }
}
