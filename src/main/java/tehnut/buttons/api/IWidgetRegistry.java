package tehnut.buttons.api;

import tehnut.buttons.api.button.utility.Button;

/**
 * Primary registry system for widgets.
 */
public interface IWidgetRegistry {
    /**
     * Registers a {@link Button} to display at the top left of the screen while a container is open. Buttons are displayed
     * in the order that they are registered in.
     * <p>
     * If a button is registered under the same {@link Button#getButtonId() buttonId} as a previously registered button,
     * the new button will override the previous.
     *
     * @param button - The button to register.
     */
    void addUtilityButton(Button button);
}
