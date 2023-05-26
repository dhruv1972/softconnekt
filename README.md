# Asset Transfer Chaincode for Hyperledger Fabric

This repository contains an Asset Transfer System implemented using Hyperledger Fabric and Electron. The chaincode here is developed for Hyperledger Fabric, and it's written in Java.

The asset transfer chaincode demonstrates the transfer of assets (licenses) from one device to another. Assets are defined by a license key, an email, a device ID, and a timestamp. The operations allowed on the assets include the creation, reading, updating (transfer), and deletion of an asset.

## Chaincode Functions

The chaincode includes the following functions:

1. **`InitLedger`** - Initializes the ledger with some test data. This function is called when the chaincode is instantiated.

2. **`CreateAsset`** - Creates a new asset and stores it on the ledger.

3. **`ReadAsset`** - Reads an existing asset from the ledger.

4. **`DeleteAsset`** - Deletes an asset from the ledger.

5. **`AssetExists`** - Checks whether an asset exists on the ledger.

6. **`TransferAsset`** - Transfers an asset from one device to another.

7. **`GetAllAssets`** - Retrieves all assets from the ledger.

## Structure of the repository

The repository contains the following:

- `AssetTransfer.java`: This is the chaincode that defines the business logic of the asset transfer system. It provides functions for creating, reading, updating, and deleting assets, checking if an asset exists, initializing the ledger with sample data, and getting all assets.

- `index.html`: This is the main page of the Electron application. It provides an interface for interacting with the chaincode.

- `render.js`: This JavaScript file handles the interaction between the Electron application and the chaincode. It sends and receives data to and from the chaincode using the Electron IPC.


## How to Use

To use this chaincode, follow the steps below:

1. Clone this repository to your local machine.
2. Install the basic requiremnt like electron and npm etc
3. Start the Electron application by running `npm start`.
4. Interact with the system through the Electron application interface.

## Dependencies

This chaincode uses the following libraries:

- **Hyperledger Fabric Contract API (Java)**: The Fabric Contract API forms the base for developing contract classes in Java.

- **Genson**: Genson is used for serialization and deserialization of the assets to and from JSON.

## Contributing

We welcome contributions to this project. Please feel free to submit issues and pull requests.

## Front end images

<image src="UI.png">

## License

This project is licensed under the Apache 2.0 License. For more information, see the [LICENSE](LICENSE) file.
