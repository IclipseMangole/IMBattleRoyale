package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.Random;

import static de.Iclipse.BARO.Data.*;

public class Events {
    public static void events() {
        Random random = new Random();
        if (Data.nextEventTime < 0) {
            Data.nextEventTime = random.nextInt(100) + 180;
            Data.nextEvent = nextEvent();
        } else {
            if (Data.nextEvent != EventState.None) {
                if (Data.nextEventTime - Data.timer > 0) {
                    if ((Data.nextEventTime - Data.timer) == 60 || (Data.nextEventTime - Data.timer) == 10) {
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            p.playSound(p.getLocation(), Sound.BLOCK_BELL_RESONATE, 1f, 1.0f);
                        });
                    }
                } else {
                    Bukkit.getScheduler().runTaskAsynchronously(Data.instance, () -> Bukkit.getPluginManager().callEvent(new EventChangeEvent(Data.estate, Data.nextEvent)));
                    Data.estate = Data.nextEvent;
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        dsp.send(p, "event.enabled", dsp.get("event." + Data.estate.getName(), p));
                    });
                    dsp.send(Bukkit.getConsoleSender(), "event.enabled", dsp.get("event." + Data.estate.getName(), Bukkit.getConsoleSender()));
                    nextEvent = EventState.None;
                    Data.nextEventTime = Data.timer + eventTimeRunning;
                }
            } else {
                if (Data.nextEventTime - Data.timer > 0) {
                    if ((Data.nextEventTime - Data.timer) == 10) {
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            p.playSound(p.getLocation(), Sound.BLOCK_BELL_RESONATE, 1f, 1.2f);
                        });
                    }
                } else {
                    Bukkit.getScheduler().runTaskAsynchronously(Data.instance, () -> Bukkit.getPluginManager().callEvent(new EventChangeEvent(Data.estate, Data.nextEvent)));
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        dsp.send(p, "event.disabled", dsp.get("event." + Data.estate.getName(), p));
                    });
                    dsp.send(Bukkit.getConsoleSender(), "event.disabled", dsp.get("event." + Data.estate.getName(), Bukkit.getConsoleSender()));
                    Data.estate = Data.nextEvent;
                    nextEvent = nextEvent();
                    Data.nextEventTime = Data.timer + random.nextInt(timeBetweenEventsMax - timeBetweenEventsMin) + timeBetweenEventsMin;
                }
            }
        }
    }

    public static EventState nextEvent() {
        Random random = new Random();
        EventState nextEvent = Data.events.get(random.nextInt(Data.events.size()));
        if (Data.eventsOneTime) {
            Data.events.remove(nextEvent);
        }
        return nextEvent;
    }

    public static void registerEvents() {
        Data.events.add(EventState.PoisonWater);
        Data.events.add(EventState.Glowing);
        Data.events.add(EventState.BurningSun);
        Data.events.add(EventState.Confusion);
        Data.events.add(EventState.Lostness);
        Data.events.add(EventState.Endergames);
        Data.events.add(EventState.LavaEvent);
        //Data.events.add(EventState.FishMutation);
        Data.events.add(EventState.Levitation);

        Data.allEvents.add(EventState.PoisonWater);
        Data.allEvents.add(EventState.Glowing);
        Data.allEvents.add(EventState.BurningSun);
        Data.allEvents.add(EventState.Confusion);
        Data.allEvents.add(EventState.Lostness);
        Data.allEvents.add(EventState.Endergames);
        Data.allEvents.add(EventState.LavaEvent);
        Data.allEvents.add(EventState.FishMutation);
        Data.allEvents.add(EventState.Levitation);
    }
}
