package com.cs.dts.logparser.deserializer;

import com.cs.dts.logparser.model.ApplicationServerLog;
import com.cs.dts.logparser.model.BaseLogEvent;
import com.cs.dts.logparser.model.ServerLog;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CustomDeserializer extends StdDeserializer<BaseLogEvent> {
    protected CustomDeserializer() {
        super(BaseLogEvent.class);
    }

    @Override
    public BaseLogEvent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        TreeNode node = jsonParser.readValueAsTree();

        if (node.get("type") != null) {
            return jsonParser.getCodec().treeToValue(node, ApplicationServerLog.class);
        }
        return jsonParser.getCodec().treeToValue(node, ServerLog.class);
    }
}

