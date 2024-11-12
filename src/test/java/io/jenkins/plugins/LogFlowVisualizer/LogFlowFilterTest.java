package io.jenkins.plugins.LogFlowVisualizer;

import io.jenkins.plugins.LogFlowVisualizer.input.LogFlowInput;
import io.jenkins.plugins.LogFlowVisualizer.input.LogFlowInputAdvanced;
import io.jenkins.plugins.LogFlowVisualizer.model.LineOutput;
import io.jenkins.plugins.LogFlowVisualizer.model.LineWithOffset;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
class LogFlowFilterTest {

    @Test
    void filter_withAdvancedConfig_shouldSuccessfullyReturnLines() {
        List<LineWithOffset> lines = new ArrayList<>();

        lines.add(new LineWithOffset("START", 0));
        lines.add(new LineWithOffset("SOME CONTENT 1", 1));
        lines.add(new LineWithOffset("NOW", 2));
        lines.add(new LineWithOffset("END", 3));

        lines.add(new LineWithOffset("START", 4));
        lines.add(new LineWithOffset("SOME CONTENT 2", 5));
        lines.add(new LineWithOffset("YOU", 6));
        lines.add(new LineWithOffset("END", 7));

        lines.add(new LineWithOffset("START", 8));
        lines.add(new LineWithOffset("SOME CONTENT 3", 9));
        lines.add(new LineWithOffset("SEE", 10));
        lines.add(new LineWithOffset("END", 11));

        lines.add(new LineWithOffset("START", 12));
        lines.add(new LineWithOffset("SOME CONTENT 4", 13));
        lines.add(new LineWithOffset("ME", 14));
        lines.add(new LineWithOffset("END", 15));

        LogFlowInputAdvanced advancedConfig = new LogFlowInputAdvanced("START", "END", false, 30, 2);

        List<LogFlowInput> configs = new ArrayList<>();
        configs.add(advancedConfig);

        List<LineOutput> result = LogFlowFilter.filter(lines, configs);

        List<LineOutput> toDisplayResult = result.stream()
                .filter(LineOutput::getDisplay)
                .collect(Collectors.toList());

        assertEquals(4, toDisplayResult.size());
        assertEquals("NOW YOU SEE ME", toDisplayResult.stream()
                .map(LineOutput::getLine)
                .collect(Collectors.joining(" ")));

    }
}