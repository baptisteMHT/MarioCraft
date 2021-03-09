package io.github.baptistemht.mariocraft.database;

import io.github.baptistemht.mariocraft.MarioCraft;
import io.github.baptistemht.mariocraft.game.GameState;
import io.github.baptistemht.mariocraft.track.Track;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.UUID;

public class Database {

    private final JedisPool pool;

    private final boolean use;

    private final String dbId;


    public Database(MarioCraft instance, String address, int port, int timeout, String auth, boolean use){

        this.pool = new JedisPool(new JedisPoolConfig(),address, port, timeout, auth);
        this.use = use;
        this.dbId = instance.getConfig().getString("server.id");

        if(!use) return;

        Jedis j = pool.getResource();
        j.sadd("servers", dbId);
        j.close();
    }

    public void updateLiveTrack(Track t){
        if(!use) return;
        int run = MarioCraft.getInstance().getRaceCount();

        Jedis j = pool.getResource();
        j.set(dbId+":track:"+run, t.getId());
        j.close();
    }

    public void updateServerState(GameState state){
        if(!use) return;
        Jedis j = pool.getResource();
        j.set(dbId+":state", state.toString());
        j.close();
    }

    public void addPlayer(UUID id){
        if(!use) return;
        Jedis j = pool.getResource();
        j.sadd(dbId+":players", id.toString());
        j.close();
    }

    public void removePlayer(UUID id){
        if(!use) return;
        Jedis j = pool.getResource();
        j.srem(dbId+":players", id.toString());
        j.close();
    }

    public JedisPool getPool() {
        if(!use) return null;
        return pool;
    }

    public void close(){
        if(!use) return;
        Jedis j = pool.getResource();
        j.srem("servers", dbId);
        j.close();
    }
}