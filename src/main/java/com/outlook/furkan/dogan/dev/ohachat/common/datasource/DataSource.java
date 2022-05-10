package com.outlook.furkan.dogan.dev.ohachat.common.datasource;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.DefaultChatTierName;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.OhaPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class DataSource {

  private static final Map<UUID, OhaPlayer> OHA_PLAYER_BY_UUID = new ConcurrentHashMap<>();

  private static final Executor executor = Executors.newSingleThreadExecutor();

  public DataSource() {
  }

  public final void loadAll() {
    this.getAll().forEach(ohaPlayer -> {
      DataSource.OHA_PLAYER_BY_UUID.put(ohaPlayer.getUniqueId(), ohaPlayer);
    });
  }

  public final OhaPlayer getPlayer(UUID uuid) {
    return Optional.ofNullable(DataSource.OHA_PLAYER_BY_UUID.get(uuid))
      .orElseGet(() -> this.createOhaPlayer(uuid));
  }

  public final void removePlayer(UUID uuid) {
    DataSource.OHA_PLAYER_BY_UUID.remove(uuid);
    DataSource.executor.execute(() -> this.removeSupply(uuid));
  }

  public final void save(OhaPlayer ohaPlayer) {
    DataSource.OHA_PLAYER_BY_UUID.put(ohaPlayer.getUniqueId(), ohaPlayer);
    DataSource.executor.execute(() -> this.saveSupply(ohaPlayer));
  }

  public final Map<UUID, OhaPlayer> getOhaPlayers() {
    return Collections.unmodifiableMap(DataSource.OHA_PLAYER_BY_UUID);
  }

  protected abstract void saveSupply(OhaPlayer ohaPlayer);

  protected abstract void removeSupply(UUID uuid);

  protected abstract Set<OhaPlayer> getAll();

  private OhaPlayer createOhaPlayer(UUID uuid) {
    OhaPlayer ohaPlayer = new OhaPlayer(uuid, new HashSet<>(), DefaultChatTierName.GLOBAL);
    this.save(ohaPlayer);
    return ohaPlayer;
  }
}
