## Ragnarök Overview
This is a plugin for bukkit/spigot minecraft server.
It is primary build as a demo for my LibSequence plugin, but you can also treat it at as a real tool with a meaningful use.

## General Purpose
If you have a planned servershutdown, you want to inform the players that the server will shut down in 30 seconds ... 20 seconds ... 10 seconds ... now.

For this you want to define a sequence of actions which are executed with predefined waits between them (e.g. broadcast a message, kick all players etc.).

Now you can write your own sequence in the LibSequence-syntax and use the additional actions provided by this plugin:
* Boradcast a message to all players (default action already included in LibSequence)
* Execute a minecraft command (default action already included in LibSequence)
* Broadcast a title to all players (new)
* Block new logins (new)
* Kick all players (new)
* Finally perform a server-shutdown (new)
 
## Security (Authorization)
To start the shutdown-sequence we have implemented a 2-factor-authorization.
The sequence is only started, if at least one player is fullfilling the following two conditions when an entitled source executes the command:

1. The Player must have a specific "approval" permission (Authorization)
2. The Player must stay in a specific region (Conscious decision)

## Usage example
You have build a role-play-world in minecraft. Now you are searching for a way to trigger the servershutdown without breaking the role-play. Here comes Ragnarök, the end of all things, (taken from the Norse mythology) where the world is rebuilded. In minecraft you build a Ragnarök temple and place a button there. The button is connected with a command-blocks which executes the command "/ragnarök toggle". Then you build a WorldGuard region to protected the button. Then you declare your server-admins as "Ragnarök acolytes" by giving them a specific minecraft permission.

What happens now? If someone else press the button, nothing happens. The "/ragnarök toggle" is rejected by the plugin. But if a server-admin press the button, the plugin detects: There is at least one player with the "ragnarök.acolyte" permission staying inside the WorldGuard region. So the sequence can start, and as a final result the server gets shut down.

## Documentation
Please see the Github Wiki.

## Support
For support please contact us as discord https://discord.gg/MBJjqUHQHR

## Homepage
comming soon
