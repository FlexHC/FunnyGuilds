package net.dzikoysk.funnyguilds.command;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.basic.user.User;
import net.dzikoysk.funnyguilds.command.util.Executor;
import net.dzikoysk.funnyguilds.data.configs.MessageConfiguration;
import net.dzikoysk.funnyguilds.data.configs.PluginConfiguration;
import net.dzikoysk.funnyguilds.util.commons.bukkit.ItemUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ExcRankReset implements Executor {

    @Override
    public void execute(CommandSender sender, String[] args) {
        PluginConfiguration config = FunnyGuilds.getInstance().getPluginConfiguration();
        MessageConfiguration messages = FunnyGuilds.getInstance().getMessageConfiguration();
        Player player = (Player) sender;
        List<ItemStack> requiredItems = config.rankResetItems;

        if (! this.playerHasEnoughItems(player, requiredItems)) {
            return;
        }

        User user = User.get(player);

        if (user != null) {
            int lastRank = user.getRank().getPoints();
            user.getRank().setPoints(config.rankStart);
            player.getInventory().removeItem(ItemUtils.toArray(requiredItems));

            String resetMessage = messages.rankResetMessage;
            resetMessage = StringUtils.replace(resetMessage, "{LAST-RANK}", String.valueOf(lastRank));
            resetMessage = StringUtils.replace(resetMessage, "{CURRENT-RANK}", String.valueOf(user.getRank().getPoints()));

            player.sendMessage(resetMessage);
        }
    }
}
