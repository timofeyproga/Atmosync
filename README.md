# 🌦️ Atmosync

> **Synchronize Minecraft weather with real-world weather.**

**Atmosync** is a lightweight **Paper plugin** that synchronizes Minecraft weather with real-world weather using **Open-Meteo**.

Atmosync periodically retrieves weather data for a configured location and applies the corresponding weather conditions to a Minecraft world.

## ✨ Features

* 🌍 Real-world weather synchronization
* 🏙️ City + country location search
* 📍 Manual latitude and longitude coordinates
* ☀️ Clear weather
* 🌧️ Rain
* ⛈️ Thunderstorms
* ❄️ Snow detection
* 🌎 Configurable Minecraft world
* ⏱️ Configurable update interval
* 🔄 Manual weather updates
* ♻️ Configuration reload
* ⌨️ Tab completion
* 🔑 No API key required
* 🧵 Asynchronous weather requests

## 📦 Requirements

* **Minecraft:** `1.21.x`
* **Server:** Paper
* **Java:** `21`

### Compatibility

Atmosync `1.0.0` has been tested on:

* Paper `1.21`
* Paper `1.21.11`

Both tested versions successfully loaded the plugin and Atmosync commands were tested through the server console.

> **Note:** Intermediate `1.21.x` versions are expected to be compatible, but the versions listed above are the versions explicitly tested for release `1.0.0`.

## 📥 Installation

1. Download the latest Atmosync `.jar` file.
2. Place the `.jar` file into your server's `plugins` folder.
3. Start or restart the server.
4. Atmosync will automatically create its configuration file.
5. Configure your location and world in `plugins/Atmosync/config.yml`.
6. Restart the server or use `/atmosync reload`.

After installation, check the server console for:

```text
Atmosync enabled!
```

## ⚙️ Configuration

The default configuration looks like this:

```yaml
location:
  city: "Moscow"
  country: "Russia"

  coordinates:
    enabled: false
    latitude: 55.75204
    longitude: 37.61781

world:
  name: "world"

weather:
  update-interval: 300
```

### 📍 Location

Atmosync can find a location using a **city and country**:

```yaml
location:
  city: "Moscow"
  country: "Russia"
```

Atmosync uses Open-Meteo's geocoding service to find the coordinates of the configured city.

### 📌 Manual Coordinates

You can disable city search and provide exact coordinates:

```yaml
location:
  city: "Moscow"
  country: "Russia"

  coordinates:
    enabled: true
    latitude: 55.75204
    longitude: 37.61781
```

When manual coordinates are enabled, Atmosync uses them instead of searching for the configured city.

Latitude must be between `-90` and `90`.

Longitude must be between `-180` and `180`.

### 🌎 Minecraft World

Choose which Minecraft world receives the synchronized weather:

```yaml
world:
  name: "world"
```

The world must exist on the server.

### ⏱️ Update Interval

Weather synchronization is configured in seconds:

```yaml
weather:
  update-interval: 300
```

For example:

```yaml
update-interval: 300
```

means that Atmosync checks the real-world weather every **5 minutes**.

## 💬 Commands

### `/atmosync status`

Displays the current Atmosync status, including:

* Configured location
* Minecraft world
* Location mode
* Current weather
* Current temperature

Available to all players.

### `/atmosync update`

Manually requests a new weather update.

The request is performed asynchronously so the HTTP request does not block the Minecraft server.

**Requires OP.**

### `/atmosync reload`

Reloads the configuration and restarts the weather synchronization task using the new settings.

**Requires OP.**

### ⌨️ Tab Completion

Atmosync provides tab completion for its commands.

Operators can see:

```text
status
update
reload
```

Non-operators only see:

```text
status
```

Administrative commands are also protected inside the command handler, so hiding them from tab completion is not the only security check.

## 🌦️ How It Works

```text
Real World
    ↓
Open-Meteo
    ↓
Atmosync
    ↓
Minecraft World
```

Atmosync periodically requests the current weather for the configured location.

The received weather data is converted into an Atmosync weather type and then applied to the configured Minecraft world.

Weather requests are performed asynchronously, while Minecraft world modifications are executed on the server's main thread.

## 🌨️ Supported Weather

Atmosync currently supports:

| Weather         | Minecraft            |
| --------------- | -------------------- |
| ☀️ Clear        | Clear                |
| 🌧️ Rain        | Rain                 |
| ❄️ Snow         | Snow / precipitation |
| ⛈️ Thunderstorm | Thunder              |

Atmosync uses Open-Meteo weather codes to determine the current weather condition.

## 🌐 Open-Meteo

Atmosync uses **Open-Meteo** for:

* Weather data
* City geocoding
* Latitude and longitude lookup

No API key or account is required.

[Open-Meteo](https://open-meteo.com/)

## 🛠️ Building From Source

### Requirements

To build Atmosync from source, you need:

* Java `21`
* Maven
* Git

Clone the repository:

```bash
git clone https://github.com/timofeyproga/Atmosync.git
cd Atmosync
```

Build the plugin:

```bash
mvn clean package
```

The compiled `.jar` will be created inside:

```text
target/
```

You can then place the generated `.jar` into your Paper server's `plugins` directory.

## 🗂️ Project Structure

The project is organized into several main components:

```text
src/
└── main/
    ├── java/
    │   └── me/
    │       └── atmosync/
    │           ├── command/
    │           ├── location/
    │           └── weather/
    │
    └── resources/
        ├── config.yml
        └── plugin.yml
```

### Main Components

* `command` - Atmosync commands and tab completion
* `location` - City geocoding and coordinate handling
* `weather` - Weather API, weather data and synchronization
* `Atmosync` - Main plugin class

## 🔐 Security

Atmosync does not collect telemetry or player information.

The plugin only makes the external requests required to obtain weather and geocoding data from Open-Meteo.

Administrative commands are restricted to server operators:

* `/atmosync update`
* `/atmosync reload`

## 🚧 Development Status

**Atmosync 1.0.0 is the first public release.**

The project is still actively evolving. More weather-related features, improvements and refinements may be added in future releases.

## 📄 License

See the repository's license information for details.

## 🤝 Contributing

Bug reports, ideas and improvements are welcome.

If you find a problem, please provide:

* Minecraft version
* Paper version
* Java version
* Atmosync version
* Relevant server log output
* Steps to reproduce the problem
