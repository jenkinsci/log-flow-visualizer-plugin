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

package io.jenkins.plugins.LogFlowVisualizer.actions;
///////////////

import hudson.model.Action;
import hudson.model.Run;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.StaplerResponse2;
import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.verb.GET;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class LogFlowOffsetAction implements SimpleBuildStep.LastBuildAction {
    private final Run run;

    public LogFlowOffsetAction(Run<?, ?> run) {
        this.run = run;
    }


    @GET
    @WebMethod(name = "get-search-offset")
    public void doSearchOffset(StaplerRequest2 req, StaplerResponse2 res) throws IOException {
        Jenkins.get().checkPermission(Jenkins.READ);
        res.setContentType("text/plain");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();

        try {
            long from = Long.parseLong(req.getParameter("from"));
            long to = Long.parseLong(req.getParameter("to"));
        } catch (NumberFormatException e) {
            res.setStatus(404);
            out.print("Couldn't parse input");
            return;
        }

        long from = Long.parseLong(req.getParameter("from"));
        long to = Long.parseLong(req.getParameter("to"));

        RandomAccessFile logs = new RandomAccessFile(this.run.getRootDir() + File.separator + "log", "r");

        Long checkFrom = from;
        Long checkTo = to;

        List<String> resultLines = new ArrayList<>();

        if (from > to || from < 0 || to < 0 || checkFrom == null || checkTo == null) {
            res.setStatus(404);
            out.print("Invalid parameters");
        } else {
            logs.seek(from);
            String line;
            // always prints the rest of the line of given offset
            while ((line = logs.readLine()) != null) {
                resultLines.add(line);

                //checking the upper limit
                if (logs.getFilePointer() >= to) {
                    break;
                }
            }

            for (String resultLine :
                    resultLines) {
                out.println(resultLine);
            }

        }
        out.flush();
        logs.close();
    }

    @Override
    public Collection<? extends Action> getProjectActions() {
        return new ArrayList<>();
    }

    @Override
    public String getIconFileName() {
        return "symbol-logs";
    }

    @Override
    public String getDisplayName() {
        return "Log Flow Visualizer Offset Logs";
    }

    @Override
    public String getUrlName() {
        return "logsOffset";
    }

}
