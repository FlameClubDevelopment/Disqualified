# Disqualified (1.7 - 1.21)

Disqualified is a Minecraft Network/Server core plugin used in FireMC Network, designed to enhance server functionality with various features including chat management, social media integration, and link restrictions. This repository contains the source code and configuration files needed to run the plugin.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Important](#important)

## Features

- Optimized
- Staff Server Switch/Join Notification
- Staff and Admin Chat
- Rank System (with /grants and /setrank)
- Coins System (with coins manager)
- Chat Control (with chat mute, slow and more)
- Name and Chat Color
- Essentials Commands (/bc, /report, /freeze and more)
- Inventory Commands (/invsee, /ci, /ec, /fix and more)
- Msg Commands (/tpm, /ignore, /socialspy and more)
- General Commands (/announce, /playerinfo)
- Punishment System (/alts, /check, and more)
- Social Commands (/discord, /store, /ts and more)
- NameMC System (/vote with rewards)
- Tags System (/tags or /prefix)
- Teleport Commands (/tphere, /top, /tpall and more)
- Time Commands (/day, /night and /sunset)
- Tips System
- Discord Webhook
- Redis System
- Customizable messages

## Installation

1. **Clone the Repository**:
    ```sh
    git clone https://github.com/FlameClubDevelopment/Disqualified.git
    ```

2. **Build the Plugin**:
    Navigate to the project directory and build the project using Maven:
    ```sh
    cd Disqualified
    mvn clean install
    ```

3. **Deploy the Plugin**:
    Copy the generated JAR file from the `target` directory to your server's `plugins` directory.

- Note: You need the Libs.

## Configuration

Configure the plugin by editing the `config.yml` file in the `plugins/Disqualified` directory. Set your server name, social media links, and other settings as needed.

## Usage

### Chat Management

Commands for managing chat settings and restrictions can be found in the `club.flame.disqualified.command.chat` package.

### Social Media Links

Social media links are configurable and can be accessed via specific commands.

### Link Restriction

The plugin prevents players from sending links in chat by default. You can customize the message and behavior in the configuration file.

## Important

This project is abandoned, you can still use it.
