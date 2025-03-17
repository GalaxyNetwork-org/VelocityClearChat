# Velocity Clear Chat

An extremely simple and lightweight plugin, designed to clear chat on the proxy side.

You are also able to clear on server swap, which may be useful to some users. Read the configuration for more info. Default configuration can be found here:

```hocon
# If velocity returns invalid information, this message will be displayed.
chat-clear-failed-message="<red>Failed to get server info</red>"
# The message to send when the chat is cleared by an admin.
# You can use <player> if you would like to display who cleared the chat.
chat-cleared-message="<green>Chat has been cleared by an admin</green>"
# On player swap, should we clear the chat history?
clear-on-server-swap=true
# The message to display if "clear-on-server-swap" is enabled, set to "" to disable it.
clear-on-server-swap-message=""
# The permission an admin needs to clear the chat.
clear-permission="lncvrt.velocityclearchat.clear"
# The number of lines the chat clearer will send.
lines=100
```
