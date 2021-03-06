package tehnut.buttons.api.button.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import tehnut.buttons.api.ClientHelper;
import tehnut.buttons.api.WidgetTexture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ButtonMode<T extends Enum<T> & IMode> extends Button {

    private final T[] modes;
    private T mode;

    public ButtonMode(WidgetTexture widgetTexture, Class<T> enumClass) {
        super(widgetTexture);

        this.modes = enumClass.getEnumConstants();
        setMode(modes[0]);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EnumActionResult onClientClick(int mouseX, int mouseY) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            cycleMode();
            return EnumActionResult.FAIL;
        }

        return getMode().onClientClick(mouseX, mouseY);
    }

    @Override
    public void onServerClick(EntityPlayerMP player) {
        getMode().onServerClick(player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawButton(int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(getMode().getModeTexture().getTextureLocation());
        ClientHelper.drawTexture(1, x, y, getMode().getModeTexture());
    }

    @Nullable
    @Override
    public List<? extends ITextComponent> getTooltip() {
        List<ITextComponent> tooltip = new ArrayList<ITextComponent>();
        if (getMode().getTooltip() != null)
            tooltip.addAll(getMode().getTooltip());
        tooltip.add(new TextComponentTranslation("button.butt.mode").setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY)));
        return tooltip;
    }

    @Nonnull
    @Override
    public ITextComponent getUseNotification(EntityPlayerMP player) {
        return new TextComponentString(String.format("%s used the %s button with mode %s.", player.getDisplayNameString(), getButtonId(), mode.name()));
    }

    @Override
    public abstract ResourceLocation getButtonId();

    /**
     * Cycles to the next mode.
     */
    public void cycleMode() {
        setMode(modes[(mode.ordinal() + 1) % modes.length]);
    }

    /**
     * @return the currently selected mode.
     */
    public T getMode() {
        return mode;
    }

    /**
     * Sets the current mode to the new mode.
     *
     * @param mode - The mode to be set to.
     * @return self for chaining.
     */
    public ButtonMode<T> setMode(T mode) {
        this.mode = mode;
        return this;
    }

    /**
     * @return an array of all the modes.
     */
    public T[] getModes() {
        return modes;
    }
}
