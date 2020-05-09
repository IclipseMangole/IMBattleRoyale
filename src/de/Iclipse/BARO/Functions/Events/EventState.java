package de.Iclipse.BARO.Functions.Events;

public enum EventState {
    None("none"), PoisonWater("poisonwater"), Glowing("glowing"), BurningSun("burningsun"), Confusion("confusion"), Lostness("lostness"), Endergames("endergames"),
    FishMutation("fishmutation"), Levitation("levitation"), LavaEvent("lava");;

    private String name;

    EventState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
