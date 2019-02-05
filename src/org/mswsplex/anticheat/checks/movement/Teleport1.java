package org.mswsplex.anticheat.checks.movement;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.mswsplex.anticheat.checks.Check;
import org.mswsplex.anticheat.checks.CheckType;
import org.mswsplex.anticheat.data.CPlayer;
import org.mswsplex.anticheat.msws.AntiCheat;
import org.mswsplex.anticheat.utils.MSG;

public class Teleport1 implements Check, Listener {

	private AntiCheat plugin;

	@Override
	public CheckType getType() {
		return CheckType.MOVEMENT;
	}

	@Override
	public void register(AntiCheat plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	private final int size = 20;

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		CPlayer cp = plugin.getCPlayer(player);

		Location from = event.getFrom(), to = event.getTo();

		double dist = from.distanceSquared(to);

		List<Double> distances = (ArrayList<Double>) cp.getTempData("teleportDistances");
		if (distances == null)
			distances = new ArrayList<>();

		int amo = 0;
		for (double d : distances) {
			if (d == 0 || d == .08199265046323716 || d == .08783953834102082)
				continue;
			if ((d + "").startsWith("0.0787"))
				continue;
			if (d == dist)
				amo++;
		}

		distances.add(0, dist);

		for (int i = distances.size() - size; i < distances.size() && i > size; i++)
			distances.remove(i);

		cp.setTempData("teleportDistances", distances);

		if (amo < 5)
			return;

		MSG.tell(player, amo + " (" + dist + ")");

		cp.flagHack(this, 5);
	}

	@Override
	public String getCategory() {
		return "Teleport";
	}

	@Override
	public String getDebugName() {
		return "Teleport#1";
	}
}
