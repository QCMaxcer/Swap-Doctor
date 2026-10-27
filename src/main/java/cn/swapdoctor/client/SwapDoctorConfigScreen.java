package cn.swapdoctor.client;

import cn.swapdoctor.config.HudDisplayMode;
import cn.swapdoctor.config.PositiveDirection;
import cn.swapdoctor.config.SwapDoctorConfig;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SwapDoctorConfigScreen {
    private SwapDoctorConfigScreen() {
    }

    public static Screen create(Screen parent) {
        var current = SwapDoctorClient.config().copy();
        var general = ConfigCategory.createBuilder()
                .name(Component.translatable("category.swapdoctor.general"))
                .option(booleanOption("option.swapdoctor.hud_enabled", current.hudEnabled,
                        SwapDoctorClient::setHudEnabled, true))
                .option(enumOption("option.swapdoctor.hud_mode", current.hudMode,
                        SwapDoctorClient::setHudMode, HudDisplayMode.class))
                .option(intOption("option.swapdoctor.hud_duration", current.hudDurationMs, 1, 3000, 1,
                        SwapDoctorClient::setHudDurationMs, SwapDoctorConfig.DEFAULT_HUD_DURATION_MS))
                .option(booleanOption("option.swapdoctor.scale_enabled", current.scaleHudEnabled,
                        SwapDoctorClient::setScaleHudEnabled, true))
                .option(enumOption("option.swapdoctor.scale_mode", current.scaleHudMode,
                        SwapDoctorClient::setScaleHudMode, HudDisplayMode.class))
                .option(intOption("option.swapdoctor.scale_duration", current.scaleHudDurationMs, 1, 3000, 1,
                        SwapDoctorClient::setScaleHudDurationMs, SwapDoctorConfig.DEFAULT_HUD_DURATION_MS))
                .option(intOption("option.swapdoctor.max_delay", current.maxDelayMs, 1, 500, 1,
                        SwapDoctorClient::setMaxDelayMs, SwapDoctorConfig.DEFAULT_MAX_DELAY_MS))
                .option(intOption("option.swapdoctor.scale_width", current.scaleWidth, 30, 300, 2,
                        SwapDoctorClient::setScaleWidth, SwapDoctorConfig.DEFAULT_SCALE_WIDTH))
                .option(floatOption("option.swapdoctor.scale_length_multiplier", current.scaleLengthMultiplier,
                        SwapDoctorConfig.MIN_SCALE_STYLE_MULTIPLIER, SwapDoctorConfig.MAX_SCALE_STYLE_MULTIPLIER,
                        SwapDoctorConfig.SCALE_STYLE_MULTIPLIER_STEP, SwapDoctorClient::setScaleLengthMultiplier,
                        SwapDoctorConfig.DEFAULT_SCALE_LENGTH_MULTIPLIER))
                .option(floatOption("option.swapdoctor.scale_thickness_multiplier", current.scaleThicknessMultiplier,
                        SwapDoctorConfig.MIN_SCALE_STYLE_MULTIPLIER, SwapDoctorConfig.MAX_SCALE_STYLE_MULTIPLIER,
                        SwapDoctorConfig.SCALE_STYLE_MULTIPLIER_STEP, SwapDoctorClient::setScaleThicknessMultiplier,
                        SwapDoctorConfig.DEFAULT_SCALE_THICKNESS_MULTIPLIER))
                .option(enumOption("option.swapdoctor.positive_direction", current.positiveDirection,
                        SwapDoctorClient::setPositiveDirection, PositiveDirection.class))
                .option(booleanOption("option.swapdoctor.show_decimals", current.showDecimals,
                        SwapDoctorClient::setShowDecimals, false))
                .option(booleanOption("option.swapdoctor.scale_values", current.showScaleValues,
                        SwapDoctorClient::setShowScaleValues, true))
                .option(ButtonOption.createBuilder()
                        .name(Component.translatable("option.swapdoctor.edit_layout"))
                        .text(Component.translatable("button.swapdoctor.open_editor"))
                        .description(OptionDescription.of(Component.translatable("option.swapdoctor.edit_layout.description")))
                        .action((screen, option) -> Minecraft.getInstance().setScreen(new SwapDoctorHudEditorScreen(screen)))
                        .build())
                .option(ButtonOption.createBuilder()
                        .name(Component.translatable("option.swapdoctor.reset_layout"))
                        .text(Component.translatable("button.swapdoctor.reset"))
                        .action((screen, option) -> SwapDoctorClient.resetHudLayout())
                        .build())
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("screen.swapdoctor.title"))
                .category(general)
                .save(SwapDoctorClient::saveConfig)
                .build()
                .generateScreen(parent);
    }

    private static Option<Boolean> booleanOption(String key, boolean value, java.util.function.Consumer<Boolean> setter, boolean defaultValue) {
        return Option.<Boolean>createBuilder()
                .name(Component.translatable(key))
                .binding(defaultValue, () -> valueFor(key), setter)
                .controller(BooleanControllerBuilder::create)
                .build();
    }

    private static boolean valueFor(String key) {
        var config = SwapDoctorClient.config();
        return switch (key) {
            case "option.swapdoctor.hud_enabled" -> config.hudEnabled;
            case "option.swapdoctor.scale_enabled" -> config.scaleHudEnabled;
            case "option.swapdoctor.show_decimals" -> config.showDecimals;
            case "option.swapdoctor.scale_values" -> config.showScaleValues;
            default -> false;
        };
    }

    private static <T extends Enum<T>> Option<T> enumOption(String key, T value, java.util.function.Consumer<T> setter, Class<T> enumClass) {
        return Option.<T>createBuilder()
                .name(Component.translatable(key))
                .binding(value, () -> enumValue(key, enumClass), setter)
                .controller(option -> EnumControllerBuilder.create(option)
                        .enumClass(enumClass)
                        .formatValue(value1 -> Component.translatable(enumTranslationKey(key, value1))))
                .build();
    }

    private static String enumTranslationKey(String key, Enum<?> value) {
        if (value instanceof HudDisplayMode mode) {
            return "swapdoctor.mode." + mode.serialized();
        }
        PositiveDirection direction = (PositiveDirection) value;
        return direction == PositiveDirection.SWITCH_THEN_ACTION
                ? "swapdoctor.direction.switch_first"
                : "swapdoctor.direction.click_first";
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> T enumValue(String key, Class<T> enumClass) {
        Object value = switch (key) {
            case "option.swapdoctor.hud_mode" -> SwapDoctorClient.config().hudMode;
            case "option.swapdoctor.scale_mode" -> SwapDoctorClient.config().scaleHudMode;
            case "option.swapdoctor.positive_direction" -> SwapDoctorClient.config().positiveDirection;
            default -> enumClass.getEnumConstants()[0];
        };
        return (T) value;
    }

    private static Option<Integer> intOption(String key, int value, int min, int max, int step,
                                             java.util.function.Consumer<Integer> setter, int defaultValue) {
        return Option.<Integer>createBuilder()
                .name(Component.translatable(key))
                .binding(defaultValue, () -> intValue(key), setter)
                .controller(option -> IntegerSliderControllerBuilder.create(option).range(min, max).step(step))
                .build();
    }

    private static int intValue(String key) {
        var config = SwapDoctorClient.config();
        return switch (key) {
            case "option.swapdoctor.hud_duration" -> config.hudDurationMs;
            case "option.swapdoctor.scale_duration" -> config.scaleHudDurationMs;
            case "option.swapdoctor.max_delay" -> config.maxDelayMs;
            case "option.swapdoctor.scale_width" -> config.scaleWidth;
            default -> 0;
        };
    }

    private static Option<Float> floatOption(String key, float value, float min, float max, float step,
                                             java.util.function.Consumer<Float> setter, float defaultValue) {
        return Option.<Float>createBuilder()
                .name(Component.translatable(key))
                .binding(defaultValue, () -> floatValue(key), setter)
                .controller(option -> FloatSliderControllerBuilder.create(option).range(min, max).step(step))
                .build();
    }

    private static float floatValue(String key) {
        var config = SwapDoctorClient.config();
        return switch (key) {
            case "option.swapdoctor.scale_length_multiplier" -> config.scaleLengthMultiplier;
            case "option.swapdoctor.scale_thickness_multiplier" -> config.scaleThicknessMultiplier;
            default -> 1.0f;
        };
    }
}
