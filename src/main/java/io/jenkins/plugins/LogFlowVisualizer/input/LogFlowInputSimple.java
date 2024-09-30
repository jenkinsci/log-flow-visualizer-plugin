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

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.verb.POST;
import org.springframework.lang.NonNull;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class LogFlowInputSimple extends LogFlowInput {

    private final String regex;

    private final Boolean deleteMark;


    @DataBoundConstructor
    public LogFlowInputSimple(String regex, Boolean deleteMark) {
        super();
        this.regex = regex;
        this.deleteMark = deleteMark;
    }

    public String getRegex() {
        return regex;
    }


    public Boolean getDeleteMark() {
        return deleteMark;
    }


    public DescriptorExtensionList<LogFlowInput, LogFlowInputDescriptor> getFormatDescriptors() {
        return Jenkins.get().getDescriptorList(LogFlowInput.class);
    }


    @Extension @Symbol("simpleInput")
    public static final class DescriptorImpl extends LogFlowInputDescriptor {

        @SuppressWarnings("lgtm[jenkins/no-permission-check]")
        @POST
        public FormValidation doCheckRegex(@QueryParameter String regex) {
            if (regex.isEmpty()) {
                return FormValidation.error("Regex is empty");
            }
            try {
                Pattern.compile(regex);
            } catch (PatternSyntaxException exception) {
                return FormValidation.error("Regex is invalid");
            }
            return FormValidation.ok();
        }

        @Override
        @NonNull
        public String getDisplayName() {
            return "Simple Regex Format";
        }


    }
}
