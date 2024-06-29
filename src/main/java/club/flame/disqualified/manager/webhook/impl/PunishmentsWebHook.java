package club.flame.disqualified.manager.webhook.impl;

import club.flame.disqualified.manager.webhook.BaseWebhook;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class PunishmentsWebHook extends BaseWebhook {

    public PunishmentsWebHook(String url) {
        super("PunishmentWebhook", url);
    }

    @Override
    public void send() {

    }
}
