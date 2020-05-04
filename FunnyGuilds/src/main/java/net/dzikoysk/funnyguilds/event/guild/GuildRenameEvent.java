package net.dzikoysk.funnyguilds.event.guild;

import net.dzikoysk.funnyguilds.basic.guild.Guild;
import net.dzikoysk.funnyguilds.basic.user.User;
import org.bukkit.event.HandlerList;

public class GuildRenameEvent extends GuildEvent {

    private final String newName;
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GuildRenameEvent(EventCause eventCause, User doer, Guild guild, String newName) {
        super(eventCause, doer, guild);

        this.newName = newName;
    }

    public String getNewName() {
        return this.newName;
    }

    @Override
    public String getDefaultCancelMessage() {
        return "[FunnyGuilds] Guild renaming has been cancelled by the server!";
    }

}
