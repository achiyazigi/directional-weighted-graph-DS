package api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class JsonGraph implements JsonDeserializer<DWGraph_DS> {
    @Override
    public DWGraph_DS deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray edges = jsonObject.get("Edges").getAsJsonArray();
        JsonArray nodes = jsonObject.get("Nodes").getAsJsonArray();
        DWGraph_DS g = new DWGraph_DS();

        for (JsonElement i : nodes) {

            String[] xyz = i.getAsJsonObject().get("pos").getAsString().split(",");
            int node_id = i.getAsJsonObject().get("id").getAsInt();
            node_data n = new NodeData(node_id);
            geo_location geo = new GeoLocation(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]), Double.parseDouble(xyz[2]));
            n.setLocation(geo);
            g.addNode(n);
        }
        for (JsonElement i : edges) {
            int src = i.getAsJsonObject().get("src").getAsInt();
            int dest = i.getAsJsonObject().get("dest").getAsInt();
            double w = i.getAsJsonObject().get("w").getAsDouble();
            g.connect(src, dest, w);
        }

        return g;
    }
}
