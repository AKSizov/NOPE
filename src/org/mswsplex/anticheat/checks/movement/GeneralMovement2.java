package org.mswsplex.anticheat.checks.movement;

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

public class GeneralMovement2 implements Check, Listener {

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

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		CPlayer cp = plugin.getCPlayer(player);
		if (player.isFlying() || player.isInsideVehicle())
			return;

		if (cp.timeSince("disableFlight") < 2000)
			return;
		if (cp.hasMovementRelatedPotion())
			return;
		if (player.getFallDistance() > 4)
			return;
		if (cp.timeSince("lastLiquid") < 400)
			return;
		if (cp.timeSince("lastDamageTaken") < 1000)
			return;

		double[] requires = {
				// Regular Movements
				0.1040803780930375, 0.0, 0.41999998688697815, 0.33319999363422426, 0.24813599859093927,
				0.1647732818260721, 0.08307781780646906, 0.07840000152587834, 0.15523200451660557, 0.23052736891296632,
				0.30431682745754074, 0.37663049823865435, 0.015555072702198913, 0.23052736891295922,
				0.23152379758701613, 0.1040803780930446, 0.44749789698342113, 0.5169479491049742, 0.5850090015087517,
				0.5546255304958976, 0.6517088341626192, 0.7170746714356042, 0.1858420248976742, 3.567357955623528,
				0.13963453200464926,

				// Cactus interactions
				0.34489540236494065, 0.07805507270219891, 0.40444491418477924, 0.4921255304958976,

				// Slab interactions
				0.23152379758701613, 0.03584062504455687, 1.5, .5, 1.0, 0.15658248110962347, 0.08133599222516352,
				1.4199999868869781, 0.2531999805212024, 1.2531999805212024, 0.3959196219069554, 0.3386639946618146,
				1.6731999674081806, 0.39937488410653543, 0.4844449272978011, 1.3386639946618146, 0.20369171156407617,
				0.5813359922251635, 0.9199999868869781, 0.3548932993483618, 0.7468000194787976, 2.2531999805212024,
				0.15523200451659847, 0.2850277037892326 };

		Location to = event.getTo(), from = event.getFrom();

		double diff = Math.abs(to.getY() - from.getY());

		boolean normal = false;

		for (double d : requires) {
			if (diff == d) {
				normal = true;
				break;
			}
		}

		if (normal)
			return;

		MSG.tell(player, "&7" + Math.abs(to.getY() - from.getY()));

		cp.flagHack(this, 5);
	}

	@Override
	public String getCategory() {
		return "GeneralMovement";
	}

	@Override
	public String getDebugName() {
		return "GeneralMovement#2";
	}
}
