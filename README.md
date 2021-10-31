# Simple java chatroom project

A simple java chatroom with GUI client and a CLI server.
Changing your nickname and creating new tabs is broken. No idea why ¯\_(ツ)_/¯
Other than its buggy and bad but hey, was fun to make.

## Install

### Prerequisites

- Apache ant
- Git

### Compiling

- Clone the repo
`git clone https://github.com/onlyTr3v0r/JChatV3.git`
- Compile client, if needed.
`ant main-client`
- Compile server, if needed.
`ant main-server`

## Running

Running Client:
`java -jar /dist/JChatV3_Client.jar`

Running Server
`java -jar /dist/JChatV3_Client.jar <args>`

### Args

- `<port>` -> The port the server should be hosted on.
- `<server name>` -> The name of the server.
- `<max clients>` -> The maximum number of clients allowed to join.
- `<logging>` -> Enable logging. Use "log" to allow logging, leave it blank for no logging
