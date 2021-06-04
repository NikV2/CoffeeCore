package me.nik.coffeecore.modules.impl.anticrash;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.nik.coffeecore.CoffeeCore;
import me.nik.coffeecore.managers.Discord;
import me.nik.coffeecore.modules.Module;
import me.nik.coffeecore.modules.impl.anticrash.checks.CrashCheck;
import me.nik.coffeecore.modules.impl.anticrash.checks.impl.Limit;
import me.nik.coffeecore.modules.impl.anticrash.checks.impl.Payload;
import me.nik.coffeecore.modules.impl.anticrash.checks.impl.Pos;
import me.nik.coffeecore.utils.Messenger;
import me.nik.coffeecore.wrappers.WrapperPlayServerKickDisconnect;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Very simple - bad, still leaves the server vulnerable but it's something!
 * Let's hope CoffeeSpigot's anticrash takes care of the rest.
 */
public class AntiCrash extends Module {
    private static final String KICK_MESSAGE = Messenger.PREFIX + "Calm down!";
    private static final List<PacketType> LISTENING_PACKETS = new ArrayList<>();

    static {
        PacketType.Play.Client.getInstance().values()
                .stream()
                .filter(val -> val.isSupported() && !val.isDeprecated())
                .forEach(LISTENING_PACKETS::add);
    }

    private final CrashCheck[] crashChecks = {
            new Pos(),
            new Payload(),
            new Limit()
    };

    public AntiCrash(CoffeeCore plugin) {
        super(plugin);
    }

    private void kick(Player player) {
        WrapperPlayServerKickDisconnect kick = new WrapperPlayServerKickDisconnect();
        kick.setReason(WrappedChatComponent.fromText(KICK_MESSAGE));
        kick.sendPacket(player);
    }

    @Override
    public void init() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                this.plugin,
                ListenerPriority.LOWEST,
                LISTENING_PACKETS,
                ListenerOptions.INTERCEPT_INPUT_BUFFER) {

            @Override
            public void onPacketReceiving(PacketEvent e) {
                if (e.isPlayerTemporary() || e.getPlayer() == null) return;

                final Player p = e.getPlayer();

                if (AntiCrash.this.plugin.getProfile(p).getJoinedMillis() < 1000L) return;

                for (CrashCheck crashCheck : crashChecks) {

                    if (!crashCheck.handle(e)) continue;

                    e.setCancelled(true);

                    Discord.sendCrashAlert(p);

                    kick(p);
                }
            }
        });
    }

    @Override
    public void disInit() {
    }
}