package cn.swapdoctor.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Cloth Config screen for the three persisted client settings. */
public final class SwapDoctorConfigScreen {
    private SwapDoctorConfigScreen() {
    }

    public static Screen create(Screen parent) {
        var current = SwapDoctorClient.config().copy();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("screen.swapdoctor.title"))
                .setDoesConfirmSave(false);
        ConfigCategory category = builder.getOrCreateCategory(Component.translatable("category.swapdoctor.general"));
        ConfigEntryBuilder entries = builder.entryBuilder();

        category.addEntry(entries.startIntSlider(
                        Component.translatable("option.swapdoctor.max_delay"), current.maxDelayMs, 1, 1000)
                .setDefaultValue(100)
                .setSaveConsumer(SwapDoctorClient::setMaxDelayMs)
                .build());
        category.addEntry(entries.startBooleanToggle(
                        Component.translatable("option.swapdoctor.hud_enabled"), current.hudEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(SwapDoctorClient::setHudEnabled)
                .build());
        category.addEntry(entries.startIntSlider(
                        Component.translatable("option.swapdoctor.hud_duration"), current.hudDurationMs, 1, 60000)
                .setDefaultValue(1500)
                .setSaveConsumer(SwapDoctorClient::setHudDurationMs)
                .build());

        builder.setSavingRunnable(SwapDoctorClient::saveConfig);
        return builder.build();
    }
}
