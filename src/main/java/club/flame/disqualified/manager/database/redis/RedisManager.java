package club.flame.disqualified.manager.database.redis;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.database.redis.payload.RedisListener;
import club.flame.disqualified.lib.config.ConfigCursor;
import lombok.Getter;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class RedisManager {

    JedisPool jedisPool;

    RedisListener redisListener;

    ConfigCursor databaseRedis = new ConfigCursor(Disqualified.getInstance().getDatabaseConfig(), "REDIS");

    private final String ip = databaseRedis.getString("HOST");

    private final int port = databaseRedis.getInt("PORT");

    private final String password = databaseRedis.getString("AUTHENTICATION.PASSWORD");

    private final boolean auth = databaseRedis.getBoolean("AUTHENTICATION.ENABLED");

    @Getter
    private boolean active = false;

    public void connect() {
        try {
            Disqualified.getInstance().getLogger().info("Connecting to redis");
            this.jedisPool = new JedisPool(this.ip, this.port);
            Jedis jedis = this.jedisPool.getResource();
            if (auth) {
                if (password != null || !password.equals(""))
                    jedis.auth(this.password);
            }
            this.redisListener = new RedisListener();
            (new Thread(() -> jedis.subscribe(this.redisListener, "Disqualified"))).start();
            jedis.connect();
            active = true;
            Bukkit.getConsoleSender().sendMessage("§aSuccessfully connect to §4Redis.");
        } catch (Exception e) {
            if (Disqualified.getInstance().isDebug()) {
                e.printStackTrace();
            }
            Bukkit.getConsoleSender().sendMessage("§6[Disqualified] An error occurred while trying to connect to Redis.");
            active = false;
        }
    }

    public void disconnect() {
        Disqualified.getInstance().getLogger().info("[Redis] Disconnecting...");
        this.redisListener.unsubscribe();
        jedisPool.getResource().close();
        jedisPool.destroy();
        Disqualified.getInstance().getLogger().info("[Redis] Disconnecting Successfully");
    }

    public void write(String json) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            if (auth) {
                if (password != null || !password.equals(""))
                    jedis.auth(this.password);
            }
            jedis.publish("Disqualified", json);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}