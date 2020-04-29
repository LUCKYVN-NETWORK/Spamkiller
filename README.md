# Spamkiller
Spigot anti-spam plugin

Checks for different spam-like behaviours and attempts to limit them by blocking messages or muting spamming players,
helping you mantain a clean chat.

Currently checks for:
- Spambots (similar message delay)
- Message similarity
- Repeated characters in a message
- Caps lock
- Unnaturally high typing speed
- Sending lots of messages quickly
- Group spam
- Non-ASCII characters (disabled by default to prevent issues with some alphabets)
- Unnaturally long words (disabled by default to prevent issues with some languages)

There is a config file that allows you to easily modify nearly every aspect of the plugin.

## Commands:
- /spamkiller reload - Reloads the config file
- /spamkiller set \<nickname\> \<time\> - Sets mute time for a specific player

Both commands have an alias "/sk" that can be used instead of "/spamkiller"

## Permissions:
- spamkiller.notify - Allows you to see mute notifications
- spamkiller.exempt - Makes the plugin ignore you, happy spamming
- spamkiller.reload - Allows you to reload the config file
- spamkiller.set - Allows you to use the set command
