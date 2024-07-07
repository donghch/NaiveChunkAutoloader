# NaiveChunkAutoLoader
This plugins helps vehicles like minecart to operate smoothly with no need of 
online players.

## Supported Versions
Any Bukkit & its forks(Except Folia & its forks) with version number greater than 1.14 should be able to run this plugin.

## Installation
Drop it into the plugins folder and restart the server.

## How it works & Considerations
Every time when a vehicle moves, the plugin could detect that movement and check whether its next block belongs to another chunk. If that is the case, the plugin would force load that chunk and unload it after a specified time.
### Performance Consideration
The above implementation could cause performance issues. More moving vehicles lead to more event handling & potentially more chunk processings. Those operations would lead to higher MSPT and even cause server lags in traditional bukkit forks like Spigot & Paper. To mitigate this more work should be done asynchronously. 
<br/>Folia may solve this problem & allows more vehicles to operate at the same time.