/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.commands;

import com.chalwk.CommandManager.CommandCooldownManager;
import com.chalwk.CommandManager.CommandInterface;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command for setting or removing the channel for the game to use.
 */
public class channel implements CommandInterface {

    private static final CommandCooldownManager COOLDOWN_MANAGER = new CommandCooldownManager();

    @Override
    public String getName() {
        return "channel";
    }

    @Override
    public String getDescription() {
        return "Set or remove the channel for the game to use (admins only)";
    }

    public List<OptionData> getOptions() {

        List<OptionData> options = new ArrayList<>();

        OptionData operation = new OptionData(OptionType.STRING, "operation", "The type of operation to perform", true);
        operation.addChoice("add", "add");
        operation.addChoice("remove", "remove");

        OptionData channel = new OptionData(OptionType.CHANNEL, "channel", "The channel to set or remove", true);
        options.add(operation);
        options.add(channel);

        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        if (COOLDOWN_MANAGER.isOnCooldown(event)) return;

        COOLDOWN_MANAGER.setCooldown(getName(), event.getUser());
    }
}