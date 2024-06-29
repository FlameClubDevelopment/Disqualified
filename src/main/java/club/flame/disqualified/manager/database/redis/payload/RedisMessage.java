package club.flame.disqualified.manager.database.redis.payload;

import com.google.gson.Gson;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class RedisMessage {

    @Getter private Payload payload;
    private Map<String, String> params;

    public RedisMessage(Payload payload) {
        this.payload = payload;
        params = new HashMap<>();
    }

    public RedisMessage setParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public String getParam(String key) {
        if (containsParam(key)) {
            return params.get(key);
        }
        return null;
    }

    public boolean containsParam(String key) {
        return params.containsKey(key);
    }

    public void removeParam(String key) {
        if (containsParam(key)) {
            params.remove(key);
        }
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
