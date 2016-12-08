package lv.ctco.cukescore.internal.context;

import com.google.common.base.Optional;
import com.google.inject.*;
import lv.ctco.cukescore.*;

import java.util.*;

public class ContextInflater extends BaseContextHandler {

    @Inject
    GlobalWorldFacade world;

    public String inflate(String input) {
        Set<String> groups = new HashSet<String>(extractGroups(input));
        boolean inflatingEnabled = world.getBoolean(CukesOptions.CONTEXT_INFLATING_ENABLED, true);
        if (inflatingEnabled) {
            return inflateGroups(input, groups);
        }
        return inflateGroupsWithPlaceholders(input, groups);
    }

    public String inflateGroups(String input, Set<String> groups) {
        String result = input;
        for (String key : groups) {
            Optional<String> $value = world.get(key);
            if ($value.isPresent()) {
                result = result.replaceAll("\\{\\(" + key + "\\)\\}", $value.get());
            }
        }
        return result;
    }

    public String inflateGroupsWithPlaceholders(String input, Set<String> groups) {
        String result = input;
        for (String key : groups) {
            result = result.replaceAll("\\{\\(" + key + "\\)\\}", "(" + key + ")");
        }
        return result;
    }
}