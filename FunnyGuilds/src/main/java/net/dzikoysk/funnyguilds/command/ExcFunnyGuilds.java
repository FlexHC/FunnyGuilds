package net.dzikoysk.funnyguilds.command;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.FunnyGuildsVersion;
import net.dzikoysk.funnyguilds.command.util.Executor;
import net.dzikoysk.funnyguilds.concurrency.requests.FunnybinRequest;
import net.dzikoysk.funnyguilds.concurrency.requests.ReloadRequest;
import net.dzikoysk.funnyguilds.data.DataModel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class ExcFunnyGuilds implements Executor {

    @Override
    public void execute(CommandSender sender, String[] args) {
        String parameter = args.length > 0 ? args[0].toLowerCase() : "";

        switch (parameter) {
            case "reload":
            case "rl":
                reload(sender);
                break;
            case "check":
            case "update":
                FunnyGuildsVersion.isNewAvailable(sender, true);
                break;
            case "save-all":
                saveAll(sender);
                break;
            case "funnybin":
                post(sender, args);
                break;
            case "help":
                sender.sendMessage(ChatColor.AQUA + "FunnyGuilds Help:");
                sender.sendMessage(ChatColor.GRAY + "/funnyguilds (reload|rl) - przeladuj plugin");
                sender.sendMessage(ChatColor.GRAY + "/funnyguilds (update|check) - sprawdz dostepnosc aktualizacji");
                sender.sendMessage(ChatColor.GRAY + "/funnyguilds save-all - zapisz wszystko");
                sender.sendMessage(ChatColor.GRAY + "/funnyguilds funnybin - zapisz konfigurację online (~ usprawnia pomoc na https://github.com/FunnyGuilds/FunnyGuilds/issues)");
                break;
            default:
                sender.sendMessage(ChatColor.GRAY + "FunnyGuilds " + ChatColor.AQUA + FunnyGuilds.getInstance().getFullVersion() + ChatColor.GRAY + " by " + ChatColor.AQUA + "FunnyGuilds Team");
                break;
        }

    }

    private void reload(CommandSender sender) {
        if (!sender.hasPermission("funnyguilds.reload")) {
            sender.sendMessage(FunnyGuilds.getInstance().getMessageConfiguration().permission);
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "Przeladowywanie...");
        FunnyGuilds.getInstance().getConcurrencyManager().postRequests(new ReloadRequest(sender));
    }

    private void saveAll(CommandSender sender) {
        if (!sender.hasPermission("funnyguilds.admin")) {
            sender.sendMessage(FunnyGuilds.getInstance().getMessageConfiguration().permission);
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "Zapisywanie...");
        long l = System.currentTimeMillis();

        DataModel dataModel = FunnyGuilds.getInstance().getDataModel();

        try {
            dataModel.save(false);
            FunnyGuilds.getInstance().getInvitationPersistenceHandler().saveInvitations();
        }
        catch (Exception e) {
            FunnyGuilds.getInstance().getPluginLogger().error("An error occurred while saving plugin data!", e);
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "Zapisano (" + ChatColor.AQUA + (System.currentTimeMillis() - l) / 1000.0F + "s" + ChatColor.GRAY + ")!");
    }

    private void post(CommandSender sender, String[] args) {
        if (!sender.hasPermission("funnyguilds.admin")) {
            sender.sendMessage(FunnyGuilds.getInstance().getMessageConfiguration().permission);
            return;
        }

        Optional<FunnybinRequest> request = FunnybinRequest.of(sender, args);

        if(request.isPresent()) {
            FunnyGuilds.getInstance().getConcurrencyManager().postRequests(request.get());;
            return;
        }

        sender.sendMessage(ChatColor.RED + "Uzycie: ");
        sender.sendMessage(ChatColor.RED + "/fg funnybin - domyslnie wysyla FunnyGuilds/config.yml i logs/latest.log");
        sender.sendMessage(ChatColor.RED + "/fg funnybin config - wysyla FunnyGuilds/config.yml");
        sender.sendMessage(ChatColor.RED + "/fg funnybin log - wysyla logs/latest.log");
        sender.sendMessage(ChatColor.RED + "/fg funnybin custom <path> - wysyla dowolny plik z folderu serwera na funnybina");
        sender.sendMessage(ChatColor.RED + "/fg funnybin bundle <file1> <fileN...> - wysyla dowolne pliki z folderu serwera na funnybina");
    }

}
