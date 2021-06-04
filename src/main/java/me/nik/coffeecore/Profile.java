package me.nik.coffeecore;

import me.nik.coffeecore.utils.custom.BreakArea;
import me.nik.coffeecore.utils.custom.CommandSign;
import me.nik.coffeecore.utils.custom.PacketBucket;
import me.nik.coffeecore.utils.custom.ScaffoldArea;
import me.nik.coffeecore.utils.custom.VelocityMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile {

    private final UUID uuid;

    private final PacketBucket packetBucket = new PacketBucket();

    private boolean scaffoldMode, breakMode, velocityMobMode;
    private ScaffoldArea scaffoldArea;
    private BreakArea breakArea;
    private VelocityMob velocityMob;

    private boolean commandSignMode;
    private CommandSign commandSign;

    private long commandSignCooldown;

    private long lastCustomPayload;
    private float customPayloadBuffer;

    private long joinedMillis = System.currentTimeMillis();

    public long getLastCustomPayload() {
        return System.currentTimeMillis() - this.lastCustomPayload;
    }

    public void updateLastCustomPayload() {
        this.lastCustomPayload = System.currentTimeMillis();
    }

    public float increaseCustomPayloadBuffer(float buffer) {
        return this.customPayloadBuffer = Math.min(10000F, this.customPayloadBuffer + buffer);
    }

    public long getJoinedMillis() {
        return (System.currentTimeMillis() - this.joinedMillis);
    }

    public float getCustomPayloadBuffer() {
        return this.customPayloadBuffer;
    }

    public void resetCustomPayloadBuffer() {
        this.customPayloadBuffer = 0F;
    }

    public Profile(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean isScaffoldMode() {
        return scaffoldMode;
    }

    public void setScaffoldMode(boolean scaffoldMode) {
        this.scaffoldMode = scaffoldMode;
    }

    public ScaffoldArea getScaffoldArea() {
        if (this.scaffoldArea == null) this.scaffoldArea = new ScaffoldArea();
        return scaffoldArea;
    }

    public boolean isBreakMode() {
        return breakMode;
    }

    public void setBreakMode(boolean breakMode) {
        this.breakMode = breakMode;
    }

    public BreakArea getBreakArea() {
        if (this.breakArea == null) this.breakArea = new BreakArea();
        return breakArea;
    }

    public boolean isCommandSignMode() {
        return commandSignMode;
    }

    public void setCommandSignMode(boolean commandSignMode) {
        this.commandSignMode = commandSignMode;
    }

    public CommandSign getCommandSign() {
        if (this.commandSign == null) this.commandSign = new CommandSign();

        return commandSign;
    }

    public void setCommandSign(CommandSign commandSign) {
        this.commandSign = commandSign;
    }

    public boolean isVelocityMobMode() {
        return velocityMobMode;
    }

    public void setVelocityMobMode(boolean velocityMobMode) {
        this.velocityMobMode = velocityMobMode;
    }

    public void resetVelocityMob() {
        this.velocityMob = null;
    }

    public VelocityMob getVelocityMob() {
        if (this.velocityMob == null) this.velocityMob = new VelocityMob();
        return velocityMob;
    }

    public void resetCommandSign() {
        this.commandSign = null;
    }

    public void addCommandSignCooldown() {
        this.commandSignCooldown = System.currentTimeMillis();
    }

    public long getCommandSignCooldown() {
        return System.currentTimeMillis() - this.commandSignCooldown;
    }

    public void resetScaffoldArea() {
        this.scaffoldArea = null;
    }

    public void resetBreakArea() {
        this.breakArea = null;
    }

    public PacketBucket getPacketBucket() {
        return packetBucket;
    }
}