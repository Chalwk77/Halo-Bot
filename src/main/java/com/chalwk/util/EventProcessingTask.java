/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */

package com.chalwk.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import static com.chalwk.util.util.getTextChannel;

public class EventProcessingTask {

    private static final String HALO_EVENTS_FILE = "halo-events.json";
    private static String serverID;

    public EventProcessingTask(String serverKey, int intervalInSeconds) {
        serverID = serverKey;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new EventProcessingTask.Task(), 1000 * 5, intervalInSeconds * 1000L);
    }

    private static void sendMessage(String title, String description, String colorName, String channelID) {
        TextChannel channel = getTextChannel(channelID);
        channel.sendMessageEmbeds(new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.getColor(colorName)).build()).queue();
    }

    private static class Task extends TimerTask {

        @Override
        public void run() {
            try {

                JSONObject parentTable = FileIO.getJSONObjectFromFile(HALO_EVENTS_FILE);
                JSONArray eventTable = parentTable.getJSONObject(serverID).getJSONArray("sapp_events");

                for (Iterator<Object> it = eventTable.iterator(); it.hasNext(); ) {
                    JSONObject event = (JSONObject) it.next();
                    EmbedData result = getGetServerData(event);
                    sendMessage(result.title(), result.description(), result.color(), result.channelID());
                    it.remove();
                }
                FileIO.saveJSONObjectToFile(parentTable, HALO_EVENTS_FILE);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @NotNull
        private EmbedData getGetServerData(JSONObject event) {
            String title = event.getString("title");
            String description = event.getString("description");
            String channelID = event.getString("channel");
            String color = event.getString("color");
            return new EmbedData(title, description, channelID, color);
        }
    }

    private record EmbedData(String title, String description, String channelID, String color) {

    }
}
