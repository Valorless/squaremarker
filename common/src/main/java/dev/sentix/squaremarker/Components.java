package dev.sentix.squaremarker;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Components {

    private Components() {}

    public static Component parse(String input) {
        return MiniMessage.miniMessage().deserialize(input);
    }

    public static void send(Audience audience, String input) {
        audience.sendMessage(parse("<gray>" + input));
    }

    public static void sendPrefixed(Audience audience, String input) {
        audience.sendMessage(parse(Lang.PREFIX + " <gray>" + input));
    }

    public static String url(String content, String hoverText, String openUrl) {
        return hoverable(hoverText) + "<click:open_url:" + openUrl + ">" + content + "</hover>";
    }

    public static String clickable(String content, String hoverText, String clickExecution) {
        return hoverable(hoverText) + "<click:run_command:" + clickExecution + ">" + content + "</hover>";
    }

    /*
        run_command
        suggest_command
        copy_to_clipboard
        open_file
        open_url
     */
    public static String clickable(String content, String hoverText, String clickAction, String clickExecution) {
        return hoverable(hoverText) + "<click:" + clickAction + ":" + clickExecution + ">" + content + "</hover>";
    }

    public static String hoverable(String hoverText) {
        return "<hover:show_text:'" + hoverText + "'>";
    }

    public static String gradient(String input) {
        return "<gradient:#C028FF:#5B00FF>" + input + "</gradient>";
    }
}
