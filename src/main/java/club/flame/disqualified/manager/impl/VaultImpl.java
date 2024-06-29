package club.flame.disqualified.manager.impl;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.grants.Grant;
import club.flame.disqualified.manager.ranks.Rank;
import club.flame.disqualified.lib.chat.CC;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class VaultImpl extends Permission {

	private Disqualified plugin;

	@Override
	public String getName() {
		return "Disqualified";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean hasSuperPermsCompat() {
		return false;
	}

	@Override
	public boolean playerHas(String s, String player, String permission) {
		if (Bukkit.getPlayer(player) == null) {
			return false;
		}

		return Bukkit.getPlayer(player).hasPermission(permission) || Bukkit.getPlayer(player).isOp();
	}

	@Override
	public boolean playerAdd(String s, String s1, String s2) {
		return false;
	}

	@Override
	public boolean playerRemove(String s, String s1, String s2) {
		return false;
	}

	@Override
	public boolean groupHas(String world, String group, String permission) {
		return Rank.getRankByName(group).getPermissions().stream().anyMatch(permission::equalsIgnoreCase);
	}

	@Override
	public boolean groupAdd(String world, String group, String permission) {
		if (Rank.getRankByName(group) == null) {
			return false;
		}

		Rank.getRankByName(group).getPermissions().add(permission);
		return true;
	}

	@Override
	public boolean groupRemove(String world, String group, String permission) {
		if (Rank.getRankByName(group) == null) {
			return false;
		}

		Rank.getRankByName(group).getPermissions().remove(permission);
		return false;
	}

	@Override
	public boolean playerInGroup(String world, String player, String group) {
		if (Rank.getRankByName(group) == null || PlayerData.getPlayerData(player) == null) {
			return false;
		}

		return PlayerData.getPlayerData(player).getActiveGrants().stream().anyMatch(grant -> grant.getRank().getName().equalsIgnoreCase(group));
	}

	public boolean playerAddGroup(String s, String playerName, String group) {
		if (Bukkit.getPlayer(playerName) != null) {
			Rank rank = Rank.getRankByName(group);
			if (rank != null) {
				PlayerData playerData = PlayerData.getPlayerData(Bukkit.getPlayer(playerName).getUniqueId());
				if (playerData != null) {
					Grant grant = new Grant(null, 1L, 1L, 1L, "", "", "", false, false, "Global");
					grant.setActive(true);
					grant.setAddedBy("Console");
					grant.setReason("Added by Vault");
					grant.setRankName(rank.getName());
					grant.setPermanent(true);
					grant.setAddedDate(System.currentTimeMillis());
					playerData.getGrants().add(grant);
					return true;
				}
			}
		}
		return false;
	}

	public boolean playerRemoveGroup(String s, String playerName, String group) {
		if (Bukkit.getPlayer(playerName) != null) {
			PlayerData playerData = PlayerData.getPlayerData(Bukkit.getPlayer(playerName).getUniqueId());
			if (playerData != null) {
				for (Grant grant : playerData.getGrants()) {
					if (grant.getRankName().equalsIgnoreCase(group)) {
						grant.setActive(false);
						grant.setRemovedDate(System.currentTimeMillis());
						grant.setRemovedBy("Console [Vault]");
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public String[] getPlayerGroups(String world, String player) {
		Collection<String> result;
		result = PlayerData.getPlayerData(player).getActiveGrants().stream().map(grant -> grant.getRank().getName()).collect(Collectors.toList());

		return (String[]) result.toArray();
	}

	@Override
	public String getPrimaryGroup(String world, String player) {
		if (PlayerData.getPlayerData(player) == null) {
			return "no_data";
		}

		return PlayerData.getPlayerData(player).getHighestRank().getName();
	}

	@Override
	public String[] getGroups() {
		return (String[]) Rank.getRanks().stream().map(Rank::getName).toArray();
	}

	@Override
	public boolean hasGroupSupport() {
		return true;
	}

	public void register() {
		Bukkit.getServer().getServicesManager().register(Permission.class, this, Disqualified.getInstance(), ServicePriority.Highest);
		Bukkit.getServer().getServicesManager().register(Chat.class, new VaultChatImpl(this, this.plugin), Disqualified.getInstance(), ServicePriority.Highest);
		Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&4Disqualified&7] " + "&aSuccessfully implemented Vault's permissions into Disqualified."));
	}
}
