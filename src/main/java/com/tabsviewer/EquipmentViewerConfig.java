package com.tabsviewer;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("equipmentviewer")
public interface EquipmentViewerConfig extends Config
{
    @ConfigItem(
        keyName = "hideWhenEquipmentTabOpen",
        name = "Hide when Equipment tab open",
        description = "Hide this overlay whenever the Equipment panel is selected"
    )
    default boolean hideWhenEquipmentTabOpen()
    {
        return true;
    }

    @ConfigItem(
        keyName = "showSlotBackgrounds",
        name = "Show slot backgrounds",
        description = "Render OSRS-style slot tiles behind items",
        position = 1
    )
    default boolean showSlotBackgrounds()
    {
        return true; // default ON (current behavior)
    }

    @Range(min = 0, max = 100)
    @ConfigItem(
        keyName = "slotBgOpacity",
        name = "Slot background opacity",
        description = "Opacity of the slot background image (0–100%)"
    )
    default int slotBgOpacity()
    {
        return 100; // fully opaque by default
    }

    @Alpha
    @ConfigItem(
        keyName = "slotBgColor",
        name = "Slot background color",
        description = "Tint color applied to the slot background (alpha controls intensity)"
    )
    default Color slotBgColor()
    {
        // Transparent = no tint by default
        return new Color(255, 255, 255, 0);
    }

    @Alpha
    @ConfigItem(
        keyName = "panelBgColor",
        name = "Panel background",
        description = "Background color of the panel container"
    )
    default Color panelBgColor()
    {
        return new Color(0, 0, 0, 80); // semi-transparent black
    }

    @Alpha
    @ConfigItem(
        keyName = "panelBorderColor",
        name = "Border color",
        description = "Color of the panel border"
    )
    default Color panelBorderColor()
    {
        return new Color(60, 60, 60, 200);
    }

    @Range(min = 0, max = 8)
    @ConfigItem(
        keyName = "panelBorderThickness",
        name = "Border thickness",
        description = "Thickness of the panel border (px)"
    )
    default int panelBorderThickness()
    {
        return 1;
    }

    @Range(min = 0, max = 16)
    @ConfigItem(
        keyName = "panelCornerRadius",
        name = "Corner radius",
        description = "Roundness of the panel corners (px)"
    )
    default int panelCornerRadius()
    {
        return 5;
    }
}
