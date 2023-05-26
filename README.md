# Asset Transfer Chaincode for Hyperledger Fabric

This repository contains an example chaincode for a simple asset transfer scenario. The chaincode is developed for Hyperledger Fabric, and it's written in Java.

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

## How to Use

To use this chaincode, follow the steps below:

1. Clone this repository to your local machine.
2. Package the chaincode using the Fabric Peer CLI.
3. Install the chaincode on your peers and approve it for your organization.
4. Commit the chaincode definition across the channels.

## Dependencies

This chaincode uses the following libraries:

- **Hyperledger Fabric Contract API (Java)**: The Fabric Contract API forms the base for developing contract classes in Java.

- **Genson**: Genson is used for serialization and deserialization of the assets to and from JSON.

## Contributing

We welcome contributions to this project. Please feel free to submit issues and pull requests.

## License

This project is licensed under the Apache 2.0 License. For more information, see the [LICENSE](LICENSE) file.
