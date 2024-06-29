package club.flame.disqualified.manager.webhook;

import lombok.AllArgsConstructor;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

@AllArgsConstructor
public abstract class BaseWebhook {

    private final String webHookName;
    private final String url;

    public abstract void send();
}
