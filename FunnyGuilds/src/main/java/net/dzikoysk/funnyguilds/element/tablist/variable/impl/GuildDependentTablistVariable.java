package net.dzikoysk.funnyguilds.element.tablist.variable.impl;

import net.dzikoysk.funnyguilds.basic.guild.Guild;
import net.dzikoysk.funnyguilds.basic.user.User;
import net.dzikoysk.funnyguilds.element.tablist.variable.TablistVariable;

import java.util.function.Function;

public final class GuildDependentTablistVariable implements TablistVariable {

    private final String[] names;
    private final Function<User, String> whenInGuild;
    private final Function<User, String> whenNotInGuild;

    public GuildDependentTablistVariable(String name, Function<User, String> whenInGuild, Function<User, String> whenNotInGuild) {
        this(new String[]{ name }, whenInGuild, whenNotInGuild);
    }

    public GuildDependentTablistVariable(String[] names, Function<User, String> whenInGuild, Function<User, String> whenNotInGuild) {
        this.names = names.clone();
        this.whenInGuild = whenInGuild;
        this.whenNotInGuild = whenNotInGuild;
    }

    @Override
    public String[] names() {
        return this.names.clone();
    }

    @Override
    public String get(User user) {
        if (user.getGuild() != null) {
            return this.whenInGuild.apply(user);
        } else {
            return this.whenNotInGuild.apply(user);
        }
    }

    public static GuildDependentTablistVariable ofGuild(String name, Function<Guild, String> whenInGuild, Function<User, String> whenNotInGuild) {
        return new GuildDependentTablistVariable(new String[]{ name }, user -> whenInGuild.apply(user.getGuild()), whenNotInGuild);
    }

}
