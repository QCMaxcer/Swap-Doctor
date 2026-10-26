package cn.swapdoctor.client;

import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;

/** Optional Mod Menu adapter; the K key remains available when Mod Menu is absent. */
public final class SwapDoctorModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SwapDoctorConfigScreen::create;
    }
}
